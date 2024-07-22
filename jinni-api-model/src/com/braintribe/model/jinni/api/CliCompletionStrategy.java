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
package com.braintribe.model.jinni.api;

import com.braintribe.model.generic.base.EnumBase;
import com.braintribe.model.generic.reflection.EnumType;
import com.braintribe.model.generic.reflection.EnumTypes;

/**
 * Specifies which identifiers should be suggested for completion for elements which have a name and possibly multiple aliases.
 * 
 * @author peter.gazdik
 */
public enum CliCompletionStrategy implements EnumBase<CliCompletionStrategy> {
	/** suggest real name and all aliases */
	all,

	/** suggest real name only */
	realName,

	/** suggest the shortest name or alias */
	shortest;

	public static final EnumType<CliCompletionStrategy> T = EnumTypes.T(CliCompletionStrategy.class);
	
	@Override
	public EnumType<CliCompletionStrategy> type() {
		return T;
	}
}
