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
package com.braintribe.model.jinni.api.template;

/**
 * Describes which sample codes should be created for given extension. Note that samples imply certain {@link ExtensionPart}s have to be created:
 * 
 * <ul>
 * <li>{@link #serviceProcessor} => {@link ExtensionPart#apiModel}, {@link ExtensionPart#initializer}
 * <li>{@link #accessProcessor} => {@link ExtensionPart#accessApiModel} (which implies further parts) , {@link ExtensionPart#initializer}
 * </ul>
 * 
 * @author peter.gazdik
 */
public enum ExtensionSample {

	serviceProcessor,
//	accessProcessor,

	all;

}
