// $Id$
/*
* JBoss, Home of Professional Open Source
* Copyright 2008, Red Hat, Inc. and/or its affiliates, and individual contributors
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
package org.hibernate.jsr303.tck.tests.bootstrap.customprovider;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Validation;
import javax.validation.ValidationProviderResolver;
import javax.validation.ValidatorFactory;
import javax.validation.spi.ValidationProvider;

import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.testharness.AbstractTest;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ArtifactType;
import org.jboss.testharness.impl.packaging.Classes;
import org.jboss.testharness.impl.packaging.jsr303.ValidationXml;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.Test;

import org.hibernate.jsr303.tck.common.TCKValidationProvider;
import org.hibernate.jsr303.tck.common.TCKValidatorConfiguration;
import org.hibernate.jsr303.tck.util.TestUtil;

/**
 * @author Hardy Ferentschik
 */
@Artifact(artifactType = ArtifactType.JSR303)
@Classes({
		TestUtil.class,
		TCKValidationProvider.class,
		TCKValidatorConfiguration.class,
		TCKValidationProvider.DummyValidatorFactory.class
})
@ValidationXml("validation-BootstrapCustomProviderDefinedInValidationXmlTest.xml")
public class BootstrapCustomProviderDefinedInValidationXmlTest extends AbstractTest {

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "4.4", id = "a"),
			@SpecAssertion(section = "4.4.4.2", id = "a")
	})
	public void testGetFactoryByProviderSpecifiedInValidationXml() {

		ValidationProviderResolver resolver = new ValidationProviderResolver() {

			public List<ValidationProvider<?>> getValidationProviders() {
				List<ValidationProvider<?>> list = new ArrayList<ValidationProvider<?>>();
				list.add( TestUtil.getValidationProviderUnderTest() );
				list.add( new TCKValidationProvider() );
				return list;
			}
		};

		ValidatorFactory factory = Validation.byDefaultProvider()
				.providerResolver( resolver )
				.configure()
				.buildValidatorFactory();
		assertNotNull( factory );
		assertTrue(
				factory instanceof TCKValidationProvider.DummyValidatorFactory,
				"Since TCKValidationProvider is configured in validation.xml it should be the bootstrapped provider " +
						"even though the provider under test is first in the list of providers in the resolver."
		);
	}
}