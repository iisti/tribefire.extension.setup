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

import com.braintribe.model.generic.annotation.Abstract;
import com.braintribe.model.generic.annotation.Initializer;
import com.braintribe.model.generic.annotation.meta.Alias;
import com.braintribe.model.generic.annotation.meta.Description;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

@Abstract
public interface PlatformSetupConfig extends SetupDependencyConfig {

	EntityType<PlatformSetupConfig> T = EntityTypes.T(PlatformSetupConfig.class);

	@Description("Projects each web context asset to its own runtime container."
			+ " The container name is based on the web context asset's name. See also shorten-runtime-container-names.")
	boolean getDisjointProjection();
	void setDisjointProjection(boolean disjointProjection);

	@Description("Whether or not to shorten runtime container names for implicitly created containers (see disjoint-projection)."
			+ " For example, if the long container name is 'tribefire.extension.example.example-cartridge' the shortened name will be 'example-cartridge'.")
	@Initializer("true")
	boolean getShortenRuntimeContainerNames();
	void setShortenRuntimeContainerNames(boolean shortenRuntimeContainerNames);

	@Description("Deprecated, use 'debugJava' instead.")
	@Deprecated
	boolean getPrepareForDebugging();
	void setPrepareForDebugging(boolean prepareForDebugging);

	@Description("If set, the setup will not deploy your TF inside Tomcat's webapps folder, but creates/updates an equivalent Tomcat"
			+ " project of given name for your (Eclipse) IDE. This project can be found in the 'debug' folder."
			+ " The expected format is ${groupId}:${artifactId}#${version}, with groupId and version being optional."
			+ " Default groupId is 'tribefire.synthetic' and default version is '1.0'.")
	String getDebugProject();
	void setDebugProject(String debugProject);

	@Description("If true, the setup will NOT deploy your TF inside Tomcat's webapps folder, but creates/updates a Tomcat project"
			+ " with a name derived from your setup artifact, namely ${groupId}:${artifactId}-debug#${version}."
			+ "This project can be found in the 'debug' folder.")
	@Alias("djs")
	boolean getDebugJava();
	void setDebugJava(boolean debugJava);

	@Description("If enabled, known logging frameworks like slf4j, log4j, commons-logging or logback are being considered when computing modules' classpaths."
			+ " Expected libraries such as slf4j-api are auto-excluded (i.e. treated as if they were excluded by the module), because the web platform anyway provides them."
			+ " (If disabled, one either has to exclude respective libraries explicitly or make sure that versions match the versions of the libraries on the web platform."
			+ " This is tedious and error-prone and therefore not recommended!)"
			+ " For some logging libraries, which are not expected to be found on the classpath, e.g. logging framework implementations such as log4j-core,"
			+ " the setup will fail early with a proper explanation. In case no exception is thrown, all interventions are logged (printed).")
	@Initializer("true")
	boolean getPreProcessClasspaths();
	void setPreProcessClasspaths(boolean preProcessClasspaths);

	@Description("If set, the js-libraries folder will use symbolic links to the library cache or project sources directly instead"
			+ " of copied files from the library cache.")
	@Alias("djs")
	boolean getDebugJs();
	void setDebugJs(boolean debugJs);

	@Description("Specifies environment configuration in form of system properties being added to 'tribefire.properties'.")
	Map<String, String> getRuntimeProperties();
	void setRuntimeProperties(Map<String, String> runtimeProperties);

}
