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
package com.braintribe.tribefire.jinni.wire.space;

import static com.braintribe.tribefire.jinni.core.JinniTools.getClassPathModel;

import java.util.ArrayList;

import com.braintribe.devrock.templates._ArtifactTemplatesConfigModel_;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.session.api.managed.ModelAccessory;
import com.braintribe.model.processing.session.api.managed.ModelAccessoryFactory;
import com.braintribe.model.service.api.PlatformRequest;
import com.braintribe.tribefire.jinni.core.JinniModelAccessoryFactory;
import com.braintribe.tribefire.jinni.wire.contract.ServiceDomainContract;
import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.util.Lists;

import hiconic.platform.reflex._ReflexSetupApiModel_;
import tribefire.extension.hydrux._HydruxSetupApiModel_;
import tribefire.extension.js._JsSetupApiModel_;
import tribefire.extension.setup._DevEnvGeneratorConfigModel_;
import tribefire.extension.setup._JinniConfigModel_;

@Managed
public class ServiceDomainSpace implements ServiceDomainContract {

	@Managed
	private ModelAccessoryFactory modelAccessoryFactory() {
		JinniModelAccessoryFactory bean = new JinniModelAccessoryFactory();

		bean.setPlatformDomainModels(platformDomainModels());

		return bean;
	}

	@Managed
	private ArrayList<GmMetaModel> platformDomainModels() {
		return Lists.list( //
				getClassPathModel("tribefire.extension.setup:jinni-api-model"), //
				getClassPathModel(_JinniConfigModel_.reflection), //
				getClassPathModel("tribefire.extension.setup:dev-env-generator-api-model"), //
				getClassPathModel("com.braintribe.devrock:dev-env-api-model"), //
				getClassPathModel(_DevEnvGeneratorConfigModel_.reflection), //
				getClassPathModel("tribefire.extension.setup:platform-setup-api-model"), //
				getClassPathModel("tribefire.extension.setup:platform-setup-cloud-api-model"), //
				getClassPathModel("com.braintribe.devrock.templates:artifact-template-service-model"), //
				getClassPathModel(_ArtifactTemplatesConfigModel_.reflection), //
				getClassPathModel("tribefire.cortex.assets.templates:platform-asset-template-service-model"), //
				getClassPathModel("tribefire.extension.schemed-xml:schemed-xml-xsd-api-model"), // TODO how did this ever work?!
				getClassPathModel("tribefire.extension.xmi:argo-exchange-api-model"), //
				getClassPathModel(_ReflexSetupApiModel_.reflection), //
				getClassPathModel(_JsSetupApiModel_.reflection), //
				getClassPathModel(_HydruxSetupApiModel_.reflection), //
				getClassPathModel("tribefire.extension.hikari:hikari-deployment-model"), // Because HikariCpConnectionPool can be attached to requests
				getClassPathModel("tribefire.extension.artifact:artifact-management-api-model") //
		);
	}

	@Override
	public ModelAccessory modelAccessory() {
		return modelAccessoryFactory().getForServiceDomain(PlatformRequest.platformDomainId);
	}

}
