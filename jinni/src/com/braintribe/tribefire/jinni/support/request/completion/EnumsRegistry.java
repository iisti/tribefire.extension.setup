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
package com.braintribe.tribefire.jinni.support.request.completion;

import static com.braintribe.utils.lcd.CollectionTools2.newMap;
import static com.braintribe.utils.lcd.CollectionTools2.newTreeMap;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.braintribe.model.generic.reflection.EnumType;
import com.braintribe.model.meta.GmEnumConstant;
import com.braintribe.model.meta.GmEnumType;

/**
 * @author peter.gazdik
 */
/* package */ class EnumsRegistry {

	public final Map<String, GmEnumType> shortIdentifierToType = newTreeMap();

	private final Map<GmEnumType, String> typeToShortIdentifier = newMap();
	private final Map<String, Integer> shortNameToCount = newMap();

	public String acquireShortIdentifier(GmEnumType gmType) {
		return typeToShortIdentifier.computeIfAbsent(gmType, this::newShortIdentifier);
	}

	private String newShortIdentifier(GmEnumType gmType) {
		String result = resolveShortIdentifier(gmType);

		shortIdentifierToType.put(result, gmType);

		return result;
	}

	private String resolveShortIdentifier(GmEnumType gmType) {
		String shortName = gmType.<EnumType<?>> reflectionType().getShortName();
		Integer c = shortNameToCount.compute(shortName, (name, count) -> (count == null ? 1 : count + 1));
		return c == 1 ? shortName : shortName + c;
	}

	public static List<String> listConstantsNames(GmEnumType gmEnumType) {
		return gmEnumType.getConstants().stream() //
				.map(GmEnumConstant::getName) //
				.sorted() //
				.collect(Collectors.toList());
	}

}
