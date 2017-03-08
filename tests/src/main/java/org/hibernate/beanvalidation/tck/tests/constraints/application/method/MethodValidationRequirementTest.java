/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.application.method;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.constraints.NotNull;
import javax.validation.executable.ExecutableValidator;

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

/**
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
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

	@Test
	@SpecAssertion(section = "4.5.2", id = "a")
	public void testMethodParameterConstraintsAreDeclaredByAnnotatingParameters() throws Exception {
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
	public void testConstructorParameterConstraintsAreDeclaredByAnnotatingParameters()
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
	@SpecAssertion(section = "5.1.2", id = "a")
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
	@SpecAssertion(section = "5.1.2", id = "g")
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
				"user",
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
				"user",
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
				TestUtil.RETURN_VALUE_NODE_NAME,
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
				TestUtil.RETURN_VALUE_NODE_NAME,
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
				"user",
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
				"user",
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
				TestUtil.RETURN_VALUE_NODE_NAME,
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
				TestUtil.RETURN_VALUE_NODE_NAME,
				"user",
				"name"
		);
	}
}
