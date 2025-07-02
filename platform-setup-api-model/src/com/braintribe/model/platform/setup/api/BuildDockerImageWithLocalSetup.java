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

import com.braintribe.model.generic.annotation.Initializer;
import com.braintribe.model.generic.annotation.meta.Description;
import com.braintribe.model.generic.annotation.meta.FolderName;
import com.braintribe.model.generic.annotation.meta.Mandatory;
import com.braintribe.model.generic.eval.EvalContext;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.service.api.ServiceRequest;
import com.braintribe.model.service.api.result.Neutral;

/**
 * Builds a Docker images containing a Hiconic application prepared with {@link SetupLocalTomcatPlatform}.
 * <p>
 * It takes the YML file with a {@link SetupLocalTomcatPlatform} as input, and assumes this request was already processed (by a previous call).
 * <p>
 * Note that the task creates a file called `Dockerfile-local-setup` in the parent directory of the {@link #getInstallationPath()}.
 */
@Description("Builds and optionally pushes a Docker image with a (previously installed) local setup.")
public interface BuildDockerImageWithLocalSetup extends SetupRequest {

	EntityType<BuildDockerImageWithLocalSetup> T = EntityTypes.T(BuildDockerImageWithLocalSetup.class);

	@Override
	EvalContext<Neutral> eval(Evaluator<ServiceRequest> evaluator);

	@Description("The directory that was used as installationPath for `setup-local-tomcat-platform`.")
	@FolderName
	@Mandatory
	String getInstallationPath();
	void setInstallationPath(String installationPath);

	// FYI: We use _GROUP_ID_ as a placeholder, not $GROUP_ID, cause passing a $ would have to be escape on a CLI
	@Description("The template for the name image being built, which may contain `_GROUP_ID_` and `_ARTIFACT_ID_`	placeholders. "
			+ " Two tags will be used, one based on the setup version and `latest`. Final values are passed as  `-t` argument of `docker build`.")
	@Mandatory
	String getImageNameTemplate();
	void setImageNameTemplate(String imageNameTemplate);

	@Description("If set, the build process will add this additional tag besides the `latest` and `version`, which will not be pushed. "
			+ "This is useful e.g. for pipelines that want to further handle the built image and need to know the name to reference it. "
			+ "Passed as: `docker tag ${latestTag} ${additionalImageName}`.")
	String getAdditionalImageName();
	void setAdditionalImageName(String additionalImageName);

	@Description("Whether or not to push Docker images after building.")
	boolean getPush();
	void setPush(boolean push);

	@Description("Specifies the base image, i.e. the image used in the FROM instruction of the Dockerfile.")
	@Initializer("'ghcr.io/hiconic-os/runtime-base:latest'")
	String getBaseImage();
	void setBaseImage(String baseImage);

	@Description("Specifies the `--label` argument of `docker build`.")
	String getLabel();
	void setLabel(String label);

	@Description("Whether or not to pull the base image, if a newer version is available. Adds `--pull` to `docker build`.")
	@Initializer("true")
	boolean getPullUpdatedBaseImage();
	void setPullUpdatedBaseImage(boolean pullUpdatedBaseImage);

	@Description("Whether or not to use Docker's cache when building the Docker images."
			+ " Except for very special cases where one needs to (temporarily) disable the cache, this should always be enabled."
			+ "Adds `--no-cache` to `docker build`.")
	boolean getNoCache();
	void setNoCache(boolean noCache);

}
