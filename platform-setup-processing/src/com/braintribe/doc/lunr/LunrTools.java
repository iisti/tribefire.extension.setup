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
package com.braintribe.doc.lunr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface LunrTools {
	static <T> List<T> splice(List<T> list, int start, int deleteCount, T... insertItems) {
		int size = list.size();

		if (start < 0) {
			start = size + start;
			
			if (start < 0)
				start = 0;
		}
		else {
			if (start > size)
				start = size;
		}
		
		int remaining = size - start;

		if (deleteCount > remaining) {
			deleteCount = remaining;
		}
		else if (deleteCount < 0) {
			deleteCount = 0;
		}
		
		List<T> subList = list.subList(start, start + deleteCount);
		
		List<T> result = new ArrayList<>(subList);
		
		if (deleteCount != 0)
			subList.clear();
		
		if (insertItems.length > 0)
			subList.addAll(Arrays.asList(insertItems));
		
		return result;
	}
}
