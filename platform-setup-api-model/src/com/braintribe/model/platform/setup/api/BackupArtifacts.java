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

import com.braintribe.model.generic.annotation.meta.Alias;
import com.braintribe.model.generic.annotation.meta.Description;
import com.braintribe.model.generic.eval.EvalContext;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.service.api.ServiceRequest;
import com.braintribe.model.service.api.result.Neutral;

/**
 * Backup concrete versions of artifacts.
 */
@Description("Backup concrete versions of artifacts.")
public interface BackupArtifacts extends SetupRequest {

	String artifacts = "artifacts";
	String file = "file";
	
	EntityType<BackupArtifacts> T = EntityTypes.T(BackupArtifacts.class);

	@Description("The artifact(s) to backup.")
	@Alias("artifacts")
	List<String> getArtifacts();
	void setArtifacts(List<String> artifacts);

	@Description("The YAML file that contains the artifact(s) to backup (each line starts with a dash followed by a space).")
	String getYamlFile();
	void setYamlFile(String yamlFile);

	@Description("Whether to generate maven-metadata.xml")
	boolean getGenerateMavenMetadata();
	void setGenerateMavenMetadata(boolean generateMavenMetadata);

	@Override
	EvalContext<? extends Neutral> eval(Evaluator<ServiceRequest> evaluator);

}
