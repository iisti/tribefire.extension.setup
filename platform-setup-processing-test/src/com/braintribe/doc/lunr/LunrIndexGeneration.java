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

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.braintribe.utils.IOTools;

public class LunrIndexGeneration {
	public static void main(String[] args) {
		try {
			ScriptEngineManager factory = new ScriptEngineManager();
			ScriptEngine engine = factory.getEngineByName("JavaScript");
			String script = IOTools.slurp(new File("markdown/sub/lunr.js"), "UTF-8");
			String index = IOTools.slurp(new File("markdown/sub/index.js"), "UTF-8");
			
			engine.eval(index);
			engine.eval(script);
			engine.eval("serIdx = JSON.stringify(lunr(function () { this.ref('id'); this.field('body'); idxContent.forEach(function (doc) { this.add(doc) }, this) }));");
			Object x = engine.get("serIdx");
			System.out.println(x);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
