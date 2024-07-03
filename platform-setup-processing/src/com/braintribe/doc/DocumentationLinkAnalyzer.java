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
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import com.braintribe.model.asset.PlatformAsset;

public class DocumentationLinkAnalyzer {

	private Set<UniversalPath> deadLinkTargets;
	private Map<UniversalPath, Set<MarkdownFile>> referencingMdFilesPerPath;
	private Map<PlatformAsset, Set<PlatformAsset>> referencedByAssetsPerAsset;
	private Map<PlatformAsset, Set<PlatformAsset>> referencesToAssetsPerAsset;

	private final DocumentationContent documentationContent;
	
	public DocumentationLinkAnalyzer(DocumentationContent documentationContent) {
		this.documentationContent = documentationContent;
	}

	public void calculate(File targetRootFolder) {
		deadLinkTargets = new HashSet<>();
		referencingMdFilesPerPath = new HashMap<>();
		referencedByAssetsPerAsset = new HashMap<>();
		referencesToAssetsPerAsset = new HashMap<>();

		documentationContent.getAssets().forEach(asset -> {
			referencedByAssetsPerAsset.put(asset, new HashSet<>());
			referencesToAssetsPerAsset.put(asset, new HashSet<>());
		});

		documentationContent.getMarkdownFiles() //
				.forEach(mdFile -> {

					mdFile.getReferencingFiles().forEach(path -> {
						Set<MarkdownFile> referencingFiles = referencingMdFilesPerPath.computeIfAbsent(path, l -> new HashSet<>());
						referencingFiles.add(mdFile);
					});
				});

		referencingMdFilesPerPath.forEach((path, referencedBy) -> {
			MarkdownFile mdFileOfPath = documentationContent.getMarkdownFile(path);

			if (mdFileOfPath == null) {
				UniversalPath mdNameOfMdFile = path.getParent().push(path.getName().replaceAll("\\.html$", ".md"));
				mdFileOfPath = documentationContent.getMarkdownFile(mdNameOfMdFile);
			}

			File targetFile = new File(targetRootFolder, path.toFilePath());

			if (mdFileOfPath == null && !targetFile.exists()) {

				deadLinkTargets.add(path);
			} else {
				// File exists but might not be a md file

				PlatformAsset targetAsset = documentationContent.getAsset(DocUtils.getAssetFromPath(path));

				referencedBy //
						.stream() //
						.map(MarkdownFile::getDocRelativeLocation) //
						.map(DocUtils::getAssetFromPath) //
						.map(documentationContent::getAsset) //
						.forEach(sourceAsset -> {
							referencesToAssetsPerAsset.get(sourceAsset).add(targetAsset);
							referencedByAssetsPerAsset.get(targetAsset).add(sourceAsset);
						});
			}
		});

		// Map<MarkdownFile, List<PlatformAsset>> referencesToAssetsFromMarkdownFile;
		// Map<MarkdownFile, List<PlatformAsset>> referencingAssetsOfMarkdownFile;

	}

	public Set<UniversalPath> getDeadLinkTargets() {
		return deadLinkTargets;
	}

	public Set<MarkdownFile> getReferencingMdFiles(UniversalPath path) {
		return referencingMdFilesPerPath.get(path);
	}

	public Stream<PlatformAsset> getAssetsOrderedByImportance() {
		return documentationContent.getAssets() //
				.stream() //
				.sorted(Comparator.comparing((PlatformAsset a) -> referencedByAssetsPerAsset.get(a).size()) //
						.reversed() //
						.thenComparing(Comparator.comparing(PlatformAsset::qualifiedAssetName)));
	}

	
}
