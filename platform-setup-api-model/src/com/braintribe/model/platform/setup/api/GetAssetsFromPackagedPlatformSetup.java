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
 * Gets assets by {@link #getNature() nature} from a given platform setup package, which has to be built separately
 * before (see {@link PackagePlatformSetup}).
 * 
 * @author michael.lafite
 */
@Description("Searches for assets of the specified nature in the given packaged platform setup and lists the fully qualified names (inluding versions).")
public interface GetAssetsFromPackagedPlatformSetup extends SetupRequest {
	EntityType<GetAssetsFromPackagedPlatformSetup> T = EntityTypes.T(GetAssetsFromPackagedPlatformSetup.class);

	@Override
	EvalContext<List<String>> eval(Evaluator<ServiceRequest> evaluator);

	@Description("Points to the file which describes the packaged platform setup.")
	@Initializer("'package/packaged-platform-setup.json'")
	String getPackagedPlatformSetupFilePath();
	void setPackagedPlatformSetupFilePath(String packagedPlatformSetupFilePath);

	@Description("Specifies the nature of the assets to get. If not set, all assets will be returned.")
	String getNature();
	void setNature(String nature);

	@Description("Whether or not to include revision in the qualified names that are returned.")
	boolean getIncludeRevision();
	void setIncludeRevision(boolean includeRevision);
}
