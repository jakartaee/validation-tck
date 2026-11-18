/*
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.customconstraint;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertNoViolations;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Set;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintValidatorInitializationContext;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Payload;
import jakarta.validation.UnexpectedTypeException;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Size;
import jakarta.validation.metadata.ConstraintDescriptor;
import jakarta.validation.metadata.PropertyDescriptor;

import org.assertj.core.api.Assertions;
import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.junit.jupiter.api.Test;

/**
 * @author Emmanuel Bernard
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
public class CustomConstraintValidatorTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( CustomConstraintValidatorTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "i")
	public void testConstraintValidatorHasADefaultInitializeMethod() {
		Validator validator = TestUtil.getValidatorUnderTest();

		Ice ice = new Ice();
		ice.temperature = 0;

		Set<ConstraintViolation<Ice>> constraintViolations = validator.validate( ice );
		assertNoViolations( constraintViolations );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "h")
	public void testInitializationContextIsAvailableBeforeValidation() {
		assertInitialization( validator -> validator.initializationContext != null );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "h")
	public void testContextualInitializerReceivesConstraintDescriptor() {
		assertInitialization( validator -> validator.descriptor != null && validator.descriptor.getAnnotation().value() == 42 );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "h")
	public void testProviderDoesNotCallLegacyInitializerSeparately() {
		assertInitialization( validator -> !validator.legacyInitializerCalled );
	}

	private void assertInitialization(InitializationCheck check) {
		Validator validator = TestUtil.getValidatorUnderTest();
		assertNoViolations( validator.validateValue( InitializationBean.class, "check", check ) );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "i")
	public void testRightValidatorIsSelectedAndInitializedCalled() {
		Validator validator = TestUtil.getValidatorUnderTest();
		Shoe shoe = new Shoe();
		shoe.size = -2;
		final PropertyDescriptor propertyDescriptor = validator.getConstraintsForClass( Shoe.class )
				.getConstraintsForProperty( "size" );
		Assertions.assertThat(  propertyDescriptor  ).isNotNull();

		BoundariesConstraintValidator.isValidCalls = 0;
		final Set<ConstraintViolation<Shoe>> constraintViolations = validator.validate( shoe );
		Assertions.assertThat( constraintViolations.size() ).isEqualTo( 1 );
		Assertions.assertThat(  BoundariesConstraintValidator.isValidCalls >= 1 ).as( "Ensure the right validator implementation class was picked." ).isTrue();
		Assertions.assertThat(  BoundariesConstraintValidator.initializeCalled ).as( "Check initialize was called. Note this is not really ensuring that it was called before isValid. That is done in the actual implementation of the validator." ).isTrue();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "j")
	public void testIsValidIsCalledForEachValidation() {
		Validator validator = TestUtil.getValidatorUnderTest();
		Shoe shoe = new Shoe();
		shoe.size = -2;
		int nbrOfValidCalls = 0;
		BoundariesConstraintValidator.isValidCalls = 0;
		validator.validate( shoe );
		Assertions.assertThat(  BoundariesConstraintValidator.isValidCalls > nbrOfValidCalls ).as( "Ensure is valid has been called." ).isTrue();
		nbrOfValidCalls = BoundariesConstraintValidator.isValidCalls;

		validator.validate( shoe );
		Assertions.assertThat(  BoundariesConstraintValidator.isValidCalls > nbrOfValidCalls ).as( "Ensure is valid has been called." ).isTrue();
		nbrOfValidCalls = BoundariesConstraintValidator.isValidCalls;

		validator.validateProperty( shoe, "size" );
		Assertions.assertThat(  BoundariesConstraintValidator.isValidCalls > nbrOfValidCalls ).as( "Ensure is valid has been called." ).isTrue();
		nbrOfValidCalls = BoundariesConstraintValidator.isValidCalls;

		validator.validateValue( Shoe.class, "size", 41 );
		Assertions.assertThat(  BoundariesConstraintValidator.isValidCalls > nbrOfValidCalls ).as( "Ensure is valid has been called." ).isTrue();
	}

	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "m")
	@Test
	public void testUnexpectedTypeExceptionIsRaisedForInvalidType() {
		Assertions.assertThatThrownBy( () -> {

			Validator validator = TestUtil.getValidatorUnderTest();
			validator.validate( new OddShoe() );
	
		} ).isInstanceOf( UnexpectedTypeException.class );
	}

	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "n")
	@Test
	public void testRuntimeExceptionFromIsValidIsWrapped() {
		Assertions.assertThatThrownBy( () -> {

			Validator validator = TestUtil.getValidatorUnderTest();
			Shoe shoe = new Shoe();
			shoe.size = -2;
			BoundariesConstraintValidator.throwRuntimeExceptionFromIsValid = true;
			validator.validate( shoe );
	
		} ).isInstanceOf( ValidationException.class );
	}

	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "n")
	@Test
	public void testRuntimeExceptionFromInitializeIsWrapped() {
		Assertions.assertThatThrownBy( () -> {

			Validator validator = TestUtil.getValidatorUnderTest();
			validator.validate( new Freezer() );
	
		} ).isInstanceOf( ValidationException.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "p")
	public void testOneConstraintViolationPerFailingConstraint() {
		Validator validator = TestUtil.getValidatorUnderTest();

		Author author = new Author();
		author.setLastName( "" );
		author.setFirstName( "" );
		author.setCompany( "" );

		Set<ConstraintViolation<Author>> constraintViolations = validator.validate( author );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Size.class ).withProperty( "firstName" ),
				violationOf( Size.class ).withProperty( "lastName" ),
				violationOf( Size.class ).withProperty( "company" )
		);

		author.setFirstName( "John" );
		constraintViolations = validator.validate( author );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Size.class ).withProperty( "lastName" ),
				violationOf( Size.class ).withProperty( "company" )
		);

		author.setLastName( "Doe" );
		constraintViolations = validator.validate( author );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Size.class ).withProperty( "company" )
		);

		author.setCompany( "JBoss" );
		constraintViolations = validator.validate( author );
		assertNoViolations( constraintViolations );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "r")
	public void testNonInterpolatedMessageParameterIsUsed() {
		Validator validator = TestUtil.getValidatorUnderTest();

		Author author = new Author();
		author.setLastName( "John" );
		author.setFirstName( "Doe" );
		author.setCompany( "" );

		Set<ConstraintViolation<Author>> constraintViolations = validator.validate( author );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Size.class ).withMessage( "The company name must be a minimum 3 characters" )
		);

		ConstraintViolation<Author> constraintViolation = constraintViolations.iterator().next();
		Assertions.assertThat(  constraintViolation.getMessageTemplate() ).isEqualTo( "The company name must be a minimum {min} characters"  );
		Assertions.assertThat( !constraintViolation.getMessageTemplate().equals( constraintViolation.getMessage() ) ).isTrue();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "s")
	public void testDefaultPropertyPath() {
		Validator validator = TestUtil.getValidatorUnderTest();

		Author author = new Author();
		author.setLastName( "John" );
		author.setFirstName( "Doe" );
		author.setCompany( "" );

		Set<ConstraintViolation<Author>> constraintViolations = validator.validate( author );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Size.class ).withProperty( "company" )
		);
	}

	public static class Shoe {
		@Positive
		public int size;
	}

	public static class OddShoe {
		@Positive
		public String size;
	}

	public static class Freezer {
		@Negative
		public int temperature;
	}

	public static class Ice {
		@Zero
		public int temperature;
	}

	@Constraint(validatedBy = InitializationValidator.class)
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface CheckInitialization {
		int value();

		String message() default "Constraint validator initialization check failed";

		Class<?>[] groups() default {};

		Class<? extends Payload>[] payload() default {};
	}

	public static class InitializationBean {
		@CheckInitialization(42)
		private InitializationCheck check;
	}

	public interface InitializationCheck {
		boolean matches(InitializationValidator validator);
	}

	public static class InitializationValidator implements ConstraintValidator<CheckInitialization, InitializationCheck> {
		private ConstraintDescriptor<CheckInitialization> descriptor;
		private ConstraintValidatorInitializationContext initializationContext;
		private boolean legacyInitializerCalled;

		@Override
		public void initialize(ConstraintDescriptor<CheckInitialization> descriptor, ConstraintValidatorInitializationContext context) {
			this.descriptor = descriptor;
			this.initializationContext = context;
		}

		@Override
		public void initialize(CheckInitialization annotation) {
			legacyInitializerCalled = true;
		}

		@Override
		public boolean isValid(InitializationCheck check, ConstraintValidatorContext context) {
			return check.matches( this );
		}
	}
}
