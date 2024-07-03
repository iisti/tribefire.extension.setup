// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ============================================================================
package com.braintribe.doc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.file.Path;

import com.vladsch.flexmark.ast.Document;

public class MarkdownDocumentLoader {
	private final File sourceRootFolder;
	private final JavadocMerger javadoc;

	public MarkdownDocumentLoader(File sourceRootFolder, JavadocMerger javadoc) {
		this.sourceRootFolder = sourceRootFolder;
		this.javadoc = javadoc;
	}

	public Document load(MarkdownFile markdownFile) {
		UniversalPath docRelativeLocation = markdownFile.getDocRelativeLocation();
		UniversalPath documentParentPath = docRelativeLocation.getParent();
		Path rootPath = sourceRootFolder.toPath();
		DocumentProcessor loader = new DocumentProcessor(rootPath, documentParentPath, markdownFile, true, javadoc);
		File file = rootPath.resolve(docRelativeLocation.toPath()).toFile();

		Document includedDocument = DocUtils.simpleParse(file);
		loader.process(includedDocument, documentParentPath, docRelativeLocation.toString());

		return includedDocument;
	}

	public Document load(String documentContent, UniversalPath documentParentPath) {
		Path rootPath = sourceRootFolder.toPath();

		DocumentProcessor loader = new DocumentProcessor(rootPath, documentParentPath, null, true, javadoc);

		Document document = DocUtils.FLEXMARK_PARSER.parse(documentContent);
		loader.process(document, "<DYNAMICALLY CREATED FILE>");

		return document;
	}
	
	public MarkdownFile compileBody(MarkdownFile markdownFile, File targetFile) {
		Document document = load(markdownFile);
		if (markdownFile.getWizard() == null)
			DocumentProcessor.ensureToc(document);

		try (Writer writer = new OutputStreamWriter(new FileOutputStream(targetFile), "UTF-8")) {
			DocUtils.FLEXMARK_RENDERER.render(document, writer);
			writer.flush();
		} catch (IOException e) {
			throw new UncheckedIOException("Can't write body of markdown file into " + targetFile.getAbsolutePath(), e);
		}
		
		return markdownFile;

		
	}
}
