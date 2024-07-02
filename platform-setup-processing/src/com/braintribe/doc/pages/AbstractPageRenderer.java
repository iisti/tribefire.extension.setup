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
package com.braintribe.doc.pages;

import java.io.File;
import java.util.Map;

import com.braintribe.doc.MarkdownCompiler;
import com.braintribe.doc.UniversalPath;
import com.braintribe.doc.stop.SequentialStopWatch;

public abstract class AbstractPageRenderer implements PageRenderer {

	private final String templateName;
	
	public AbstractPageRenderer(String templateName) {
		this.templateName = templateName;
	}
	
	@Override
	public void render(PageRenderingContext context) {
		SequentialStopWatch stopWatch = MarkdownCompiler.renderStopWatch.hatch(this.getClass().getSimpleName());
		UniversalPath relativeTargetPath = targetFile(context);
		File targetFile = new File(context.getTargetRootFolder(), relativeTargetPath.toFilePath());
		targetFile.getParentFile().mkdirs();

		stopWatch.start("data model");
		Map<String, Object> dataModel = dataModel(context);
		stopWatch.stop("data model");
		stopWatch.start("render");
		context.getFreemarkerRenderer().writeFileFromTemplate(templateName, dataModel, targetFile);
		stopWatch.stop("render");
		MarkdownCompiler.renderStopWatch.terminate(stopWatch);
	}
	
	protected abstract Map<String, Object> dataModel(PageRenderingContext context);
	protected abstract UniversalPath targetFile(PageRenderingContext context);
}
