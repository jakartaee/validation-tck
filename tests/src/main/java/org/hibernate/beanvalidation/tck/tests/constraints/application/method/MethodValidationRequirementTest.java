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
package org.hibernate.beanvalidation.tck.tests.constraints.application.method;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.MethodValidator;
import javax.validation.constraints.NotNull;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.util.Groups;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectConstraintTypes;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectNumberOfViolations;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertNodeNames;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

/**
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "1.1.0")
public class MethodValidationRequirementTest extends Arquillian {

	private MethodValidator executableValidator;

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClassPackage( MethodValidationRequirementTest.class )
				.build();
	}

	@BeforeMethod
	public void setupValidator() {
		executableValidator = TestUtil.getValidatorUnderTest().forMethods();
	}

	@Test(groups = Groups.NOT_IMPLEMENTED)
	@SpecAssertion(section = "4.5", id = "a")
	public void testGettersAreNotConsideredMethods() {
	}

	@Test(expectedExceptions = Exception.class)
	@SpecAssertion(section = "4.5.1", id = "a")
	public void testValidatedMethodsMustNotBeStatic() throws Exception {
		Object object = new CalendarService();
		Method method = getStaticMethod();
		Object[] parameterValues = new Object[3];

		executableValidator.validateParameters( object, method, parameterValues );
		fail( "Validated methods must not be static. Expected exception wasn't thrown." );
	}

	@Test
	@SpecAssertion(section = "4.5.2", id = "a")
	public void testMethodParameterConstraint() throws Exception {
		Object object = new CalendarService();
		Method method = getParameterConstrainedMethod();
		Object[] parameterValues = new Object[1];

		Set<ConstraintViolation<Object>> constraintViolations = executableValidator.validateParameters(
				object,
				method,
				parameterValues
		);
		assertNotNull( constraintViolations );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintTypes( constraintViolations, NotNull.class );
	}

	@Test
	@SpecAssertion(section = "4.5.2", id = "a")
	public void testConstructorParameterConstraint() throws Exception {
		//TODO Use wildcard constructor once BV API is updated (BVAL-358)
		//Constructor<?> constructor = getParameterConstrainedConstructor();
		Constructor constructor = getParameterConstrainedConstructor();
		Object[] parameterValues = new Object[1];

		Set<ConstraintViolation<Object>> constraintViolations = executableValidator.validateConstructorParameters(
				constructor,
				parameterValues
		);

		assertNotNull( constraintViolations );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintTypes( constraintViolations, NotNull.class );
	}

	@Test
	@SpecAssertion(section = "4.5.2.1", id = "a")
	public void testMethodCrossParameterConstraint() throws Exception {
		Object object = new CalendarService();
		Method method = getCrossParameterConstrainedMethod();
		Object[] parameterValues = new Object[] {
				new Date(),
				new Date( new Date().getTime() - 1000 )
		};

		Set<ConstraintViolation<Object>> constraintViolations = executableValidator.validateParameters(
				object,
				method,
				parameterValues
		);
		assertNotNull( constraintViolations );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintTypes( constraintViolations, ConsistentDateParameters.class );
	}

	@Test
	@SpecAssertion(section = "4.5.2.1", id = "a")
	public void testConstructorCrossParameterConstraint() throws Exception {
		//TODO Use wildcard constructor once BV API is updated (BVAL-358)
		//Constructor<?> constructor = getParameterConstrainedConstructor();
		Constructor constructor = getCrossParameterConstrainedConstructor();
		Object[] parameterValues = new Object[] {
				new Date(),
				new Date( new Date().getTime() - 1000 )
		};

		Set<ConstraintViolation<Object>> constraintViolations = executableValidator.validateConstructorParameters(
				constructor,
				parameterValues
		);
		assertNotNull( constraintViolations );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintTypes( constraintViolations, ConsistentDateParameters.class );
	}

	@Test
	@SpecAssertion(section = "4.5.2.1", id = "b")
	public void testMethodParameterAndCrossParameterConstraintsAreEvaluatedAtTheSameTime()
			throws Exception {
		Object object = new CalendarService();
		Method method = getParameterAndCrossParameterConstrainedMethod();
		Object[] parameterValues = new Object[3];

		Set<ConstraintViolation<Object>> constraintViolations = executableValidator.validateParameters(
				object,
				method,
				parameterValues
		);
		assertNotNull( constraintViolations );
		assertCorrectNumberOfViolations( constraintViolations, 2 );
		assertCorrectConstraintTypes(
				constraintViolations,
				NotNull.class,
				ConsistentDateParameters.class
		);
	}

	@Test
	@SpecAssertion(section = "4.5.2.1", id = "b")
	public void testConstructorParameterAndCrossParameterConstraintsAreEvaluatedAtTheSameTime()
			throws Exception {
		//TODO Use wildcard constructor once BV API is updated (BVAL-358)
		//Constructor<?> constructor = getParameterAndCrossParameterConstrainedConstructor();
		Constructor constructor = getParameterAndCrossParameterConstrainedConstructor();
		Object[] parameterValues = new Object[3];

		Set<ConstraintViolation<Object>> constraintViolations = executableValidator.validateConstructorParameters(
				constructor,
				parameterValues
		);
		assertNotNull( constraintViolations );
		assertCorrectNumberOfViolations( constraintViolations, 2 );
		assertCorrectConstraintTypes(
				constraintViolations,
				NotNull.class,
				ConsistentDateParameters.class
		);
	}

	@Test
	@SpecAssertion(section = "4.5.3", id = "a")
	public void testMethodReturnValueConstraint() throws Exception {
		Object object = new CalendarService();
		Method method = getReturnValueConstrainedMethod();
		Object returnValue = null;

		Set<ConstraintViolation<Object>> constraintViolations = executableValidator.validateReturnValue(
				object,
				method,
				returnValue
		);
		assertNotNull( constraintViolations );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintTypes( constraintViolations, NotNull.class );
	}

	@Test
	@SpecAssertion(section = "4.5.3", id = "a")
	public void testConstructorReturnValueConstraint() throws Exception {
		//TODO Use wildcard constructor once BV API is updated (BVAL-358)
		//Constructor<?> constructor = getReturnValueConstrainedConstructor();
		Constructor constructor = getReturnValueConstrainedConstructor();
		Object returnValue = new CalendarService();

		Set<ConstraintViolation<Object>> constraintViolations = executableValidator.validateConstructorReturnValue(
				constructor,
				returnValue
		);
		assertNotNull( constraintViolations );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintTypes( constraintViolations, OnlineCalendarService.class );
	}

	//fails in RI due to traversable resolver not handling method arguments correctly
	@Test(groups=Groups.FAILING_IN_RI)
	@SpecAssertion(section = "4.5.4", id = "a")
	public void testCascadedMethodParameter() throws Exception {
		Object object = new CalendarEvent();
		Method method = getMethodWithCascadedParameter();
		Object[] parameterValues = new Object[] { new User() };

		Set<ConstraintViolation<Object>> constraintViolations = executableValidator.validateParameters(
				object,
				method,
				parameterValues
		);
		assertNotNull( constraintViolations );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintTypes( constraintViolations, NotNull.class );
		assertNodeNames(
				constraintViolations.iterator().next().getPropertyPath(),
				"setUser",
				"arg0",
				"name"
		);
	}

	//fails in RI due to traversable resolver not handling method arguments correctly
	@Test(groups=Groups.FAILING_IN_RI)
	@SpecAssertion(section = "4.5.4", id = "a")
	public void testCascadedConstructorParameter() throws Exception {
		//TODO Use wildcard constructor once BV API is updated (BVAL-358)
		//Constructor<?> constructor = getConstructorWithCascadedParameter();		
		Constructor constructor = getConstructorWithCascadedParameter();
		Object[] parameterValues = new Object[] { new User() };

		Set<ConstraintViolation<Object>> constraintViolations = executableValidator.validateConstructorParameters(
				constructor,
				parameterValues
		);
		assertNotNull( constraintViolations );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintTypes( constraintViolations, NotNull.class );
		assertNodeNames(
				constraintViolations.iterator().next().getPropertyPath(),
				"CalendarEvent",
				"arg0",
				"name"
		);
	}

	//fails in RI due to wrong name for return value node
	@Test(groups = Groups.FAILING_IN_RI)
	@SpecAssertion(section = "4.5.4", id = "a")
	public void testCascadedMethodReturnValue() throws Exception {
		Object object = new CalendarEvent();
		Method method = getMethodWithCascadedReturnValue();
		Object returnValue = new User();

		Set<ConstraintViolation<Object>> constraintViolations = executableValidator.validateReturnValue(
				object,
				method,
				returnValue
		);
		assertNotNull( constraintViolations );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintTypes( constraintViolations, NotNull.class );
		assertNodeNames(
				constraintViolations.iterator().next().getPropertyPath(),
				"getUser",
				null,
				"name"
		);
	}

	//fails in RI due to wrong name for return value node
	@Test(groups = Groups.FAILING_IN_RI)
	@SpecAssertion(section = "4.5.4", id = "a")
	public void testCascadedConstructorReturnValue() throws Exception {
		//TODO Use wildcard constructor once BV API is updated (BVAL-358)
		//Constructor<?> constructor = getConstructorWithCascadedReturnValue();		
		Constructor constructor = getConstructorWithCascadedReturnValue();
		Object returnValue = new CalendarEvent( null, null );

		Set<ConstraintViolation<Object>> constraintViolations = executableValidator.validateConstructorReturnValue(
				constructor,
				returnValue
		);
		assertNotNull( constraintViolations );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintTypes( constraintViolations, NotNull.class );
		assertNodeNames(
				constraintViolations.iterator().next().getPropertyPath(),
				"CalendarEvent",
				null,
				"type"
		);
	}

	//fails in RI due to traversable resolver not handling method arguments correctly
	@Test(groups=Groups.FAILING_IN_RI)
	@SpecAssertion(section = "4.5.4", id = "b")
	public void testCascadedMethodParameterWithNullValue() throws Exception {
		Object object = new CalendarEvent();
		Method method = getMethodWithCascadedParameter();
		Object[] parameterValues = new Object[1];

		Set<ConstraintViolation<Object>> constraintViolations = executableValidator.validateParameters(
				object,
				method,
				parameterValues
		);
		assertNotNull( constraintViolations );
		assertCorrectNumberOfViolations( constraintViolations, 0 );
	}

	//fails in RI due to traversable resolver not handling method arguments correctly
	@Test(groups=Groups.FAILING_IN_RI)
	@SpecAssertion(section = "4.5.4", id = "b")
	public void testCascadedConstructorParameterWithNullValue() throws Exception {
		//TODO Use wildcard constructor once BV API is updated (BVAL-358)
		//Constructor<?> constructor = getConstructorWithCascadedParameter();		
		Constructor constructor = getConstructorWithCascadedParameter();
		Object[] parameterValues = new Object[1];

		Set<ConstraintViolation<Object>> constraintViolations = executableValidator.validateConstructorParameters(
				constructor,
				parameterValues
		);
		assertNotNull( constraintViolations );
		assertCorrectNumberOfViolations( constraintViolations, 0 );
	}

	@Test
	@SpecAssertion(section = "4.5.4", id = "b")
	public void testCascadedMethodReturnValueWithNullValue() throws Exception {
		Object object = new CalendarEvent();
		Method method = getMethodWithCascadedReturnValue();
		Object returnValue = null;

		Set<ConstraintViolation<Object>> constraintViolations = executableValidator.validateReturnValue(
				object,
				method,
				returnValue
		);
		assertNotNull( constraintViolations );
		assertCorrectNumberOfViolations( constraintViolations, 0 );
	}

	//fails in RI due to traversable resolver not handling method arguments correctly
	@Test(groups=Groups.FAILING_IN_RI)
	@SpecAssertion(section = "4.5.4", id = "c")
	public void testCascadedMethodParameterIsValidatedRecursively() throws Exception {
		Object object = new CalendarEvent();
		Method method = getMethodWithCascadedParameter();
		Object[] parameterValues = new Object[] { new User( new Account() ) };

		Set<ConstraintViolation<Object>> constraintViolations = executableValidator.validateParameters(
				object,
				method,
				parameterValues
		);
		assertNotNull( constraintViolations );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintTypes( constraintViolations, NotNull.class );
		assertNodeNames(
				constraintViolations.iterator().next().getPropertyPath(),
				"setUser",
				"arg0",
				"account",
				"login"
		);
	}

	//fails in RI due to traversable resolver not handling method arguments correctly
	@Test(groups=Groups.FAILING_IN_RI)
	@SpecAssertion(section = "4.5.4", id = "c")
	public void testCascadedConstructorParameterIsValidatedRecursively() throws Exception {
		//TODO Use wildcard constructor once BV API is updated (BVAL-358)
		//Constructor<?> constructor = getConstructorWithCascadedParameter();		
		Constructor constructor = getConstructorWithCascadedParameter();
		Object[] parameterValues = new Object[] { new User( new Account() ) };

		Set<ConstraintViolation<Object>> constraintViolations = executableValidator.validateConstructorParameters(
				constructor,
				parameterValues
		);
		assertNotNull( constraintViolations );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintTypes( constraintViolations, NotNull.class );
		assertNodeNames(
				constraintViolations.iterator().next().getPropertyPath(),
				"CalendarEvent",
				"arg0",
				"account",
				"login"
		);
	}

	//fails in RI due to wrong name for return value node
	@Test(groups = Groups.FAILING_IN_RI)
	@SpecAssertion(section = "4.5.4", id = "c")
	public void testCascadedMethodReturnValueIsValidatedRecursively() throws Exception {
		Object object = new CalendarEvent();
		Method method = getMethodWithCascadedReturnValue();
		Object returnValue = new User( new Account() );

		Set<ConstraintViolation<Object>> constraintViolations = executableValidator.validateReturnValue(
				object,
				method,
				returnValue
		);
		assertNotNull( constraintViolations );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintTypes( constraintViolations, NotNull.class );
		assertNodeNames(
				constraintViolations.iterator().next().getPropertyPath(),
				"getUser",
				null,
				"account",
				"login"
		);
	}

	//fails in RI due to wrong name for return value node
	@Test(groups = Groups.FAILING_IN_RI)
	@SpecAssertion(section = "4.5.4", id = "c")
	public void testCascadedConstructorReturnValueIsValidatedRecursively() throws Exception {
		//TODO Use wildcard constructor once BV API is updated (BVAL-358)
		//Constructor<?> constructor = getConstructorWithCascadedReturnValue();		
		Constructor constructor = getConstructorWithCascadedReturnValue();
		Object returnValue = new CalendarEvent();

		Set<ConstraintViolation<Object>> constraintViolations = executableValidator.validateConstructorReturnValue(
				constructor,
				returnValue
		);
		assertNotNull( constraintViolations );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintTypes( constraintViolations, NotNull.class );
		assertNodeNames(
				constraintViolations.iterator().next().getPropertyPath(),
				"CalendarEvent",
				null,
				"user",
				"name"
		);
	}

	private Method getStaticMethod() throws Exception {
		return CalendarService.class.getMethod(
				"createEvent",
				String.class,
				Date.class,
				Date.class
		);
	}

	private Method getParameterConstrainedMethod() throws Exception {
		return CalendarService.class.getMethod( "setType", String.class );
	}

	private Method getCrossParameterConstrainedMethod() throws Exception {
		return CalendarService.class.getMethod( "createEvent", Date.class, Date.class );
	}

	private Method getParameterAndCrossParameterConstrainedMethod() throws Exception {
		return CalendarService.class.getMethod(
				"createEvent",
				Date.class,
				Date.class,
				Integer.class
		);
	}

	private Method getMethodWithCascadedParameter() throws Exception {
		return CalendarEvent.class.getMethod( "setUser", User.class );
	}

	private Method getReturnValueConstrainedMethod() throws Exception {
		return CalendarService.class.getMethod( "findEvents", String.class );
	}

	private Method getMethodWithCascadedReturnValue() throws Exception {
		return CalendarEvent.class.getMethod( "getUser" );
	}

	private Constructor<?> getParameterConstrainedConstructor() throws Exception {
		return CalendarService.class.getConstructor( String.class );
	}

	private Constructor<?> getCrossParameterConstrainedConstructor() throws Exception {
		return CalendarService.class.getConstructor( Date.class, Date.class );
	}

	private Constructor<?> getParameterAndCrossParameterConstrainedConstructor() throws Exception {
		return CalendarService.class.getConstructor(
				Date.class,
				Date.class,
				Integer.class
		);
	}

	private Constructor<?> getConstructorWithCascadedParameter() throws Exception {
		return CalendarEvent.class.getConstructor( User.class );
	}

	private Constructor<?> getReturnValueConstrainedConstructor() throws Exception {
		return CalendarService.class.getConstructor();
	}

	private Constructor<?> getConstructorWithCascadedReturnValue() throws Exception {
		return CalendarEvent.class.getConstructor( String.class );
	}
}
