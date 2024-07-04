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
package com.braintribe.model.jinni.api;

import com.braintribe.model.generic.annotation.Initializer;
import com.braintribe.model.generic.annotation.meta.Description;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.service.api.PlatformRequest;

@Description("Updates the SDK itself. Kind of. Downloads the \"latest\" (see 'version') SDK and extracts the tools into a sibling of current tools dir.")
public interface UpdateSdk extends PlatformRequest {

	EntityType<UpdateSdk> T = EntityTypes.T(UpdateSdk.class);

	@Description("The desired target SDK version. "
			+ "As of right now, only the major.minor part is considered, and the highest version for given major.minor will be used for update.")
	@Initializer("'2.1'")
	String getVersion();
	void setVersion(String version);

}
