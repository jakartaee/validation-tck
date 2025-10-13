/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertNoViolations;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.fail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;

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
 * Tests for the implementation of <code>Validator</code>.
 *
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
public class ValidateValueTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( ValidateValueTest.class )
				.withClasses(
						Customer.class,
						Person.class,
						Order.class,
						Address.class
				)
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_VALIDATIONMETHODS, id = "h")
	public void testValidateValueSuccess() {
		Validator validator = TestUtil.getValidatorUnderTest();

		Set<ConstraintViolation<Address>> constraintViolations = validator.validateValue(
				Address.class, "city", "Paris"
		);
		assertNoViolations( constraintViolations );
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_VALIDATIONMETHODS, id = "h")
	public void testValidateValueFailure() {
		Validator validator = TestUtil.getValidatorUnderTest();

		Set<ConstraintViolation<Address>> constraintViolations = validator.validateValue(
				Address.class, "city", null
		);
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withProperty( "city" )
						.withMessage( "You have to specify a city." )
		);
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_VALIDATIONMETHODS, id = "h"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "d"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "e"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "f"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "g"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "h"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "i")
	})
	public void testValidateValue() {
		Validator validator = TestUtil.getValidatorUnderTest();

		Set<ConstraintViolation<Order>> constraintViolations = validator.validateValue(
				Order.class, "orderNumber", null
		);
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withProperty( "orderNumber" )
						.withMessage( "An order must have an order number." )
						.withInvalidValue( null )
						.withRootBeanClass( Order.class )
		);

		ConstraintViolation<Order> constraintViolation = constraintViolations.iterator().next();
		assertNull( constraintViolation.getRootBean() );
		assertNull( constraintViolation.getLeafBean() );
		assertNull( constraintViolation.getExecutableParameters() );
		assertNull( constraintViolation.getExecutableReturnValue() );

		constraintViolations = validator.validateValue( Order.class, "orderNumber", 1234 );
		assertNoViolations( constraintViolations );
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_VALIDATIONMETHODS, id = "i")
	public void testValidateValueWithInvalidPropertyPath() {
		Validator validator = TestUtil.getValidatorUnderTest();

		try {
			validator.validateValue( Customer.class, "foobar", null );
			fail();
		}
		catch ( IllegalArgumentException e ) {
			// success
		}

		// firstname exists, but the capitalisation is wrong
		try {
			validator.validateValue( Customer.class, "FirstName", null );
			fail();
		}
		catch ( IllegalArgumentException e ) {
			// success
		}
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_VALIDATIONMETHODS, id = "i")
	public void testExistingPropertyWoConstraintsNorCascaded() {
		Validator validator = TestUtil.getValidatorUnderTest();
		validator.validateValue( Customer.class, "middleName", new ArrayList<String>() );
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_VALIDATIONMETHODS, id = "i")
	public void testValidateValuePassingNullAsGroup() {
		Validator validator = TestUtil.getValidatorUnderTest();
		validator.validateValue( Customer.class, "firstName", "foobar", (Class<?>) null );
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_VALIDATIONMETHODS, id = "i")
	public void testValidateValueWithEmptyPropertyPath() {
		Validator validator = TestUtil.getValidatorUnderTest();
		validator.validateValue( Customer.class, "", null );
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_VALIDATIONMETHODS, id = "i")
	public void testValidateValueWithNullObject() {
		Validator validator = TestUtil.getValidatorUnderTest();
		validator.validateValue( null, "firstName", "foobar" );
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_VALIDATIONMETHODS, id = "i")
	public void testValidateValueWithNullPropertyName() {
		Validator validator = TestUtil.getValidatorUnderTest();
		validator.validateValue( Customer.class, null, "foobar" );
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_VALIDATIONMETHODS, id = "j")
	public void testValidIsNotHonoredValidateValue() {
		Validator validator = TestUtil.getValidatorUnderTest();

		Set<ConstraintViolation<Customer>> constraintViolations = validator.validateValue(
				Customer.class, "orders", Collections.singleton( new Order() )
		);
		assertNoViolations( constraintViolations );
	}
}
