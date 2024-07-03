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
package com.braintribe.model.platform.setup.api;

import com.braintribe.model.generic.annotation.Initializer;
import com.braintribe.model.generic.annotation.meta.Confidential;
import com.braintribe.model.generic.annotation.meta.Mandatory;
import com.braintribe.model.generic.eval.EvalContext;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.service.api.ServiceRequest;

public interface Encrypt extends SetupRequest {
	EntityType<Encrypt> T = EntityTypes.T(Encrypt.class);
	
	@Initializer("'AES/CBC/PKCS5Padding'")
	String getAlgorithm();
	void setAlgorithm(String algorithm);
	
	@Initializer("'c36e99ec-e108-11e8-9f32-f2801f1b9fd1'")
	String getSecret();
	void setSecret(String secret);
	
	@Initializer("'PBKDF2WithHmacSHA1'")
	String getKeyFactoryAlgorithm();
	void setKeyFactoryAlgorithm(String keyFactoryAlgorithm);
	
	@Initializer("128")
	int getKeyLength();
	void setKeyLength(int keyLength);

	@Confidential
	@Mandatory
	String getValue();
	void setValue(String value);
	
	@Override
	EvalContext<String> eval(Evaluator<ServiceRequest> evaluator);
}
