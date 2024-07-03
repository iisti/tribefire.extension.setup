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
package com.braintribe.tribefire.jinni;

import java.io.File;

import com.braintribe.model.processing.platform.setup.wire.contract.PlatformSetupDependencyEnvironmentContract;
import com.braintribe.ve.api.VirtualEnvironment;

public class JinniPlatformSetupDependencyEnvironment implements PlatformSetupDependencyEnvironmentContract {

	private File installationDir;

	private VirtualEnvironment virtualEnvironment;

	public JinniPlatformSetupDependencyEnvironment(File installationDir) {
		this.installationDir = installationDir;
	}

	public void setVirtualEnvironment(VirtualEnvironment virtualEnvironment) {
		this.virtualEnvironment = virtualEnvironment;
	}

	@Override
	public VirtualEnvironment virtualEnvironment() {
		return virtualEnvironment;
	}

	@Override
	public File installationDir() {
		return installationDir;
	}

}
