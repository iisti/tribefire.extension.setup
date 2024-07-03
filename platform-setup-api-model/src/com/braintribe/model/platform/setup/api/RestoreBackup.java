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

import com.braintribe.model.generic.annotation.meta.Description;
import com.braintribe.model.generic.annotation.meta.Mandatory;
import com.braintribe.model.generic.eval.EvalContext;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.service.api.ServiceRequest;

/**
 * Restores a backup from an archived zip file. Restore will fail if installation folder is not empty unless
 * {@link #getForce()} is set to {@code true}. In that case, a backup folder of the installation folder is created and
 * restore overwrites its content.
 */
@Description("Restores a backup from an archived zip file which contains a tribefire installation.")
public interface RestoreBackup extends SetupRequest {
	EntityType<RestoreBackup> T = EntityTypes.T(RestoreBackup.class);

	@Override
	EvalContext<List<String>> eval(Evaluator<ServiceRequest> evaluator);

	@Description("The path of the backup file which contains a tribefire installation.")
	@Mandatory
	String getBackupArchive();
	void setBackupArchive(String backupArchive);

	@Description("The tribefire installation folder where the backup file will be extracted to.")
	@Mandatory
	String getInstallationFolder();
	void setInstallationFolder(String installationFolder);
	
	@Description("Whether or not to delete the installation folder if a previous tribefire installation is found.")
	boolean getForce();
	void setForce(boolean force);
}
