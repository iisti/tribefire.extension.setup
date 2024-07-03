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
package com.braintribe.tribefire.jinni.support.request;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.braintribe.codec.marshaller.api.DecodingLenience;
import com.braintribe.codec.marshaller.api.GmDeserializationOptions;
import com.braintribe.codec.marshaller.yaml.YamlMarshaller;
import com.braintribe.logging.Logger;
import com.braintribe.model.service.api.ServiceRequest;

/**
 * Deals with storing and processing aliases.
 * 
 */
public abstract class RequestPersistenceManipulator {

	protected Path installationDir;

	protected static final String SEPARATOR = FileSystems.getDefault().getSeparator();
	private static final Logger LOG = Logger.getLogger(RequestPersistenceManipulator.class);

	public Path getDirectory() {
		return getServiceDirectory(getDirName());
	}

	protected abstract String getDirName();

	public ServiceRequest getServiceRequestFromFile(Path path) {
		try {
			return (ServiceRequest) new YamlMarshaller().unmarshall(Files.newBufferedReader(path),
					GmDeserializationOptions.deriveDefaults().setDecodingLenience(new DecodingLenience(true)).build());
		} catch (Exception ex) {
			LOG.error("Error when reading a persisted request. ", ex);
			return null;
		}
	}

	public String getRequestStringFromFile(Path path) throws IOException {
		return new String(Files.readAllBytes(path));
	}

	public void setInstallationDir(File installationDir) {
		this.installationDir = installationDir.toPath();
	}

	protected Path getServiceDirectory(String directory) {
		Path serviceDir = Paths.get(installationDir + SEPARATOR + directory);

		// create if the directory does not exist
		if (!Files.exists(serviceDir)) {
			LOG.info("Service directory " + serviceDir.toAbsolutePath() + " does not exist. Creating.");
			try {
				Files.createDirectories(serviceDir);
			} catch (IOException ioex) {
				LOG.error("Attempting to delete " + serviceDir.toAbsolutePath() + " caused an error: " + ioex.getMessage(), ioex);
				throw new IllegalStateException("Unable to retrieve or create " + serviceDir.toAbsolutePath() + "directory.", ioex);
			}
		}

		return serviceDir.normalize();
	}

}
