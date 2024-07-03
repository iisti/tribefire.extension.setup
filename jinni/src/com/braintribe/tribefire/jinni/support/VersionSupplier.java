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
package com.braintribe.tribefire.jinni.support;

import static com.braintribe.utils.lcd.CollectionTools2.nullSafe;

import java.io.File;
import java.util.Set;

public class VersionSupplier {

	public static String jinniVersion(File installationDir) {
		return versionOf("tribefire.extension.setup", "jinni", installationDir);
	}

	/** Returns a version of some artifact in the group. This makes sense if the caller only cares for major.minor. */
	public static String groupVersion(String groupId, File installationDir) {
		return packagedSolutions(installationDir).stream() //
				.filter(s -> s.groupId.equals(groupId)) //
				.map(s -> s.version) //
				.findFirst() //
				.orElseThrow(() -> new IllegalArgumentException(
						"Cannot determine version for group " + groupId + ", as no artifact from that group is used in Jinni itself."));
	}

	private static String versionOf(String groupId, String artifactId, File installationDir) {
		return packagedSolutions(installationDir).stream() //
				.filter(s -> s.groupId.equals(groupId) && s.artifactId.equals(artifactId)) //
				.map(s -> s.version) //
				.findFirst() //
				.orElse("unknown");
	}

	private static Set<PackagedSolution> packagedSolutions(File installationDir) {
		return nullSafe(PackagedSolution.readSolutionsFrom(installationDir));
	}

}
