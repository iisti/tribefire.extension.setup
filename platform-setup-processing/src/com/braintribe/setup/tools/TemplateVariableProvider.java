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
package com.braintribe.setup.tools;

import java.util.function.Function;

import com.braintribe.utils.template.Template;
import com.braintribe.utils.template.model.MergeContext;

/**
 * <p>
 * This class is used in context of {@link Template Templates}. If this provider is assigned to a via
 * {@link MergeContext#setVariableProvider(Function)} the given {@link #valueToCheck} is being evaluated and substituted
 * by {@link #returnValue} in case a match is given.
 * 
 * <p>
 * If {@link #valueToCheck} did not match, the provider states this with {@link #failed} set to true.
 * 
 */
public class TemplateVariableProvider implements Function<String, String> {
	private boolean failed;
	private String valueToCheck;
	private String returnValue;

	public TemplateVariableProvider(String valueToCheck, String returnValue) {
		this.valueToCheck = valueToCheck;
		this.returnValue = returnValue;
	}

	@Override
	public String apply(String value) {
		if (value.equals(valueToCheck))
			return returnValue;
		else {
			this.failed = true;
			return "";
		}
	}

	public boolean getFailed() {
		return failed;
	}
	
}
