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
package tribefire.extension.setup.dev_env_generator.processing.eclipse;

import java.io.File;

public class EclipseWorkspaceTomcatPrefs extends EclipseWorkspaceHelper {

	public EclipseWorkspaceTomcatPrefs(File devEnv) {
		super(devEnv, //
				".metadata/.plugins/org.eclipse.core.runtime/.settings", // folder name in eclipse-workspace
				"net.sf.eclipse.tomcat.prefs", // file name
				/// content, IF created new from scratch:
				"""
				computeSourcePath=true
				eclipse.preferences.version=1
				jvmParameters=-Djava.util.logging.config.file%3Dconf%2Flogging.properties;-Djava.util.logging.manager%3Dcom.braintribe.logging.juli.BtClassLoaderLogManager;
				managerUrl=http\\://localhost\\:8080/manager
				contextsDir=@TOMCAT_HOME@/conf/Catalina/localhost
				tomcatConfigFile=@TOMCAT_HOME@/conf/server.xml
				tomcatDir=@TOMCAT_HOME@
				tomcatVersion=tomcatV9
				""");
	}

}
