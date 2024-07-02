// ============================================================================
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
// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
// 
// This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with this library; See http://www.gnu.org/licenses/.
// ============================================================================
package tribefire.extension.setup.dev_env_generator.processing.eclipse;

import java.io.File;

public class EclipseWorkspaceOrgJdtCorePrefs extends EclipseWorkspaceHelper {

	public EclipseWorkspaceOrgJdtCorePrefs(File devEnv) {

		super(devEnv, //
				".metadata/.plugins/org.eclipse.core.runtime/.settings", // folder name in eclipse-workspace
				"org.eclipse.jdt.core.prefs", // file name
				"""
				eclipse.preferences.version=1
				org.eclipse.jdt.core.classpathVariable.HICONIC_SDK_HOME=@HICONIC_SDK_HOME@
				org.eclipse.jdt.core.classpathVariable.DEV_ENV_HOME=@DEV_ENV_HOME@
				org.eclipse.jdt.core.classpathVariable.TOMCAT_HOME=@TOMCAT_HOME@
				org.eclipse.jdt.core.compiler.codegen.inlineJsrBytecode=enabled
				org.eclipse.jdt.core.compiler.codegen.targetPlatform=17
				org.eclipse.jdt.core.compiler.compliance=17
				org.eclipse.jdt.core.compiler.problem.assertIdentifier=error
				org.eclipse.jdt.core.compiler.problem.enumIdentifier=error
				org.eclipse.jdt.core.compiler.release=enabled
				org.eclipse.jdt.core.compiler.source=17
				""");

	}

}
