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
import static com.braintribe.utils.lcd.CollectionTools2.asMap;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;

public class FreemarkerEscapeLab {
	
	private static final Version FREEMARKER_VERSION = Configuration.VERSION_2_3_28;
	
	public static void main(String args[]) {
		try {
			Map<String, Object> dataModel = asMap();

			Configuration freeMarkerConfig = new Configuration(FREEMARKER_VERSION);

			String literals[] = {
					"$\\{foobar}",
					"\\\"",
					"<#assign a=\"\\{\">${a}",
			};
			
			for (String s: literals) {
			
				System.out.print(s + " -> ");
				
				String result = null;
				try (StringReader in = new StringReader(s); Writer out = new StringWriter()) {
					Template template = new Template("", in, freeMarkerConfig);
					template.process(dataModel, out);
					result = out.toString();
				}
			
				System.out.println(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

	private String processStringWithFreeMarker(Configuration freeMarkerConfig, String templatedString,
			Map<String, Object> dataModel) throws Exception {
		String result = null;
		try (StringReader in = new StringReader(templatedString); Writer out = new StringWriter()) {
			Template template = new Template("", in, freeMarkerConfig);
			template.process(dataModel, out);
			result = out.toString();
		}
		return result;
	}
}
