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
package org.hibernate.jsr303.tck.tests.bootstrap.customprovider;

import javax.validation.Configuration;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.testharness.AbstractTest;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ArtifactType;
import org.jboss.testharness.impl.packaging.Classes;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.Resource;
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
		TestUtil.PathImpl.class,
		TestUtil.NodeImpl.class,
		TCKValidationProvider.class,
		TCKValidatorConfiguration.class,
		TCKValidationProvider.DummyValidatorFactory.class
})
@Resource(source = "javax.validation.spi.ValidationProvider",
		destination = "WEB-INF/classes/META-INF/services/javax.validation.spi.ValidationProvider")
@IntegrationTest
public class BootstrapCustomProviderDefinedInServiceFileTest extends AbstractTest {

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "4.4", id = "a"),
			@SpecAssertion(section = "4.4.4.2", id = "a")
	})
	public void testGetFactoryByProviderSpecifiedProgrammatically() {
		TCKValidatorConfiguration configuration = Validation.byProvider( TCKValidationProvider.class ).configure();
		ValidatorFactory factory = configuration.buildValidatorFactory();
		assertNotNull( factory );
		assertTrue( factory instanceof TCKValidationProvider.DummyValidatorFactory );
	}

	@Test
	@SpecAssertion(section = "4.4.4.1", id = "a")
	public void testProviderResolverReturnsListOfAvailableProviders() {

		// really an indirect test since there is no way to get hold of the default provider resolver.
		// we just confirm that the provider under test and the TCKValidationProvider are loadable.

		TCKValidatorConfiguration configuration = Validation.byProvider( TCKValidationProvider.class ).configure();
		ValidatorFactory factory = configuration.buildValidatorFactory();
		assertNotNull( factory );

		Configuration<?> config = Validation.byProvider( TestUtil.getValidationProviderUnderTest().getClass() )
				.configure();
		factory = config.buildValidatorFactory();
		assertNotNull( factory );
	}
}
