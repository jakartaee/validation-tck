/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations;

import static org.testng.Assert.fail;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

import jakarta.validation.ConstraintDeclarationException;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
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
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
public class InvalidMethodConstraintDeclarationTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( InvalidMethodConstraintDeclarationTest.class )
				.withPackage( ImplementationAddingParameterConstraints.class.getPackage() )
				.withPackage( AbstractCalendarService.class.getPackage() )
				.withPackage( Person.class.getPackage() )
				.build();
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_INHERITANCE, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_INHERITANCE, id = "e")
	public void testParameterConstraintsAddedInInterfaceImplementationCausesException()
			throws Exception {
		Object object = new ImplementationAddingParameterConstraints();
		Method method = getCreateEventMethod( object );
		Object[] parameterValues = new Object[3];

		getExecutableValidator().validateParameters( object, method, parameterValues );
		fail( "Implementing method must add no parameter constraints. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_INHERITANCE, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_INHERITANCE, id = "e")
	public void testParameterConstraintsAddedInSubClassCausesException() throws Exception {
		Object object = new SubClassAddingParameterConstraints();
		Method method = getCreateEventMethod( object );
		Object[] parameterValues = new Object[3];

		getExecutableValidator().validateParameters( object, method, parameterValues );
		fail( "Overriding subclass method must add no parameter constraints. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_INHERITANCE, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_INHERITANCE, id = "e")
	public void testParameterMarkedAsCascadedInInterfaceImplementationCausesException()
			throws Exception {
		Object object = new ImplementationMarkingParameterAsCascaded();
		Method method = getCreateEventMethod( object );
		Object[] parameterValues = new Object[3];

		getExecutableValidator().validateParameters( object, method, parameterValues );
		fail( "Implementing method must not mark a parameter cascaded. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_INHERITANCE, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_INHERITANCE, id = "e")
	public void testParameterMarkedAsCascadedInSubClassCausesException() throws Exception {
		Object object = new SubClassMarkingParameterAsCascaded();
		Method method = getCreateEventMethod( object );
		Object[] parameterValues = new Object[3];

		getExecutableValidator().validateParameters( object, method, parameterValues );
		fail( "Overriding subclass method must not mark a parameter cascaded. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_INHERITANCE, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_INHERITANCE, id = "e")
	public void testConstrainedParameterInOneMethodOfParallelInterfacesCausesException()
			throws Exception {
		Object object = new ImplementationOfConstrainedAndUnconstrainedInterfaces();
		Method method = getCreateEventMethod( object );
		Object[] parameterValues = new Object[3];

		getExecutableValidator().validateParameters( object, method, parameterValues );
		fail( "A method defined in two parallel interfaces must have no parameter constraints. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_INHERITANCE, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_INHERITANCE, id = "e")
	public void testParameterIsConstrainedInInterfaceMethodAndSuperClassMethodCausesException()
			throws Exception {
		Object object = new ImplementationOfConstrainedInterfaceExtendingUnconstrainedSuperClass();
		Method method = getCreateEventMethod( object );
		Object[] parameterValues = new Object[3];

		getExecutableValidator().validateParameters( object, method, parameterValues );
		fail( "A method defined in an interface and a superclass not implementing this interface must have no parameter constraints. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_INHERITANCE, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_INHERITANCE, id = "e")
	public void testParameterIsCascadingInOneMethodOfParallelInterfacesCausesException()
			throws Exception {
		Object object = new ImplementationOfCascadingAndNonCascadingInterfaces();
		Method method = getCreateEventMethod( object );
		Object[] parameterValues = new Object[3];

		getExecutableValidator().validateParameters( object, method, parameterValues );
		fail( "A method defined in two parallel interfaces must not have no parameters marked as cascaded. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_INHERITANCE, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_INHERITANCE, id = "e")
	public void testParameterIsCascadingInInterfaceMethodAndSuperClassMethodCausesException()
			throws Exception {
		Object object = new ImplementationOfCascadingInterfaceExtendingUncascadingSuperClass();
		Method method = getCreateEventMethod( object );
		Object[] parameterValues = new Object[3];

		getExecutableValidator().validateParameters( object, method, parameterValues );
		fail( "A method defined in an interface and a superclass not implementing this interface must have no parameters marked as cascaded. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_INHERITANCE, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_INHERITANCE, id = "e")
	public void testParameterContainerElementConstraintsAddedInInterfaceImplementationCausesException()
			throws Exception {
		Object object = new ImplementationAddingParameterConstraints();
		Method method = getAddParticipantsMethod( object );
		Object[] parameterValues = new Object[3];

		getExecutableValidator().validateParameters( object, method, parameterValues );
		fail( "Implementing method must add no parameter constraints. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_INHERITANCE, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_INHERITANCE, id = "e")
	public void testParameterContainerElementConstraintsAddedInSubClassCausesException() throws Exception {
		Object object = new SubClassAddingParameterConstraints();
		Method method = getAddParticipantsMethod( object );
		Object[] parameterValues = new Object[3];

		getExecutableValidator().validateParameters( object, method, parameterValues );
		fail( "Overriding subclass method must add no parameter constraints. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_INHERITANCE, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_INHERITANCE, id = "e")
	public void testParameterContainerElementMarkedAsCascadedInInterfaceImplementationCausesException()
			throws Exception {
		Object object = new ImplementationMarkingParameterAsCascaded();
		Method method = getAddParticipantsMethod( object );
		Object[] parameterValues = new Object[3];

		getExecutableValidator().validateParameters( object, method, parameterValues );
		fail( "Implementing method must not mark a parameter cascaded. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_INHERITANCE, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_INHERITANCE, id = "e")
	public void testParameterContainerElementMarkedAsCascadedInSubClassCausesException() throws Exception {
		Object object = new SubClassMarkingParameterAsCascaded();
		Method method = getAddParticipantsMethod( object );
		Object[] parameterValues = new Object[3];

		getExecutableValidator().validateParameters( object, method, parameterValues );
		fail( "Overriding subclass method must not mark a parameter cascaded. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_INHERITANCE, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_INHERITANCE, id = "e")
	public void testConstrainedContainerElementOfParameterInOneMethodOfParallelInterfacesCausesException()
			throws Exception {
		Object object = new ImplementationOfConstrainedAndUnconstrainedInterfaces();
		Method method = getAddParticipantsMethod( object );
		Object[] parameterValues = new Object[3];

		getExecutableValidator().validateParameters( object, method, parameterValues );
		fail( "A method defined in two parallel interfaces must have no parameter constraints. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_INHERITANCE, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_INHERITANCE, id = "e")
	public void testContainerElementOfParameterIsConstrainedInInterfaceMethodAndSuperClassMethodCausesException()
			throws Exception {
		Object object = new ImplementationOfConstrainedInterfaceExtendingUnconstrainedSuperClass();
		Method method = getAddParticipantsMethod( object );
		Object[] parameterValues = new Object[3];

		getExecutableValidator().validateParameters( object, method, parameterValues );
		fail( "A method defined in an interface and a superclass not implementing this interface must have no parameter constraints. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_INHERITANCE, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_INHERITANCE, id = "e")
	public void testContainerElementOfParameterIsCascadingInOneMethodOfParallelInterfacesCausesException()
			throws Exception {
		Object object = new ImplementationOfCascadingAndNonCascadingInterfaces();
		Method method = getAddParticipantsMethod( object );
		Object[] parameterValues = new Object[3];

		getExecutableValidator().validateParameters( object, method, parameterValues );
		fail( "A method defined in two parallel interfaces must not have no parameters marked as cascaded. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_INHERITANCE, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_INHERITANCE, id = "e")
	public void testContainerElementOfParameterIsCascadingInInterfaceMethodAndSuperClassMethodCausesException()
			throws Exception {
		Object object = new ImplementationOfCascadingInterfaceExtendingUncascadingSuperClass();
		Method method = getAddParticipantsMethod( object );
		Object[] parameterValues = new Object[3];

		getExecutableValidator().validateParameters( object, method, parameterValues );
		fail( "A method defined in an interface and a superclass not implementing this interface must have no parameters marked as cascaded. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_INHERITANCE, id = "d")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_INHERITANCE, id = "e")
	public void testReturnValueIsMarkedAsCascadedInInterfaceAndImplementationCausesException()
			throws Exception {
		Object object = new OrderServiceImplementation();
		Method method = getPlaceOrderMethod( object );
		Object returnValue = new Order();

		getExecutableValidator().validateReturnValue( object, method, returnValue );
		fail( "A method must not mark the return value as cascaded if the implemented interface method is cascaded, too. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_INHERITANCE, id = "d")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_INHERITANCE, id = "e")
	public void testReturnValueIsMarkedAsCascadedInBaseAndSubClassCausesException()
			throws Exception {
		Object object = new OrderServiceSubClass();
		Method method = getPlaceOrderMethod( object );
		Object returnValue = new Order();

		getExecutableValidator().validateReturnValue( object, method, returnValue );
		fail( "A method must not mark the return value as cascaded if the overridden superclass method is cascaded, too. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_INHERITANCE, id = "d")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_INHERITANCE, id = "e")
	public void testReturnValueIsMarkedAsCascadedInSuperAndDerivedInterfaceCausesException()
			throws Exception {
		Object object = new ExtendedOrderServiceImplementation();
		Method method = getPlaceOrderMethod( object );
		Object returnValue = new Order();

		getExecutableValidator().validateReturnValue( object, method, returnValue );
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

	private Method getAddParticipantsMethod(Object object) throws NoSuchMethodException {
		return object.getClass().getMethod(
				"addParticipants",
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
