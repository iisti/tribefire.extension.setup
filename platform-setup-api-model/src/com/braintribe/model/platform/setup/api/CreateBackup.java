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
 * Creates a backup of a tribefire installation, archived as a zip file.
 */
@Description("Creates a backup of tribefire installation. Backup is archived as a zip file.")
public interface CreateBackup extends SetupRequest {
	EntityType<CreateBackup> T = EntityTypes.T(CreateBackup.class);

	@Override
	EvalContext<List<String>> eval(Evaluator<ServiceRequest> evaluator);

	@Description("The tribefire installation folder to back up.")
	@Mandatory
	String getInstallationFolder();
	void setInstallationFolder(String installationFolder);

	@Description("Whether to include the hostname in the backup file name.")
	boolean getIncludeHostName();
	void setIncludeHostName(boolean includeHostName);

	@Description("The folder path where the backup file will be stored.")
	String getBackupFolder();
	void setBackupFolder(String backupFolder);

	@Description("The name of the backup file. Default file name is [projectName]-[version]-[hostName]-[YYYYMMDD-HHMMSS].zip or [projectName]-[version]-[timestamp] if no hostname is found.")
	String getBackupFilename();
	void setBackupFilename(String backupFilename);

}
