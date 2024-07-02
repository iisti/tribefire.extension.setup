// ============================================================================
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
// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
// 
// This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with this library; See http://www.gnu.org/licenses/.
// ============================================================================
package com.braintribe.doc.lunr;

import com.vladsch.flexmark.ast.Document;
import com.vladsch.flexmark.ast.Link;
import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;

public class MarkdownIncludeLab {
	public static void main(String[] args) {
		Parser parser = Parser.builder().build();
		
		Document document = parser.parse("**one** _two_ ___three___ Hello World this is my message:\n\n[Message](http://www.google.de)\n# super");
		
		Document includedDocument = parser.parse("This is the message yeah yeah");
		
		Link node = findFirstLink(document);
		
		Node nodeToBeReplaced = node.getParent();
		for (Node topNode: includedDocument.getChildren()) {
			nodeToBeReplaced.insertBefore(topNode);
		}
		nodeToBeReplaced.unlink();
		
		
		HtmlRenderer renderer = HtmlRenderer.builder().build();
		
		System.out.println(renderer.render(document));
		//System.out.println(renderer.render(includedDocument));
		
	}

	private static Link findFirstLink(Node node) {
		if (node instanceof Link) {
			return (Link)node;
		}
		else {
			for (Node child: node.getChildren()) {
				Link link = findFirstLink(child);
				
				if (link != null)
					return link;
			}
		}
		
		return null;
	}
}
