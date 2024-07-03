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
package com.braintribe.velocity;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.velocity.app.event.ReferenceInsertionEventHandler;
import org.apache.velocity.context.Context;

public class EscapeReferenceInserter implements ReferenceInsertionEventHandler {

	private String templatePath;
	private String type = "";
	private Function<String, String> escaper = Function.identity();
	private static Pattern typePattern = Pattern.compile(".*\\.(.*)\\.vm");
	
	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;

		Matcher matcher = typePattern.matcher(templatePath);
		
		if (matcher.matches()) {
			type = matcher.group(1);
			
			switch (type) {
				case "html":
					escaper = EscapeReferenceInserter::escapeHtml;
					break;
				case "xml":
					escaper = EscapeReferenceInserter::escapeXml;
					break;
			}
		}
	}
	
	public String getTemplatePath() {
		return templatePath;
	}
	
	public String getType() {
		return type;
	}
	

	@Override
	public Object referenceInsert(Context context, String reference, Object value) {
		if (value == null)
			return null;
		
		if (reference.endsWith("_noesc"))
			return value;
		
		return escaper.apply(value.toString());
	}

	public Function<String, String> getEscaper() {
		return escaper;
	}
	
	private static String escapeHtml(String s) {
		return escapeXml(s);
	}
	
	private static String escapeXml(String s) {
		int c = s.length();
		if (c == 0)
			return s;

		char[] chars = new char[c];
		s.getChars(0, c, chars, 0);

		StringBuilder b = new StringBuilder(c * 2);

		for (int i = 0; i < chars.length; i++) {
			char ch = chars[i];

			switch (ch) {
			case '&':
				b.append("&amp;");
				break;
			case '<':
				b.append("&lt;");
				break;
			case '>':
				b.append("&gt;");
				break;
			case '"':
				b.append("&quot;");
				break;
			case '\'':
				b.append("&apos;");
				break;
			default:
				if (ch > 0x7F) {
					b.append("&#");
					b.append(Integer.toString(ch, 10));
					b.append(';');
				} else {
					b.append(ch);
				}
			}
		}
		
		return b.toString();
	}
	
	public static void main(String[] args) {
		Matcher matcher = typePattern.matcher("test.xml.vm");
		
		if (matcher.matches()) {
			System.out.println(matcher.group(1));
		}
	}

}