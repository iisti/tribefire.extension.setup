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
package tribefire.extension.setup.dev_env_generator_api.model;

import com.braintribe.model.generic.annotation.meta.Alias;
import com.braintribe.model.generic.annotation.meta.Description;
import com.braintribe.model.generic.annotation.meta.Mandatory;
import com.braintribe.model.generic.annotation.meta.PositionalArguments;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.service.api.PlatformRequest;

/**
 * 
 *
 */
@Description("Generates a DevEnv by patching an existing template. Configure via jinni/conf/dev-env-generator-config.yaml")
@PositionalArguments("name")
public interface CreateDevEnv extends PlatformRequest {
	EntityType<CreateDevEnv> T = EntityTypes.T(CreateDevEnv.class);

	@Description("The name for the new dev-env. Cannot exists yet.")
	@Alias("n")
	@Mandatory
	String getName();
	void setName(String name);

}
