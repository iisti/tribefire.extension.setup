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
package com.braintribe.tribefire.jinni.cmdline.impl;

import static com.braintribe.tribefire.jinni.core.JinniTools.getClassPathModel;

import com.braintribe.codec.marshaller.yaml.YamlMarshaller;
import com.braintribe.gm.model.reason.Maybe;
import com.braintribe.model.generic.GMF;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.manipulation.ChangeValueManipulation;
import com.braintribe.model.jinni.api.JinniOptions;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.session.api.managed.ModelAccessory;
import com.braintribe.model.record.ListRecord;
import com.braintribe.model.service.api.PlatformRequest;
import com.braintribe.tribefire.jinni.cmdline.api.CommandLineParser;
import com.braintribe.tribefire.jinni.cmdline.api.EntityFactory;
import com.braintribe.tribefire.jinni.cmdline.api.ParsedCommandLine;
import com.braintribe.tribefire.jinni.core.JinniModelAccessoryFactory;
import com.braintribe.wire.api.util.Lists;

public class PosixParseLab {
	public static void main(String[] args) {
		try {
			// @formatter:off
			args = new String[]{
					ChangeValueManipulation.T.getTypeSignature(),
					"--newValue",
					"boolean",
					"true",
					":",
					ListRecord.T.getTypeSignature(),
					"--values",
					"string",
					"Hello",
					"integer",
					"23",
					"@e",
					"null",
					":e",
					GmMetaModel.T.getTypeSignature(),
					"--name",
					"foobar"
			};
			// @formatter:on

			EntityFactory entityFactory = signature -> {
				if (signature.equals("options")) {
					return Maybe.complete(JinniOptions.T.create());
				} else
					return Maybe.complete(GMF.getTypeReflection().getEntityType(signature).create());
			};

			CommandLineParser cmdLineParser = new PosixCommandLineParser();
			JinniModelAccessoryFactory factory = new JinniModelAccessoryFactory();
			factory.setPlatformDomainModels(Lists.list( //
					getClassPathModel("com.braintribe.gm:record-model"), //
					getClassPathModel("com.braintribe.gm:manipulation-model"), //
					getClassPathModel("com.braintribe.gm:meta-model"), //
					getClassPathModel("tribefire.extension.setup:jinni-api-model"), //
					getClassPathModel("tribefire.extension.setup:platform-setup-api-model"), //
					getClassPathModel("com.braintribe.devrock.templates:artifact-template-service-model") //
			));

			ModelAccessory modelAccessory = factory.getForServiceDomain(PlatformRequest.platformDomainId);
			ParsedCommandLine commandLine = cmdLineParser.parse(args, entityFactory, modelAccessory);

			new YamlMarshaller().marshall(System.out, commandLine.listInstances(GenericEntity.T));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
