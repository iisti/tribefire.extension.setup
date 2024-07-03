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

import java.util.Map;

import com.braintribe.model.generic.annotation.meta.Description;
import com.braintribe.model.generic.annotation.meta.Mandatory;
import com.braintribe.model.generic.eval.EvalContext;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.service.api.ServiceRequest;
import com.braintribe.model.service.api.result.Neutral;

/**
 * Restores backed up artifacts. See {@link BackupArtifacts} command.
 */
@Description("Restores backed up artifacts. See backup-artifacts command.")
public interface RestoreArtifacts extends SetupRequest {

	String folder = "folder";
	String user = "user";
	String password = "password";
	String url = "url";
	String changedRepositoryIds = "changedRepositoryIds";

	EntityType<RestoreArtifacts> T = EntityTypes.T(RestoreArtifacts.class);

	@Description("The folder that contains the artifacts to restore.")
	@Mandatory
	String getFolder();
	void setFolder(String folder);

	@Description("The user with access to target repositories.")
	@Mandatory
	String getUser();
	void setUser(String user);

	@Description("The password of the user with access to target repositories.")
	@Mandatory
	String getPassword();
	void setPassword(String password);

	@Description("The base url of the repository manager for all target repositories.")
	String getUrl();
	void setUrl(String url);

	@Description("The map of repository ids based on which artifacts can be restored to the mapped target repositories.")
	Map<String, String> getChangedRepositoryIds();
	void setChangedRepositoryIds(Map<String, String> changedRepositoryIds);

	@Override
	EvalContext<? extends Neutral> eval(Evaluator<ServiceRequest> evaluator);

}
