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
package com.braintribe.doc;

public class TitledLink {
	private final String text;
	private final String url;
	private final String title;

	public TitledLink(String text, String url, String title) {
		super();
		this.text = text;
		this.url = url;
		this.title = title;
	}

	public TitledLink(String text, String url) {
		this(text, url, text);
	}

	public TitledLink(String name) {
		this(name, null);
	}

	public String getTitle() {
		return title;
	}

	public String getText() {
		return text;
	}

	public String getUrl() {
		return url;
	}
}
