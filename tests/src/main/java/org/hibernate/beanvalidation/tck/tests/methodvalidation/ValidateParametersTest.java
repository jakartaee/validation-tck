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
import javax.validation.ExecutableValidator;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.validation.metadata.ElementDescriptor.Kind;

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
import org.hibernate.beanvalidation.tck.tests.methodvalidation.model.User;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.model.User.Basic;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.model.User.Extended;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectConstraintTypes;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectNumberOfViolations;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectPathDescriptorKinds;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectPathNodeNames;
import static org.hibernate.beanvalidation.tck.util.TestUtil.kinds;
import static org.hibernate.beanvalidation.tck.util.TestUtil.names;
import static org.testng.Assert.assertEquals;

/**
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "1.1.0")
public class ValidateParametersTest extends Arquillian {

	private ExecutableValidator executableValidator;

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClass( ValidateParametersTest.class )
				.withPackage( MyCrossParameterConstraint.class.getPackage() )
				.withClass( Address.class )
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
			@SpecAssertion(section = "5.1.2", id = "b")
	})
	public void testOneViolation() throws Exception {
		String methodName = "setFirstName";

		Object object = new User();
		Method method = User.class.getMethod( methodName, String.class );
		Object[] parameterValues = new Object[] { null };

		Set<ConstraintViolation<Object>> violations = executableValidator.validateParameters(
				object,
				method,
				parameterValues
		);

		assertCorrectNumberOfViolations( violations, 1 );

		assertCorrectConstraintTypes( violations, NotNull.class );
		assertCorrectPathNodeNames( violations, names( methodName, "arg0" ) );
		assertCorrectPathDescriptorKinds( violations, kinds( Kind.METHOD, Kind.PARAMETER ) );
	}

	@Test
	@SpecAssertion(section = "5.1.2", id = "b")
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
		assertCorrectPathNodeNames( violations, names( methodName ) );
		assertCorrectPathDescriptorKinds( violations, kinds( Kind.METHOD ) );
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
				names( methodName, "arg0" ),
				names( methodName, "arg1" )
		);
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.METHOD, Kind.PARAMETER ),
				kinds( Kind.METHOD, Kind.PARAMETER )
		);
	}

	@Test
	@SpecAssertion(section = "5.1.2", id = "b")
	public void testTwoViolationsOnSameParameter() throws Exception {
		String methodName = "setFirstNameStrict";

		Object object = new User();
		Method method = User.class.getMethod( methodName, String.class, int.class );
		Object[] parameterValues = new Object[] { "S" };

		Set<ConstraintViolation<Object>> violations = executableValidator.validateParameters(
				object,
				method,
				parameterValues
		);

		assertCorrectNumberOfViolations( violations, 2 );

		assertCorrectConstraintTypes( violations, Pattern.class, Size.class );
		assertCorrectPathNodeNames(
				violations,
				names( methodName, "arg0" ),
				names( methodName, "arg0" )
		);
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.METHOD, Kind.PARAMETER ),
				kinds( Kind.METHOD, Kind.PARAMETER )
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
				names( methodName, "arg0" ),
				names( methodName, "arg0" )
		);
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.METHOD, Kind.PARAMETER ),
				kinds( Kind.METHOD, Kind.PARAMETER )
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
		assertCorrectPathNodeNames( violations, names( methodName ), names( methodName ) );
		assertCorrectPathDescriptorKinds( violations, kinds( Kind.METHOD ), kinds( Kind.METHOD ) );
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
		Object[] parameterValues = new Object[] { "S" };

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
		assertCorrectPathNodeNames( violations, names( methodName, "arg0" ) );
		assertCorrectPathDescriptorKinds( violations, kinds( Kind.METHOD, Kind.PARAMETER ) );
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
		assertCorrectPathNodeNames( violations, names( methodName ) );
		assertCorrectPathDescriptorKinds( violations, kinds( Kind.METHOD ) );
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
				names( methodName, "arg0" ),
				names( methodName, "arg1" ),
				names( methodName, "arg2" )
		);
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.METHOD, Kind.PARAMETER ),
				kinds( Kind.METHOD, Kind.PARAMETER ),
				kinds( Kind.METHOD, Kind.PARAMETER )
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


	@Test
	@SpecAssertion(section = "5.2", id = "e")
	public void testGetInvalidValueForCrossParameterConstraint() throws Exception {
		String methodName = "setAddress";

		Object object = new User();
		Method method = User.class.getMethod( methodName, String.class, String.class );
		Object[] parameterValues = new Object[] { "Bob", "Alice" };

		Set<ConstraintViolation<Object>> violations = executableValidator.validateParameters(
				object,
				method,
				parameterValues
		);

		assertCorrectNumberOfViolations( violations, 1 );
		assertEquals( violations.iterator().next().getInvalidValue(), parameterValues );
	}

	@Test
	@SpecAssertion(section = "5.2", id = "e")
	public void testGetInvalidValueForCrossParameterConstraintOnParameterlessMethod()
			throws Exception {
		String methodName = "setAddress";

		Object object = new User();
		Method method = User.class.getMethod( methodName );
		Object[] parameterValues = new Object[] { };

		Set<ConstraintViolation<Object>> violations = executableValidator.validateParameters(
				object,
				method,
				parameterValues
		);

		assertCorrectNumberOfViolations( violations, 1 );
		assertEquals( violations.iterator().next().getInvalidValue(), parameterValues );
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
}
