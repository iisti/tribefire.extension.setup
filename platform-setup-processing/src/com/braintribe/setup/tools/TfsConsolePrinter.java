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

import static com.braintribe.console.ConsoleOutputs.sequence;
import static com.braintribe.console.ConsoleOutputs.text;

import com.braintribe.console.ConsoleOutputs;
import com.braintribe.console.output.ConsoleOutput;

/**
 * Utility class for printing in the console using {@link ConsoleOutputs}. This class keeps track of the indentation and is thus suited for printing
 * out nested structures.
 * 
 * @author peter.gazdik
 */
public class TfsConsolePrinter {

	private static final String DEFAULT_TAB = "    ";

	private final String tab;
	private final boolean strict;

	private String indent = "";

	public TfsConsolePrinter() {
		this(DEFAULT_TAB);
	}

	public TfsConsolePrinter(String tab) {
		this(tab, true);
	}

	public TfsConsolePrinter(String tab, boolean strict) {
		this.tab = tab;
		this.strict = strict;
	}

	public TfsConsolePrinter up() {
		indent += tab;
		return this;
	}

	public TfsConsolePrinter down() {
		if (indent.isEmpty())
			if (strict)
				throw new IllegalStateException("Cannot decrease an already empty indent.");
			else
				return this;

		indent = indent.substring(tab.length());
		return this;
	}

	public TfsConsolePrinter newLine() {
		ConsoleOutputs.println("\n");
		return this;
	}

	public TfsConsolePrinter out(ConsoleOutput output) {
		ConsoleOutputs.println(sequence( //
				text(indent), //
				output //
		));
		return this;
	}

	public TfsConsolePrinter out(String text) {
		ConsoleOutputs.println(indent + text);
		return this;
	}

}
