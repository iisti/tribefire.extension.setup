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
package tribefire.extension.setup.dev_env_generator.processing.eclipse;

import java.io.File;
import java.util.Map;

import com.braintribe.gm.model.reason.Reasons;
import com.braintribe.gm.model.reason.UnsatisfiedMaybeTunneling;
import com.braintribe.gm.model.reason.essential.IoError;
import com.braintribe.utils.FileTools;

public abstract class EclipseWorkspaceHelper {

	private File cfgFile;
	private String content;

	EclipseWorkspaceHelper(File devEnv, String folder, String name, String content) {
		this.content = content;
		this.cfgFile = new File(devEnv + "/eclipse-workspace/" + folder + "/" + name);
	}

	public File getCfgFile() {
		return cfgFile;
	}
	public boolean exists() {
		return cfgFile.exists();
	}

	public void create(Map<String, String> substitutions) {
		String finalContent = getContent(content, substitutions);
		writeFile(cfgFile, finalContent);
	}

	/**
	 * Patches the content of an existing template file with additional information.
	 * 
	 * 
	 * @param custom
	 *            String to be added to template data (prepended)
	 * @param substitutions
	 *            Map of string-to-string to be replaced in original data template. This can be used for replacements, but
	 *            also to delete specific lines/data.
	 */
	public void patch(String custom, Map<String, String> substitutions) {
		String fileContent = readFile(cfgFile);
		String contentFinal = getContent(fileContent, substitutions);
		String text = custom + "\n" + contentFinal;
		writeFile(cfgFile, text);
	}

	private String getContent(String data, Map<String, String> substitutions) {
		String contentFinal = data;
		for (Map.Entry<String, String> entry : substitutions.entrySet()) {
			String key = entry.getKey();
			String val = entry.getValue();
			contentFinal = contentFinal.replaceAll(key, val);
		}
		return contentFinal;
	}

	private void writeFile(File file, String content) {
		try {
			FileTools.write(file).string(content);

		} catch (Exception e) {
			throw new UnsatisfiedMaybeTunneling(Reasons.build(IoError.T).text(e.getMessage()).toMaybe());
		}
	}

	private String readFile(File file) {
		return FileTools.read(file).asString();
	}

}
