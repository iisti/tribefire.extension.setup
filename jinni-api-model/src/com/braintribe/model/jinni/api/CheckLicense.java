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
package com.braintribe.model.jinni.api;

import com.braintribe.model.generic.annotation.Initializer;
import com.braintribe.model.generic.annotation.meta.Alias;
import com.braintribe.model.generic.annotation.meta.Description;
import com.braintribe.model.generic.annotation.meta.Mandatory;
import com.braintribe.model.generic.annotation.meta.PositionalArguments;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.resource.FileResource;
import com.braintribe.model.service.api.PlatformRequest;

@Description("Check particular directory for correct licensing.")
@PositionalArguments({ "dir" })
public interface CheckLicense extends PlatformRequest {
	EntityType<CheckLicense> T = EntityTypes.T(CheckLicense.class);

	@Description("The directory from which the content should be checked")
	@Alias("d")
	@Mandatory
	FileResource getDir();
	void setDir(FileResource dir);

	@Description("Only checking. No modifications.")
	@Alias("c")
	@Initializer("true")
	Boolean getOnlyChecking();
	void setOnlyChecking(Boolean onlyChecking);

	@Description("Fix Windows-style CRLF. ")
	@Alias("w")
	@Initializer("false")
	Boolean getFixCRLF();
	void setFixCRLF(Boolean fixCRLF);

	@Description("Cleanup JavaDoc Authors")
	@Alias("j")
	@Initializer("false")
	Boolean getJDocCleanup();
	void setJDocCleanup(Boolean jdocCleanup);
}
