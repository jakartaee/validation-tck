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
import javax.validation.MethodValidator;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations.impl.ExtendedOrderServiceImplementation;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations.impl.ImplementationAddingParameterConstraints;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations.impl.ImplementationMarkingParameterAsCascaded;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations.impl.ImplementationOfCascadingAndNonCascadingInterfaces;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations.impl.ImplementationOfCascadingInterfaceExtendingUncascadingSuperClass;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations.impl.ImplementationOfConstrainedAndUnconstrainedInterfaces;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations.impl.ImplementationOfConstrainedInterfaceExtendingUnconstrainedSuperClass;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations.impl.OrderServiceImplementation;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations.impl.OrderServiceSubClass;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations.impl.SubClassAddingParameterConstraints;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations.impl.SubClassMarkingParameterAsCascaded;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations.model.Order;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations.model.Person;
import org.hibernate.beanvalidation.tck.util.Groups;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

import static org.testng.Assert.fail;

/**
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "1.1.0")
public class InvalidMethodConstraintDeclarationTest extends Arquillian {

	private MethodValidator executableValidator;

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClassPackage( InvalidMethodConstraintDeclarationTest.class )
				.withPackage( ImplementationAddingParameterConstraints.class.getPackage() )
				.withPackage( Person.class.getPackage() )
				.build();
	}

	@BeforeMethod
	public void setupValidator() {
		executableValidator = TestUtil.getValidatorUnderTest().forMethods();
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = "4.5.5", id = "a")
	public void testParameterConstraintsAddedInInterfaceImplementation() throws Exception {
		Object object = new ImplementationAddingParameterConstraints();
		Method method = getCreateEventMethod( object );
		Object[] parameterValues = new Object[3];

		executableValidator.validateParameters( object, method, parameterValues );
		fail( "Implementing method must add no parameter constraints. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = "4.5.5", id = "a")
	public void testParameterConstraintsAddedInSubClass() throws Exception {
		Object object = new SubClassAddingParameterConstraints();
		Method method = getCreateEventMethod( object );
		Object[] parameterValues = new Object[3];

		executableValidator.validateParameters( object, method, parameterValues );
		fail( "Overriding subclass method must add no parameter constraints. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = "4.5.5", id = "a")
	public void testParameterMarkedAsCascadedInInterfaceImplementation() throws Exception {
		Object object = new ImplementationMarkingParameterAsCascaded();
		Method method = getCreateEventMethod( object );
		Object[] parameterValues = new Object[3];

		executableValidator.validateParameters( object, method, parameterValues );
		fail( "Implementing method must not mark a parameter cascaded. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = "4.5.5", id = "a")
	public void testParameterMarkedAsCascadedInSubClass() throws Exception {
		Object object = new SubClassMarkingParameterAsCascaded();
		Method method = getCreateEventMethod( object );
		Object[] parameterValues = new Object[3];

		executableValidator.validateParameters( object, method, parameterValues );
		fail( "Overriding subclass method must not mark a parameter cascaded. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = "4.5.5", id = "b")
	public void testParameterIsConstrainedInParallelInterfaceMethod() throws Exception {
		Object object = new ImplementationOfConstrainedAndUnconstrainedInterfaces();
		Method method = getCreateEventMethod( object );
		Object[] parameterValues = new Object[3];

		executableValidator.validateParameters( object, method, parameterValues );
		fail( "A method defined in two parallel interfaces must have no parameter constraints. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = "4.5.5", id = "b")
	public void testParameterIsConstrainedInInterfaceMethodAndSuperClassMethod() throws Exception {
		Object object = new ImplementationOfConstrainedInterfaceExtendingUnconstrainedSuperClass();
		Method method = getCreateEventMethod( object );
		Object[] parameterValues = new Object[3];

		executableValidator.validateParameters( object, method, parameterValues );
		fail( "A method defined in an interface and a superclass not implementing this interface must have no parameter constraints. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = "4.5.5", id = "b")
	public void testParameterIsCascadingInParallelInterfaceMethod() throws Exception {
		Object object = new ImplementationOfCascadingAndNonCascadingInterfaces();
		Method method = getCreateEventMethod( object );
		Object[] parameterValues = new Object[3];

		executableValidator.validateParameters( object, method, parameterValues );
		fail( "A method defined in two parallel interfaces must not have no parameters marked as cascaded. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = "4.5.5", id = "b")
	public void testParameterIsCascadingInInterfaceMethodAndSuperClassMethod() throws Exception {
		Object object = new ImplementationOfCascadingInterfaceExtendingUncascadingSuperClass();
		Method method = getCreateEventMethod( object );
		Object[] parameterValues = new Object[3];

		executableValidator.validateParameters( object, method, parameterValues );
		fail( "A method defined in an interface and a superclass not implementing this interface must have no parameters marked as cascaded. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class, groups = Groups.FAILING_IN_RI)
	@SpecAssertion(section = "4.5.5", id = "d")
	public void testReturnValueIsMarkedAsCascadedInInterfaceAndImplementation() throws Exception {
		Object object = new OrderServiceImplementation();
		Method method = getPlaceOrderMethod( object );
		Object returnValue = new Order();

		executableValidator.validateReturnValue( object, method, returnValue );
		fail( "A method must not mark the return value as cascaded if the implemented interface method is cascaded, too. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class, groups = Groups.FAILING_IN_RI)
	@SpecAssertion(section = "4.5.5", id = "d")
	public void testReturnValueIsMarkedAsCascadedInBaseAndSubClass() throws Exception {
		Object object = new OrderServiceSubClass();
		Method method = getPlaceOrderMethod( object );
		Object returnValue = new Order();

		executableValidator.validateReturnValue( object, method, returnValue );
		fail( "A method must not mark the return value as cascaded if the overridden superclass method is cascaded, too. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class, groups = Groups.FAILING_IN_RI)
	@SpecAssertion(section = "4.5.5", id = "d")
	public void testReturnValueIsMarkedAsCascadedInSuperAndDerivedInterface() throws Exception {
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
