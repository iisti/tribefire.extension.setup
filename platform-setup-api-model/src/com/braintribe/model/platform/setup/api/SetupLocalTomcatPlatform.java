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

import com.braintribe.model.asset.natures.LocalSetupTomcatConfig;
import com.braintribe.model.generic.annotation.meta.Description;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.platform.setup.api.cdp.CrossDomainPolicy;
import com.braintribe.model.platform.setup.api.logging.LoggingOptions;

@Description("Sets up a single tomcat instance based tribefire installation from a project and/or setupDependency."
		+ " The tomcat runtime template asset defined at 'tomcatAsset' is merged with the information of the SetupLocalTomcatPlatform "
		+ "request properties. Furthermore, the instance is enriched with prepared assets resulting from request PackagePlatformSetup.")
public interface SetupLocalTomcatPlatform extends SetupLocalPlatform, LocalSetupTomcatConfig, LoggingOptions {
	EntityType<SetupLocalTomcatPlatform> T = EntityTypes.T(SetupLocalTomcatPlatform.class);

	@Description("The Cross Domain Policy is used to define cross-domain related security settings," +
			" for example whether another web application is allowed to access contents of this application (e.g. via an embedded iframe)." + 
			" This affects HTTP security configuration such as" +
			" Cross-Origin Resource Sharing (CORS), Content Security Policy (CSP) or X-Frame-Options.")
	CrossDomainPolicy getCrossDomainPolicy();
	void setCrossDomainPolicy(CrossDomainPolicy crossDomainPolicy);
}
