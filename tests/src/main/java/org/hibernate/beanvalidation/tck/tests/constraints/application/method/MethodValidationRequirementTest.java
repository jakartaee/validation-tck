/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.application.method;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertCorrectConstraintTypes;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertNumberOfViolations;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.pathWith;
import static org.testng.Assert.assertNotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.constraints.NotNull;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class MethodValidationRequirementTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( MethodValidationRequirementTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_PARAMETERCONSTRAINTS, id = "a")
	public void testMethodParameterConstraintsAreDeclaredByAnnotatingParameters() throws Exception {
		Object object = new CalendarService();
		Method method = CalendarService.class.getMethod( "setType", String.class );
		Object[] parameterValues = new Object[1];

		Set<ConstraintViolation<Object>> constraintViolations = getExecutableValidator().validateParameters(
				object,
				method,
				parameterValues
		);
		assertNotNull( constraintViolations );
		assertNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintTypes( constraintViolations, NotNull.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_PARAMETERCONSTRAINTS, id = "a")
	public void testConstructorParameterConstraintsAreDeclaredByAnnotatingParameters()
			throws Exception {
		Constructor<?> constructor = CalendarService.class.getConstructor( String.class );
		Object[] parameterValues = new Object[1];

		Set<ConstraintViolation<Object>> constraintViolations = getExecutableValidator().validateConstructorParameters(
				constructor,
				parameterValues
		);

		assertNotNull( constraintViolations );
		assertNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintTypes( constraintViolations, NotNull.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_PARAMETERCONSTRAINTS_CROSSPARAMETERCONSTRAINTS, id = "a")
	public void testCrossParameterConstraintsAreDeclaredByAnnotatingMethods() throws Exception {
		Object object = new CalendarService();
		Method method = CalendarService.class.getMethod( "createEvent", Date.class, Date.class );
		Object[] parameterValues = new Object[] {
				new Date(),
				new Date( new Date().getTime() - 1000 )
		};

		Set<ConstraintViolation<Object>> constraintViolations = getExecutableValidator().validateParameters(
				object,
				method,
				parameterValues
		);
		assertNotNull( constraintViolations );
		assertNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintTypes( constraintViolations, ConsistentDateParameters.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_PARAMETERCONSTRAINTS_CROSSPARAMETERCONSTRAINTS, id = "a")
	public void testCrossParameterConstraintsAreDeclaredByAnnotatingConstructors()
			throws Exception {
		Constructor<?> constructor = CalendarService.class.getConstructor( Date.class, Date.class );
		Object[] parameterValues = new Object[] {
				new Date(),
				new Date( new Date().getTime() - 1000 )
		};

		Set<ConstraintViolation<Object>> constraintViolations = getExecutableValidator().validateConstructorParameters(
				constructor,
				parameterValues
		);
		assertNotNull( constraintViolations );
		assertNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintTypes( constraintViolations, ConsistentDateParameters.class );
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "a")
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

		Set<ConstraintViolation<Object>> constraintViolations = getExecutableValidator().validateParameters(
				object,
				method,
				parameterValues
		);
		assertNotNull( constraintViolations );
		assertNumberOfViolations( constraintViolations, 2 );
		assertCorrectConstraintTypes(
				constraintViolations,
				NotNull.class,
				ConsistentDateParameters.class
		);
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "g")
	public void testConstructorParameterAndCrossParameterConstraintsAreEvaluatedAtTheSameTime()
			throws Exception {
		Constructor<?> constructor = CalendarService.class.getConstructor(
				Date.class,
				Date.class,
				Integer.class
		);
		Object[] parameterValues = new Object[3];

		Set<ConstraintViolation<Object>> constraintViolations = getExecutableValidator().validateConstructorParameters(
				constructor,
				parameterValues
		);
		assertNotNull( constraintViolations );
		assertNumberOfViolations( constraintViolations, 2 );
		assertCorrectConstraintTypes(
				constraintViolations,
				NotNull.class,
				ConsistentDateParameters.class
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_RETURNVALUECONSTRAINTS, id = "a")
	public void testReturnValueConstraintsAreDeclaredByAnnotatingMethods() throws Exception {
		Object object = new CalendarService();
		Method method = CalendarService.class.getMethod( "findEvents", String.class );
		Object returnValue = null;

		Set<ConstraintViolation<Object>> constraintViolations = getExecutableValidator().validateReturnValue(
				object,
				method,
				returnValue
		);
		assertNotNull( constraintViolations );
		assertNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintTypes( constraintViolations, NotNull.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_RETURNVALUECONSTRAINTS, id = "a")
	public void testReturnValueConstraintsAreDeclaredByAnnotatingConstructors() throws Exception {
		Constructor<?> constructor = CalendarService.class.getConstructor();
		Object returnValue = new CalendarService();

		Set<ConstraintViolation<Object>> constraintViolations = getExecutableValidator().validateConstructorReturnValue(
				constructor,
				returnValue
		);
		assertNotNull( constraintViolations );
		assertNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintTypes( constraintViolations, OnlineCalendarService.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_CASCADEDVALIDATION, id = "a")
	public void testMethodParameterIsMarkedAsCascaded() throws Exception {
		Object object = new CalendarEvent();
		Method method = CalendarEvent.class.getMethod( "setUser", User.class );
		Object[] parameterValues = new Object[] { new User() };

		Set<ConstraintViolation<Object>> constraintViolations = getExecutableValidator().validateParameters(
				object,
				method,
				parameterValues
		);
		assertNotNull( constraintViolations );
		assertNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintTypes( constraintViolations, NotNull.class );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.method( "setUser" )
						.parameter( "user", 0 )
						.property( "name" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_CASCADEDVALIDATION, id = "a")
	public void testConstructorParameterIsMarkedAsCascaded() throws Exception {
		Constructor<?> constructor = CalendarEvent.class.getConstructor( User.class );
		Object[] parameterValues = new Object[] { new User() };

		Set<ConstraintViolation<Object>> constraintViolations = getExecutableValidator().validateConstructorParameters(
				constructor,
				parameterValues
		);
		assertNotNull( constraintViolations );
		assertNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintTypes( constraintViolations, NotNull.class );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.constructor( CalendarEvent.class )
						.parameter( "user", 0 )
						.property( "name" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_CASCADEDVALIDATION, id = "a")
	public void testMethodReturnValueIsMarkedAsCascaded() throws Exception {
		Object object = new CalendarEvent();
		Method method = CalendarEvent.class.getMethod( "getUser" );
		Object returnValue = new User();

		Set<ConstraintViolation<Object>> constraintViolations = getExecutableValidator().validateReturnValue(
				object,
				method,
				returnValue
		);
		assertNotNull( constraintViolations );
		assertNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintTypes( constraintViolations, NotNull.class );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.method( "getUser" )
						.returnValue()
						.property( "name" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_CASCADEDVALIDATION, id = "a")
	public void testConstructorReturnValueIsMarkedAsCascaded() throws Exception {
		Constructor<?> constructor = CalendarEvent.class.getConstructor( String.class );
		Object returnValue = new CalendarEvent( null, null );

		Set<ConstraintViolation<Object>> constraintViolations = getExecutableValidator().validateConstructorReturnValue(
				constructor,
				returnValue
		);
		assertNotNull( constraintViolations );
		assertNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintTypes( constraintViolations, NotNull.class );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.constructor( CalendarEvent.class )
						.returnValue()
						.property( "type" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_CASCADEDVALIDATION, id = "b")
	public void testPassingNullToCascadedMethodParameterCausesNoViolation() throws Exception {
		Object object = new CalendarEvent();
		Method method = CalendarEvent.class.getMethod( "setUser", User.class );
		Object[] parameterValues = new Object[1];

		Set<ConstraintViolation<Object>> constraintViolations = getExecutableValidator().validateParameters(
				object,
				method,
				parameterValues
		);
		assertNotNull( constraintViolations );
		assertNumberOfViolations( constraintViolations, 0 );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_CASCADEDVALIDATION, id = "b")
	public void testPassingNullToCascadedConstructorParameterCausesNoViolation() throws Exception {
		Constructor<?> constructor = CalendarEvent.class.getConstructor( User.class );
		Object[] parameterValues = new Object[1];

		Set<ConstraintViolation<Object>> constraintViolations = getExecutableValidator().validateConstructorParameters(
				constructor,
				parameterValues
		);
		assertNotNull( constraintViolations );
		assertNumberOfViolations( constraintViolations, 0 );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_CASCADEDVALIDATION, id = "b")
	public void testReturningNullFromCascadedMethodCausesNoViolation() throws Exception {
		Object object = new CalendarEvent();
		Method method = CalendarEvent.class.getMethod( "getUser" );
		Object returnValue = null;

		Set<ConstraintViolation<Object>> constraintViolations = getExecutableValidator().validateReturnValue(
				object,
				method,
				returnValue
		);
		assertNotNull( constraintViolations );
		assertNumberOfViolations( constraintViolations, 0 );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_CASCADEDVALIDATION, id = "c")
	public void testCascadedMethodParameterIsValidatedRecursively() throws Exception {
		Object object = new CalendarEvent();
		Method method = CalendarEvent.class.getMethod( "setUser", User.class );
		Object[] parameterValues = new Object[] { new User( new Account() ) };

		Set<ConstraintViolation<Object>> constraintViolations = getExecutableValidator().validateParameters(
				object,
				method,
				parameterValues
		);
		assertNotNull( constraintViolations );
		assertNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintTypes( constraintViolations, NotNull.class );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.method( "setUser" )
						.parameter( "user", 0 )
						.property( "account" )
						.property( "login" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_CASCADEDVALIDATION, id = "c")
	public void testCascadedConstructorParameterIsValidatedRecursively() throws Exception {
		Constructor<?> constructor = CalendarEvent.class.getConstructor( User.class );
		Object[] parameterValues = new Object[] { new User( new Account() ) };

		Set<ConstraintViolation<Object>> constraintViolations = getExecutableValidator().validateConstructorParameters(
				constructor,
				parameterValues
		);
		assertNotNull( constraintViolations );
		assertNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintTypes( constraintViolations, NotNull.class );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.constructor( CalendarEvent.class )
						.parameter( "user", 0 )
						.property( "account" )
						.property( "login" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_CASCADEDVALIDATION, id = "c")
	public void testCascadedMethodReturnValueIsValidatedRecursively() throws Exception {
		Object object = new CalendarEvent();
		Method method = CalendarEvent.class.getMethod( "getUser" );
		Object returnValue = new User( new Account() );

		Set<ConstraintViolation<Object>> constraintViolations = getExecutableValidator().validateReturnValue(
				object,
				method,
				returnValue
		);
		assertNotNull( constraintViolations );
		assertNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintTypes( constraintViolations, NotNull.class );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.method( "getUser" )
						.returnValue()
						.property( "account" )
						.property( "login" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_CASCADEDVALIDATION, id = "c")
	public void testCascadedConstructorReturnValueIsValidatedRecursively() throws Exception {
		Constructor<?> constructor = CalendarEvent.class.getConstructor( String.class );
		Object returnValue = new CalendarEvent();

		Set<ConstraintViolation<Object>> constraintViolations = getExecutableValidator().validateConstructorReturnValue(
				constructor,
				returnValue
		);
		assertNotNull( constraintViolations );
		assertNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintTypes( constraintViolations, NotNull.class );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.constructor( CalendarEvent.class )
						.returnValue()
						.property( "user" )
						.property( "name" )
		);
	}
}
