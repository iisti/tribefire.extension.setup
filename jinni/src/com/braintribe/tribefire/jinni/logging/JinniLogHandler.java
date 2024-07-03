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
package com.braintribe.tribefire.jinni.logging;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

import com.braintribe.utils.stream.DelegateOutputStream;
import com.braintribe.utils.stream.NullOutputStream;

public class JinniLogHandler extends StreamHandler {

	public static OutputStream out = NullOutputStream.getInstance();

	/**
	 * Creates a new <code>ConsoleHandler</code> instance. This {@link StreamHandler#setOutputStream(java.io.OutputStream) sets the OutputStream} to
	 * <code>System.out</code>.
	 */
	public JinniLogHandler() {
		super();
		setOutputStream(new JinniLogOutputStream());
	}

	/**
	 * Similar to {@link java.util.logging.ConsoleHandler#publish(LogRecord)}.
	 */
	@Override
	public void publish(LogRecord record) {
		super.publish(record);
		flush();
	}

	/**
	 * Similar to {@link java.util.logging.ConsoleHandler#close()}.
	 */
	@Override
	public void close() {
		flush();
	}

	private static class JinniLogOutputStream extends DelegateOutputStream {

		@Override
		protected OutputStream openDelegate() throws IOException {
			throw new UnsupportedOperationException();
		}

		@Override
		protected OutputStream getDelegate() throws IOException {
			return out;
		}
	}
}
