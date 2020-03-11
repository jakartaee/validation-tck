/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.messageinterpolation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Locale;
import java.util.Set;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Payload;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.Default;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
// CHECKSTYLE:OFF
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
// CHECKSTYLE:ON
import org.testng.annotations.Test;

/**
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "3.0.0")
public class ExpressionLanguageMessageInterpolationTest extends AbstractTCKTest {

	private Locale originalLocale;

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( ExpressionLanguageMessageInterpolationTest.class )
				.build();
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
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_DEFAULTMESSAGEINTERPOLATION, id = "e")
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_DEFAULTMESSAGEINTERPOLATION_EXPRESSIONLANGUAGE, id = "a")
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_DEFAULTMESSAGEINTERPOLATION_EXPRESSIONLANGUAGE, id = "b")
	public void testInterpolationWithElExpression() {
		Set<ConstraintViolation<TestBean>> violations = getValidator().validateProperty( new TestBean(), "firstName" );
		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class ).withMessage( "2" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_DEFAULTMESSAGEINTERPOLATION_EXPRESSIONLANGUAGE, id = "a")
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_DEFAULTMESSAGEINTERPOLATION_EXPRESSIONLANGUAGE, id = "b")
	public void testInterpolationWithSeveralElExpressions() {
		Set<ConstraintViolation<TestBean>> violations = getValidator().validateProperty( new TestBean(), "lastName" );
		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class ).withMessage( "2 some text 6" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_DEFAULTMESSAGEINTERPOLATION_EXPRESSIONLANGUAGE, id = "a")
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_DEFAULTMESSAGEINTERPOLATION_EXPRESSIONLANGUAGE, id = "f")
	public void testInterpolationWithUnknownElExpression() {
		Set<ConstraintViolation<TestBean>> violations = getValidator().validateProperty( new TestBean(), "houseNo" );
		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class ).withMessage( "${unknown}" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_DEFAULTMESSAGEINTERPOLATION_EXPRESSIONLANGUAGE, id = "f")
	public void testInterpolationWithInvalidElExpression() {
		Set<ConstraintViolation<TestBean>> violations = getValidator().validateProperty( new TestBean(), "addition" );
		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class ).withMessage( "${1*}" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_DEFAULTMESSAGEINTERPOLATION_EXPRESSIONLANGUAGE, id = "f")
	public void testInterpolationWithElExpressionThrowingAnException() {
		Set<ConstraintViolation<TestBean>> violations = getValidator().validateProperty( new TestBean(), "continent" );
		assertThat( violations ).containsOnlyViolations(
				violationOf( ValidContinent.class ).withMessage( "${validatedValue}" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_DEFAULTMESSAGEINTERPOLATION_EXPRESSIONLANGUAGE, id = "a")
	public void testInterpolationWithIncompleteElExpression() {
		Set<ConstraintViolation<TestBean>> violations = getValidator().validateProperty( new TestBean(), "zipCode" );
		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class ).withMessage( "${incomplete" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_DEFAULTMESSAGEINTERPOLATION_EXPRESSIONLANGUAGE, id = "a")
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_DEFAULTMESSAGEINTERPOLATION_EXPRESSIONLANGUAGE, id = "b")
	public void testOnlyDollarSignIsSupportedForEnclosingElExpressions() {
		Set<ConstraintViolation<TestBean>> violations = getValidator().validateProperty( new TestBean(), "middleName" );
		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class ).withMessage( "#{1+1}" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_DEFAULTMESSAGEINTERPOLATION_EXPRESSIONLANGUAGE, id = "a")
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_DEFAULTMESSAGEINTERPOLATION_EXPRESSIONLANGUAGE, id = "c")
	public void testInterpolationUsingAnnotationAttributesInElExpression() {
		Set<ConstraintViolation<TestBean>> violations = getValidator().validateProperty( new TestBean(), "street" );
		assertThat( violations ).containsOnlyViolations(
				violationOf( Size.class ).withMessage( "must be longer than 30" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_DEFAULTMESSAGEINTERPOLATION_EXPRESSIONLANGUAGE, id = "a")
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_DEFAULTMESSAGEINTERPOLATION_EXPRESSIONLANGUAGE, id = "c")
	public void testInterpolationUsingGroupsAndPayloadInElExpression() {
		Set<ConstraintViolation<TestBean>> violations = getValidator().validateProperty( new TestBean(), "country" );
		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class ).withMessage( "groups: Default, payload: CustomPayload" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_DEFAULTMESSAGEINTERPOLATION_EXPRESSIONLANGUAGE, id = "a")
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_DEFAULTMESSAGEINTERPOLATION_EXPRESSIONLANGUAGE, id = "d")
	public void testInterpolationUsingValidatedValueInElExpression() {
		Set<ConstraintViolation<TestBean>> violations = getValidator().validateProperty( new TestBean(), "city" );
		assertThat( violations ).containsOnlyViolations(
				violationOf( Size.class ).withMessage( "Foo is not long enough" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_DEFAULTMESSAGEINTERPOLATION_EXPRESSIONLANGUAGE, id = "a")
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_DEFAULTMESSAGEINTERPOLATION_EXPRESSIONLANGUAGE, id = "e")
	public void testInterpolationUsingFormatterInElExpression() {
		Set<ConstraintViolation<TestBean>> violations = getValidator().validateProperty( new TestBean(), "longitude" );
		assertThat( violations ).containsOnlyViolations(
				violationOf( Min.class ).withMessage( "98.12 must be larger than 100" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_DEFAULTMESSAGEINTERPOLATION_EXPRESSIONLANGUAGE, id = "a")
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_DEFAULTMESSAGEINTERPOLATION_EXPRESSIONLANGUAGE, id = "e")
	public void testInterpolationUsingFormatterWithSeveralObjectsInElExpression() {
		Set<ConstraintViolation<TestBean>> violations = getValidator().validateProperty( new TestBean(), "latitude" );
		assertThat( violations ).containsOnlyViolations(
				violationOf( Min.class ).withMessage( "98.12 (that is, 98.1235) must be larger than 100" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_DEFAULTMESSAGEINTERPOLATION_EXPRESSIONLANGUAGE, id = "a")
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_DEFAULTMESSAGEINTERPOLATION_EXPRESSIONLANGUAGE, id = "e")
	public void testInterpolationWithFormatterUsesDefaultLocaleInElExpression() {
		Locale.setDefault( Locale.GERMAN );
		Validator validator = TestUtil.getValidatorUnderTest();
		Set<ConstraintViolation<TestBean>> violations = validator.validateProperty( new TestBean(), "longitude" );
		assertThat( violations ).containsOnlyViolations(
				violationOf( Min.class ).withMessage( "98,12 must be larger than 100" )
		);
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
			public boolean isValid(Continent continent, ConstraintValidatorContext constraintValidatorContext) {
				return false;
			}
		}
	}
}
