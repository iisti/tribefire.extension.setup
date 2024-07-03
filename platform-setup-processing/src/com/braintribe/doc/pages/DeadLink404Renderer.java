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
package com.braintribe.doc.pages;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.braintribe.console.ConsoleOutputs;
import com.braintribe.doc.MarkdownFile;
import com.braintribe.doc.UniversalPath;
import com.braintribe.model.asset.PlatformAsset;
import com.braintribe.utils.CollectionTools;

public class DeadLink404Renderer extends AbstractPageRenderer {
	private UniversalPath deadLinkTarget;
	private Set<MarkdownFile> referencingMdFiles;
	
	public DeadLink404Renderer(UniversalPath deadLinkTarget, Set<MarkdownFile> referencingMdFiles) {
		super("file404.ftlh");
		this.deadLinkTarget = deadLinkTarget;
		this.referencingMdFiles = referencingMdFiles;
	}
	
	@Override
	protected Map<String, Object> dataModel(PageRenderingContext context) {
		
		Map<String, List<MarkdownFile>> filesPerAsset = referencingMdFiles.stream()
				.peek(referencingMdFile -> context.verboseOut(ConsoleOutputs.red("  Link target " + deadLinkTarget.toSlashPath()
						+ " does not exist but is referenced by " + referencingMdFile.getDocRelativeLocation())))
				.collect(Collectors.toMap(MarkdownFile::getAssetId, CollectionTools::getList, CollectionTools::addElementsToCollection));

		String relativeFilePathToRoot = "";

		for (int i = 0; i < deadLinkTarget.getNameCount() - 1; i++) {
			relativeFilePathToRoot += "../";
		}

		PlatformAsset assetFromPath = context.getDocumentationContent().getAsset(deadLinkTarget);

//		String menu = menuCompiler.getMenu(assetFromPath, deadLinkTarget);

		Map<String, Object> dataModel = context.newDataModel(relativeFilePathToRoot);
		dataModel.put("referencingMdFiles", referencingMdFiles);
		dataModel.put("referencingMdFilesPerAsset", filesPerAsset);
		dataModel.put("menu", context.getMenu(assetFromPath));
		
		return dataModel;

	}
	
	@Override
	protected UniversalPath targetFile(PageRenderingContext context) {
		
		String targetFileName = deadLinkTarget.getName() + ".404.html";
		
		return deadLinkTarget.getParent().push(targetFileName);

	}
	
}
