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
package com.braintribe.tribefire.jinni.wire;

import static com.braintribe.wire.api.util.Lists.list;

import java.io.File;
import java.util.List;

import com.braintribe.gm.config.yaml.wire.ModeledYamlConfigurationWireModule;
import com.braintribe.gm.service.wire.common.CommonServiceProcessingWireModule;
import com.braintribe.model.processing.platform.setup.wire.PlatformSetupProcessingWireModule;
import com.braintribe.model.processing.platform.setup.wire.contract.PlatformSetupDependencyEnvironmentContract;
import com.braintribe.template.processing.wire.ArtifactTemplateProcessingWireModule;
import com.braintribe.tribefire.jinni.wire.contract.HelpProcessorConfigurationContract;
import com.braintribe.tribefire.jinni.wire.contract.JinniConfDirContract;
import com.braintribe.tribefire.jinni.wire.contract.JinniContract;
import com.braintribe.tribefire.jinni.wire.space.JinniHelpProcessorConfigurationSpace;
import com.braintribe.tribefire.jinni.wire.space.JsSetupConfigSpace;
import com.braintribe.wire.api.context.WireContextBuilder;
import com.braintribe.wire.api.module.WireModule;
import com.braintribe.wire.api.module.WireTerminalModule;

import tribefire.extension.js.processing.wire.JsSetupWireModule;
import tribefire.extension.js.processing.wire.contract.JsSetupConfigContract;
import tribefire.extension.setup.dev_env_generator.wire.DevEnvGeneratorWireModule;
import tribefire.extension.xml.schemed.processing.wire.XsdAnalyzingProcessorWireModule;

public class JinniWireModule implements WireTerminalModule<JinniContract> {
	private PlatformSetupDependencyEnvironmentContract platformSetupDependencyEnvironment;
	private File configDir;

	public JinniWireModule(PlatformSetupDependencyEnvironmentContract platformSetupDependencyEnvironment, File configDir) {
		this.platformSetupDependencyEnvironment = platformSetupDependencyEnvironment;
		this.configDir = configDir;
	}

	@Override
	public Class<JinniContract> contract() {
		return JinniContract.class;
	}

	@Override
	public void configureContext(WireContextBuilder<?> contextBuilder) {
		WireTerminalModule.super.configureContext(contextBuilder);
		contextBuilder.bindContract(HelpProcessorConfigurationContract.class, JinniHelpProcessorConfigurationSpace.class);
		contextBuilder.bindContract(PlatformSetupDependencyEnvironmentContract.class, platformSetupDependencyEnvironment);
		contextBuilder.bindContract(JinniConfDirContract.class, () -> configDir);

		// Js Setup
		contextBuilder.bindContract(JsSetupConfigContract.class, JsSetupConfigSpace.class);
	}

	@Override
	public List<WireModule> dependencies() {
		return list(CommonServiceProcessingWireModule.INSTANCE, //
				PlatformSetupProcessingWireModule.INSTANCE, //
				ArtifactTemplateProcessingWireModule.INSTANCE, //
				DevEnvGeneratorWireModule.INSTANCE, //
				XsdAnalyzingProcessorWireModule.INSTANCE, //
				JsSetupWireModule.INSTANCE, new ModeledYamlConfigurationWireModule(configDir));
	}
}
