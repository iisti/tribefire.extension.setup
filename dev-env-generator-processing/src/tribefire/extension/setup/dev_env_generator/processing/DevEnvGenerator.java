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
package tribefire.extension.setup.dev_env_generator.processing;

import static com.braintribe.console.ConsoleOutputs.println;
import static com.braintribe.console.ConsoleOutputs.red;
import static com.braintribe.console.ConsoleOutputs.white;
import static com.braintribe.console.output.ConsoleOutputFiles.outputProjectionDirectoryTree;
import static java.util.Collections.emptyMap;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.braintribe.cfg.Required;
import com.braintribe.gm.model.reason.Maybe;
import com.braintribe.gm.model.reason.Reasons;
import com.braintribe.gm.model.reason.essential.AlreadyExists;
import com.braintribe.gm.model.reason.essential.InvalidArgument;
import com.braintribe.gm.model.reason.essential.IoError;
import com.braintribe.model.processing.service.api.OutputConfig;
import com.braintribe.model.processing.service.api.OutputConfigAspect;
import com.braintribe.model.processing.service.api.ReasonedServiceProcessor;
import com.braintribe.model.processing.service.api.ServiceRequestContext;
import com.braintribe.model.service.api.result.Neutral;
import com.braintribe.utils.FileTools;

import tribefire.extension.setup.dev_env_generator.processing.eclipse.EclipseWorkspaceHelper;
import tribefire.extension.setup.dev_env_generator.processing.eclipse.EclipseWorkspaceOrgJdtCorePrefs;
import tribefire.extension.setup.dev_env_generator.processing.eclipse.EclipseWorkspaceOrgJdtUiPrefs;
import tribefire.extension.setup.dev_env_generator.processing.eclipse.EclipseWorkspaceOrgUiIdePrefs;
import tribefire.extension.setup.dev_env_generator.processing.eclipse.EclipseWorkspaceTomcatPrefs;
import tribefire.extension.setup.dev_env_generator_api.model.CreateDevEnv;
import tribefire.extension.setup.dev_env_generator_config.model.DevEnvGeneratorConfig;

/**
 * Processor for {@link CreateDevEnv}.
 */
public class DevEnvGenerator implements ReasonedServiceProcessor<CreateDevEnv, Neutral> {

	private final Maybe<Neutral> OK = Maybe.complete(Neutral.NEUTRAL);

	private DevEnvGeneratorConfig config;

	private boolean verbose;

	@Required
	public void setConfiguration(DevEnvGeneratorConfig config) {
		this.config = config;
	}

	@Override
	public Maybe<Neutral> processReasoned(ServiceRequestContext requestContext, CreateDevEnv request) {
		verbose = requestContext.getAspect(OutputConfigAspect.class, OutputConfig.empty).verbose();
		if (verbose) {
			println("DevEnvGeneratorConfig read from dev-env-generator-config.yaml:");
			println("  - eclipse workspace template: " + config.getEclipseWorkspaceTemplate());
			println("  - repository-config template: " + config.getRepoConfTemplate());
		}

		String name = request.getName();
		File devEnv = new File(name);

		return OK
				.flatMap(r -> createDirectories(devEnv)) //
				.flatMap(r -> createDevEnv(devEnv)) //
				.flatMap(r -> createCommands(devEnv)) //
				.flatMap(r -> copyEclipseWorkspace(devEnv)) //
				.flatMap(r -> patchEclipseWorkspace(devEnv)) //
				.flatMap(r -> copyRepositoryConfiguration(devEnv)) //
				.flatMap(r -> output(devEnv));
	}

	private Maybe<Neutral> copyEclipseWorkspace(File devEnv) {
		String template = config.getEclipseWorkspaceTemplate();
		if (template == null) {
			println(red("No \"eclipseWorkspaceTemplate\" configured in \"dev-env-generator-config.yaml\". Only minimal workspace created. "));
			return OK;
		}

		File wspc = new File(template);
		if (!wspc.exists())
			return Reasons.build(InvalidArgument.T) //
					.text("The eclipseWorkspaceTemplate at \"" + template + "\" not found.") //
					.toMaybe();

		FileTools.copy(wspc) //
				.as(new File(devEnv + "/eclipse-workspace")) //
				.please();

		return OK;
	}

	private Maybe<Neutral> patchEclipseWorkspace(File devEnv) {
		File cfgdir = new File(devEnv, "eclipse-workspace/.metadata/.plugins/org.eclipse.core.runtime/.settings");
		if (!cfgdir.exists())
			cfgdir.mkdirs();

		String DEV_ENV_HOME = espaceWindowsPathForEclipse(devEnv.getAbsolutePath());

		String HICONIC_SDK_HOME = espaceWindowsPathForEclipse(resolveHiconicSdkHome());
		String TOMCAT_HOME = DEV_ENV_HOME + "/tf-setups/main/runtime/host";

		EclipseWorkspaceOrgJdtCorePrefs jdtCore = new EclipseWorkspaceOrgJdtCorePrefs(devEnv);
		printFileInfo(jdtCore);
		if (jdtCore.exists()) {
			jdtCore.patch(//
					"org.eclipse.jdt.core.classpathVariable.HICONIC_SDK_HOME=" + HICONIC_SDK_HOME + "\n" + //
							"org.eclipse.jdt.core.classpathVariable.DEV_ENV_HOME=" + DEV_ENV_HOME + "\n" + //
							"org.eclipse.jdt.core.classpathVariable.TOMCAT_HOME=" + TOMCAT_HOME, //

					Map.of( //
							"(?mi).*\\.hiconic_sdk_home=.*", "", //
							"(?mi).*\\.dev_env_home=.*", "", //
							"(?mi).*\\.tomcat_home=.*", "")
					); // remove line

		} else {
			// create it
			jdtCore.create(Map.of(//
					"@HICONIC_SDK_HOME@", HICONIC_SDK_HOME, //
					"@DEV_ENV_HOME@", DEV_ENV_HOME, //
					"@TOMCAT_HOME@", TOMCAT_HOME //
			));
		}

		EclipseWorkspaceOrgJdtUiPrefs jdtUi = new EclipseWorkspaceOrgJdtUiPrefs(devEnv);
		printFileInfo(jdtUi);
		if (!jdtUi.exists())
			jdtUi.create(emptyMap());

		EclipseWorkspaceTomcatPrefs tomcat = new EclipseWorkspaceTomcatPrefs(devEnv);
		printFileInfo(tomcat);
		if (tomcat.exists())
			// patch existing
			tomcat.patch( //
					"contextsDir=" + TOMCAT_HOME + "/conf/Catalina/localhost\n" + //
							"tomcatConfigFile=" + TOMCAT_HOME + "/conf/server.xml\n" + //
							"tomcatDir=" + TOMCAT_HOME + "\n",
					Map.of(//
							"(?mi)^\s*contextsDir=.*", "", // remove line
							"(?mi)^\s*tomcatConfigFile=.*", "", // remove line
							"(?mi)^\s*tomcatDir=.*", "")); // remove line

		else 
			tomcat.create(Map.of("@TOMCAT_HOME@", TOMCAT_HOME));

		EclipseWorkspaceOrgUiIdePrefs ui = new EclipseWorkspaceOrgUiIdePrefs(devEnv);
		printFileInfo(ui);
		if (ui.exists())
			ui.patch("WORKSPACE_NAME=" + devEnv, Map.of("(?mi)^\s*workspace_name=.*", ""));
		 else 
			ui.create(Map.of("@THENAME@", devEnv.toString()));

		return OK;
	}

	private void printFileInfo(EclipseWorkspaceHelper wsHelper) {
		if (verbose)
			println("cfg file: " + wsHelper.getCfgFile() + " exists: " + wsHelper.exists());
	}

	private String resolveHiconicSdkHome() {
		String result = System.getenv("HICONIC_SDK_HOME");
		if (result != null)
			return result;

		result = System.getenv("DEVROCK_SDK_HOME");
		if (result != null)
			return result;

		return "HICONIC_SDK_HOME_IS_UNKNOWN";
	}

	private String espaceWindowsPathForEclipse(String path) {
		return path.replace("\\", "/").replace(":", "\\:");
	}

	private Maybe<Neutral> copyRepositoryConfiguration(File devEnv) {
		String template = config.getRepoConfTemplate();
		if (template == null)
			return Reasons.build(InvalidArgument.T) //
					.text("No \"repoConfTemplate\" configured in \"dev-env-generator-config.yaml\". Requires fix.")
					.toMaybe();

		File cfg = new File(template);
		if (!cfg.exists()) 
			return Reasons.build(InvalidArgument.T) //
					.text("The repository configuration \"" + template + "\" not found.") //
					.toMaybe();
		
		if (!cfg.isFile())
			return Reasons.build(InvalidArgument.T) //
					.text("The repository configuration can only be a yaml file, but \"" + template + "\" was given.") //
					.toMaybe();

		try {
			Path target = Paths.get(devEnv.toString(), "/artifacts/repository-configuration.yaml");
			Files.copy(cfg.toPath(), target);

		} catch (IOException e) {
			return Reasons.build(IoError.T) //
					.text("Error while copying repository configuration: \"" + template + "\" was given.") //
					.toMaybe();
		}

		return OK;
	}

	private Maybe<Neutral> createCommands(File devEnv) {
		String fileName = devEnv + "/commands/setup-main.yaml";
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
			writer.write("!com.braintribe.model.platform.setup.api.SetupLocalTomcatPlatform {\n");
			writer.write("\tsetupDependency: \"<your-dependency>\",\n");
			writer.write("\tinstallationPath: \"${config.base}/../tf-setups/main\",\n");
			writer.write("}\n");
			writer.close();

		} catch (IOException e) {
			return Reasons.build(IoError.T) //
					.text("Error while creating dev-environment: \"" + fileName + "\".") //
					.toMaybe();
		}
		return OK;
	}

	private Maybe<Neutral> createDevEnv(File devEnv) {
		Path path = Paths.get(devEnv + "/dev-environment.yaml");
		try {
			Files.createFile(path);
		} catch (IOException e) {
			return Reasons.build(IoError.T).text("Error while creating dev-environment: \"" + path + "\".") //
					.toMaybe();
		}
		return OK;
	}

	private Maybe<Neutral> createDirectories(File devEnv) {
		List<File> dirs = Arrays.asList( //
				devEnv, //
				new File(devEnv, "artifacts"), //
				new File(devEnv, "artifacts/inst"), //
				new File(devEnv, "git"), //
				new File(devEnv, "commands"), //
				new File(devEnv, "eclipse-workspace"), //
				new File(devEnv, "tf-setups"), //
				new File(devEnv, "tf-setups/main") //
		);

		// first check
		for (File dir : dirs) {
			if (dir.exists()) {
				return Reasons.build(AlreadyExists.T).text("dev-env '" + devEnv + "', dir '" + dir + "' already exists.").toMaybe();
			}
		}
		// then create
		for (File dir : dirs)
			dir.mkdirs();

		return OK;
	}

	private Maybe<Neutral> output(File devEnv) {
		println(white("Installing:"));
		List<Path> foldPaths = new ArrayList<>();
		if (!verbose)
			foldPaths.add(Paths.get(devEnv.toString(), "eclipse-workspace"));
		outputProjectionDirectoryTree(devEnv.toPath(), foldPaths);
		return OK;
	}
}
