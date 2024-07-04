// ============================================================================
// This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with this library; See http://www.gnu.org/licenses/.
// ============================================================================
package com.braintribe.tribefire.jinni.support;

import static com.braintribe.console.ConsoleOutputs.println;
import static com.braintribe.console.ConsoleOutputs.red;
import static com.braintribe.console.ConsoleOutputs.sequence;
import static com.braintribe.console.ConsoleOutputs.text;
import static com.braintribe.model.service.api.result.Neutral.NEUTRAL;
import static com.braintribe.setup.tools.TfSetupOutputs.outProperty;
import static com.braintribe.setup.tools.TfSetupTools.artifactName;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import com.braintribe.build.cmd.assets.api.ArtifactResolutionContext;
import com.braintribe.cfg.Required;
import com.braintribe.model.artifact.compiled.CompiledArtifactIdentification;
import com.braintribe.model.artifact.essential.PartIdentification;
import com.braintribe.model.jinni.api.UpdateSdk;
import com.braintribe.model.processing.service.api.ServiceProcessor;
import com.braintribe.model.processing.service.api.ServiceRequestContext;
import com.braintribe.model.resource.Resource;
import com.braintribe.model.service.api.result.Neutral;
import com.braintribe.setup.tools.TfSetupTools;
import com.braintribe.utils.FileTools;
import com.braintribe.utils.OsTools;
import com.braintribe.utils.UnixTools;

/**
 * Processor for {@link UpdateSdk}
 */
public class UpdateSdkProcessor extends AbstractUpdator implements ServiceProcessor<UpdateSdk, Neutral> {

	private static boolean BREAK = false;
	private static boolean CONTINUE = true;

	private File installationDir;

	@Required
	public void setJinniInstallationDir(File installationDir) {
		this.installationDir = installationDir;
	}

	@Override
	public Neutral process(ServiceRequestContext requestContext, UpdateSdk request) {
		withNewArtifactResolutionContext(arc -> doUpdate(request, arc));

		return NEUTRAL;
	}

	private void doUpdate(UpdateSdk request, ArtifactResolutionContext arc) {
		new SdkUpdater(request, arc).updateJinni();
	}

	private class SdkUpdater {

		private final PartIdentification ZIP_TYPE = PartIdentification.create("zip");

		private final String targetSdkMajorMinor;
		private final ArtifactResolutionContext resolutionContext;

		private File sdkDir;
		private CompiledArtifactIdentification latestSdk;
		private String latestSdkVersion;

		private File tools2Dir;
		private Resource sdkZip;

		public SdkUpdater(UpdateSdk request, ArtifactResolutionContext arc) {
			targetSdkMajorMinor = request.getVersion();
			resolutionContext = arc;
		}

		public void updateJinni() {
			if (tryJinniUpdate())
				printUpdateSuccessful();
		}

		private boolean tryJinniUpdate() {
			return resolveSdkDir() && //
					resolveLatestSdk() && //
					pepareFiles() && //
					extractSdkFiles();
			// makeExecutableIfOnUnix()*/;
		}

		private void printUpdateSuccessful() {
			outProperty("Jinni prepared in", tools2Dir.getAbsolutePath());
		}

		private boolean resolveSdkDir() {
			sdkDir = findToolsParentDir();
			if (sdkDir != null) {
				outProperty("SDK dir (based on jinni installation dir)", sdkDir.getAbsolutePath());
				return CONTINUE;
			}

			sdkDir = resolveDirFromEnv("HICONIC_SDK_HOME");
			if (sdkDir != null) {
				outProperty("SDK dir (based on HICONIC_SDK_HOME)", sdkDir.getAbsolutePath());
				return CONTINUE;
			}

			sdkDir = resolveDirFromEnv("DEVROCK_SDK_HOME");
			if (sdkDir != null) {
				outProperty("SDK dir (based on DEVROCK_SDK_HOME)", sdkDir.getAbsolutePath());
				return CONTINUE;
			}

			outError("SDK directory not found. Neither HICONIC_SDK_HOME nor DEVROCK_SDK_HOME is set.");
			return BREAK;
		}
		private File resolveDirFromEnv(String env) {
			String value = System.getenv(env);
			if (value == null)
				return null;
			File dir = new File(value);
			if (!dir.isDirectory())
				return null;
			return dir;
		}

		private File findToolsParentDir() {
			File dir = installationDir;
			while (true) {
				dir = dir.getParentFile();
				if (dir == null)
					return null;
				if ("tools".equals(dir.getName()))
					return dir.getParentFile();
			}
		}

		private boolean resolveLatestSdk() {
			this.latestSdk = resolutionContext.resolveArtifactIdentification(sdkArtifactNameWithTargetVersion());
			this.latestSdkVersion = latestSdk.getVersion().asString();

			outProperty("Latest found sdk version", latestSdkVersion);

			return CONTINUE;
		}

		private String sdkArtifactNameWithTargetVersion() {
			// Note that Malaclypse only considers major.minor, but otherwise resolves the latest revision.
			// So using currentJinni.version makes sense, it will resolve latest release with same major.minor
			return artifactName("tribefire.extension.setup", "hiconic-sdk", targetSdkMajorMinor);
		}

		private boolean pepareFiles() {
			tools2Dir = new File(sdkDir, "tools2");
			if (tools2Dir.exists()) {
				outError("\nTarget tools dir already exists: " + tools2Dir.getAbsolutePath());
				return BREAK;
			}

			sdkZip = resolutionContext.requirePart(latestSdk, ZIP_TYPE).getResource();

			return CONTINUE;
		}

		private boolean extractSdkFiles() {
			File tmpDir = new File(System.getProperty("java.io.tmpdir"));
			File downloadDir = new File(tmpDir, "/hiconic/downloads/sdk");
			if (downloadDir.exists())
				FileTools.deleteDirectoryRecursivelyUnchecked(downloadDir);

			outProperty("Extracting latest sdk", latestSdkVersion);
			TfSetupTools.unzipResource(sdkZip, downloadDir);

			File downloadedToolsDir = new File(downloadDir, "/hiconic-sdk/tools");

			try {
				Files.move(downloadedToolsDir.toPath(), tools2Dir.toPath(), StandardCopyOption.ATOMIC_MOVE);
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}

			outProperty("hiconic-sdk tools copied as tools2: ", sdkDir.getAbsolutePath());
			println(sequence(text("\nPlease rename the 'tools2' to 'tools' in order to use the new version of the SDK.")));

			return CONTINUE;
		}

		private void outError(String text) {
			println(sequence(red("Error: "), text(text)));
		}

		@SuppressWarnings("unused")
		private boolean makeExecutableIfOnUnix() {
			if (!OsTools.isUnixSystem())
				return CONTINUE;

			File binFolder = new File(tools2Dir, "bin");

			Stream<File> bashScriptStream = Stream.of(binFolder.listFiles()).filter(File::isFile).filter(this::isBashScript);

			UnixTools.setUnixFilePermissions(bashScriptStream, "rwxr--r--");

			return CONTINUE;
		}

		private boolean isBashScript(File file) {
			return !file.getName().endsWith(".bat");
		}
	}

}
