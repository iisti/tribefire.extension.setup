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

import com.braintribe.model.generic.annotation.meta.Description;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.service.api.PlatformRequest;

@Description("Jinni history extension. When run without a parameter, last service requests are displayed. " +
		     "When run with the integer 'repeat' parameter, the corresponding historized service will be executed again." + 
			 "When run with 'repeat' and 'alias' parameters, the corresponding historized service will be stored under the provided alias.")
public interface History extends PlatformRequest {
	EntityType<History> T = EntityTypes.T(History.class);

	@Description("If set, the history service will attempt to repeat the service call corresponding to the number of this parameter. See 'jinni history' for more details.")
	@com.braintribe.model.generic.annotation.meta.Alias("r")
	String getRepeat();
	void setRepeat(String repeat);

	@Description("If set, the history service will attempt to create an alias corresponding to the history entry. See 'jinni history' for more details.")
	@com.braintribe.model.generic.annotation.meta.Alias("a")
	String getAlias();
	void setAlias(String repeat);

}
