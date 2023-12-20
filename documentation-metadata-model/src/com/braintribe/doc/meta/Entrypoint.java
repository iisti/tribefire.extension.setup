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
