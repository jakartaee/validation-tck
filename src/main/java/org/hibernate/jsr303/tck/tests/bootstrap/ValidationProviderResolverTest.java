// $Id$
/*
* JBoss, Home of Professional Open Source
* Copyright 2009, Red Hat, Inc. and/or its affiliates, and individual contributors
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* http://www.apache.org/licenses/LICENSE-2.0
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.hibernate.jsr303.tck.tests.bootstrap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.validation.spi.ValidationProvider;

import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.testharness.AbstractTest;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ArtifactType;
import org.jboss.testharness.impl.packaging.Classes;
import org.testng.annotations.Test;

import org.hibernate.jsr303.tck.common.TCKValidationProvider;
import org.hibernate.jsr303.tck.common.TCKValidatorConfiguration;
import org.hibernate.jsr303.tck.util.TestUtil;

import static org.testng.Assert.assertTrue;

/**
 * @author Hardy Ferentschik
 */
@Artifact(artifactType = ArtifactType.JSR303)
@Classes({
		TestUtil.class,
		TestUtil.PathImpl.class,
		TestUtil.NodeImpl.class,
		TCKValidationProvider.class,
		TCKValidatorConfiguration.class,
		TCKValidationProvider.DummyValidatorFactory.class
})
public class ValidationProviderResolverTest extends AbstractTest {
	private static final String SERVICES_FILE = "META-INF/services/" + ValidationProvider.class.getName();

	@Test
	@SpecAssertion(section = "4.4.4.1", id = "c")
	public void testServiceFileExists() {
		List<String> providers = readBeanValidationServiceFile();
		assertTrue( !providers.isEmpty(), "There should be at least one provider" );

		assertTrue(
				providers.contains( TestUtil.getValidationProviderClassName() ),
				"The validation class of the provider under test has to be in the list."
		);
	}

	private List<String> readBeanValidationServiceFile() {
		ClassLoader classloader = TestUtil.getClassLoaderForValidationProvider();
		List<String> providers = new ArrayList<String>();
		try {

			Enumeration<URL> providerDefinitions = classloader.getResources( SERVICES_FILE );
			while ( providerDefinitions.hasMoreElements() ) {
				URL url = providerDefinitions.nextElement();
				addProviderToList( providers, url );
			}
		}
		catch ( Exception e ) {
			throw new RuntimeException( "Unable to load service file", e );
		}
		return providers;
	}

	private void addProviderToList(List<String> providers, URL url)
			throws IOException, InstantiationException, IllegalAccessException {
		InputStream stream = url.openStream();
		try {
			BufferedReader reader = new BufferedReader( new InputStreamReader( stream ), 100 );
			String name = reader.readLine();
			while ( name != null ) {
				name = name.trim();
				if ( !name.startsWith( "#" ) ) {
					providers.add( name );
				}
				name = reader.readLine();
			}
		}
		finally {
			stream.close();
		}
	}
}
