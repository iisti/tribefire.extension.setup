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
package com.braintribe.setup.tools.impl;

import static com.braintribe.utils.lcd.CollectionTools2.newMap;

import java.io.ByteArrayInputStream;
import java.util.Map;

import com.braintribe.devrock.model.repolet.content.Artifact;
import com.braintribe.devrock.model.repolet.content.Dependency;
import com.braintribe.devrock.model.repolet.content.RepoletContent;
import com.braintribe.model.artifact.essential.PartIdentification;
import com.braintribe.model.resource.Resource;

/**
 * @author peter.gazdik
 */
public class RepoContentBuilder {

	private final Resource emptyRes = Resource.createTransient(() -> new ByteArrayInputStream(new byte[0]));

	private final Map<String, Artifact> artifactByName = newMap();

	public RepoletContent build() {
		RepoletContent result = RepoletContent.T.create();
		result.getArtifacts().addAll(artifactByName.values());

		return result;

	}

	public Dependency addDependency(String name, String depName) {
		return addDependency(name, depName, null);
	}

	public Dependency addDependency(String name, String depName, String classifier) {
		Artifact artifact = acquireArtifact(name);

		Dependency result = newDependencyWithExistingArtifact(depName, classifier);
		artifact.getDependencies().add(result);
		return result;
	}

	private Dependency newDependencyWithExistingArtifact(String depName, String classifier) {
		Artifact artifact = acquireArtifact(depName);
		Dependency result = Dependency.parse(depName);
		if (classifier != null) {
			result.setClassifier(classifier);
			artifact.getParts().put(classifier + ":jar", emptyRes);
		}

		return result;
	}

	public Artifact requireArtifact(String condensedName) {
		return artifactByName.computeIfAbsent(condensedName, n -> {
			throw new IllegalStateException("Artifact does not exist: " + condensedName);
		});

	}

	public Artifact acquireArtifact(String condensedName) {
		return artifactByName.computeIfAbsent(condensedName, Artifact::from);
	}

	public void enrichArtifact(String condensedName, PartIdentification part, Resource resource) {
		acquireArtifact(condensedName).getParts().put(part.asString(), resource);
	}

}
