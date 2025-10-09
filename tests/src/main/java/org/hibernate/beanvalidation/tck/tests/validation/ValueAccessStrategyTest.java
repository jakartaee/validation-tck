/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.testng.Assert.assertEquals;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
public class ValueAccessStrategyTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( ValueAccessStrategyTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONSTRAINTDECLARATION, id = "a")
	public void testValidatedObjectIsPassedToValidatorOfClassLevelConstraint() {
		Person person = new Person();
		getValidator().validate( person );

		assertEquals( ValidPerson.ValidPersonValidator.validatedValue, person );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONSTRAINTDECLARATION, id = "a"),
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE, id = "a")
	})
	public void testValueFromFieldIsPassedToValidatorOfFieldLevelConstraint() {
		Person person = new Person();
		getValidator().validate( person );

		assertEquals(
				ValidFirstName.ValidFirstNameValidator.validatedValue,
				"Bob",
				"Expected value to be retrieved from field."
		);
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONSTRAINTDECLARATION, id = "a"),
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE, id = "a")
	})
	public void testValueFromGetterIsPassedToValidatorOfPropertyLevelConstraint() {
		Person person = new Person();
		getValidator().validate( person );

		assertEquals( ValidName.Validator.validatedValue, "Billy", "Expected value to be retrieved from getter." );
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
			public boolean isValid(String value, ConstraintValidatorContext context) {
				validatedValue = value;
				return false;
			}
		}
	}
}
