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

import java.util.List;
import java.util.Map;

import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * {@link CustomDocMetaData} is used in the configuration artifact of a documentation-asset chain.
 * It declares its content, i.e. specifies what is to come to the landing-page
 * 
 * 
 * @author pit - javadoc only
 *
 */
public interface CustomDocMetaData extends CustomJavaDocMetaData {
	EntityType<CustomDocMetaData> T = EntityTypes.T(CustomDocMetaData.class);

	/**
	 * @return - a string to act as the title of the landing-page
	 */
	String getTitle();
	void setTitle(String title);
	
	/**
	 * @return - true if entry points should be generated automatically (why? what's the alternative? the entry points below?)
	 */
	boolean getAutoGenerateEntryPoints();
	void setAutoGenerateEntryPoints(boolean autoGenerateEntryPoints);
	
	/**
	 * @return - a {@link List} of {@link Entrypoint} making up the contents of the documentation, each entry-point
	 * stands for a contained documentation asset 
	 */
	List<Entrypoint> getEntrypoints();
	void setEntrypoints(List<Entrypoint> entrypoints);

	/**
	 * @return - a {@link List} of {@link TagDisplayInfo} (to be displayed where?)
	 */
	List<TagDisplayInfo> getTags();
	void setTags(List<TagDisplayInfo> tags);
	
	/**
	 * @return - a {@link Map} of String {@code (<groupId>:<artifactId>)} to {@link CustomAssetMetaData}. Per cfg, the metadata is null
	 */
	Map<String, CustomAssetMetaData> getAssets();
	void setAssets(Map<String, CustomAssetMetaData> assets);
}
