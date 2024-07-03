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

import java.util.Set;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * @author pit - javadoc only
 *
 */
public interface FileDisplayInfo extends GenericEntity{
	EntityType<FileDisplayInfo> T = EntityTypes.T(FileDisplayInfo.class);

	/**
	 * @return - {@link String} to display as title (where is it shown?)
	 */
	String getDisplayTitle();
	void setDisplayTitle(String displayTitle);
	
	/**
	 * @return - {@link String} to act as a short description (where is it shown?)
	 */
	String getShortDescription();
	void setShortDescription(String shortDescription);
	
	/**
	 * @return - an int representing the 'priority' (what is the metric here? And what is the priority used for?)
	 */
	int getPriority();
	void setPriority(int priority);
	
	/**
	 * @return - true if it's to be hidden (why? from where?)
	 */
	boolean getHidden();
	void setHidden(boolean hidden);
	
	/**
	 * @return - a {@link Set} of {@link String} to act as 'tags' (for what? and where?)
	 */
	Set<String> getTags();
	void setTags(Set<String> tags);
	
	/**
	 * @return - a {@link Set} of {@link String} that represents 'boosted search terms' (what? why? where?)
	 */
	Set<String> getBoostedSearchTerms();
	void setBoostedSearchTerms(Set<String> boosted);
}
