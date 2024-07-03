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
package com.braintribe.doc.stop;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * 
 * @author Neidhart.Orlich
 *
 */
public class ParallelStopWatch extends AbstractStopWatch {
	private final Collection<AbstractStopWatch> currentSubWatches = new HashSet<>();
	
	public ParallelStopWatch(MultiStopWatch multiStopWatch, TimeMeasure timeMeasure) {
		super(multiStopWatch, timeMeasure);
	}
	
	@Override
	synchronized protected void stop() {
		if (hasSubWatchRunning())
			throw new IllegalStateException("Cant't stop " + multiStopWatch.getKey() + " as subtimers " + currentSubKeys() + " are still running.");
		
		timeMeasure.stop();
	}
	
	private Collection<Object> currentSubKeys() {
		return currentSubWatches.stream()//
				.map(w -> w.multiStopWatch.getKey()) //
				.collect(Collectors.toList());
	}
	
	synchronized public SequentialStopWatch hatch(Object key) {
		if (!isRunning()) {
			throw new IllegalStateException("Can't start a new timer (" + key + ") in " + multiStopWatch.getKey() + " because it is already stopped.");
		}
		SequentialStopWatch linearStopWatch = multiStopWatch.start(key);
		currentSubWatches.add(linearStopWatch);
		
		return linearStopWatch;
	}
	
	synchronized public void terminate(AbstractStopWatch stopWatch) {
		Object key = stopWatch.multiStopWatch.getKey();
		if (!currentSubWatches.contains(stopWatch)) {
			throw new IllegalStateException("Can't stop subtimer for key " + key + " because it is not running as subtimer of this stop watch.");
		}
			
		stopWatch.stop();
		currentSubWatches.remove(stopWatch);
	}
	
	@Override
	synchronized public boolean hasSubWatchRunning() {
		return !currentSubWatches.isEmpty();
	}
}