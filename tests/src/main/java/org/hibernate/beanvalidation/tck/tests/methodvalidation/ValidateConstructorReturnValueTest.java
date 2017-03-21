/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.methodvalidation;

import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectConstraintTypes;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectNumberOfViolations;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectPathNodeKinds;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectPathNodeNames;
import static org.hibernate.beanvalidation.tck.util.TestUtil.kinds;
import static org.hibernate.beanvalidation.tck.util.TestUtil.names;
import static org.hibernate.beanvalidation.tck.util.TestUtil.webArchiveBuilder;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ElementKind;
import javax.validation.ValidationException;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.BaseExecutableValidatorTest;
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
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class ValidateConstructorReturnValueTest extends BaseExecutableValidatorTest {

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

		assertCorrectNumberOfViolations( violations, 1 );

		assertCorrectConstraintTypes( violations, ValidCustomer.class );
		assertCorrectPathNodeNames( violations, names( "Customer", TestUtil.RETURN_VALUE_NODE_NAME ) );
		assertCorrectPathNodeKinds(
				violations,
				kinds( ElementKind.CONSTRUCTOR, ElementKind.RETURN_VALUE )
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

		assertCorrectNumberOfViolations( violations, 2 );

		assertCorrectConstraintTypes( violations, ValidCustomer.class, ValidBusinessCustomer.class );
		assertCorrectPathNodeNames(
				violations,
				names( "Customer", TestUtil.RETURN_VALUE_NODE_NAME ),
				names( "Customer", TestUtil.RETURN_VALUE_NODE_NAME )
		);
		assertCorrectPathNodeKinds(
				violations,
				kinds( ElementKind.CONSTRUCTOR, ElementKind.RETURN_VALUE ),
				kinds( ElementKind.CONSTRUCTOR, ElementKind.RETURN_VALUE )
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

		assertCorrectNumberOfViolations( violations, 2 );

		assertCorrectConstraintTypes( violations, ValidCustomer.class, ValidCustomer.class );
		assertCorrectPathNodeNames(
				violations,
				names( "Customer", TestUtil.RETURN_VALUE_NODE_NAME ),
				names( "Customer", TestUtil.RETURN_VALUE_NODE_NAME )
		);
		assertCorrectPathNodeKinds(
				violations,
				kinds( ElementKind.CONSTRUCTOR, ElementKind.RETURN_VALUE ),
				kinds( ElementKind.CONSTRUCTOR, ElementKind.RETURN_VALUE )
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

		assertCorrectNumberOfViolations( violations, 0 );
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

		assertCorrectNumberOfViolations( violations, 0 );

		violations = getExecutableValidator().validateConstructorReturnValue(
				constructor,
				returnValue,
				Extended.class
		);

		assertCorrectConstraintTypes( violations, ValidCustomer.class );
		assertCorrectPathNodeNames( violations, names( "Customer", TestUtil.RETURN_VALUE_NODE_NAME ) );
		assertCorrectPathNodeKinds(
				violations,
				kinds( ElementKind.CONSTRUCTOR, ElementKind.RETURN_VALUE )
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

		assertCorrectNumberOfViolations( violations, 0 );

		violations = getExecutableValidator().validateConstructorReturnValue(
				constructor,
				returnValue,
				Basic.class,
				Extended.class
		);

		assertCorrectConstraintTypes( violations, ValidCustomer.class, ValidBusinessCustomer.class );
		assertCorrectPathNodeNames(
				violations,
				names( "Customer", TestUtil.RETURN_VALUE_NODE_NAME ),
				names( "Customer", TestUtil.RETURN_VALUE_NODE_NAME )
		);
		assertCorrectPathNodeKinds(
				violations,
				kinds( ElementKind.CONSTRUCTOR, ElementKind.RETURN_VALUE ),
				kinds( ElementKind.CONSTRUCTOR, ElementKind.RETURN_VALUE )
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

		assertCorrectNumberOfViolations( violations, 1 );

		ConstraintViolation<Object> violation = violations.iterator().next();

		assertEquals( violation.getLeafBean(), leaf );
		assertEquals( violation.getInvalidValue(), "foo" );
		assertNull( violation.getExecutableParameters() );
		assertEquals( violation.getExecutableReturnValue(), createdObject );
	}
}
