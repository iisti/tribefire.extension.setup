package com.braintribe.build.cmd.assets.impl;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import com.braintribe.model.platform.setup.api.RestoreBackup;
import com.braintribe.utils.FileTools;

import net.lingala.zip4j.ZipFile;

/**
 * Processor for {@link RestoreBackup}
 */
public class RestoreBackupProcessor {

	public static String process(RestoreBackup request) {

		Instant start = Instant.now();

		File installationFolder = new File(request.getInstallationFolder());
		if (installationFolder.exists()) 
			backupExistingInstallationFolderIfDesiredOrFail(request, installationFolder);

		File backupFile = new File(request.getBackupArchive());
		if (!backupFile.exists())
			throw new IllegalStateException("Backup file " + backupFile.getAbsolutePath() + " not found!");

		unzipArchive(backupFile, installationFolder);

		Instant finish = Instant.now();
		long timeElapsed = Duration.between(start, finish).getSeconds();

		return "Successfully restored backup: " + installationFolder.getAbsolutePath() //
				+ "\nDuration: " + timeElapsed + " secs";
	}

	private static void backupExistingInstallationFolderIfDesiredOrFail(RestoreBackup request, File installationFolder) {
		File tribefirePropertiesFile = new File(installationFolder, "conf/tribefire.properties");
		if (!tribefirePropertiesFile.exists())
			throw new IllegalStateException(
					"Specified folder (" + installationFolder.getAbsolutePath() + ") is not a valid installation folder (file "
							+ tribefirePropertiesFile.getAbsolutePath() + " not found)! Please try again with a valid installation path.");

		if (!request.getForce())
			throw new IllegalStateException("Warning! The installation folder " + installationFolder.getAbsolutePath()
					+ " already exists! Please move or delete the folder before running a restore command, or use the --force flag.");

		try {
			String timeStamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
			File backupInstallationFolder = new File(installationFolder.getAbsolutePath() + "-backup-" + timeStamp);
			FileTools.copyDirectoryUnchecked(installationFolder, backupInstallationFolder);
			FileTools.deleteDirectoryRecursively(installationFolder);

		} catch (IOException e) {
			throw new IllegalStateException("Failed while deleting " + installationFolder.getAbsolutePath());
		}
	}

	private static void unzipArchive(File src, File dst) {
		try (ZipFile zipFile = new ZipFile(src)) {
			zipFile.extractAll(dst.getAbsolutePath());

		} catch (IOException e) {
			throw new IllegalStateException("Failed while unzipping " + src.getAbsolutePath() + " to " + dst.getAbsolutePath(), e);
		}
	}
}
