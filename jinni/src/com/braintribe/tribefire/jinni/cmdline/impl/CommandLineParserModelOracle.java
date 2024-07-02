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
package com.braintribe.tribefire.jinni.cmdline.impl;

import static java.util.Collections.emptyList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.Property;
import com.braintribe.model.meta.data.mapping.Alias;
import com.braintribe.model.meta.data.mapping.PositionalArguments;
import com.braintribe.model.processing.session.api.managed.ModelAccessory;

public class CommandLineParserModelOracle {
	private final ModelAccessory modelAccessory;
	private final Map<EntityType<?>, Map<String, Property>> propertyNameIndex = new HashMap<>();

	public CommandLineParserModelOracle(ModelAccessory modelAccessory) {
		this.modelAccessory = modelAccessory;
	}

	Property findProperty(EntityType<?> entityType, String identifier) {
		return propertyNameIndex.computeIfAbsent(entityType, this::generateIndex).get(identifier);
	}

	List<Property> getPositionalArguments(EntityType<?> entityType) {
		PositionalArguments posArgs = modelAccessory.getCmdResolver().getMetaData().entityType(entityType).meta(PositionalArguments.T).exclusive();

		if (posArgs == null)
			return emptyList();

		return posArgs.getProperties().stream() //
				.map(entityType::getProperty) //
				.collect(Collectors.toList());
	}

	private Map<String, Property> generateIndex(EntityType<?> entityType) {
		Map<String, Property> properties = new HashMap<>();

		for (Property property : entityType.getProperties()) {
			String name = property.getName();
			properties.put(name, property);

			List<Alias> aliases = modelAccessory.getCmdResolver().getMetaData().useCase("command-line").property(property).meta(Alias.T).list();
			for (Alias alias : aliases) {
				properties.put(alias.getName(), property);
			}
		}

		return properties;
	}
}
