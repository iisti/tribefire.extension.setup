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
import java.io.IOException;
import java.util.stream.Collectors;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Value;

import com.braintribe.exception.Exceptions;
import com.braintribe.utils.IOTools;


public class LunrJsIndexJavascript implements LunrJsIndex {
	private final Value array;
	private final Context context;

	public LunrJsIndexJavascript() {
		context = Context.newBuilder().build();
		
	    array = eval("var idxContent = []; idxContent;");
	}

	@Override
	public void serialize(File serializedIndexFile) throws IOException {
		String lunr_js = IOTools.slurp(MarkdownCompiler.class.getResource("res/lunr.js"), "UTF-8");

		eval(lunr_js);

		String lunrIndex = eval("JSON.stringify(lunr(function () { this.ref('id'); this.field('body'); this.field('title'); this.field('headings'); this.field('tags'); idxContent.forEach(function (doc) { this.add(doc) }, this) }));")
				.asString();

		IOTools.spit(serializedIndexFile, "serializedIndex = ", "UTF-8", false);
		IOTools.spit(serializedIndexFile, lunrIndex, "UTF-8", true);

	}

	@Override
	public void add(MarkdownFile markdownFile) {
		String id = markdownFile.getDocRelativeHtmlFileLocation().toSlashPath();
		String content = markdownFile.unloadContentText();
		String title = markdownFile.getTitle();
		
		Value indexObject = eval("new Object();");

		indexObject.putMember("id", id);
		indexObject.putMember("body", content);
		indexObject.putMember("title", title);
		indexObject.putMember("tags", markdownFile.getTags().stream().map(Tag::getName).collect(Collectors.joining(" ")));
		indexObject.putMember("headings", String.join(" ", markdownFile.getHeadings()));

		Long size = array.getArraySize();
		array.setArrayElement(size, indexObject);
	}
	
	private Value eval(String source) {
		try {
			return context.eval("js", source);
		} catch (PolyglotException e) {
			throw Exceptions.unchecked(e, "Could not build up fulltext index with lunr.js script engine");
		}
	}

	@Override
	public void close() {
		context.close();
	}
}
