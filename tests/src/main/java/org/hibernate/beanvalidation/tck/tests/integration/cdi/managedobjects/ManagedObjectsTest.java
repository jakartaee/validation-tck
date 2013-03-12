/*
* JBoss, Home of Professional Open Source
* Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual contributors
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
package org.hibernate.beanvalidation.tck.tests.integration.cdi.managedobjects;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import javax.inject.Inject;
import javax.validation.ConstraintValidatorFactory;
import javax.validation.MessageInterpolator;
import javax.validation.ParameterNameProvider;
import javax.validation.TraversableResolver;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.util.Groups;
import org.hibernate.beanvalidation.tck.util.IntegrationTest;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Tests for dependency injection into message interpolators, traversable
 * resolvers etc. All test objects rely on a {@link Greeter} object to be
 * injected which is then used to perform message interpolation etc.
 *
 * @author Gunnar Morling
 */
@IntegrationTest
@SpecVersion(spec = "beanvalidation", version = "1.1.0")
public class ManagedObjectsTest extends Arquillian {

	@Inject
	private ValidatorFactory defaultValidatorFactory;

	@Inject
	private Validator defaultValidator;

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClassPackage( ManagedObjectsTest.class )
				.withValidationXml( "validation-ManagedObjectsTest.xml" )
				.withEmptyBeansXml()
				.build();
	}

	@Test(groups = Groups.FAILING_ON_AS)
	@SpecAssertions({
			@SpecAssertion(section = "10.1.1", id = "c"),
			@SpecAssertion(section = "10.3.2", id = "a")
	})
	public void testMessageInterpolatorIsSubjectToDependencyInjection() {
		assertNotNull( defaultValidatorFactory );
		MessageInterpolator messageInterpolator = defaultValidatorFactory.getMessageInterpolator();

		assertEquals( messageInterpolator.interpolate( null, null ), Greeter.MESSAGE );
	}

	@Test(groups = Groups.FAILING_ON_AS)
	@SpecAssertions({
			@SpecAssertion(section = "10.1.1", id = "c"),
			@SpecAssertion(section = "10.3.2", id = "a")
	})
	public void testTraversableResolverIsSubjectToDependencyInjection() {
		assertNotNull( defaultValidatorFactory );

		TraversableResolver traversableResolver = defaultValidatorFactory.getTraversableResolver();
		MessageHolder message = new MessageHolder();
		traversableResolver.isCascadable( message, null, null, null, null );

		assertEquals( message.getValue(), Greeter.MESSAGE );
	}

	@Test(groups = Groups.FAILING_ON_AS)
	@SpecAssertions({
			@SpecAssertion(section = "10.1.1", id = "c"),
			@SpecAssertion(section = "10.3.2", id = "a")
	})
	public void testConstraintValidatorFactoryIsSubjectToDependencyInjection() {
		assertNotNull( defaultValidatorFactory );

		ConstraintValidatorFactory constraintValidatorFactory = defaultValidatorFactory.getConstraintValidatorFactory();
		GreetingConstraintValidator validator = constraintValidatorFactory.getInstance(
				GreetingConstraintValidator.class
		);

		assertEquals( validator.getMessage(), Greeter.MESSAGE );
	}

	@Test(groups = Groups.FAILING_ON_AS)
	@SpecAssertions({
			@SpecAssertion(section = "10.1.1", id = "c"),
			@SpecAssertion(section = "10.3.2", id = "a")
	})
	public void testParameterNameProviderIsSubjectToDependencyInjection() {
		assertNotNull( defaultValidatorFactory );
		ParameterNameProvider parameterNameProvider = defaultValidatorFactory.getParameterNameProvider();

		assertEquals(
				parameterNameProvider.getParameterNames( (Constructor<?>) null ),
				Arrays.asList( Greeter.MESSAGE )
		);
	}
}
