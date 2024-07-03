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

import static com.braintribe.console.ConsoleOutputs.brightBlack;
import static com.braintribe.console.ConsoleOutputs.green;
import static com.braintribe.console.ConsoleOutputs.println;
import static com.braintribe.console.ConsoleOutputs.sequence;
import static com.braintribe.console.ConsoleOutputs.text;
import static com.braintribe.console.ConsoleOutputs.yellow;

import com.braintribe.console.ConsoleOutputs;
import com.braintribe.console.output.ConfigurableConsoleOutputContainer;
import com.braintribe.console.output.ConsoleOutput;
import com.braintribe.console.output.ConsoleOutputContainer;
import com.braintribe.utils.lcd.StringTools;

import tribefire.cortex.asset.resolving.ng.impl.ArtifactOutputs;

/**
 * Utility class for managing console output, to make printing output easier, and more consistent from style perspective.
 * 
 * <h3>Naming conventions</h3>
 * 
 * Methods that return a {@link ConsoleOutput} should simply be called by what they are printing, e.g. {@link #property(String, String)}.
 * <p>
 * Methods that print directly should start with "out", e.g. {@link #outProperty}.
 * 
 * 
 * @see TfSetupTools
 * 
 * @author peter.gazdik
 */
public abstract class TfSetupOutputs extends ArtifactOutputs {

	public static void outProperty(String name, String value) {
		println(property(name, value));
	}

	public static ConsoleOutputContainer property(String name, String value) {
		return sequence(yellow(name + ": "), text(value));
	}

	public static ConsoleOutput version(String version) {
		return ArtifactOutputs.version(version);
	}

	public static ConsoleOutput warning(String text) {
		return yellow(text);
	}

	public static ConsoleOutput fileName(String fileName) {
		return ArtifactOutputs.fileName(fileName);
	}

	// temp
	
	@Deprecated
	public static ConfigurableConsoleOutputContainer solutionOutput(String groupId, String artifactId, String version) {
		return solution(groupId, artifactId, version);
	}

	/** {@code groupId} and {@code version} are both optional. */
	public static ConfigurableConsoleOutputContainer solution(String groupId, String artifactId, String version) {
		ConfigurableConsoleOutputContainer configurableSequence = ConsoleOutputs.configurableSequence();

		if (groupId != null)
			configurableSequence.append(brightBlack(groupId + ":"));

		configurableSequence.append(text(artifactId));

		if (!StringTools.isEmpty(version))
			configurableSequence //
					.append(brightBlack("#")) //
					.append(isSnapshot(version) ? yellow(version) : green(version));

		return configurableSequence;
	}

	private static boolean isSnapshot(String version) {
		return version.endsWith("-pc") || version.endsWith("-SNAPSHOT");
	}
	
}
