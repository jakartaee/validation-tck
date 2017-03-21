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

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ElementKind;
import javax.validation.ValidationException;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.BaseExecutableValidatorTest;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.constraint.MyCrossParameterConstraint;
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
public class ValidateReturnValueTest extends BaseExecutableValidatorTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( ValidateReturnValueTest.class )
				.withPackage( MyCrossParameterConstraint.class.getPackage() )
				.withClass( Address.class )
				.withClass( Customer.class )
				.withClass( Email.class )
				.withClass( Item.class )
				.withClass( OrderLine.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "d")
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "e")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "d")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "e")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "f")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "g")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "h")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "i")
	public void testOneViolation() throws Exception {
		String methodName = "getAddress";

		Object object = new Customer();
		Method method = Customer.class.getMethod( methodName );
		Object returnValue = "B";

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateReturnValue(
				object,
				method,
				returnValue
		);

		assertCorrectNumberOfViolations( violations, 1 );

		assertCorrectConstraintTypes( violations, Size.class );
		assertCorrectPathNodeNames( violations, names( methodName, TestUtil.RETURN_VALUE_NODE_NAME ) );
		assertCorrectPathNodeKinds( violations, kinds( ElementKind.METHOD, ElementKind.RETURN_VALUE ) );

		ConstraintViolation<Object> violation = violations.iterator().next();
		assertEquals( violation.getRootBean(), object );
		assertEquals( violation.getRootBeanClass(), Customer.class );
		assertEquals( violation.getLeafBean(), object );
		assertEquals( violation.getInvalidValue(), returnValue );
		assertNull( violation.getExecutableParameters() );
		assertEquals( violation.getExecutableReturnValue(), returnValue );
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "e")
	public void testTwoViolations() throws Exception {
		String methodName = "getFirstName";

		Object object = new Customer();
		Method method = Customer.class.getMethod( methodName, String.class );
		Object returnValue = "S";

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateReturnValue(
				object,
				method,
				returnValue
		);

		assertCorrectNumberOfViolations( violations, 2 );

		assertCorrectConstraintTypes( violations, Pattern.class, Size.class );
		assertCorrectPathNodeNames(
				violations,
				names( methodName, TestUtil.RETURN_VALUE_NODE_NAME ),
				names( methodName, TestUtil.RETURN_VALUE_NODE_NAME )
		);
		assertCorrectPathNodeKinds(
				violations,
				kinds( ElementKind.METHOD, ElementKind.RETURN_VALUE ),
				kinds( ElementKind.METHOD, ElementKind.RETURN_VALUE )
		);
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "e")
	public void testTwoConstraintsOfSameType() throws Exception {
		String methodName = "getLastName";

		Object object = new Customer();
		Method method = Customer.class.getMethod( methodName, CharSequence.class );
		Object returnValue = "S";

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateReturnValue(
				object,
				method,
				returnValue
		);

		assertCorrectNumberOfViolations( violations, 2 );

		assertCorrectConstraintTypes( violations, Size.class, Size.class );
		assertCorrectPathNodeNames(
				violations,
				names( methodName, TestUtil.RETURN_VALUE_NODE_NAME ),
				names( methodName, TestUtil.RETURN_VALUE_NODE_NAME )
		);
		assertCorrectPathNodeKinds(
				violations,
				kinds( ElementKind.METHOD, ElementKind.RETURN_VALUE ),
				kinds( ElementKind.METHOD, ElementKind.RETURN_VALUE )
		);
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "e")
	public void testNoViolations() throws Exception {
		Object object = new Customer();
		Method method = Customer.class.getMethod( "getFirstName", String.class );
		Object returnValue = "aaa";

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateReturnValue(
				object,
				method,
				returnValue
		);

		assertCorrectNumberOfViolations( violations, 0 );
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "e")
	public void testValidationWithGroup() throws Exception {
		String methodName = "getLastName";

		Object object = new Customer();
		Method method = Customer.class.getMethod( methodName, long.class );
		Object returnValue = "S";

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateReturnValue(
				object,
				method,
				returnValue
		);

		assertCorrectNumberOfViolations( violations, 0 );

		violations = getExecutableValidator().validateReturnValue(
				object,
				method,
				returnValue,
				Extended.class
		);

		assertCorrectConstraintTypes( violations, Size.class );
		assertCorrectPathNodeNames( violations, names( methodName, TestUtil.RETURN_VALUE_NODE_NAME ) );
		assertCorrectPathNodeKinds( violations, kinds( ElementKind.METHOD, ElementKind.RETURN_VALUE ) );
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "e")
	public void testValidationWithSeveralGroups() throws Exception {
		String methodName = "getAllData";

		Object object = new Customer();
		Method method = Customer.class.getMethod( methodName, Date.class );
		Object returnValue = "S";

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateReturnValue(
				object,
				method,
				returnValue
		);

		assertCorrectNumberOfViolations( violations, 0 );

		violations = getExecutableValidator().validateReturnValue(
				object,
				method,
				returnValue,
				Basic.class,
				Extended.class
		);

		assertCorrectConstraintTypes( violations, Size.class, Pattern.class );
		assertCorrectPathNodeNames(
				violations,
				names( methodName, TestUtil.RETURN_VALUE_NODE_NAME ),
				names( methodName, TestUtil.RETURN_VALUE_NODE_NAME )
		);
		assertCorrectPathNodeKinds(
				violations,
				kinds( ElementKind.METHOD, ElementKind.RETURN_VALUE ),
				kinds( ElementKind.METHOD, ElementKind.RETURN_VALUE )
		);
	}

	@Test(expectedExceptions = ValidationException.class)
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "d")
	public void testUnexpectedType() throws Exception {
		String methodName = "getValue";

		Object object = new Email();
		Method method = Email.class.getMethod( methodName );
		Object returnValue = "S";

		getExecutableValidator().validateReturnValue( object, method, returnValue );
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "f")
	public void testNullPassedForObjectCausesException() throws Exception {
		Object object = null;
		Method method = Customer.class.getMethod( "getAddress" );
		Object returnValue = null;

		getExecutableValidator().validateReturnValue(
				object,
				method,
				returnValue
		);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "f")
	public void testNullPassedForMethodCausesException() throws Exception {
		Object object = new Customer();
		Method method = null;
		Object returnValue = null;

		getExecutableValidator().validateReturnValue(
				object,
				method,
				returnValue
		);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "f")
	public void testNullPassedForGroupsCausesException() throws Exception {
		Object object = new Customer();
		Method method = Customer.class.getMethod( "getAddress" );
		Object returnValue = null;

		getExecutableValidator().validateReturnValue(
				object,
				method,
				returnValue,
				(Class<?>[]) null
		);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "f")
	public void testNullPassedAsSingleGroupCausesException() throws Exception {
		Object object = new Customer();
		Method method = Customer.class.getMethod( "getAddress" );
		Object returnValue = null;

		getExecutableValidator().validateReturnValue(
				object,
				method,
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
		String methodName = "getItem";

		Object object = new OrderLine( null );
		Item returnValue = new Item( "foo" );
		Method method = OrderLine.class.getMethod( methodName );

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateReturnValue(
				object,
				method,
				returnValue
		);

		assertCorrectNumberOfViolations( violations, 1 );

		ConstraintViolation<Object> violation = violations.iterator().next();

		assertEquals( violation.getLeafBean(), returnValue );
		assertEquals( violation.getInvalidValue(), "foo" );
		assertNull( violation.getExecutableParameters() );
		assertEquals( violation.getExecutableReturnValue(), returnValue );
	}
}
