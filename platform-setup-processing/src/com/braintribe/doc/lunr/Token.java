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
package com.braintribe.doc.lunr;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * A token wraps a string representation of a token
 * as it is passed through the text processing pipeline.
 *
 * @constructor
 * @param {string} [str=''] - The string token being wrapped.
 * @param {object} [metadata={}] - Metadata associated with this token.
 */
public class Token {
	
	public String str;
	public Map<String, Object> metadata;
	
	public Token(String str, Map<String, Object> metadata) {
	  this.str = str != null? str: "";
	  this.metadata = metadata != null? metadata: new HashMap<>();
	}

	/**
	 * Returns the token string that is being wrapped by this object.
	 *
	 * @returns {string}
	 */
	@Override
	public String toString() {
	  return this.str;
	}

	/**
	 * A token update function is used when updating or optionally
	 * when cloning a token.
	 *
	 * @callback lunr.Token~updateFunction
	 * @param {string} str - The string representation of the token.
	 * @param {Object} metadata - All metadata associated with this token.
	 */

	/**
	 * Applies the given function to the wrapped string token.
	 *
	 * @example
	 * token.update(function (str, metadata) {
	 *   return str.toUpperCase()
	 * })
	 *
	 * @param {lunr.Token~updateFunction} fn - A function to apply to the token string.
	 * @returns {lunr.Token}
	 */
	public Token update(BiFunction<String, Map<String, Object>, String> fn) {
	  this.str = fn.apply(this.str, this.metadata);
	  return this;
	}

	/**
	 * Creates a clone of this token. Optionally a function can be
	 * applied to the cloned token.
	 *
	 * @param {lunr.Token~updateFunction} [fn] - An optional function to apply to the cloned token.
	 * @returns {lunr.Token}
	 */
	public Token clone(BiFunction<String, Map<String, Object>, String> fn) {
	  String s = fn != null? fn.apply(this.str, this.metadata): this.str; 
	  return new Token (s, this.metadata);
	}

}
