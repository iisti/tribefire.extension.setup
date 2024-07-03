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
package com.braintribe.doc.lunr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.braintribe.utils.FileTools;
import com.vladsch.flexmark.ast.Document;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;

public class TxtMarkLab {
	public static void main(String[] args) {
		try {
			File outputFolder = new File("html");
			
			if (outputFolder.exists())
				FileTools.deleteDirectoryRecursively(outputFolder);
			
			outputFolder.mkdirs();
			
			compile(new File("markdown"), outputFolder, new Stack<>());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void compile(File folder, File targetFolder, Stack<String> path) {
		for (File file: folder.listFiles()) {
			if (file.isDirectory()) {
				path.push(file.getName());
				try {
					compile(file, targetFolder, path);
				}
				finally {
					path.pop();
				}
			}
			else if (file.getName().endsWith(".md")){
				try {
					
				    Parser PARSER = Parser.builder().build();
				    HtmlRenderer RENDERER = HtmlRenderer.builder().build();
				    
				    
					String targetName = toHtmlName(file.getName());
					
					File targetFile = new File(targetFolder, Stream.concat(path.stream(), Stream.of(targetName)).collect(Collectors.joining(File.separator)));
					targetFile.getParentFile().mkdirs();

					
					try (Reader reader = new InputStreamReader(new FileInputStream(file), "UTF-8"); Writer writer = new OutputStreamWriter(new FileOutputStream(targetFile), "UTF-8")) {
						
						Document document = PARSER.parseReader(reader);
						RENDERER.render(document, writer);
					}
					
				} catch (IOException e) {
					throw new UncheckedIOException(e);
				}
			}
		}
	}
	
	private static String toHtmlName(String name) {
		int index = name.lastIndexOf('.');
		String htmlName = name.substring(0, index) + ".html";
		return htmlName;
	}

}
