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
package org.hibernate.beanvalidation.tck.tests.bootstrap.customprovider;

import javax.validation.Configuration;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.common.TCKValidationProvider;
import org.hibernate.beanvalidation.tck.common.TCKValidatorConfiguration;
import org.hibernate.beanvalidation.tck.util.IntegrationTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * @author Hardy Ferentschik
 */
@IntegrationTest
@SpecVersion(spec = "beanvalidation", version = "1.1.0")
public class BootstrapCustomProviderDefinedInServiceFileTest extends Arquillian {

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClass( BootstrapCustomProviderDefinedInServiceFileTest.class )
				.withClasses( TCKValidatorConfiguration.class, TCKValidationProvider.class )
				.withResource(
						"javax.validation.spi.ValidationProvider",
						"META-INF/services/javax.validation.spi.ValidationProvider",
						true
				)
				.build();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.5", id = "a"),
			@SpecAssertion(section = "5.5.3", id = "k"),
			@SpecAssertion(section = "5.5.4.2", id = "a")
	})
	public void testGetFactoryByProviderSpecifiedProgrammatically() {
		TCKValidatorConfiguration configuration = Validation.byProvider( TCKValidationProvider.class ).configure();
		ValidatorFactory factory = configuration.buildValidatorFactory();
		assertNotNull( factory );
		assertTrue( factory instanceof TCKValidationProvider.DummyValidatorFactory );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.5.3", id = "k"),
			@SpecAssertion(section = "5.5.4.1", id = "a")
	})
	public void testProviderResolverReturnsListOfAvailableProviders() {

		// really an indirect test since there is no way to get hold of the default provider resolver.
		// we just confirm that the provider under test and the TCKValidationProvider are loadable.

		TCKValidatorConfiguration configuration = Validation.byProvider( TCKValidationProvider.class ).configure();
		ValidatorFactory factory = configuration.buildValidatorFactory();
		assertNotNull( factory );

		@SuppressWarnings("unchecked")
		Configuration<?> config = Validation.byProvider( TestUtil.getValidationProviderUnderTest().getClass() )
				.configure();
		factory = config.buildValidatorFactory();
		assertNotNull( factory );
	}
}
