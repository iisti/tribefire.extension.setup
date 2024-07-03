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
package com.braintribe.tribefire.jinni.core;

import com.braintribe.common.artifact.ArtifactReflection;
import com.braintribe.model.generic.GMF;
import com.braintribe.model.meta.GmMetaModel;

public class JinniTools {

	public static GmMetaModel getClassPathModel(ArtifactReflection artifactReflection) {
		return GMF.getTypeReflection().getModel(artifactReflection.name()).getMetaModel();
	}
	
	public static GmMetaModel getClassPathModel(String modelName) {
		return GMF.getTypeReflection().getModel(modelName).getMetaModel();
	}

	public static boolean isSnapshotVersion(String version) {
		return version.endsWith("-pc") || version.endsWith("-SNAPSHOT");
	}

}
