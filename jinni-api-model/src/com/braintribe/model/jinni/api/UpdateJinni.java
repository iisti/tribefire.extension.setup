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
package com.braintribe.model.jinni.api;

import com.braintribe.model.generic.annotation.meta.Description;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

@Description("Updates the Jinni itself. Kind of. Downloads and extracts the \"latest\" (see 'version') Jinni into a sibling of the current Jinni installation dir.")
public interface UpdateJinni extends UpdateJinniRequest {

	EntityType<UpdateJinni> T = EntityTypes.T(UpdateJinni.class);

	@Description("The desired target Jinni version. "
			+ "As of right now, only the major.minor part is considered, and the highest version for given major.minor will be used for update."
			+ "\n\n" + "If no value is given here, current Jinni version will be used instead. "
			+ "This means if there is a newer Jinni version with a higher major.minor, you have to specify those explicitly.")
	String getVersion();
	void setVersion(String version);

}
