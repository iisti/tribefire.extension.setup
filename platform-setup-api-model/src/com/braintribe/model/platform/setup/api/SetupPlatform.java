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

import com.braintribe.model.dcsadeployment.DcsaSharedStorage;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.annotation.Abstract;
import com.braintribe.model.generic.annotation.Initializer;
import com.braintribe.model.generic.annotation.meta.Description;
import com.braintribe.model.generic.eval.EvalContext;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.service.api.ServiceRequest;

@Abstract
public interface SetupPlatform extends FileSystemPlatformSetupConfig, SetupRequest {

	EntityType<SetupPlatform> T = EntityTypes.T(SetupPlatform.class);

	@Description("Delete the intermediate package containing prepared asset components after the setup has been finished.")
	@Initializer("true")
	boolean getDeletePackageBaseDir();
	void setDeletePackageBaseDir(boolean deletePackageBaseDir);

	@Description("Configures predefined components. If PredefinedComponent.DEFAULT_DB_CONNECTION is defined, all unconfigured components are derived from it.")
	Map<PredefinedComponent, GenericEntity> getPredefinedComponents();
	void setPredefinedComponents(Map<PredefinedComponent, GenericEntity> predefinedComponents);

	@Description("Custom components to be imported into the cortex database to make them available for further use.")
	List<GenericEntity> getCustomComponents();
	void setCustomComponents(List<GenericEntity> customComponents);

	@Description("Defines project specific parameters like the name of the project that is being setup.")
	ProjectDescriptor getProjectDescriptor();
	void setProjectDescriptor(ProjectDescriptor projectDescriptor);

	@Description("The SharedStorage implementation for system's (distributed) collaborative accesses. When empty, simple (non-distributed) CSA will be used instead.")
	DcsaSharedStorage getDcsaSharedStorage();
	void setDcsaSharedStorage(DcsaSharedStorage dcsaSharedStorage);

	@Override
	EvalContext<? extends SetupInfo> eval(Evaluator<ServiceRequest> evaluator);

}