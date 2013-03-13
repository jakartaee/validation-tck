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
package org.hibernate.beanvalidation.tck.tests.integration.cdi.defaultinjection;

import java.util.Set;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.util.IntegrationTest;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectConstraintViolationMessages;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * @author Gunnar Morling
 */
@IntegrationTest
@SpecVersion(spec = "beanvalidation", version = "1.1.0")
public class DefaultInjectionTest extends Arquillian {

	@Inject
	private ValidatorFactory defaultValidatorFactory;

	@Inject
	@Default
	private ValidatorFactory qualifiedDefaultValidatorFactory;

	@Inject
	private Validator defaultValidator;

	@Inject
	@Default
	private Validator qualifiedDefaultValidator;

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClassPackage( DefaultInjectionTest.class )
				.withValidationXml( "validation-DefaultInjectionTest.xml" )
				.withEmptyBeansXml()
				.build();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "10.1.1", id = "a"),
			@SpecAssertion(section = "10.3.1", id = "a"),
			@SpecAssertion(section = "10.3", id = "a")
	})
	public void testDefaultValidatorFactoryGetsInjected() {
		assertNotNull( defaultValidatorFactory, "Default validator factory should be injectable." );
		assertTrue(
				defaultValidatorFactory.getMessageInterpolator() instanceof ConstantMessageInterpolator,
				"Injected default validator factory should be configured based on META-INF/validation.xml."
		);

		Set<ConstraintViolation<Foo>> violations = defaultValidatorFactory.getValidator()
				.validate( new Foo() );

		//expecting message from interpolator configured in META-INF/validation.xml
		assertCorrectConstraintViolationMessages( violations, "Invalid constraint" );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "10.1.1", id = "a"),
			@SpecAssertion(section = "10.3.1", id = "a")
	})
	public void testQualifiedDefaultValidatorFactoryGetsInjected() {
		assertNotNull(
				qualifiedDefaultValidatorFactory,
				"Qualified default validator factory should be injectable."
		);
		assertTrue(
				qualifiedDefaultValidatorFactory.getMessageInterpolator() instanceof ConstantMessageInterpolator,
				"Injected qualified default validator factory should be configured based on META-INF/validation.xml."
		);

		Set<ConstraintViolation<Foo>> violations = qualifiedDefaultValidatorFactory.getValidator()
				.validate( new Foo() );

		//expecting message from interpolator configured in META-INF/validation.xml
		assertCorrectConstraintViolationMessages( violations, "Invalid constraint" );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "10.1.1", id = "a"),
			@SpecAssertion(section = "10.3.1", id = "a"),
			@SpecAssertion(section = "10.3", id = "a")
	})
	public void testDefaultValidatorGetsInjected() {
		assertNotNull( defaultValidator, "Default validator should be injectable." );

		Set<ConstraintViolation<Foo>> violations = defaultValidator.validate( new Foo() );

		//expecting message from interpolator configured in META-INF/validation.xml
		assertCorrectConstraintViolationMessages( violations, "Invalid constraint" );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "10.1.1", id = "a"),
			@SpecAssertion(section = "10.3.1", id = "a")
	})
	public void testQualifiedDefaultValidatorGetsInjected() {
		assertNotNull(
				qualifiedDefaultValidator,
				"Qualified default validator should be injectable."
		);

		Set<ConstraintViolation<Foo>> violations = qualifiedDefaultValidator.validate( new Foo() );

		//expecting message from interpolator configured in META-INF/validation.xml
		assertCorrectConstraintViolationMessages( violations, "Invalid constraint" );
	}

	private static class Foo {
		@NotNull
		public String bar;
	}
}
