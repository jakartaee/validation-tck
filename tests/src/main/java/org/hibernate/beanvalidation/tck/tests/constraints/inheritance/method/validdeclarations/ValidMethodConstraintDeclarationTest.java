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
package org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.validdeclarations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.MethodValidator;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.validdeclarations.constraint.ValidBusinessCalendarEvent;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.validdeclarations.constraint.ValidCalendarEvent;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.validdeclarations.constraint.ValidCalendarServiceSubClass;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.validdeclarations.model.CalendarEvent;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.validdeclarations.service.CalendarService;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.validdeclarations.service.impl.BusinessCalendarServiceImplementation;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.validdeclarations.service.impl.CalendarServiceImplementation;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.validdeclarations.service.impl.CalendarServiceSubClass;
import org.hibernate.beanvalidation.tck.util.Groups;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectConstraintTypes;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertNodeNames;

/**
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "1.1.0")
public class ValidMethodConstraintDeclarationTest extends Arquillian {

	private MethodValidator executableValidator;

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClassPackage( ValidMethodConstraintDeclarationTest.class )
				.withPackage( ValidCalendarEvent.class.getPackage() )
				.withPackage( CalendarEvent.class.getPackage() )
				.withPackage( CalendarServiceImplementation.class.getPackage() )
				.withPackage( CalendarService.class.getPackage() )
				.build();
	}

	@BeforeMethod
	public void setupValidator() {
		executableValidator = TestUtil.getValidatorUnderTest().forMethods();
	}

	@Test
	@SpecAssertion(section = "4.5.5", id = "c")
	public void testReturnValueConstraintAddedInInterfaceImplementation() throws Exception {
		Object object = new CalendarServiceImplementation();
		Method method = getCreateEventMethod( object );
		Object returnValue = null;

		Set<ConstraintViolation<Object>> violations = executableValidator.validateReturnValue(
				object,
				method,
				returnValue
		);

		assertCorrectConstraintTypes( violations, NotNull.class );
	}

	@Test
	@SpecAssertion(section = "4.5.5", id = "c")
	public void testReturnValueConstraintAddedInSubClass() throws Exception {
		Object object = new CalendarServiceSubClass();
		Method method = getCreateEventMethod( object );
		Object returnValue = null;

		Set<ConstraintViolation<Object>> violations = executableValidator.validateReturnValue(
				object,
				method,
				returnValue
		);

		assertCorrectConstraintTypes( violations, NotNull.class );
	}

	//Fails due to wrong return value node name
	@Test(groups = Groups.FAILING_IN_RI)
	@SpecAssertion(section = "4.5.5", id = "c")
	public void testReturnValueMarkedAsCascadedInInterfaceImplementation() throws Exception {
		Object object = new CalendarServiceImplementation();
		Method method = getCreateEventWithDurationMethod( object );
		Object returnValue = new CalendarEvent();

		Set<ConstraintViolation<Object>> violations = executableValidator.validateReturnValue(
				object,
				method,
				returnValue
		);

		assertCorrectConstraintTypes( violations, NotNull.class );
		assertNodeNames(
				violations.iterator().next().getPropertyPath(),
				"createEvent",
				null,
				"name"
		);
	}

	@Test
	@SpecAssertion(section = "4.5.5", id = "c")
	public void testReturnValueConstraintFromInterfaceAndImplementationAddUp() throws Exception {
		Object object = new CalendarServiceImplementation();
		Method method = getCreateEventWithParticipantsMethod( object );
		Object returnValue = null;

		Set<ConstraintViolation<Object>> violations = executableValidator.validateReturnValue(
				object,
				method,
				returnValue
		);

		assertCorrectConstraintTypes( violations, NotNull.class, ValidCalendarEvent.class );
	}

	@Test
	@SpecAssertion(section = "4.5.5", id = "c")
	public void testReturnValueConstraintFromInterfacesAndImplementationAddUp() throws Exception {
		Object object = new BusinessCalendarServiceImplementation();
		Method method = getCreateEventWithParticipantsMethod( object );
		Object returnValue = null;

		Set<ConstraintViolation<Object>> violations = executableValidator.validateReturnValue(
				object,
				method,
				returnValue
		);

		assertCorrectConstraintTypes(
				violations,
				NotNull.class,
				ValidCalendarEvent.class,
				ValidBusinessCalendarEvent.class
		);
	}

	//Fails due to wrong return value node name
	@Test(groups = Groups.FAILING_IN_RI)
	@SpecAssertion(section = "4.5.5", id = "c")
	public void testReturnValueMarkedAsCascadedInSubClass() throws Exception {
		Object object = new CalendarServiceSubClass();
		Method method = getCreateEventWithDurationMethod( object );
		Object returnValue = new CalendarEvent();

		Set<ConstraintViolation<Object>> violations = executableValidator.validateReturnValue(
				object,
				method,
				returnValue
		);

		assertCorrectConstraintTypes( violations, NotNull.class );
		assertNodeNames(
				violations.iterator().next().getPropertyPath(),
				"createEvent",
				null,
				"name"
		);
	}

	@Test
	@SpecAssertion(section = "4.5.5", id = "e")
	public void testParameterConstraintAddedToConstructorInSubClass() throws Exception {
		//Constructor<?> constructor = CalendarServiceSubClass.class.getConstructor( int.class );
		//TODO: Use wildcard constructor
		Constructor constructor = CalendarServiceSubClass.class.getConstructor( int.class );
		Object[] parameterValues = new Object[] { 4 };

		Set<ConstraintViolation<Object>> violations = executableValidator.validateConstructorParameters(
				constructor,
				parameterValues
		);

		assertCorrectConstraintTypes( violations, Min.class );
	}

	//fails in RI due to traversable resolver not handling method arguments correctly
	@Test(groups = Groups.FAILING_IN_RI)
	@SpecAssertion(section = "4.5.5", id = "e")
	public void testParameterConstraintMarkedAsCascadedAtConstructorInSubClass() throws Exception {
		//Constructor<?> constructor = CalendarServiceSubClass.class.getConstructor( CalendarEvent.class );
		//TODO: Use wildcard constructor
		Constructor constructor = CalendarServiceSubClass.class.getConstructor( CalendarEvent.class );
		Object[] parameterValues = new Object[] { new CalendarEvent() };

		Set<ConstraintViolation<Object>> violations = executableValidator.validateConstructorParameters(
				constructor,
				parameterValues
		);

		assertCorrectConstraintTypes( violations, NotNull.class );
		assertNodeNames(
				violations.iterator().next().getPropertyPath(),
				"CalendarServiceSubClass",
				"arg0",
				"name"
		);
	}

	//Fails due to wrong return type node name
	@Test(groups = Groups.FAILING_IN_RI)
	@SpecAssertion(section = "4.5.5", id = "e")
	public void testReturnValueConstraintAddedToConstructorInSubClass() throws Exception {
		//Constructor<?> constructor = CalendarServiceSubClass.class.getConstructor( String.class );
		//TODO: Use wildcard constructor
		Constructor constructor = CalendarServiceSubClass.class.getConstructor( String.class );
		Object returnValue = new CalendarServiceSubClass();

		Set<ConstraintViolation<Object>> violations = executableValidator.validateConstructorReturnValue(
				constructor,
				returnValue
		);

		//only the constraint on the sub-type constructor should be validated
		assertCorrectConstraintTypes( violations, ValidCalendarServiceSubClass.class );
		assertNodeNames(
				violations.iterator().next().getPropertyPath(),
				"CalendarServiceSubClass",
				null
		);
	}

	//Fails due to wrong return type node name
	@Test(groups = Groups.FAILING_IN_RI)
	@SpecAssertion(section = "4.5.5", id = "e")
	public void testReturnValueMarkedAsCascadedAtConstructorInSuperAndSubClass() throws Exception {
		//Constructor<?> constructor = CalendarServiceSubClass.class.getConstructor( long.class );
		//TODO: Use wildcard constructor
		Constructor constructor = CalendarServiceSubClass.class.getConstructor( long.class );
		Object returnValue = new CalendarServiceSubClass();

		Set<ConstraintViolation<Object>> violations = executableValidator.validateConstructorReturnValue(
				constructor,
				returnValue
		);

		assertCorrectConstraintTypes( violations, Min.class );
		assertNodeNames(
				violations.iterator().next().getPropertyPath(),
				"CalendarServiceSubClass",
				null,
				"mode"
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
