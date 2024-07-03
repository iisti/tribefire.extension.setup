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

public class EclipseWorkspaceOrgUiIdePrefs extends EclipseWorkspaceHelper {

	public EclipseWorkspaceOrgUiIdePrefs(File devEnv) {

		super(devEnv, //
				".metadata/.plugins/org.eclipse.core.runtime/.settings", // folder name in eclipse-workspace
				"org.eclipse.ui.ide.prefs", // file name
				/// content IF CREATED FROM SCRATCH:
				"""
				WORKSPACE_NAME=@THENAME@
				eclipse.preferences.version=1
				quickStart=false
				tipsAndTricks=true
				""");

	}

}
