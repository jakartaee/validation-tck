/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.validdeclarations;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.pathWith;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.validdeclarations.constraint.ValidBusinessCalendarEvent;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.validdeclarations.constraint.ValidCalendarEvent;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.validdeclarations.constraint.ValidCalendarServiceSubClass;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.validdeclarations.model.CalendarEvent;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.validdeclarations.service.CalendarService;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.validdeclarations.service.impl.BusinessCalendarServiceImplementation;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.validdeclarations.service.impl.CalendarServiceImplementation;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.validdeclarations.service.impl.CalendarServiceSubClass;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.validdeclarations.service.impl.ImplementationOfParallelInterfacesMarkingReturnValueAsCascaded;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "3.0.0")
public class ValidMethodConstraintDeclarationTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( ValidMethodConstraintDeclarationTest.class )
				.withPackage( ValidCalendarEvent.class.getPackage() )
				.withPackage( CalendarEvent.class.getPackage() )
				.withPackage( CalendarServiceImplementation.class.getPackage() )
				.withPackage( CalendarService.class.getPackage() )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_INHERITANCE, id = "c")
	public void testReturnValueConstraintAddedInInterfaceImplementation() throws Exception {
		Object object = new CalendarServiceImplementation();
		Method method = getCreateEventMethod( object );
		Object returnValue = null;

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateReturnValue(
				object,
				method,
				returnValue
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_INHERITANCE, id = "c")
	public void testReturnValueConstraintAddedInSubClass() throws Exception {
		Object object = new CalendarServiceSubClass();
		Method method = getCreateEventMethod( object );
		Object returnValue = null;

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateReturnValue(
				object,
				method,
				returnValue
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_INHERITANCE, id = "c")
	public void testReturnValueMarkedAsCascadedInInterfaceImplementation() throws Exception {
		Object object = new CalendarServiceImplementation();
		Method method = getCreateEventWithDurationMethod( object );
		Object returnValue = new CalendarEvent();

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateReturnValue(
				object,
				method,
				returnValue
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.method( "createEvent" )
								.returnValue()
								.property( "name" )
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_INHERITANCE, id = "c")
	public void testReturnValueConstraintFromInterfaceAndImplementationAddUp() throws Exception {
		Object object = new CalendarServiceImplementation();
		Method method = getCreateEventWithParticipantsMethod( object );
		Object returnValue = null;

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateReturnValue(
				object,
				method,
				returnValue
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class ),
				violationOf( ValidCalendarEvent.class )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_INHERITANCE, id = "c")
	public void testReturnValueConstraintFromInterfacesAndImplementationAddUp() throws Exception {
		Object object = new BusinessCalendarServiceImplementation();
		Method method = getCreateEventWithParticipantsMethod( object );
		Object returnValue = null;

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateReturnValue(
				object,
				method,
				returnValue
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class ),
				violationOf( ValidBusinessCalendarEvent.class ),
				violationOf( ValidCalendarEvent.class )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_INHERITANCE, id = "c")
	public void testReturnValueMarkedAsCascadedInSubClass() throws Exception {
		Object object = new CalendarServiceSubClass();
		Method method = getCreateEventWithDurationMethod( object );
		Object returnValue = new CalendarEvent();

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateReturnValue(
				object,
				method,
				returnValue
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.method( "createEvent" )
								.returnValue()
								.property( "name" )
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_INHERITANCE, id = "f")
	public void testParameterConstraintAddedToConstructorInSubClass() throws Exception {
		Constructor<?> constructor = CalendarServiceSubClass.class.getConstructor( int.class );
		Object[] parameterValues = new Object[] { 4 };

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateConstructorParameters(
				constructor,
				parameterValues
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( Min.class )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_INHERITANCE, id = "f")
	public void testParameterConstraintMarkedAsCascadedAtConstructorInSubClass() throws Exception {
		Constructor<?> constructor = CalendarServiceSubClass.class.getConstructor( CalendarEvent.class );
		Object[] parameterValues = new Object[] { new CalendarEvent() };

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateConstructorParameters(
				constructor,
				parameterValues
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.constructor( CalendarServiceSubClass.class )
								.parameter( "defaultEvent", 0 )
								.property( "name" )
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_INHERITANCE, id = "f")
	public void testReturnValueConstraintAddedToConstructorInSubClass() throws Exception {
		Constructor<?> constructor = CalendarServiceSubClass.class.getConstructor( String.class );
		Object returnValue = new CalendarServiceSubClass();

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateConstructorReturnValue(
				constructor,
				returnValue
		);

		//only the constraint on the sub-type constructor should be validated
		assertThat( violations ).containsOnlyViolations(
				violationOf( ValidCalendarServiceSubClass.class )
						.withPropertyPath( pathWith()
								.constructor( CalendarServiceSubClass.class )
								.returnValue()
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_INHERITANCE, id = "f")
	public void testReturnValueMarkedAsCascadedAtConstructorInSuperAndSubClass() throws Exception {
		Constructor<?> constructor = CalendarServiceSubClass.class.getConstructor( long.class );
		Object returnValue = new CalendarServiceSubClass();

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateConstructorReturnValue(
				constructor,
				returnValue
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( Min.class )
						.withPropertyPath( pathWith()
								.constructor( CalendarServiceSubClass.class )
								.returnValue()
								.property( "mode" )
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_INHERITANCE, id = "d")
	public void testReturnValueMarkedAsCascadedInParallelInterfaces() throws Exception {
		Object object = new ImplementationOfParallelInterfacesMarkingReturnValueAsCascaded();
		Method method = getCreateEventMethod( object );
		Object returnValue = new CalendarEvent();

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateReturnValue(
				object,
				method,
				returnValue
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.method( "createEvent" )
								.returnValue()
								.property( "name" )
						)
		);
	}

	private Method getCreateEventMethod(Object object) throws NoSuchMethodException {
		return object.getClass().getMethod(
				"createEvent",
				Date.class,
				Date.class
		);
	}

	private Method getCreateEventWithDurationMethod(Object object) throws NoSuchMethodException {
		return object.getClass().getMethod(
				"createEvent",
				Date.class,
				Date.class,
				int.class
		);
	}

	private Method getCreateEventWithParticipantsMethod(Object object)
			throws NoSuchMethodException {
		return object.getClass().getMethod(
				"createEvent",
				Date.class,
				Date.class,
				List.class
		);
	}
}
