/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.customconstraint;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertNoViolations;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.UnexpectedTypeException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.constraints.Size;
import javax.validation.metadata.PropertyDescriptor;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Emmanuel Bernard
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
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
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "h")
	public void testRightValidatorIsSelectedAndInitializedCalled() {
		Validator validator = TestUtil.getValidatorUnderTest();
		Shoe shoe = new Shoe();
		shoe.size = -2;
		final PropertyDescriptor propertyDescriptor = validator.getConstraintsForClass( Shoe.class )
				.getConstraintsForProperty( "size" );
		assertNotNull( propertyDescriptor );

		BoundariesConstraintValidator.isValidCalls = 0;
		final Set<ConstraintViolation<Shoe>> constraintViolations = validator.validate( shoe );
		assertEquals( 1, constraintViolations.size() );
		assertTrue(
				BoundariesConstraintValidator.isValidCalls >= 1,
				"Ensure the right validator implementation class was picked."
		);
		assertTrue(
				BoundariesConstraintValidator.initializeCalled,
				"Check initialize was called. Note this is not really ensuring that it was called before isValid. That is done in the actual implementation of the validator."
		);
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
		assertTrue(
				BoundariesConstraintValidator.isValidCalls > nbrOfValidCalls,
				"Ensure is valid has been called."
		);
		nbrOfValidCalls = BoundariesConstraintValidator.isValidCalls;

		validator.validate( shoe );
		assertTrue(
				BoundariesConstraintValidator.isValidCalls > nbrOfValidCalls,
				"Ensure is valid has been called."
		);
		nbrOfValidCalls = BoundariesConstraintValidator.isValidCalls;

		validator.validateProperty( shoe, "size" );
		assertTrue(
				BoundariesConstraintValidator.isValidCalls > nbrOfValidCalls,
				"Ensure is valid has been called."
		);
		nbrOfValidCalls = BoundariesConstraintValidator.isValidCalls;

		validator.validateValue( Shoe.class, "size", 41 );
		assertTrue(
				BoundariesConstraintValidator.isValidCalls > nbrOfValidCalls,
				"Ensure is valid has been called."
		);
	}


	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "m")
	@Test(expectedExceptions = UnexpectedTypeException.class)
	public void testUnexpectedTypeExceptionIsRaisedForInvalidType() {
		Validator validator = TestUtil.getValidatorUnderTest();
		validator.validate( new OddShoe() );
	}

	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "n")
	@Test(expectedExceptions = ValidationException.class)
	public void testRuntimeExceptionFromIsValidIsWrapped() {
		Validator validator = TestUtil.getValidatorUnderTest();
		Shoe shoe = new Shoe();
		shoe.size = -2;
		BoundariesConstraintValidator.throwRuntimeExceptionFromIsValid = true;
		validator.validate( shoe );
	}

	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "n")
	@Test(expectedExceptions = ValidationException.class)
	public void testRuntimeExceptionFromInitializeIsWrapped() {
		Validator validator = TestUtil.getValidatorUnderTest();
		validator.validate( new Freezer() );
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
		assertEquals( constraintViolation.getMessageTemplate(), "The company name must be a minimum {min} characters" );
		assertTrue( !constraintViolation.getMessageTemplate().equals( constraintViolation.getMessage() ) );
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
}
