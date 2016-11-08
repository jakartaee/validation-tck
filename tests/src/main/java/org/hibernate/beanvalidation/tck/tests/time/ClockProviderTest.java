/*
* JBoss, Home of Professional Open Source
* Copyright 2016, Red Hat, Inc. and/or its affiliates, and individual contributors
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
package org.hibernate.beanvalidation.tck.tests.time;

import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectNumberOfViolations;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectPropertyPaths;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertSame;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Set;

import javax.validation.ClockProvider;
import javax.validation.Configuration;
import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Past;

import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Guillaume Smet
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class ClockProviderTest extends Arquillian {

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClassPackage( ClockProviderTest.class )
				.build();
	}

	@Test
	// XXX BVAL-496 update specification references
	@SpecAssertion(section = "", id = "")
	public void testCustomClockProviderFromValidatorFactory() {
		Configuration<?> configuration = TestUtil.getConfigurationUnderTest();
		CustomClockProvider clockProvider = new CustomClockProvider();

		configuration.clockProvider( clockProvider );
		ValidatorFactory factory = configuration.buildValidatorFactory();

		assertSame( factory.getClockProvider(), clockProvider );
	}

	@Test
	// XXX BVAL-496 update specification references
	@SpecAssertion(section = "", id = "")
	public void testDefaultClockProviderIsNotNull() {
		Configuration<?> config = TestUtil.getConfigurationUnderTest();
		assertNotNull( config.getDefaultClockProvider() );
	}

	@Test
	// XXX BVAL-496 update specification references
	@SpecAssertion(section = "", id = "")
	public void testCustomClockProviderViaConfiguration() {
		// use the default configuration
		Validator validator = TestUtil.getValidatorUnderTest();

		Person person = new Person();

		person.setBirthday( Instant.now().minus( Duration.ofDays( 15 ) ) );
		Set<ConstraintViolation<Person>> constraintViolations = validator.validate( person );
		assertCorrectNumberOfViolations( constraintViolations, 0 );

		person.setBirthday( Instant.now().plus( Duration.ofDays( 15 ) ) );
		constraintViolations = validator.validate( person );
		assertCorrectPropertyPaths( constraintViolations, "birthday" );

		// get a new validator with a custom configuration
		validator = TestUtil.getConfigurationUnderTest()
				.clockProvider( new FixedClockProvider( ZonedDateTime.now().plus( Duration.ofDays( 60 ) ) ) )
				.buildValidatorFactory()
				.getValidator();

		person.setBirthday( Instant.now().plus( Duration.ofDays( 15 ) ) );
		constraintViolations = validator.validate( person );
		assertCorrectNumberOfViolations( constraintViolations, 0 );

		person.setBirthday( Instant.now().plus( Duration.ofDays( 90 ) ) );
		constraintViolations = validator.validate( person );
		assertCorrectPropertyPaths( constraintViolations, "birthday" );
	}


	@Test(expectedExceptions = ValidationException.class)
	// XXX BVAL-496 update specification references
	@SpecAssertion(section = "", id = "")
	public void testClockProviderExceptionsGetWrappedInValidationException() {
		ExceptionThrowingClockProvider clockProvider = new ExceptionThrowingClockProvider();
		Configuration<?> config = TestUtil.getConfigurationUnderTest().clockProvider( clockProvider );

		ValidatorFactory factory = config.buildValidatorFactory();
		Validator v = factory.getValidator();

		Person person = new Person();
		person.setBirthday( Instant.now().minus( Duration.ofDays( 3 ) ) );

		v.validate( person );
	}

	@Test
	// XXX BVAL-496 update specification references
	@SpecAssertion(section = "", id = "")
	public void canConfigureClockProviderForValidator() {
		Validator validator = TestUtil.getValidatorUnderTest();

		Person person = new Person();
		person.setBirthday( Instant.now().plus( Duration.ofDays( 15 ) ) );

		Set<ConstraintViolation<Person>> constraintViolations = validator.validate( person );
		assertCorrectPropertyPaths( constraintViolations, "birthday" );

		validator = TestUtil.getValidatorFactoryUnderTest()
				.usingContext()
						.clockProvider( new FixedClockProvider( ZonedDateTime.now().plus( Duration.ofDays( 60 ) ) ) )
				.getValidator();

		constraintViolations = validator.validate( person );
		assertCorrectNumberOfViolations( constraintViolations, 0 );
	}

	private static class Person {
		@Past
		private Instant birthday;

		public void setBirthday(Instant birthday) {
			this.birthday = birthday;
		}
	}

	private static class CustomClockProvider implements ClockProvider {

		@Override
		public Clock getClock() {
			return Clock.systemDefaultZone();
		}

	}

	private static class ExceptionThrowingClockProvider implements ClockProvider {

		@Override
		public Clock getClock() {
			throw new RuntimeException( "This clock provider throws an exception that should be wrapped in a ValidationException" );
		}

	}
}
