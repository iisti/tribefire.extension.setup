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
package com.braintribe.model.platform.setup.api.cdp;

import com.braintribe.model.generic.annotation.Initializer;
import com.braintribe.model.generic.annotation.meta.Description;
import com.braintribe.model.generic.annotation.meta.Mandatory;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * A very simplified {@link CrossDomainPolicy} where one just configures a {@link #getTrustedDomain() trusted domain}
 * (optionally with wildcard). This domain is considered to be trusted and thus it is assumed that cross origin access
 * within that domain is safe.
 * 
 * @author michael.lafite
 */
public interface SimpleCrossDomainPolicy extends CrossDomainPolicy {
	EntityType<SimpleCrossDomainPolicy> T = EntityTypes.T(SimpleCrossDomainPolicy.class);

	@Mandatory
	@Initializer("''")
	@Description("The trusted domain within which access is considered to be safe, e.g. '*.example.com'.")
	String getTrustedDomain();
	void setTrustedDomain(String trustedDomain);
}
