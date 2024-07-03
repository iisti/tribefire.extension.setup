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

import freemarker.ext.beans.BeanModel;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public class MapWithNonStringKeys implements TemplateMethodModelEx {

	private final Map<?, ?> map;

	public MapWithNonStringKeys(Map<?, ?> map) {
		this.map = map;
	}

	@Override
	public TemplateModel exec(List args) throws TemplateModelException {
		if (args.size() != 1) {
			throw new TemplateModelException("Wrong arguments");
		}

		BeanModel argument = (BeanModel) args.get(0);
		Object originalObject = argument.getWrappedObject();

		DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_28);
		builder.setExposeFields(true);
		builder.setExposureLevel(BeansWrapper.EXPOSE_ALL);

		Object valueObject = map.get(originalObject);
		BeanModel beanModel = new BeanModel(valueObject, builder.build());
		return beanModel;
	}
}
