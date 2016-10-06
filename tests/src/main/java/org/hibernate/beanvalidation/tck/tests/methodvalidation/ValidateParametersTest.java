/*
* JBoss, Home of Professional Open Source
* Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual contributors
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* http://www.apache.org/licenses/LICENSE-2.0
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.hibernate.beanvalidation.tck.tests.methodvalidation;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ElementKind;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.validation.executable.ExecutableValidator;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.tests.methodvalidation.constraint.MyCrossParameterConstraint;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.model.Address;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.model.Item;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.model.OrderLine;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.model.User;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.model.User.Basic;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.model.User.Extended;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectConstraintTypes;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectNumberOfViolations;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectPathNodeKinds;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectPathNodeNames;
import static org.hibernate.beanvalidation.tck.util.TestUtil.kinds;
import static org.hibernate.beanvalidation.tck.util.TestUtil.names;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

/**
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class ValidateParametersTest extends Arquillian {

	private ExecutableValidator executableValidator;

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClass( ValidateParametersTest.class )
				.withPackage( MyCrossParameterConstraint.class.getPackage() )
				.withClass( Address.class )
				.withClass( Item.class )
				.withClass( OrderLine.class )
				.withClass( User.class )
				.build();
	}

	@BeforeMethod
	public void setupValidator() {
		executableValidator = TestUtil.getValidatorUnderTest().forExecutables();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.1.2", id = "a"),
			@SpecAssertion(section = "5.1.2", id = "b"),
			@SpecAssertion(section = "5.2", id = "d"),
			@SpecAssertion(section = "5.2", id = "e"),
			@SpecAssertion(section = "5.2", id = "f"),
			@SpecAssertion(section = "5.2", id = "g"),
			@SpecAssertion(section = "5.2", id = "h"),
			@SpecAssertion(section = "5.2", id = "i")
	})
	public void testOneViolation() throws Exception {
		String methodName = "setFirstName";

		Object object = new User();
		Method method = User.class.getMethod( methodName, String.class );
		String arg0 = "B";
		Object[] parameterValues = new Object[] { arg0 };

		Set<ConstraintViolation<Object>> violations = executableValidator.validateParameters(
				object,
				method,
				parameterValues
		);

		assertCorrectNumberOfViolations( violations, 1 );

		assertCorrectConstraintTypes( violations, Size.class );
		assertCorrectPathNodeNames( violations, names( methodName, "firstName" ) );
		assertCorrectPathNodeKinds( violations, kinds( ElementKind.METHOD, ElementKind.PARAMETER ) );

		ConstraintViolation<Object> violation = violations.iterator().next();
		assertEquals( violation.getRootBean(), object );
		assertEquals( violation.getRootBeanClass(), User.class );
		assertEquals( violation.getLeafBean(), object );
		assertEquals( violation.getInvalidValue(), arg0 );
		assertEquals( violation.getExecutableParameters(), parameterValues );
		assertNull( violation.getExecutableReturnValue() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.1.2", id = "b"),
			@SpecAssertion(section = "5.2", id = "f"),
			@SpecAssertion(section = "5.2", id = "j")
	})
	public void testOneViolationFromCrossParameterConstraint() throws Exception {
		String methodName = "setAddress";

		Object object = new User();
		Method method = User.class.getMethod( methodName, String.class, String.class );
		Object[] parameterValues = new Object[] { null, null };

		Set<ConstraintViolation<Object>> violations = executableValidator.validateParameters(
				object,
				method,
				parameterValues
		);

		assertCorrectNumberOfViolations( violations, 1 );

		assertCorrectConstraintTypes( violations, MyCrossParameterConstraint.class );
		assertCorrectPathNodeNames( violations, names( methodName, TestUtil.CROSS_PARAMETER_NODE_NAME ) );
		assertCorrectPathNodeKinds( violations, kinds( ElementKind.METHOD, ElementKind.CROSS_PARAMETER ) );

		ConstraintViolation<Object> violation = violations.iterator().next();
		assertEquals( violation.getLeafBean(), object );
		assertEquals( violation.getInvalidValue(), parameterValues );
	}

	@Test
	@SpecAssertion(section = "5.1.2", id = "b")
	public void testTwoViolations() throws Exception {
		String methodName = "setNames";

		Object object = new User();
		Method method = User.class.getMethod( methodName, String.class, CharSequence.class );
		Object[] parameterValues = new Object[] { null, "S" };

		Set<ConstraintViolation<Object>> violations = executableValidator.validateParameters(
				object,
				method,
				parameterValues
		);

		assertCorrectNumberOfViolations( violations, 2 );

		assertCorrectConstraintTypes( violations, NotNull.class, Size.class );
		assertCorrectPathNodeNames(
				violations,
				names( methodName, "firstName" ),
				names( methodName, "lastName" )
		);
		assertCorrectPathNodeKinds(
				violations,
				kinds( ElementKind.METHOD, ElementKind.PARAMETER ),
				kinds( ElementKind.METHOD, ElementKind.PARAMETER )
		);
	}

	@Test
	@SpecAssertion(section = "5.1.2", id = "b")
	public void testTwoViolationsOnSameParameter() throws Exception {
		String methodName = "setFirstNameStrict";

		Object object = new User();
		Method method = User.class.getMethod( methodName, String.class, int.class );
		Object[] parameterValues = new Object[] { "S", 0 };

		Set<ConstraintViolation<Object>> violations = executableValidator.validateParameters(
				object,
				method,
				parameterValues
		);

		assertCorrectNumberOfViolations( violations, 2 );

		assertCorrectConstraintTypes( violations, Pattern.class, Size.class );
		assertCorrectPathNodeNames(
				violations,
				names( methodName, "firstName" ),
				names( methodName, "firstName" )
		);
		assertCorrectPathNodeKinds(
				violations,
				kinds( ElementKind.METHOD, ElementKind.PARAMETER ),
				kinds( ElementKind.METHOD, ElementKind.PARAMETER )
		);
	}

	@Test
	@SpecAssertion(section = "5.1.2", id = "b")
	public void testTwoConstraintsOfSameType() throws Exception {
		String methodName = "setLastNameStrict";

		Object object = new User();
		Method method = User.class.getMethod( methodName, CharSequence.class );
		Object[] parameterValues = new Object[] { "S" };

		Set<ConstraintViolation<Object>> violations = executableValidator.validateParameters(
				object,
				method,
				parameterValues
		);

		assertCorrectNumberOfViolations( violations, 2 );

		assertCorrectConstraintTypes( violations, Size.class, Size.class );
		assertCorrectPathNodeNames(
				violations,
				names( methodName, "lastName" ),
				names( methodName, "lastName" )
		);
		assertCorrectPathNodeKinds(
				violations,
				kinds( ElementKind.METHOD, ElementKind.PARAMETER ),
				kinds( ElementKind.METHOD, ElementKind.PARAMETER )
		);
	}

	@Test
	@SpecAssertion(section = "5.1.2", id = "b")
	public void testCrossParameterConstraintGivenSeveralTimes() throws Exception {
		String methodName = "setAddress";

		Object object = new User();
		Method method = User.class.getMethod(
				methodName,
				String.class,
				String.class,
				String.class
		);
		Object[] parameterValues = new Object[] { null, null, null };

		Set<ConstraintViolation<Object>> violations = executableValidator.validateParameters(
				object,
				method,
				parameterValues
		);

		assertCorrectNumberOfViolations( violations, 2 );

		assertCorrectConstraintTypes(
				violations,
				MyCrossParameterConstraint.class,
				MyCrossParameterConstraint.class
		);
		assertCorrectPathNodeNames(
				violations,
				names( methodName, TestUtil.CROSS_PARAMETER_NODE_NAME ),
				names( methodName, TestUtil.CROSS_PARAMETER_NODE_NAME )
		);
		assertCorrectPathNodeKinds(
				violations,
				kinds( ElementKind.METHOD, ElementKind.CROSS_PARAMETER ),
				kinds( ElementKind.METHOD, ElementKind.CROSS_PARAMETER )
		);
	}

	@Test
	@SpecAssertion(section = "5.1.2", id = "b")
	public void testNoViolations() throws Exception {
		Object object = new User();
		Method method = User.class.getMethod( "setNames", String.class, CharSequence.class );
		Object[] parameterValues = new Object[] { "Bob", "Smith" };

		Set<ConstraintViolation<Object>> violations = executableValidator.validateParameters(
				object,
				method,
				parameterValues
		);

		assertCorrectNumberOfViolations( violations, 0 );
	}

	@Test
	@SpecAssertion(section = "5.1.2", id = "b")
	public void testValidationWithGroup() throws Exception {
		String methodName = "setLastName";

		Object object = new User();
		Method method = User.class.getMethod( methodName, String.class, long.class );
		Object[] parameterValues = new Object[] { "S", 0l };

		Set<ConstraintViolation<Object>> violations = executableValidator.validateParameters(
				object,
				method,
				parameterValues
		);

		assertCorrectNumberOfViolations( violations, 0 );

		violations = executableValidator.validateParameters(
				object,
				method,
				parameterValues,
				Extended.class
		);

		assertCorrectConstraintTypes( violations, Size.class );
		assertCorrectPathNodeNames( violations, names( methodName, "lastName" ) );
		assertCorrectPathNodeKinds( violations, kinds( ElementKind.METHOD, ElementKind.PARAMETER ) );
	}

	@Test
	@SpecAssertion(section = "5.1.2", id = "b")
	public void testCrossParameterConstraintValidationWithGroup() throws Exception {
		String methodName = "setAddressExtended";

		Object object = new User();
		Method method = User.class.getMethod( methodName, CharSequence.class, String.class );
		Object[] parameterValues = new Object[] { null, null };

		Set<ConstraintViolation<Object>> violations = executableValidator.validateParameters(
				object,
				method,
				parameterValues
		);

		assertCorrectNumberOfViolations( violations, 0 );

		violations = executableValidator.validateParameters(
				object,
				method,
				parameterValues,
				Extended.class
		);

		assertCorrectConstraintTypes( violations, MyCrossParameterConstraint.class );
		assertCorrectPathNodeNames( violations, names( methodName, TestUtil.CROSS_PARAMETER_NODE_NAME ) );
		assertCorrectPathNodeKinds( violations, kinds( ElementKind.METHOD, ElementKind.CROSS_PARAMETER ) );
	}

	@Test
	@SpecAssertion(section = "5.1.2", id = "b")
	public void testValidationWithSeveralGroups() throws Exception {
		String methodName = "setAllData";

		Object object = new User();
		Method method = User.class.getMethod( methodName, String.class, String.class, Date.class );
		Object[] parameterValues = new Object[] { null, "S", null };

		Set<ConstraintViolation<Object>> violations = executableValidator.validateParameters(
				object,
				method,
				parameterValues
		);

		assertCorrectNumberOfViolations( violations, 0 );

		violations = executableValidator.validateParameters(
				object,
				method,
				parameterValues,
				Basic.class,
				Extended.class
		);

		assertCorrectConstraintTypes( violations, NotNull.class, Size.class, NotNull.class );
		assertCorrectPathNodeNames(
				violations,
				names( methodName, "dateOfBirth" ),
				names( methodName, "firstName" ),
				names( methodName, "lastName" )
		);
		assertCorrectPathNodeKinds(
				violations,
				kinds( ElementKind.METHOD, ElementKind.PARAMETER ),
				kinds( ElementKind.METHOD, ElementKind.PARAMETER ),
				kinds( ElementKind.METHOD, ElementKind.PARAMETER )
		);
	}

	@Test(expectedExceptions = ValidationException.class)
	@SpecAssertion(section = "5.1.2", id = "a")
	public void testUnexpectedType() throws Exception {
		String methodName = "setName";

		Object object = new Address();
		Method method = Address.class.getMethod( methodName, String.class );
		Object[] parameterValues = new Object[] { "S" };

		executableValidator.validateParameters( object, method, parameterValues );
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	@SpecAssertion(section = "5.1.2", id = "c")
	public void testNullPassedForObjectCausesException() throws Exception {
		Object object = null;
		Method method = User.class.getMethod( "setFirstName", String.class );
		Object[] parameterValues = new Object[] { null };

		executableValidator.validateParameters(
				object,
				method,
				parameterValues
		);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	@SpecAssertion(section = "5.1.2", id = "c")
	public void testNullPassedForMethodCausesException() throws Exception {
		Object object = new User();
		Method method = null;
		Object[] parameterValues = new Object[] { null };

		executableValidator.validateParameters(
				object,
				method,
				parameterValues
		);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	@SpecAssertion(section = "5.1.2", id = "c")
	public void testNullPassedForParameterValuesCausesException() throws Exception {
		Object object = new User();
		Method method = User.class.getMethod( "setFirstName", String.class );
		Object[] parameterValues = null;

		executableValidator.validateParameters(
				object,
				method,
				parameterValues
		);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	@SpecAssertion(section = "5.1.2", id = "c")
	public void testNullPassedForGroupsCausesException() throws Exception {
		Object object = new User();
		Method method = User.class.getMethod( "setFirstName", String.class );
		Object[] parameterValues = new Object[] { null };

		executableValidator.validateParameters(
				object,
				method,
				parameterValues,
				(Class<?>[]) null
		);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	@SpecAssertion(section = "5.1.2", id = "c")
	public void testNullPassedAsSingleGroupCausesException() throws Exception {
		Object object = new User();
		Method method = User.class.getMethod( "setFirstName", String.class );
		Object[] parameterValues = new Object[] { null };

		executableValidator.validateParameters(
				object,
				method,
				parameterValues,
				(Class<?>) null
		);
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.2", id = "f"),
			@SpecAssertion(section = "5.2", id = "g"),
			@SpecAssertion(section = "5.2", id = "h"),
			@SpecAssertion(section = "5.2", id = "i")
	})
	public void testOneViolationForCascadedValidation() throws Exception {
		String methodName = "setItem";

		Object object = new OrderLine( null );
		Item leaf = new Item( "foo" );
		Method method = OrderLine.class.getMethod( methodName, Item.class );
		Object[] parameterValues = new Object[] { leaf };

		Set<ConstraintViolation<Object>> violations = executableValidator.validateParameters(
				object,
				method,
				parameterValues
		);

		assertCorrectNumberOfViolations( violations, 1 );

		ConstraintViolation<Object> violation = violations.iterator().next();

		assertEquals( violation.getLeafBean(), leaf );
		assertEquals( violation.getInvalidValue(), "foo" );
		assertEquals( violation.getExecutableParameters(), parameterValues );
		assertNull( violation.getExecutableReturnValue() );
	}
}
