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
package com.braintribe.model.csa;

import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * {@link SmoodInitializer} which represents a script file based persistence initializer.
 * <p>
 * The implementation loads and runs given script files. The actual files are always called the same, for groovy scripts
 * (the first ones to support) the names are "model.groovy" and "data.groovy" and are found in a sub-folder with the
 * name of this initializer.
 * 
 * @author peter.gazdik
 */
public interface ScriptInitializer extends SmoodInitializer {

	EntityType<ScriptInitializer> T = EntityTypes.T(ScriptInitializer.class);

}