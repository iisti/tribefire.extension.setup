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
package com.braintribe.tribefire.jinni.cmdline.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.tribefire.jinni.cmdline.api.EntityFactory;
import com.braintribe.tribefire.jinni.cmdline.api.ParsedCommandLine;

public class ParsedCommandLineImpl implements ParsedCommandLine {

	private final EntityFactory entityFactory;
	private final List<GenericEntity> options = new ArrayList<>();
	private final Map<EntityType<?>, GenericEntity> optionsByType = new ConcurrentHashMap<>();

	public ParsedCommandLineImpl(EntityFactory entityFactory) {
		super();
		this.entityFactory = entityFactory;
	}

	@Override
	public void addEntity(GenericEntity entity) {
		options.add(entity);
		optionsByType.put(entity.entityType(), entity);
	}

	@Override
	public <O extends GenericEntity> O acquireInstance(EntityType<O> optionsType) {
		return (O) optionsByType.computeIfAbsent(optionsType, t -> entityFactory.create(t.getTypeSignature()).get());
	}

	@Override
	public <O extends GenericEntity> Optional<O> findInstance(EntityType<O> optionsType) {
		return (Optional<O>) options.stream().filter(optionsType::isInstance).findFirst();
	}

	@Override
	public <O extends GenericEntity> List<O> listInstances(EntityType<O> optionsType) {
		return (List<O>) options.stream().filter(optionsType::isInstance).collect(Collectors.toList());
	}
}
