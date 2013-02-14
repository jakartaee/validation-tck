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
package org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import javax.validation.ConstraintDeclarationException;
import javax.validation.executable.ExecutableValidator;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations.model.Order;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations.model.Person;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations.service.AbstractCalendarService;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations.service.impl.ExtendedOrderServiceImplementation;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations.service.impl.ImplementationAddingParameterConstraints;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations.service.impl.ImplementationMarkingParameterAsCascaded;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations.service.impl.ImplementationOfCascadingAndNonCascadingInterfaces;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations.service.impl.ImplementationOfCascadingInterfaceExtendingUncascadingSuperClass;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations.service.impl.ImplementationOfConstrainedAndUnconstrainedInterfaces;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations.service.impl.ImplementationOfConstrainedInterfaceExtendingUnconstrainedSuperClass;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations.service.impl.OrderServiceImplementation;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations.service.impl.OrderServiceSubClass;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations.service.impl.SubClassAddingParameterConstraints;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations.service.impl.SubClassMarkingParameterAsCascaded;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

import static org.testng.Assert.fail;

/**
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "1.1.0")
public class InvalidMethodConstraintDeclarationTest extends Arquillian {

	private ExecutableValidator executableValidator;

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClassPackage( InvalidMethodConstraintDeclarationTest.class )
				.withPackage( ImplementationAddingParameterConstraints.class.getPackage() )
				.withPackage( AbstractCalendarService.class.getPackage() )
				.withPackage( Person.class.getPackage() )
				.build();
	}

	@BeforeMethod
	public void setupValidator() {
		executableValidator = TestUtil.getValidatorUnderTest().forExecutables();
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = "4.5.5", id = "a")
	public void testParameterConstraintsAddedInInterfaceImplementationCausesException()
			throws Exception {
		Object object = new ImplementationAddingParameterConstraints();
		Method method = getCreateEventMethod( object );
		Object[] parameterValues = new Object[3];

		executableValidator.validateParameters( object, method, parameterValues );
		fail( "Implementing method must add no parameter constraints. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = "4.5.5", id = "a")
	public void testParameterConstraintsAddedInSubClassCausesException() throws Exception {
		Object object = new SubClassAddingParameterConstraints();
		Method method = getCreateEventMethod( object );
		Object[] parameterValues = new Object[3];

		executableValidator.validateParameters( object, method, parameterValues );
		fail( "Overriding subclass method must add no parameter constraints. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = "4.5.5", id = "a")
	public void testParameterMarkedAsCascadedInInterfaceImplementationCausesException()
			throws Exception {
		Object object = new ImplementationMarkingParameterAsCascaded();
		Method method = getCreateEventMethod( object );
		Object[] parameterValues = new Object[3];

		executableValidator.validateParameters( object, method, parameterValues );
		fail( "Implementing method must not mark a parameter cascaded. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = "4.5.5", id = "a")
	public void testParameterMarkedAsCascadedInSubClassCausesException() throws Exception {
		Object object = new SubClassMarkingParameterAsCascaded();
		Method method = getCreateEventMethod( object );
		Object[] parameterValues = new Object[3];

		executableValidator.validateParameters( object, method, parameterValues );
		fail( "Overriding subclass method must not mark a parameter cascaded. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = "4.5.5", id = "b")
	public void testConstrainedParameterInOneMethodOfParallelInterfacesCausesException()
			throws Exception {
		Object object = new ImplementationOfConstrainedAndUnconstrainedInterfaces();
		Method method = getCreateEventMethod( object );
		Object[] parameterValues = new Object[3];

		executableValidator.validateParameters( object, method, parameterValues );
		fail( "A method defined in two parallel interfaces must have no parameter constraints. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = "4.5.5", id = "b")
	public void testParameterIsConstrainedInInterfaceMethodAndSuperClassMethodCausesException()
			throws Exception {
		Object object = new ImplementationOfConstrainedInterfaceExtendingUnconstrainedSuperClass();
		Method method = getCreateEventMethod( object );
		Object[] parameterValues = new Object[3];

		executableValidator.validateParameters( object, method, parameterValues );
		fail( "A method defined in an interface and a superclass not implementing this interface must have no parameter constraints. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = "4.5.5", id = "b")
	public void testParameterIsCascadingInOneMethodOfParallelInterfacesCausesException()
			throws Exception {
		Object object = new ImplementationOfCascadingAndNonCascadingInterfaces();
		Method method = getCreateEventMethod( object );
		Object[] parameterValues = new Object[3];

		executableValidator.validateParameters( object, method, parameterValues );
		fail( "A method defined in two parallel interfaces must not have no parameters marked as cascaded. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = "4.5.5", id = "b")
	public void testParameterIsCascadingInInterfaceMethodAndSuperClassMethodCausesException()
			throws Exception {
		Object object = new ImplementationOfCascadingInterfaceExtendingUncascadingSuperClass();
		Method method = getCreateEventMethod( object );
		Object[] parameterValues = new Object[3];

		executableValidator.validateParameters( object, method, parameterValues );
		fail( "A method defined in an interface and a superclass not implementing this interface must have no parameters marked as cascaded. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = "4.5.5", id = "d")
	public void testReturnValueIsMarkedAsCascadedInInterfaceAndImplementationCausesException()
			throws Exception {
		Object object = new OrderServiceImplementation();
		Method method = getPlaceOrderMethod( object );
		Object returnValue = new Order();

		executableValidator.validateReturnValue( object, method, returnValue );
		fail( "A method must not mark the return value as cascaded if the implemented interface method is cascaded, too. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = "4.5.5", id = "d")
	public void testReturnValueIsMarkedAsCascadedInBaseAndSubClassCausesException()
			throws Exception {
		Object object = new OrderServiceSubClass();
		Method method = getPlaceOrderMethod( object );
		Object returnValue = new Order();

		executableValidator.validateReturnValue( object, method, returnValue );
		fail( "A method must not mark the return value as cascaded if the overridden superclass method is cascaded, too. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = "4.5.5", id = "d")
	public void testReturnValueIsMarkedAsCascadedInSuperAndDerivedInterfaceCausesException()
			throws Exception {
		Object object = new ExtendedOrderServiceImplementation();
		Method method = getPlaceOrderMethod( object );
		Object returnValue = new Order();

		executableValidator.validateReturnValue( object, method, returnValue );
		fail( "An interface method must not mark the return value as cascaded if the overridden superinterface method is cascaded, too. Expected exception wasn't thrown." );
	}

	private Method getCreateEventMethod(Object object) throws NoSuchMethodException {
		return object.getClass().getMethod(
				"createEvent",
				Date.class,
				Date.class,
				List.class
		);
	}

	private Method getPlaceOrderMethod(Object object) throws NoSuchMethodException {
		return object.getClass().getMethod(
				"placeOrder",
				String.class,
				int.class
		);
	}
}
