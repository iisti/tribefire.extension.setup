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
import com.braintribe.model.generic.annotation.meta.Alias;
import com.braintribe.model.generic.annotation.meta.Description;
import com.braintribe.model.generic.annotation.meta.Mandatory;
import com.braintribe.model.generic.eval.EvalContext;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.service.api.ServiceRequest;
import com.braintribe.model.service.api.result.Neutral;

public interface SetupRepositoryConfiguration extends SetupRequest {

	EntityType<SetupRepositoryConfiguration> T = EntityTypes.T(SetupRepositoryConfiguration.class);

	@Mandatory
	@Alias("views")
	List<String> getRepositoryViews();
	void setRepositoryViews(List<String> repositoryViews);

	@Initializer("'repository-configuration'")
	String getInstallationPath();
	void setInstallationPath(String installationPath);

	@Description("If enabled, wraps artifact filters with the standard development view artifact filter."
			+ " This restricts only the specified groups, but e.g. makes it possible to add new extensions or third party libraries.")
	boolean getEnableDevelopmentMode();
	void setEnableDevelopmentMode(boolean enableDevelopmentMode);

	@Override
	EvalContext<? extends Neutral> eval(Evaluator<ServiceRequest> evaluator);

}
