/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertConstraintViolation;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertCorrectConstraintTypes;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertCorrectConstraintViolationMessages;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertNumberOfViolations;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.fail;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.constraints.Size;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests for the implementation of {@code Validator}.
 *
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class ValidatePropertyTest extends AbstractTCKTest {


	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( ValidatePropertyTest.class )
				.withClasses( Customer.class, Person.class, Order.class, Address.class, BadlyBehavedEntity.class )
				.build();
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_VALIDATIONMETHODS, id = "e")
	public void testPassingNullAsGroup() {
		Customer customer = new Customer();
		getValidator().validateProperty( customer, "firstName", (Class<?>) null );
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_VALIDATIONMETHODS, id = "e")
	public void testIllegalArgumentExceptionIsThrownForNullValue() {
		getValidator().validateProperty( null, "firstName" );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_VALIDATIONMETHODS, id = "e"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_VALIDATIONMETHODS, id = "f")
	})
	public void testValidatePropertyWithInvalidPropertyPath() {
		Customer customer = new Customer();
		try {
			getValidator().validateProperty( customer, "foobar" );
			fail();
		}
		catch ( IllegalArgumentException e ) {
			// success
		}


		// firstname exists, but the capitalisation is wrong
		try {
			getValidator().validateProperty( customer, "FirstName" );
			fail();
		}
		catch ( IllegalArgumentException e ) {
			// success
		}
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_VALIDATIONMETHODS, id = "e")
	public void testValidatePropertyWithNullProperty() {
		Customer customer = new Customer();
		getValidator().validateProperty( customer, null );
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_VALIDATIONMETHODS, id = "e")
	public void testValidatePropertyWithEmptyProperty() {
		Customer customer = new Customer();
		Order order = new Order();
		customer.addOrder( order );

		getValidator().validateProperty( customer, "" );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_VALIDATIONMETHODS, id = "c"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_VALIDATIONMETHODS, id = "d"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_VALIDATIONMETHODS, id = "f"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "e"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "f"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "g"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "h"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "i")
	})
	public void testValidateProperty() {
		Address address = new Address();
		address.setStreet( null );
		address.setZipCode( null );
		String townInNorthWales = "Llanfairpwllgwyngyllgogerychwyrndrobwyll-llantysiliogogogoch";
		address.setCity( townInNorthWales );

		Set<ConstraintViolation<Address>> constraintViolations = getValidator().validateProperty( address, "city" );
		assertNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintTypes( constraintViolations, Size.class );

		ConstraintViolation<Address> violation = constraintViolations.iterator().next();
		assertConstraintViolation( violation, Address.class, townInNorthWales, "city" );
		assertEquals( violation.getRootBean(), address );
		assertEquals( violation.getLeafBean(), address );
		assertEquals( violation.getInvalidValue(), townInNorthWales );
		assertNull( violation.getExecutableParameters() );
		assertNull( violation.getExecutableReturnValue() );
		assertCorrectConstraintViolationMessages(
				constraintViolations, "City name cannot be longer than 30 characters."
		);

		address.setCity( "London" );
		constraintViolations = getValidator().validateProperty( address, "city" );
		assertNumberOfViolations( constraintViolations, 0 );
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_VALIDATIONMETHODS, id = "g")
	public void testValidIsNotHonoredValidateProperty() {
		Customer customer = new Customer();
		Order order = new Order();
		customer.addOrder( order );

		Set<ConstraintViolation<Customer>> constraintViolations = getValidator().validateProperty( customer, "orders" );
		assertNumberOfViolations( constraintViolations, 0 );
	}

	@Test(expectedExceptions = ValidationException.class)
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_VALIDATIONMETHODS, id = "k")
	public void testUnexpectedExceptionsInValidatePropertyGetWrappedInValidationExceptions() {
		getValidator().validateProperty( new BadlyBehavedEntity(), "value" );
	}
}
