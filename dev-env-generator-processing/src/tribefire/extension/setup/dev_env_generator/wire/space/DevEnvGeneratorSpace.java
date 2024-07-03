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
package tribefire.extension.setup.dev_env_generator.wire.space;

import com.braintribe.gm.config.wire.contract.ModeledConfigurationContract;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;

import tribefire.extension.setup.dev_env_generator.processing.DevEnvGenerator;
import tribefire.extension.setup.dev_env_generator.wire.contract.DevEnvGeneratorContract;
import tribefire.extension.setup.dev_env_generator_config.model.DevEnvGeneratorConfig;

@Managed
public class DevEnvGeneratorSpace implements DevEnvGeneratorContract {

	@Import
	private ModeledConfigurationContract modeledConfiguration;

	@Managed
	@Override
	public DevEnvGenerator devEnvGenerator() {
		DevEnvGenerator bean = new DevEnvGenerator();
		bean.setConfiguration(modeledConfiguration.config(DevEnvGeneratorConfig.T));
		return bean;
	}

}