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
package com.braintribe.tribefire.jinni;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

import com.braintribe.exception.Exceptions;
import com.braintribe.ve.impl.OverridingEnvironment;
import com.braintribe.ve.impl.StandardEnvironment;

public class JinniEnvironment extends OverridingEnvironment {

	public static final String JINNI_LINE_WIDTH_VAR_NAME = "BT__JINNI_LINE_WIDTH";

	public JinniEnvironment(File file) {
		super(new StandardEnvironment());
		final Properties properties = new Properties();
		if (file != null && file.exists()) {
			try (Reader reader = new InputStreamReader(new FileInputStream(file), "UTF-8")) {
				properties.load(reader);
			} catch (Exception e) {
				throw Exceptions.unchecked(e, "Error while loading environment properties file: " + file.getAbsolutePath());
			}
		}
		properties.forEach((name, value) -> {
			setProperty((String) name, (String) value);
		});
	}
}
