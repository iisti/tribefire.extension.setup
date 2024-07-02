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
package com.braintribe.model.platform.setup.api;

import com.braintribe.model.generic.annotation.Initializer;
import com.braintribe.model.generic.annotation.meta.Description;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * Sets up a Tomcat instance which can be used to create a tribefire Docker image. It is used internally by
 * {@link BuildDockerImages}.
 *
 * @author michael.lafite
 */
@Description("Sets up a Tomcat instance which can be used to create tribefire Docker images."
		+ " Except for testing purposes there is usually no need to run this command directly. It is used internally by 'build-docker-images'.")
public interface SetupLocalTomcatPlatformForDocker extends SetupLocalTomcatPlatform {
	EntityType<SetupLocalTomcatPlatformForDocker> T = EntityTypes.T(SetupLocalTomcatPlatformForDocker.class);

	@Description("If enabled, container specific assets and settings will be omitted."
			+ " That means that the resulting Tomcat installation will only contain those assets and settings"
			+ " which are the same for all containers in a disjoint projection.")
	@Initializer("true")
	boolean getOmitContainerSpecificAssets();
	void setOmitContainerSpecificAssets(boolean omitContainerSpecificAssets);

	@Description("If enabled, values which change with each run of the setup command will be omitted."
			+ " This e.g. includes the timestamp in the setup descriptor. Purpose of this setting is that,"
			+ " as long as Jinni itself, the request settings and the assets do not change, also the created Tomcat"
			+ " installation stays the same, i.e. same files and file contents."
			+ " Note that, if a request property has a dynamic default such as a uuid, the property must be set"
			+ " to avoid changes in the request settings (see e.g. shutdownCommand).")
	@Initializer("true")
	boolean getOmitRunSpecificValues();
	void setOmitRunSpecificValues(boolean omitRunSpecificValues);
}
