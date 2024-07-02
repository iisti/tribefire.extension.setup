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

public class FieldRef {
	private static final char joiner = '/';
	public String docRef;
	public String fieldName;
	private String _stringValue;
	
	public FieldRef(String docRef, String fieldName, String stringValue) {
	  this.docRef = docRef;
	  this.fieldName = fieldName;
	  this._stringValue = stringValue;
	}
	
	public FieldRef(String docRef, String fieldName) {
		this.docRef = docRef;
		this.fieldName = fieldName;
	}

		

	public static FieldRef fromString(String s) {
	  int n = s.indexOf(joiner);

	  if (n == -1) {
	    throw new IllegalStateException("malformed field ref string");
	  }

	  String fieldName = s.substring(0, n);
	  String docRef = s.substring(n + 1);

	  return new FieldRef (docRef, fieldName, s);
	}

	@Override
	public String toString() {
	  if (this._stringValue == null) {
	    this._stringValue = fieldName + joiner + docRef;
	  }

	  return _stringValue;
	}

}
