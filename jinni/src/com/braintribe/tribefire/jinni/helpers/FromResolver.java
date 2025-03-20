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
package com.braintribe.tribefire.jinni.helpers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.NoSuchElementException;
import java.util.function.Function;

import com.braintribe.codec.marshaller.api.EntityFactory;
import com.braintribe.codec.marshaller.api.GmDeserializationOptions;
import com.braintribe.codec.marshaller.api.Marshaller;
import com.braintribe.codec.marshaller.api.options.GmDeserializationContextBuilder;
import com.braintribe.codec.marshaller.yaml.YamlMarshaller;
import com.braintribe.gm.config.yaml.ConfigVariableResolver;
import com.braintribe.gm.config.yaml.YamlConfigurations;
import com.braintribe.gm.config.yaml.api.ConfigurationReadBuilder;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.jinni.api.From;
import com.braintribe.model.jinni.api.FromFile;
import com.braintribe.model.jinni.api.FromStdin;
import com.braintribe.model.jinni.api.FromUrl;
import com.braintribe.model.resource.FileResource;
import com.braintribe.tribefire.jinni.wire.contract.JinniContract;
import com.braintribe.utils.stream.KeepAliveDelegateInputStream;
import com.braintribe.ve.api.VirtualEnvironment;

/**
 * @author peter.gazdik
 */
public class FromResolver implements Function<From, GenericEntity> {

	private final JinniContract jinniContract;
	private final VirtualEnvironment virtualEnvironment;

	public FromResolver(JinniContract jinniContract, VirtualEnvironment virtualEnvironment) {
		this.jinniContract = jinniContract;
		this.virtualEnvironment = virtualEnvironment;
	}

	@Override
	public GenericEntity apply(From from) {

		String mimeType = from.getMimeType();

		Marshaller marshaller = jinniContract.marshallerRegistry().getMarshaller(mimeType);

		if (marshaller == null)
			throw new NoSuchElementException("No marshaller registered for mimetype: " + mimeType);
		
		if (marshaller instanceof YamlMarshaller && from instanceof FromFile && ((FromFile)from).getHasVars()) {
			FromFile fromFile = (FromFile)from;
			File file = new File(fromFile.getFile().getPath());
			
			ConfigVariableResolver variableResolver = new ConfigVariableResolver(virtualEnvironment, file);
					
			ConfigurationReadBuilder<GenericEntity> builder = YamlConfigurations.read(GenericEntity.T).placeholders(variableResolver::resolve);
			if (from.getReproduce())
				builder.noDefaulting();
			
			return builder.from(file).get();
		}
		else {
			try (InputStream in = openInputStream(from)) {
				GmDeserializationContextBuilder options = GmDeserializationOptions.deriveDefaults();
				// options
				if (!from.getReproduce())
					options = options.set(EntityFactory.class, EntityType::create);

				return (GenericEntity) marshaller.unmarshall(in, options.build());

			} catch (URISyntaxException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		}
	}

	private InputStream openInputStream(From from) throws IOException, URISyntaxException {
		if (from instanceof FromStdin) {
			return new KeepAliveDelegateInputStream(System.in);
		} else if (from instanceof FromUrl) {
			FromUrl fromUrl = (FromUrl) from;
			String urlProperty = fromUrl.getUrl();

			if (urlProperty == null)
				throw new IllegalStateException("FromUrl is missing url");

			URL url = new URI(urlProperty).toURL();
			return url.openStream();
		} else if (from instanceof FromFile) {
			FromFile fromFile = (FromFile) from;
			FileResource file = fromFile.getFile();

			if (file == null)
				throw new IllegalStateException("FromFile is missing file");

			return file.openStream();
		} else {
			throw new NoSuchElementException("No support for From type: " + from.entityType());
		}
	}
}