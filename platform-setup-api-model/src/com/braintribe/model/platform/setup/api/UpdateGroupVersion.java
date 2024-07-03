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

import com.braintribe.model.generic.annotation.Initializer;
import com.braintribe.model.generic.annotation.meta.Description;
import com.braintribe.model.generic.eval.EvalContext;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.service.api.ServiceRequest;

/**
 * Updates the (major.minor) version of an artifact group. For further information see type and properties descriptions.
 * 
 * @author michael.lafite
 */
@Description("Updates the (major.minor) version of the specified artifact group, e.g. from 1.0 to 1.1 or 2.0."
		+ " This is done by iterating through all the artifacts in the specified artifact group folder and adapting the respective POM files."
		+ " The revisions of all artifacts will be reset, i.e. with the switch to version '2.0' the next published version of all artifacts in the group will be '2.0.1'."
		+ " The version to update to can either be specified directly, see 'version', or one can increment the major or minor version, see 'incrementMajor' and 'incrementMinor'."
		+ " These options cannot be combined.")
public interface UpdateGroupVersion extends SetupRequest {
	EntityType<UpdateGroupVersion> T = EntityTypes.T(UpdateGroupVersion.class);

	public static final String groupFolder = "groupFolder";
	
	@Override
	EvalContext<List<String>> eval(Evaluator<ServiceRequest> evaluator);

	@Description("The root folder of the artifact group to update. By default, the version update will be performed in the current working directory.")
	@Initializer("'.'")
	String getGroupFolder();
	void setGroupFolder(String groupFolder);

	@Description("The version to update to. Alternatively one may also specify to increment the major or minor version, see 'incrementMajor' and 'incrementMinor'.")
	String getVersion();
	void setVersion(String version);

	@Description("Enabling this increments the major version, e.g. from 2.3 to 3.0. Alternatively one may also specify a concrete version, see 'version' setting.")
	boolean getIncrementMajor();
	void setIncrementMajor(boolean incrementMajor);

	@Description("Enabling this increments the minor version, e.g. from 2.3 to 2.4. Alternatively one may also specify a concrete version, see 'version' setting.")
	boolean getIncrementMinor();
	void setIncrementMinor(boolean incrementMinor);
}
