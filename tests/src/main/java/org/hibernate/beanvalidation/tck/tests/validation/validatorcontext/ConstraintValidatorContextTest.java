/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation.validatorcontext;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.pathWith;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Payload;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
public class ConstraintValidatorContextTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( ConstraintValidatorContextTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "p")
	public void testDefaultError() {
		Validator validator = TestUtil.getValidatorUnderTest();

		DummyValidator.disableDefaultError( false );
		DummyValidator.setCustomErrorMessages( null );

		DummyBean bean = new DummyBean( "foobar" );

		Set<ConstraintViolation<DummyBean>> constraintViolations = validator.validate( bean );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Dummy.class ).withMessage( "dummy message" )
		);
	}

	@Test(expectedExceptions = ValidationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "u")
	public void testDisableDefaultErrorWithoutAddingCustomError() {
		Validator validator = TestUtil.getValidatorUnderTest();

		DummyValidator.disableDefaultError( true );
		Map<String, String> errors = new HashMap<String, String>();
		DummyValidator.setCustomErrorMessages( errors );

		DummyBean bean = new DummyBean( "foobar" );
		validator.validate( bean );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "q")
	})
	public void testDisableDefaultErrorWithCustomErrorNoSubNode() {
		Validator validator = TestUtil.getValidatorUnderTest();

		DummyValidator.disableDefaultError( true );
		Map<String, String> errors = new HashMap<String, String>();
		errors.put( null, "message1" );
		DummyValidator.setCustomErrorMessages( errors );

		DummyBean bean = new DummyBean( "foobar" );

		Set<ConstraintViolation<DummyBean>> constraintViolations = validator.validate( bean );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Dummy.class )
						.withMessage( "message1" )
						.withProperty( "value" )
		);
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "q"),
			@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "t")
	})
	public void testDisableDefaultErrorWithCustomErrorWithSubNode() {
		Validator validator = TestUtil.getValidatorUnderTest();

		DummyValidator.disableDefaultError( true );
		Map<String, String> errors = new HashMap<String, String>();
		errors.put( "subnode", "subnode message" );
		DummyValidator.setCustomErrorMessages( errors );

		DummyBean bean = new DummyBean( "foobar" );

		Set<ConstraintViolation<DummyBean>> constraintViolations = validator.validate( bean );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Dummy.class )
						.withMessage( "subnode message" )
						.withPropertyPath( pathWith()
							   .property( "value" )
							   .property( "subnode" )
						)
		);
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "q"),
			@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "t")
	})
	public void propertyPathInIterable() {
		Validator validator = TestUtil.getValidatorUnderTest();
		Group group = new Group( Gender.MALE, new Person( Gender.FEMALE ) );

		Set<ConstraintViolation<Group>> constraintViolations = validator.validate( group );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( CompatiblePersons.class )
						.withPropertyPath( pathWith()
							   .property( "persons" )
							   .property( null, true, null, 0 )
						)
		);
	}

	private enum Gender {
		MALE, FEMALE
	}

	@CompatiblePersons
	private class Group {
		Gender gender;
		List<Person> persons = new ArrayList<Person>();

		public Group(Gender gender, Person... persons) {
			this.gender = gender;
			this.persons.addAll( Arrays.asList( persons ) );
		}
	}

	private class Person {
		Gender gender;

		public Person(Gender gender) {
			this.gender = gender;
		}
	}

	@Target({ java.lang.annotation.ElementType.TYPE, java.lang.annotation.ElementType.ANNOTATION_TYPE })
	@Retention(RetentionPolicy.RUNTIME)
	@Constraint(validatedBy = { CompatiblePersonsValidator.class })
	@Documented
	public @interface CompatiblePersons {
		String message() default "";

		Class<?>[] groups() default { };

		Class<? extends Payload>[] payload() default { };
	}

	public static class CompatiblePersonsValidator implements ConstraintValidator<CompatiblePersons, Group> {

		@SuppressWarnings("deprecation")
		@Override
		public boolean isValid(Group group, ConstraintValidatorContext constraintValidatorContext) {
			if ( group == null ) {
				return true;
			}

			constraintValidatorContext.disableDefaultConstraintViolation();

			for ( int index = 0; index < group.persons.size(); index++ ) {
				Person person = group.persons.get( index );
				if ( !group.gender.equals( person.gender ) ) {
					constraintValidatorContext
							.buildConstraintViolationWithTemplate( "constraints.CompatiblePersons.gender.message" )
							.addNode( "persons" )
							.addNode( null ).inIterable().atIndex( index )
							.addConstraintViolation();
					return false;
				}
			}
			return true;
		}
	}
}
