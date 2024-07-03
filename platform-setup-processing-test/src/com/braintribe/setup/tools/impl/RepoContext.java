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

import java.io.File;

import com.braintribe.devrock.model.repolet.content.RepoletContent;
import com.braintribe.devrock.repolet.launcher.Launcher;
import com.braintribe.logging.Logger;
import com.braintribe.utils.FileTools;

/**
 * @author peter.gazdik
 */
public class RepoContext implements AutoCloseable {

	public final String testName;
	public final String repoName;
	public final Launcher launcher;
	public final File repoFolder;

	public RepoContext(String testName, RepoletContent content) {
		this.testName = testName;
		this.repoName = "repo-" + testName;

		// @formatter:off
		this.repoFolder = new File("ignored/" + repoName);
		this.launcher = Launcher.build()
				.repolet()
//				.name(repoName)
				.name("archive")
					.descriptiveContent()
						.descriptiveContent(content)
					.close()
				.close()
			.done();
		// @formatter:on

		lanuch();
	}

	private void lanuch() {
		FileTools.deleteDirectoryRecursivelyUnchecked(repoFolder);
		FileTools.ensureFolderExists(repoFolder);

		launcher.launch();
	}

	private static final Logger log = Logger.getLogger(RepoContext.class);

	@Override
	public void close() {
		try {
			FileTools.deleteDirectoryRecursivelyUnchecked(repoFolder);
		} catch (Exception e) {
			log.warn("Unable to cleanup repo folder: " + repoFolder.getAbsolutePath());
		}
		launcher.shutdown();
	}

}
