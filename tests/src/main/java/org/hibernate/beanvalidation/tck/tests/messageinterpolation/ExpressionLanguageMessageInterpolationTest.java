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
package org.hibernate.beanvalidation.tck.tests.messageinterpolation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Locale;
import java.util.Set;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import javax.validation.Payload;
import javax.validation.Validator;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectConstraintViolationMessages;

/**
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "1.1.0")
public class ExpressionLanguageMessageInterpolationTest extends Arquillian {

	private Validator validator;
	private Locale originalLocale;

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClass( ExpressionLanguageMessageInterpolationTest.class )
				.build();
	}

	@BeforeMethod
	public void setupValidator() {
		validator = TestUtil.getValidatorUnderTest();
	}

	@BeforeMethod
	public void setDefaultLocaleToEnglish() {
		originalLocale = Locale.getDefault();
		Locale.setDefault( Locale.ENGLISH );
	}

	@AfterMethod
	public void resetDefaultLocale() {
		Locale.setDefault( originalLocale );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.3.1", id = "e"),
			@SpecAssertion(section = "5.3.1.3", id = "a"),
			@SpecAssertion(section = "5.3.1.3", id = "b")
	})
	public void testInterpolationWithElExpression() {
		Set<ConstraintViolation<TestBean>> violations = validator.validateProperty( new TestBean(), "firstName" );
		assertCorrectConstraintViolationMessages( violations, "2" );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.3.1.3", id = "a"),
			@SpecAssertion(section = "5.3.1.3", id = "b")
	})
	public void testInterpolationWithSeveralElExpressions() {
		Set<ConstraintViolation<TestBean>> violations = validator.validateProperty( new TestBean(), "lastName" );
		assertCorrectConstraintViolationMessages( violations, "2 some text 6" );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.3.1.3", id = "a"),
			@SpecAssertion(section = "5.3.1.3", id = "f"),
	})
	public void testInterpolationWithUnknownElExpression() {
		Set<ConstraintViolation<TestBean>> violations = validator.validateProperty( new TestBean(), "houseNo" );
		assertCorrectConstraintViolationMessages( violations, "${unknown}" );
	}

	@Test
	@SpecAssertion(section = "5.3.1.3", id = "f")
	public void testInterpolationWithInvalidElExpression() {
		Set<ConstraintViolation<TestBean>> violations = validator.validateProperty( new TestBean(), "addition" );
		assertCorrectConstraintViolationMessages( violations, "${1*}" );
	}

	@Test
	@SpecAssertion(section = "5.3.1.3", id = "f")
	public void testInterpolationWithElExpressionThrowingAnException() {
		Set<ConstraintViolation<TestBean>> violations = validator.validateProperty( new TestBean(), "continent" );
		assertCorrectConstraintViolationMessages( violations, "${validatedValue}" );
	}

	@Test
	@SpecAssertion(section = "5.3.1.3", id = "a")
	public void testInterpolationWithIncompleteElExpression() {
		Set<ConstraintViolation<TestBean>> violations = validator.validateProperty( new TestBean(), "zipCode" );
		assertCorrectConstraintViolationMessages( violations, "${incomplete" );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.3.1.3", id = "a"),
			@SpecAssertion(section = "5.3.1.3", id = "b")
	})
	public void testOnlyDollarSignIsSupportedForEnclosingElExpressions() {
		Set<ConstraintViolation<TestBean>> violations = validator.validateProperty( new TestBean(), "middleName" );
		assertCorrectConstraintViolationMessages( violations, "#{1+1}" );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.3.1.3", id = "a"),
			@SpecAssertion(section = "5.3.1.3", id = "c")
	})
	public void testInterpolationUsingAnnotationAttributesInElExpression() {
		Set<ConstraintViolation<TestBean>> violations = validator.validateProperty( new TestBean(), "street" );
		assertCorrectConstraintViolationMessages( violations, "must be longer than 30" );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.3.1.3", id = "a"),
			@SpecAssertion(section = "5.3.1.3", id = "c")
	})
	public void testInterpolationUsingGroupsAndPayloadInElExpression() {
		Set<ConstraintViolation<TestBean>> violations = validator.validateProperty( new TestBean(), "country" );
		assertCorrectConstraintViolationMessages( violations, "groups: Default, payload: CustomPayload" );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.3.1.3", id = "a"),
			@SpecAssertion(section = "5.3.1.3", id = "d")
	})
	public void testInterpolationUsingValidatedValueInElExpression() {
		Set<ConstraintViolation<TestBean>> violations = validator.validateProperty( new TestBean(), "city" );
		assertCorrectConstraintViolationMessages( violations, "Foo is not long enough" );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.3.1.3", id = "a"),
			@SpecAssertion(section = "5.3.1.3", id = "e")
	})
	public void testInterpolationUsingFormatterInElExpression() {
		Set<ConstraintViolation<TestBean>> violations = validator.validateProperty( new TestBean(), "longitude" );
		assertCorrectConstraintViolationMessages( violations, "98.12 must be larger than 100" );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.3.1.3", id = "a"),
			@SpecAssertion(section = "5.3.1.3", id = "e")
	})
	public void testInterpolationUsingFormatterWithSeveralObjectsInElExpression() {
		Set<ConstraintViolation<TestBean>> violations = validator.validateProperty( new TestBean(), "latitude" );
		assertCorrectConstraintViolationMessages( violations, "98.12 (that is, 98.1235) must be larger than 100" );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.3.1.3", id = "a"),
			@SpecAssertion(section = "5.3.1.3", id = "e")
	})
	public void testInterpolationWithFormatterUsesDefaultLocaleInElExpression() {
		Locale.setDefault( Locale.GERMAN );
		Validator validator = TestUtil.getValidatorUnderTest();
		Set<ConstraintViolation<TestBean>> violations = validator.validateProperty( new TestBean(), "longitude" );
		assertCorrectConstraintViolationMessages( violations, "98,12 must be larger than 100" );
	}

	private interface CustomPayload extends Payload {
	}

	private static class TestBean {

		@NotNull(message = "${1+1}")
		private final String firstName = null;

		@NotNull(message = "${1+1} some text ${2*3}")
		private final String lastName = null;

		@NotNull(message = "#{1+1}")
		private final String middleName = null;

		@Size(message = "must be longer than ${(min * 2) + (max * 2)}", min = 5, max = 10)
		private final String street = "Foo";

		@Size(message = "${validatedValue} is not long enough", min = 5)
		private final String city = "Foo";

		@NotNull(message = "groups: ${groups[0].simpleName}, payload: ${payload[0].simpleName}",
				groups = Default.class,
				payload = CustomPayload.class)
		private final String country = null;

		@Min(message = "${formatter.format('%1$.2f', validatedValue)} must be larger than {value}", value = 100)
		private final double longitude = 98.12345678;

		@Min(message = "${formatter.format('%1$.2f (that is, %2$.4f)', validatedValue, validatedValue)} must be larger than {value}",
				value = 100)
		private final double latitude = 98.12345678;

		@NotNull(message = "${unknown}")
		private final Integer houseNo = null;

		@NotNull(message = "${incomplete")
		private final Integer zipCode = null;

		@NotNull(message = "${1*}")
		private final String addition = null;

		@ValidContinent(message = "${validatedValue}")
		private final Continent continent = new Continent();
	}

	private static class Continent {

		@Override
		public String toString() {
			throw new RuntimeException( "Invalid continent" );
		}
	}

	@Documented
	@Constraint(validatedBy = { ValidContinent.Validator.class })
	@Target({ FIELD })
	@Retention(RUNTIME)
	public @interface ValidContinent {
		String message() default "default message";

		Class<?>[] groups() default { };

		Class<? extends Payload>[] payload() default { };

		public static class Validator implements ConstraintValidator<ValidContinent, Continent> {

			@Override
			public void initialize(ValidContinent annotation) {
			}

			@Override
			public boolean isValid(Continent continent, ConstraintValidatorContext constraintValidatorContext) {
				return false;
			}
		}
	}
}
