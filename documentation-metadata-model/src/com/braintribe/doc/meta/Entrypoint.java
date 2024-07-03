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
package com.braintribe.doc.meta;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.annotation.meta.Mandatory;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * 
 * @author pit
 *
 */
public interface Entrypoint extends GenericEntity {
	EntityType<Entrypoint> T = EntityTypes.T(Entrypoint.class);

	/**
	 * @return assetId in the format {@code $groupId:$artifactId}
	 */
	@Mandatory
	String getAssetId();
	void setAssetId(String assetId);

	/**
	 * @return the {@link CustomAssetMetaData} of the asset behind the entry point
	 */
	CustomAssetMetaData getDisplayInfo();
	void setDisplayInfo(CustomAssetMetaData assetConfig);

	/**
	 * @return - a derived relative path the translated data of the asset
	 */
	default String targetUrl() {
		String assetPath = getAssetId().replace(":", "/");
		// NOTE the displayInfo is set if not configured, thus this doesn't throw NPE even if the prop is not mandatory
		return assetPath + "/" + getDisplayInfo().getStartingPoint().replaceAll("\\.md$", ".html");
	}

}
