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
import javax.validation.ExecutableValidator;
import javax.validation.constraints.NotNull;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

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

	private ExecutableValidator executableValidator;

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClassPackage( MethodValidationRequirementTest.class )
				.build();
	}

	@BeforeMethod
	public void setupValidator() {
		executableValidator = TestUtil.getValidatorUnderTest().forExecutables();
	}

	@Test(expectedExceptions = Exception.class)
	@SpecAssertion(section = "4.5.1", id = "a")
	public void testValidatedMethodsMustNotBeStatic() throws Exception {
		Object object = new CalendarService();
		Method method = CalendarService.class.getMethod(
				"createEvent",
				String.class,
				Date.class,
				Date.class
		);
		Object[] parameterValues = new Object[3];

		executableValidator.validateParameters( object, method, parameterValues );
		fail( "Validated methods must not be static. Expected exception wasn't thrown." );
	}

	@Test
	@SpecAssertion(section = "4.5.2", id = "a")
	public void testMethodParameterConstraintsAreDeclaredByAnnotingParameters() throws Exception {
		Object object = new CalendarService();
		Method method = CalendarService.class.getMethod( "setType", String.class );
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
	public void testConstructorParameterConstraintsAreDeclaredByAnnotingParameters()
			throws Exception {
		Constructor<?> constructor = CalendarService.class.getConstructor( String.class );
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
	public void testCrossParameterConstraintsAreDeclaredByAnnotatingMethods() throws Exception {
		Object object = new CalendarService();
		Method method = CalendarService.class.getMethod( "createEvent", Date.class, Date.class );
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
	public void testCrossParameterConstraintsAreDeclaredByAnnotatingConstructors()
			throws Exception {
		Constructor<?> constructor = CalendarService.class.getConstructor( Date.class, Date.class );
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
		Method method = CalendarService.class.getMethod(
				"createEvent",
				Date.class,
				Date.class,
				Integer.class
		);
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
		Constructor<?> constructor = CalendarService.class.getConstructor(
				Date.class,
				Date.class,
				Integer.class
		);
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
	public void testReturnValueConstraintsAreDeclaredByAnnotatingMethods() throws Exception {
		Object object = new CalendarService();
		Method method = CalendarService.class.getMethod( "findEvents", String.class );
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
	public void testReturnValueConstraintsAreDeclaredByAnnotatingConstructors() throws Exception {
		Constructor<?> constructor = CalendarService.class.getConstructor();
		Object returnValue = new CalendarService();

		Set<ConstraintViolation<Object>> constraintViolations = executableValidator.validateConstructorReturnValue(
				constructor,
				returnValue
		);
		assertNotNull( constraintViolations );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintTypes( constraintViolations, OnlineCalendarService.class );
	}

	@Test
	@SpecAssertion(section = "4.5.4", id = "a")
	public void testMethodParameterIsMarkedAsCascaded() throws Exception {
		Object object = new CalendarEvent();
		Method method = CalendarEvent.class.getMethod( "setUser", User.class );
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

	@Test
	@SpecAssertion(section = "4.5.4", id = "a")
	public void testConstructorParameterIsMarkedAsCascaded() throws Exception {
		Constructor<?> constructor = CalendarEvent.class.getConstructor( User.class );
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

	@Test
	@SpecAssertion(section = "4.5.4", id = "a")
	public void testMethodReturnValueIsMarkedAsCascaded() throws Exception {
		Object object = new CalendarEvent();
		Method method = CalendarEvent.class.getMethod( "getUser" );
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

	@Test
	@SpecAssertion(section = "4.5.4", id = "a")
	public void testConstructorReturnValueIsMarkedAsCascaded() throws Exception {
		Constructor<?> constructor = CalendarEvent.class.getConstructor( String.class );
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

	@Test
	@SpecAssertion(section = "4.5.4", id = "b")
	public void testPassingNullToCascadedMethodParameterCausesNoViolation() throws Exception {
		Object object = new CalendarEvent();
		Method method = CalendarEvent.class.getMethod( "setUser", User.class );
		Object[] parameterValues = new Object[1];

		Set<ConstraintViolation<Object>> constraintViolations = executableValidator.validateParameters(
				object,
				method,
				parameterValues
		);
		assertNotNull( constraintViolations );
		assertCorrectNumberOfViolations( constraintViolations, 0 );
	}

	@Test
	@SpecAssertion(section = "4.5.4", id = "b")
	public void testPassingNullToCascadedConstructorParameterCausesNoViolation() throws Exception {
		Constructor<?> constructor = CalendarEvent.class.getConstructor( User.class );
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
	public void testReturningNullFromCascadedMethodCausesNoViolation() throws Exception {
		Object object = new CalendarEvent();
		Method method = CalendarEvent.class.getMethod( "getUser" );
		Object returnValue = null;

		Set<ConstraintViolation<Object>> constraintViolations = executableValidator.validateReturnValue(
				object,
				method,
				returnValue
		);
		assertNotNull( constraintViolations );
		assertCorrectNumberOfViolations( constraintViolations, 0 );
	}

	@Test
	@SpecAssertion(section = "4.5.4", id = "c")
	public void testCascadedMethodParameterIsValidatedRecursively() throws Exception {
		Object object = new CalendarEvent();
		Method method = CalendarEvent.class.getMethod( "setUser", User.class );
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

	@Test
	@SpecAssertion(section = "4.5.4", id = "c")
	public void testCascadedConstructorParameterIsValidatedRecursively() throws Exception {
		Constructor<?> constructor = CalendarEvent.class.getConstructor( User.class );
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

	@Test
	@SpecAssertion(section = "4.5.4", id = "c")
	public void testCascadedMethodReturnValueIsValidatedRecursively() throws Exception {
		Object object = new CalendarEvent();
		Method method = CalendarEvent.class.getMethod( "getUser" );
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

	@Test
	@SpecAssertion(section = "4.5.4", id = "c")
	public void testCascadedConstructorReturnValueIsValidatedRecursively() throws Exception {
		Constructor<?> constructor = CalendarEvent.class.getConstructor( String.class );
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
}
