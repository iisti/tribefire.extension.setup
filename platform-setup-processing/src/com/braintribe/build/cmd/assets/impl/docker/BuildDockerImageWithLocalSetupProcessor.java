// ========================================================================
// Braintribe IT-Technologies GmbH - www.braintribe.com
// Copyright Braintribe IT-Technologies GmbH, Austria, 2002-2020 - All Rights Reserved
// It is strictly forbidden to copy, modify, distribute or use this code without written permission.
// To this file the Braintribe License Agreement applies.
// ========================================================================

package com.braintribe.build.cmd.assets.impl.docker;

import static com.braintribe.console.ConsoleOutputs.brightRed;
import static com.braintribe.console.ConsoleOutputs.println;
import static com.braintribe.console.ConsoleOutputs.sequence;
import static com.braintribe.console.ConsoleOutputs.text;
import static com.braintribe.console.ConsoleOutputs.yellow;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.braintribe.build.process.ProcessException;
import com.braintribe.build.process.ProcessExecution;
import com.braintribe.build.process.ProcessResults;
import com.braintribe.codec.marshaller.yaml.YamlMarshaller;
import com.braintribe.gm.model.reason.Maybe;
import com.braintribe.gm.model.reason.Reason;
import com.braintribe.gm.model.reason.essential.InvalidArgument;
import com.braintribe.gm.model.reason.essential.NotFound;
import com.braintribe.logging.Logger;
import com.braintribe.model.artifact.essential.VersionedArtifactIdentification;
import com.braintribe.model.platform.setup.api.BuildDockerImageWithLocalSetup;
import com.braintribe.model.platform.setup.api.SetupLocalTomcatPlatform;
import com.braintribe.model.platform.setup.api.data.SetupDescriptor;
import com.braintribe.model.service.api.result.Neutral;
import com.braintribe.utils.CollectionTools;
import com.braintribe.utils.FileTools;

/**
 * Processor for {@link BuildDockerImageWithLocalSetup}.
 * <p>
 * It builds and optionally pushes a single Docker image with a Hiconic application built via {@link SetupLocalTomcatPlatform} (see the request for
 * more details).
 */
public class BuildDockerImageWithLocalSetupProcessor {

	public static Maybe<Neutral> process(BuildDockerImageWithLocalSetup request) {
		return new BuildDockerImageWithLocalSetupProcessor(request).run();
	}

	private static final String DOCKERFILE_TEMPLATE = "Dockerfile-local-setup.template";
	private static final String DOCKERFILE_NAME = "Dockerfile-local-setup";

	private static final Logger log = Logger.getLogger(BuildDockerImageWithLocalSetupProcessor.class);

	private final BuildDockerImageWithLocalSetup request;

	private final File baseDir;
	private final File fDockerfile;
	private final File installationDir;
	private final File setupInfoFile;

	private String latestTag;
	private String versionedTag;

	private String dockerfile;

	private Reason error;

	private BuildDockerImageWithLocalSetupProcessor(BuildDockerImageWithLocalSetup request) {
		this.request = request;
		this.installationDir = new File(request.getInstallationPath());
		this.setupInfoFile = new File(installationDir, "setup-info/setup-descriptor.yaml");
		this.baseDir = installationDir.getParentFile();
		this.fDockerfile = new File(baseDir, DOCKERFILE_NAME);
	}

	private Maybe<Neutral> run() {
		println(sequence(text("Building Docker image for local setup in: "), yellow(installationDir.getAbsolutePath())));

		if (processOk())
			return Maybe.complete(Neutral.NEUTRAL);
		else
			return error.asMaybe();
	}

	private boolean processOk() {
		return validate() && //
				resolveImageTags() && //
				prepareDockerfile() && //
				buildLatestDockerImage() && //
				tagLatestDockerImageWithVersion() && //
				pushDockerImagesIfRequested();
	}

	// ######################################################
	// ## . . . . . . . . . Validation . . . . . . . . . . ##
	// ######################################################

	private boolean validate() {
		return verifyExistingDirectory(installationDir, "installationPath", request.getInstallationPath()) && //
				verifyExistingFile(setupInfoFile);
	}

	private boolean verifyExistingDirectory(File file, String whatItIs, String parameterGiven) {
		if (file.isDirectory())
			return true;

		if (!file.exists())
			error = NotFound.create("No directory exists for " + whatItIs + ": " + parameterGiven + ", full path: " + file.getAbsolutePath());
		else
			error = InvalidArgument.create("Given " + whatItIs + " is not a directory: " + parameterGiven + ", full path:" + file.getAbsolutePath());

		return false;
	}

	private boolean verifyExistingFile(File file) {
		if (file.exists())
			return true;

		error = NotFound.create("File " + file.getName() + " not found inside the installation directory");
		return false;
	}

	// ######################################################
	// ## . . . . . . . . . Image tags . . . . . . . . . . ##
	// ######################################################

	private boolean resolveImageTags() {
		YamlMarshaller yamlMarshaller = new YamlMarshaller();
		SetupDescriptor setupDescriptor = (SetupDescriptor) FileTools.read(setupInfoFile).fromInputStream(yamlMarshaller::unmarshall);

		String primarySetupAsset = setupDescriptor.getPrimarySetupAsset();
		VersionedArtifactIdentification vai = VersionedArtifactIdentification.parse(primarySetupAsset);

		String imageName = request.getImageNameTemplate() //
				.replace("$GROUP_ID", vai.getGroupId()) //
				.replace("$ARTIFACT_ID", vai.getArtifactId());

		latestTag = imageName + ":latest";
		versionedTag = imageName + ":" + vai.getVersion();

		return true;
	}

	// ######################################################
	// ## . . . . . . . Preparing Dockerfile . . . . . . . ##
	// ######################################################

	private boolean prepareDockerfile() {
		println(sequence(text("Preparing Dockerfile: "), yellow(fDockerfile.getAbsolutePath())));

		loadDockerfileTemplateFromClasspath();
		resolveDockerfileTemplate();
		writeDockerfile();

		return true;
	}

	private void loadDockerfileTemplateFromClasspath() {
		try (InputStream in = getClass().getResourceAsStream(DOCKERFILE_TEMPLATE)) {
			dockerfile = new String(in.readAllBytes(), StandardCharsets.UTF_8);

		} catch (IOException e) {
			if (dockerfile == null)
				throw new RuntimeException("Error while reading " + DOCKERFILE_TEMPLATE + " from classpath", e);
			else
				// File was loaded, so exception on closing the stream can be ignored
				log.warn("Error while reading " + DOCKERFILE_TEMPLATE + " from classpath", e);
		}
	}

	private void resolveDockerfileTemplate() {
		dockerfile = dockerfile //
				.replace("${BASE_IMAGE}", request.getBaseImage()) //
				.replace("${INSTALLATION_PATH}", installationDir.getName()) //
		;
	}

	private void writeDockerfile() {
		FileTools.write(fDockerfile).string(dockerfile);
	}

	// ######################################################
	// ## . . . . . . Build/Push Docker image . . . . . . .##
	// ######################################################

	private boolean buildLatestDockerImage() {
		println("Building Docker image...");

		List<String> cmd = CollectionTools.getList("docker", "build", ".", "-t", latestTag, "-f", DOCKERFILE_NAME);

		if (request.getPullUpdatedBaseImage())
			cmd.add("--pull");

		if (request.getNoCache())
			cmd.add("--no-cache");

		return runCommand(cmd);
	}

	private boolean tagLatestDockerImageWithVersion() {
		println("Tagging latest Docker image with version...");

		return runCommand(Arrays.asList("docker", "tag", latestTag, versionedTag));
	}

	private boolean pushDockerImagesIfRequested() {
		if (!request.getPush()) {
			println("Will not push Docker images.");
			return true;
		}

		println("Pushing Docker images...");

		return runCommand(Arrays.asList("docker", "push", latestTag)) && //
				runCommand(Arrays.asList("docker", "push", versionedTag));
	}

	private boolean runCommand(List<String> cmd) {
		String cmdStringForLogging = cmd.stream().collect(Collectors.joining(" "));

		println(sequence(text("\nExecuting: "), yellow(cmdStringForLogging)));

		try {
			ProcessResults result = ProcessExecution.runCommand(cmd, baseDir, null, null);

			if (result.getNormalText() != null)
				println(result.getNormalText());

			if (result.getErrorText() != null)
				println(brightRed(result.getErrorText()));

			if (result.getRetVal() != 0) {
				error = Reason
						.create("Executing Docker command terminated with exit code " + result.getRetVal() + ". Command: " + cmdStringForLogging);
				return false;
			}

		} catch (ProcessException e) {
			error = Reason.create("Error executing Docker command: " + e.getMessage() + ". Command: " + cmdStringForLogging);
			log.error("Error while running command '" + cmdStringForLogging + "' in directory " + baseDir + "!", e);
			return false;
		}

		println("\nDocker command executed successfully.\n");

		return true;
	}

}
