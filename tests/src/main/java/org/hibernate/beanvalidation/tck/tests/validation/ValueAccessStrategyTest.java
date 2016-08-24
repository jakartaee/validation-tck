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
package org.hibernate.beanvalidation.tck.tests.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import javax.validation.Validator;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.testng.Assert.assertEquals;

/**
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class ValueAccessStrategyTest extends Arquillian {

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClass( ValueAccessStrategyTest.class )
				.build();
	}

	private Validator validator;

	@Test
	@SpecAssertion(section = "4.2", id = "a")
	public void testValidatedObjectIsPassedToValidatorOfClassLevelConstraint() {
		Person person = new Person();
		validator.validate( person );

		assertEquals( ValidPerson.ValidPersonValidator.validatedValue, person );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "4.2", id = "a"),
			@SpecAssertion(section = "4.6", id = "a")
	})
	public void testValueFromFieldIsPassedToValidatorOfFieldLevelConstraint() {
		Person person = new Person();
		validator.validate( person );

		assertEquals(
				ValidFirstName.ValidFirstNameValidator.validatedValue,
				"Bob",
				"Expected value to be retrieved from field."
		);
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "4.2", id = "a"),
			@SpecAssertion(section = "4.6", id = "a")
	})
	public void testValueFromGetterIsPassedToValidatorOfPropertyLevelConstraint() {
		Person person = new Person();
		validator.validate( person );

		assertEquals( ValidName.Validator.validatedValue, "Billy", "Expected value to be retrieved from getter." );
	}

	@BeforeMethod
	public void setupValidator() {
		validator = TestUtil.getValidatorUnderTest();
	}

	@ValidPerson
	private static class Person {

		@ValidFirstName
		private final String firstName = "Bob";

		@ValidName
		public String getFirstName() {
			return "Billy";
		}
	}

	@Constraint(validatedBy = ValidPerson.ValidPersonValidator.class)
	@Documented
	@Target({ METHOD, CONSTRUCTOR, FIELD, TYPE })
	@Retention(RUNTIME)
	public @interface ValidPerson {
		String message() default "{ValidPerson.message}";

		Class<?>[] groups() default { };

		Class<? extends Payload>[] payload() default { };

		public static class ValidPersonValidator implements ConstraintValidator<ValidPerson, Person> {

			private static Person validatedValue;

			@Override
			public void initialize(ValidPerson constraintAnnotation) {
			}

			@Override
			public boolean isValid(Person value, ConstraintValidatorContext context) {
				validatedValue = value;
				return false;
			}
		}
	}

	@Constraint(validatedBy = ValidFirstName.ValidFirstNameValidator.class)
	@Documented
	@Target({ METHOD, CONSTRUCTOR, FIELD, TYPE })
	@Retention(RUNTIME)
	public @interface ValidFirstName {
		String message() default "{ValidFirstName.message}";

		Class<?>[] groups() default { };

		Class<? extends Payload>[] payload() default { };

		public static class ValidFirstNameValidator implements ConstraintValidator<ValidFirstName, String> {

			private static String validatedValue;

			@Override
			public void initialize(ValidFirstName constraintAnnotation) {
			}

			@Override
			public boolean isValid(String value, ConstraintValidatorContext context) {
				validatedValue = value;
				return false;
			}
		}
	}

	@Constraint(validatedBy = ValidName.Validator.class)
	@Documented
	@Target({ METHOD, CONSTRUCTOR, FIELD, TYPE })
	@Retention(RUNTIME)
	public @interface ValidName {
		String message() default "{ValidFirstName.message}";

		Class<?>[] groups() default { };

		Class<? extends Payload>[] payload() default { };

		public static class Validator implements ConstraintValidator<ValidName, String> {

			private static String validatedValue;

			@Override
			public void initialize(ValidName constraintAnnotation) {
			}

			@Override
			public boolean isValid(String value, ConstraintValidatorContext context) {
				validatedValue = value;
				return false;
			}
		}
	}
}
