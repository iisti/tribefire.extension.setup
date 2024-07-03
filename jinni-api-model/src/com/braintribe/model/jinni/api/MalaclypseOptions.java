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
package com.braintribe.model.jinni.api;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.annotation.Abstract;
import com.braintribe.model.generic.annotation.meta.Description;

@Abstract
public interface MalaclypseOptions extends GenericEntity {
	@Description("Sets environment variable ARTIFACT_REPOSITORIES_USER_SETTINGS. "
			+ "See Malaclypse documentation for more information.")
	String getUserSettings();
	void setUserSettings(String userSettings);
	
	@Description("Sets environment variable ARTIFACT_REPOSITORIES_GLOBAL_SETTINGS. "
			+ "See Malaclypse documentation for more information.")
	String getGlobalSettings();
	void setGlobalSettings(String globalSettings);
	
	@Description("Sets environment variable ARTIFACT_REPOSITORIES_EXCLUSIVE_SETTINGS. "
			+ "See Malaclypse documentation for more information.")
	String getExclusiveSettings();
	void setExclusiveSettings(String exclusiveSettings);
	

	@Description("Sets environment variable MC_CONNECTIVITY_MODE to switch into offline mode. "
			+ "See Malaclypse documentation for more information.")
	Boolean getOffline();
	void setOffline(Boolean offline);

	@Description("Sets environment variable DEVROCK_REPOSITORY_CONFIGURATION to point to a repository configuration yaml.")
	String getRepositoryConfiguration();
	void setRepositoryConfiguration(String repositoryConfiguration);
}
