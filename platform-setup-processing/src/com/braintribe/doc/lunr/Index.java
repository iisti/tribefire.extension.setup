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
package com.braintribe.doc.lunr;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * An index contains the built index of all documents and provides a query interface to the index.
 *
 * Usually instances of lunr.Index will not be created using this constructor, instead lunr.Builder should be used to
 * construct new indexes, or lunr.Index.load should be used to load previously built and serialized indexes.
 *
 * @constructor
 * @param {Object}
 *            attrs - The attributes of the built search index.
 * @param {Object}
 *            attrs.invertedIndex - An index of term/field to document reference.
 * @param {Object<string,
 *            lunr.Vector>} attrs.fieldVectors - Field vectors
 * @param {lunr.TokenSet}
 *            attrs.tokenSet - An set of all corpus tokens.
 * @param {string[]}
 *            attrs.fields - The names of indexed document fields.
 * @param {lunr.Pipeline}
 *            attrs.pipeline - The pipeline to use for search terms.
 */

public class Index {

	public Map<String, Map<String, Object>> invertedIndex;
	public Map<String, Vector> fieldVectors;
	public TokenSet tokenSet;
	public List<String> fields;
	public Pipeline pipeline;

	public Index(Map<String, Map<String, Object>> invertedIndex, Map<String, Vector> fieldVectors, TokenSet tokenSet, List<String> fields,
			Pipeline pipeline) {
		super();
		this.invertedIndex = invertedIndex;
		this.fieldVectors = fieldVectors;
		this.tokenSet = tokenSet;
		this.fields = fields;
		this.pipeline = pipeline;
	}

	/**
	 * Prepares the index for JSON serialization.
	 *
	 * The schema for this JSON blob will be described in a separate JSON schema file.
	 *
	 * @returns {Object}
	 */
	public Map<String, Object> toJSON() {
		List<List<Object>> invertedIndex = this.invertedIndex.entrySet().stream().sorted((e1, e2) -> e1.getKey().compareTo(e2.getKey()))
				.map(e -> Arrays.<Object> asList(e.getKey(), e.getValue())).collect(Collectors.toList());

		List<List<Object>> fieldVectors = this.fieldVectors.entrySet().stream().map(e -> Arrays.<Object> asList(e.getKey(), e.getValue().toJSON()))
				.collect(Collectors.toList());

		Map<String, Object> json = new LinkedHashMap<String, Object>();

		json.put("version", Lunr.version);
		json.put("fields", fields);
		json.put("fieldVectors", fieldVectors);
		json.put("invertedIndex", invertedIndex);
		json.put("pipeline", pipeline.toJSON());

		return json;
	}
}
