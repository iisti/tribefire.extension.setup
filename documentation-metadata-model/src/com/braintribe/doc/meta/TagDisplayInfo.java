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
 * @author pit
 *
 */
public interface TagDisplayInfo extends GenericEntity{
	EntityType<TagDisplayInfo> T = EntityTypes.T(TagDisplayInfo.class);

	/**
	 * @return - the description of the tag (to be shown where?) 
	 */
	String getDescription();
	void setDescription(String description);
	
	/**
	 * @return - the title of the tag's display (to be shown where?)
	 */
	String getDisplayTitle();
	void setDisplayTitle(String displayTitle);
	
	/**
	 * @return - the ID of the tag (correlates with {@link FileDisplayInfo#getTags()}?)
	 */
	@Mandatory
	String getTagId();
	void setTagId(String tagId);
	
	/**
	 * @return - the path (URL) to an image (to be shown where?)
	 */
	String getImageUrl();
	void setImageUrl(String assetSchemedUrl);
}
