/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.methodvalidation;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertNoViolations;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.pathWith;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.Size;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.constraint.MyCrossParameterConstraint;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.constraint.ValidBusinessCustomer;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.constraint.ValidCustomer;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.model.Address;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.model.Customer;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.model.Customer.Basic;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.model.Customer.Extended;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.model.Email;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.model.Item;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.model.OrderLine;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
public class ValidateConstructorReturnValueTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( ValidateConstructorReturnValueTest.class )
				.withPackage( MyCrossParameterConstraint.class.getPackage() )
				.withClass( Address.class )
				.withClass( Customer.class )
				.withClass( Email.class )
				.withClass( Item.class )
				.withClass( OrderLine.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "j")
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "k")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "d")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "e")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "f")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "g")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "h")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "i")
	public void testOneViolation() throws Exception {
		Constructor<Customer> constructor = Customer.class.getConstructor();
		Customer returnValue = new Customer();

		Set<ConstraintViolation<Customer>> violations = getExecutableValidator().validateConstructorReturnValue(
				constructor,
				returnValue
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( ValidCustomer.class )
						.withPropertyPath( pathWith()
								.constructor( Customer.class )
								.returnValue()
						)
		);

		ConstraintViolation<Customer> violation = violations.iterator().next();
		assertNull( violation.getRootBean() );
		assertEquals( violation.getRootBeanClass(), Customer.class );
		assertEquals( violation.getLeafBean(), returnValue );
		assertEquals( violation.getInvalidValue(), returnValue );
		assertNull( violation.getExecutableParameters() );
		assertEquals( violation.getExecutableReturnValue(), returnValue );
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "k")
	public void testTwoViolations() throws Exception {
		Constructor<Customer> constructor = Customer.class.getConstructor( String.class );
		Customer returnValue = new Customer();

		Set<ConstraintViolation<Customer>> violations = getExecutableValidator().validateConstructorReturnValue(
				constructor,
				returnValue
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( ValidCustomer.class )
						.withPropertyPath( pathWith()
								.constructor( Customer.class )
								.returnValue()
						),
				violationOf( ValidBusinessCustomer.class )
						.withPropertyPath( pathWith()
								.constructor( Customer.class )
								.returnValue()
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "k")
	public void testTwoConstraintsOfSameType() throws Exception {
		Constructor<Customer> constructor = Customer.class.getConstructor( CharSequence.class );
		Customer returnValue = new Customer();

		Set<ConstraintViolation<Customer>> violations = getExecutableValidator().validateConstructorReturnValue(
				constructor,
				returnValue
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( ValidCustomer.class )
						.withPropertyPath( pathWith()
								.constructor( Customer.class )
								.returnValue()
						),
				violationOf( ValidCustomer.class )
						.withPropertyPath( pathWith()
								.constructor( Customer.class )
								.returnValue()
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "k")
	public void testNoViolations() throws Exception {
		Constructor<Customer> constructor = Customer.class.getConstructor();
		Customer returnValue = new Customer( "Bob" );

		Set<ConstraintViolation<Customer>> violations = getExecutableValidator().validateConstructorReturnValue(
				constructor,
				returnValue
		);

		assertNoViolations( violations );
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "k")
	public void testValidationWithGroup() throws Exception {
		Constructor<Customer> constructor = Customer.class.getConstructor( long.class );
		Customer returnValue = new Customer();

		Set<ConstraintViolation<Customer>> violations = getExecutableValidator().validateConstructorReturnValue(
				constructor,
				returnValue
		);

		assertNoViolations( violations );

		violations = getExecutableValidator().validateConstructorReturnValue(
				constructor,
				returnValue,
				Extended.class
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( ValidCustomer.class )
						.withPropertyPath( pathWith()
								.constructor( Customer.class )
								.returnValue()
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "k")
	public void testValidationWithSeveralGroups() throws Exception {
		Constructor<Customer> constructor = Customer.class.getConstructor( Date.class );
		Customer returnValue = new Customer();

		Set<ConstraintViolation<Customer>> violations = getExecutableValidator().validateConstructorReturnValue(
				constructor,
				returnValue
		);

		assertNoViolations( violations );

		violations = getExecutableValidator().validateConstructorReturnValue(
				constructor,
				returnValue,
				Basic.class,
				Extended.class
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( ValidCustomer.class )
						.withPropertyPath( pathWith()
								.constructor( Customer.class )
								.returnValue()
						),
				violationOf( ValidBusinessCustomer.class )
						.withPropertyPath( pathWith()
								.constructor( Customer.class )
								.returnValue()
						)
		);
	}

	@Test(expectedExceptions = ValidationException.class)
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "j")
	public void testUnexpectedType() throws Exception {
		Constructor<Email> constructor = Email.class.getConstructor();
		Email returnValue = new Email();

		getExecutableValidator().validateConstructorReturnValue( constructor, returnValue );
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "l")
	public void testNullPassedForConstructorCausesException() throws Exception {
		Constructor<Customer> constructor = null;
		Customer returnValue = new Customer();

		getExecutableValidator().validateConstructorReturnValue(
				constructor,
				returnValue
		);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "l")
	public void testNullPassedForReturnValueCausesException() throws Exception {
		Constructor<Customer> constructor = Customer.class.getConstructor();
		Customer returnValue = null;

		getExecutableValidator().validateConstructorReturnValue(
				constructor,
				returnValue
		);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "l")
	public void testNullPassedForGroupsCausesException() throws Exception {
		Constructor<Customer> constructor = Customer.class.getConstructor();
		Customer returnValue = new Customer();

		getExecutableValidator().validateConstructorReturnValue(
				constructor,
				returnValue,
				(Class<?>[]) null
		);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "l")
	public void testNullPassedAsSingleGroupCausesException() throws Exception {
		Constructor<Customer> constructor = Customer.class.getConstructor();
		Customer returnValue = new Customer();

		getExecutableValidator().validateConstructorReturnValue(
				constructor,
				returnValue,
				(Class<?>) null
		);
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "f")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "g")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "h")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "i")
	public void testOneViolationForCascadedValidation() throws Exception {
		Item leaf = new Item( "foo" );
		Object createdObject = new OrderLine( leaf );
		Constructor<OrderLine> constructor = OrderLine.class.getConstructor( Item.class );

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateConstructorReturnValue(
				constructor,
				createdObject
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( Size.class )
		);

		ConstraintViolation<Object> violation = violations.iterator().next();

		assertEquals( violation.getLeafBean(), leaf );
		assertEquals( violation.getInvalidValue(), "foo" );
		assertNull( violation.getExecutableParameters() );
		assertEquals( violation.getExecutableReturnValue(), createdObject );
	}
}
