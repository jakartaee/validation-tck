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
package org.hibernate.beanvalidation.tck.tests.bootstrap;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Configuration;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.ValidationProviderResolver;
import javax.validation.ValidatorFactory;
import javax.validation.spi.ValidationProvider;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.common.TCKValidationProvider;
import org.hibernate.beanvalidation.tck.common.TCKValidatorConfiguration;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

import static org.testng.Assert.assertTrue;
import static org.testng.FileAssert.fail;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "1.1.0")
public class ValidationProviderTest extends Arquillian {

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClass( ValidationProviderTest.class )
				.withClasses( TCKValidationProvider.class, TCKValidatorConfiguration.class )
				.build();
	}

	@Test
	@SpecAssertion(section = "4.4.4.2", id = "b")
	public void testFirstMatchingValidationProviderResolverIsReturned() {
		ValidationProviderResolver resolver = new ValidationProviderResolver() {

			public List<ValidationProvider<?>> getValidationProviders() {
				List<ValidationProvider<?>> list = new ArrayList<ValidationProvider<?>>();
				list.add( TestUtil.getValidationProviderUnderTest() );
				list.add( new TCKValidationProvider() );
				return list;
			}
		};

		Configuration<?> configuration = Validation
				.byProvider( TCKValidationProvider.class )
				.providerResolver( resolver )
				.configure();

		ValidatorFactory factory = configuration.buildValidatorFactory();
		assertTrue( factory instanceof TCKValidationProvider.DummyValidatorFactory );
	}

	@Test
	@SpecAssertion(section = "4.4.4.2", id = "c")
	public void testByDefaultProviderUsesTheFirstProviderReturnedByValidationProviderResolver() {
		ValidationProviderResolver resolver = new ValidationProviderResolver() {

			public List<ValidationProvider<?>> getValidationProviders() {
				List<ValidationProvider<?>> list = new ArrayList<ValidationProvider<?>>();
				list.add( new TCKValidationProvider() );
				list.add( TestUtil.getValidationProviderUnderTest() );
				return list;
			}
		};

		Configuration<?> configuration = Validation
				.byDefaultProvider()
				.providerResolver( resolver )
				.configure();
		ValidatorFactory factory = configuration.buildValidatorFactory();
		assertTrue( factory instanceof TCKValidationProvider.DummyValidatorFactory );
	}

	@Test
	@SpecAssertion(section = "4.4.4.2", id = "d")
	public void testValidationProviderContainsNoArgConstructor() {
		ValidationProvider<?> validationProviderUnderTest = TestUtil.getValidationProviderUnderTest();
		try {
			Constructor<?> constructor = validationProviderUnderTest.getClass().getConstructor();
			assertTrue( Modifier.isPublic( constructor.getModifiers() ) );
		}
		catch ( Exception e ) {
			fail( "The validation provider must have a public no arg constructor" );
		}
	}

	@Test(expectedExceptions = ValidationException.class)
	@SpecAssertions({
			@SpecAssertion(section = "4.4.5", id = "f"),
			@SpecAssertion(section = "4.4.4.2", id = "e")
	})
	public void testValidationExceptionIsThrownInCaseValidatorFactoryCreationFails() {
		ValidationProviderResolver resolver = new ValidationProviderResolver() {

			public List<ValidationProvider<?>> getValidationProviders() {
				throw new RuntimeException( "ValidationProviderResolver failed!" );
			}
		};

		Validation.byDefaultProvider().providerResolver( resolver ).configure();
	}
}
