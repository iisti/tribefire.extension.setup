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
package com.braintribe.doc;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.braintribe.console.ConsoleConfiguration;
import com.braintribe.console.PlainSysoutConsole;
import com.braintribe.model.asset.PlatformAsset;
import com.braintribe.testing.test.AbstractTest;
import com.braintribe.utils.CollectionTools;
import com.braintribe.utils.FileTools;

public class MarkdownLab extends AbstractTest{
	public static void main(String[] args) {
		ConsoleConfiguration.install(PlainSysoutConsole.INSTANCE);
		complex();
	}
	
	public static void simple() {
		try {
			File baseFolder = new File("markdown");
			File outputFolder = new File("output");
			
			if (outputFolder.exists())
				FileTools.deleteDirectoryRecursively(outputFolder);
			
			outputFolder.mkdirs();

			PlatformAsset testPA = getAsset("tribefire.group", "asset-name");
			
			Map<String, Object> dataModel = new HashMap<>();
			
			MarkdownCompiler.compile(baseFolder, outputFolder, CollectionTools.getSet(testPA), true, dataModel);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("\nDone.");
	}
	
	public static void complex() {
		try {
			File baseFolder = testDir(MarkdownLab.class);
			File outputFolder = new File("output");
			
			if (outputFolder.exists())
				FileTools.deleteDirectoryRecursively(outputFolder);
			
			outputFolder.mkdirs();
			
			Set<PlatformAsset> assets = Stream.of(baseFolder.listFiles())
				.filter(group -> !group.getName().equals(".mdoc"))
				.flatMap(group -> Stream.of(group.listFiles()))
				.map(assetFolder -> getAsset(assetFolder.getParentFile().getName(), assetFolder.getName()))
				.collect(Collectors.toSet());
			
			Map<String, Object> dataModel = new HashMap<>();
			dataModel.put("jinniVersion", "9.77.complex-pc");
			
			MarkdownCompiler.compile(baseFolder, outputFolder, assets, true, dataModel);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("\nDone.");
	}
	
	private static PlatformAsset getAsset(String groupId, String name) {
		PlatformAsset asset = PlatformAsset.T.create();
		asset.setGroupId(groupId);
		asset.setName(name);
		
		return asset;
	}
}
