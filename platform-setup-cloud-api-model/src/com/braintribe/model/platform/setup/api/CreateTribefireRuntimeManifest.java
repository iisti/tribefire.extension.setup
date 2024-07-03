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
package com.braintribe.model.platform.setup.api;

import java.util.List;
import java.util.Map;

import com.braintribe.model.generic.annotation.Initializer;
import com.braintribe.model.generic.annotation.meta.Alias;
import com.braintribe.model.generic.annotation.meta.Mandatory;
import com.braintribe.model.generic.eval.EvalContext;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.platform.setup.api.tfruntime.CustomComponentSettings;
import com.braintribe.model.platform.setup.api.tfruntime.Database;
import com.braintribe.model.service.api.ServiceRequest;
import com.braintribe.model.service.api.result.Neutral;

/**
 * Creates a tribefire runtime manifest file for a given platform setup package, which has to be built separately before (see
 * {@link PackagePlatformSetup}).
 *
 * @author michael.lafite
 */
public interface CreateTribefireRuntimeManifest extends SetupRequest {

	String apiVersion = "apiVersion";

	EntityType<CreateTribefireRuntimeManifest> T = EntityTypes.T(CreateTribefireRuntimeManifest.class);

	@Override
	EvalContext<Neutral> eval(Evaluator<ServiceRequest> evaluator);

	@Initializer("'package/packaged-platform-setup.json'")
	String getPackagedPlatformSetupFilePath();
	void setPackagedPlatformSetupFilePath(String packagedPlatformSetupFilePath);

	@Initializer("'tribefire-runtime.yaml'")
	@Alias("target")
	String getTargetFilePath();
	void setTargetFilePath(String targetFilePath);

	String getRuntimeName();
	void setRuntimeName(String runtimeName);

	String getNamespace();
	void setNamespace(String namespace);

	String getStage();
	void setStage(String stage);

	String getDomain();
	void setDomain(String domain);

	Map<String, String> getLabels();
	void setLabels(Map<String, String> labels);

	@Initializer("'local'")
	String getDatabaseType();
	void setDatabaseType(String databaseType);

	List<Database> getDatabases();
	void setDatabases(List<Database> databases);

	Database getDcsaConfig();
	void setDcsaConfig(Database dcsaConfig);

	@Initializer("'etcd'")
	String getBackendType();
	void setBackendType(String backendType);

	String getImageTag();
	void setImageTag(String imageTag);

	String getImagePrefix();
	void setImagePrefix(String imagePrefix);

	List<CustomComponentSettings> getCustomComponentsSettings();
	void setCustomComponentsSettings(List<CustomComponentSettings> customComponentsSettings);

	@Mandatory
	TribefireRuntimeManifestApiVersion getApiVersion();
	void setApiVersion(TribefireRuntimeManifestApiVersion apiVersion);
}
