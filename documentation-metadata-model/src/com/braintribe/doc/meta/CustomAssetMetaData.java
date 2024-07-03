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
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * generic entity to represent a documentation asset 
 *  
 * @author pit - javadoc only
 *
 */
public interface CustomAssetMetaData extends GenericEntity {
	EntityType<CustomAssetMetaData> T = EntityTypes.T(CustomAssetMetaData.class);

	/**
	 * @return - true if it's supposed to be 'hidden' (from where?)
	 */
	boolean getHidden();
	void setHidden(boolean hidden);
	
	/**
	 * @return - a string to act as a title (shown in the landing page) 
	 */
	String getDisplayTitle();
	void setDisplayTitle(String displayTitle);
	
	/**
	 * @return - a string that represents the file name of the starting point (used in the left side menu) (html? i.e. after production)
	 */
	String getStartingPoint();
	void setStartingPoint(String startingPoint);
	
	/**
	 * @return - a string with a short description (shown in the landing page)
	 */
	String getShortDescription();
	void setShortDescription(String shortDescription);
	
	/**
	 * @return - a relative URL to the image to be used (where? what imagetypes?)  
	 */
	String getImageUrl();
	void setImageUrl(String assetRelativeUrl);
}
