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
package com.braintribe.doc;

import java.util.HashSet;
import java.util.Set;

import com.braintribe.doc.meta.TagDisplayInfo;

public class Tag {
	private final String id;
	
	private String name;
	private String description;
	private Set<MarkdownFile> taggedFiles = new HashSet<>();

	private TagDisplayInfo tagDisplayInfo;
	
	public Tag(TagDisplayInfo tagDisplayInfo) {
		this.tagDisplayInfo = tagDisplayInfo;
		this.id = tagDisplayInfo.getTagId();
		this.description = tagDisplayInfo.getDescription();
		this.name = tagDisplayInfo.getDisplayTitle();
	}
	
	public TagDisplayInfo getTagDisplayInfo() {
		return tagDisplayInfo;
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name == null ? id : name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Set<MarkdownFile> getTaggedFiles() {
		return taggedFiles;
	}
	
	public void addTaggedFile(MarkdownFile mdFile) {
		taggedFiles.add(mdFile);
	}
	
	public String getDocRelativeUrl() {
		return MarkdownCompiler.MDOC_TAGS_SUBPATH + getId() + ".html"; 
	}
	
}
