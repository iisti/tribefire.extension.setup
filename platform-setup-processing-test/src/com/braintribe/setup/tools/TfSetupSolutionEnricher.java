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
package com.braintribe.setup.tools;

import com.braintribe.model.artifact.analysis.AnalysisArtifact;
import com.braintribe.model.artifact.essential.PartIdentification;

/**
 * @author peter.gazdik
 */
@FunctionalInterface
public interface TfSetupSolutionEnricher {

	void enrich(AnalysisArtifact solution, PartIdentification type);

	static TfSetupSolutionEnricher noEnrichingExpected = (s, t) -> {
		throw new UnsupportedOperationException(
				"Error in test. This test was configured in a way that no solution enriching should occur. But it has...");
	};

}
