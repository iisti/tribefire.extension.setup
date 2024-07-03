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

/**
 * 
 * @author Neidhart.Orlich
 *
 */
public class SequentialStopWatch extends AbstractStopWatch {
	
	private AbstractStopWatch currentSubWatch;
	private TimeMeasure unattributedTimer;
	
	public SequentialStopWatch(MultiStopWatch multiStopWatch, TimeMeasure timeMeasure) {
		super(multiStopWatch, timeMeasure);
		unattributedTimer = multiStopWatch.startUnattributedTime();
	}
	
	public static SequentialStopWatch newTimer(Object key) {
		MultiStopWatch multiStopWatch = new MultiStopWatch(key);
		SequentialStopWatch linearStopWatch = multiStopWatch.start();
		return linearStopWatch;
	}
	
	public static void forceJIT() {
		SequentialStopWatch timer = newTimer("");
		timer.start("");
		timer.stop("");
		ParallelStopWatch parallel = timer.startParallel("p");
		
		SequentialStopWatch p1 = parallel.hatch("");
		parallel.terminate(p1);
		
		timer.stop("p");
		summarize(timer);
	}
	
	public static String summarize(SequentialStopWatch linearStopWatch) {
		if (linearStopWatch.timeMeasure.isRunning())
			linearStopWatch.stop();
		return linearStopWatch.multiStopWatch.summary();
	}
	
	@Override
	protected void stop() {
		if (hasSubWatchRunning())
			throw new IllegalStateException("Cant't stop " + multiStopWatch.getKey() + " as subwatch " + currentSubKey() + " is still running.");
		
		timeMeasure.stop();
		unattributedTimer.stop();
	}
	
	private Object currentSubKey() {
		if (hasSubWatchRunning())
			return currentSubWatch.multiStopWatch.getKey();
		
		return null;
	}
	
	public SequentialStopWatch start(Object key) {
		if (hasSubWatchRunning()) {
			throw new IllegalStateException("Can't start a new subtimer for key " + key + " because " + currentSubKey() + " is still running.");
		}
		
		unattributedTimer.stop();
		unattributedTimer = null;
		
		SequentialStopWatch linearStopWatch = multiStopWatch.start(key);
		currentSubWatch = linearStopWatch;
		
		return linearStopWatch;
	}
	
	public ParallelStopWatch startParallel(Object key) {
		if (hasSubWatchRunning()) {
			throw new IllegalStateException("Can't start a new subtimer for key " + key + " because " + currentSubKey() + " is still running.");
		}
		
		unattributedTimer.stop();
		unattributedTimer = null;
		
		ParallelStopWatch linearStopWatch = multiStopWatch.startParallel(key);
		currentSubWatch = linearStopWatch;
		
		return linearStopWatch;
	}
	
	public void stop(Object key) {
		if (!hasSubWatchRunning()) {
			throw new IllegalStateException("Can't stop subtimer for key " + key + " because currently there is no subtimmer running.");
		}
		
		Object currentSubKey = currentSubKey();
		
		if (currentSubKey == key)
			currentSubWatch.stop();
		else
			throw new IllegalStateException("Current subtimer has key " + currentSubKey + " but you tried to stop " + key);
		
		currentSubWatch = null;
		unattributedTimer = multiStopWatch.startUnattributedTime();
	}
	
	@Override
	public boolean hasSubWatchRunning() {
		return currentSubWatch != null;
	}

}