/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.methodvalidation;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertNoViolations;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.pathWith;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.Default;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.constraint.MyCrossParameterConstraint;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.constraint.ValidOrder;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.constraint.ValidOrderService;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.constraint.ValidRetailOrder;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.constraint.ValidRetailOrderService;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.model.Item;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.model.Order;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.service.ExtendedOrderService;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.service.OrderService;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.service.OrderService.Basic;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.service.OrderService.OrderServiceSequence;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.service.OrderServiceImpl;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.service.OrderServiceWithRedefinedDefaultGroupSequence;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class MethodValidationTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( ValidateConstructorParametersTest.class )
				.withPackage( MyCrossParameterConstraint.class.getPackage() )
				.withPackage( ExtendedOrderService.class.getPackage() )
				.withClass( Item.class )
				.withClass( Order.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_METHODCONSTRUCTORVALIDATION, id = "a")
	public void methodParameterValidationTargetsParameterCrossParameterAndCascadedConstraints()
			throws Exception {
		String methodName = "placeOrder";

		Object object = new OrderService();
		Method method = OrderService.class.getMethod(
				methodName,
				String.class,
				Item.class,
				int.class
		);
		Object[] parameterValues = new Object[] { null, new Item( "" ), 0 };

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateParameters(
				object,
				method,
				parameterValues
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.parameter( "customer", 0 )
						),
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.parameter( "item", 1 )
								.property( "name" )
						),
				violationOf( Min.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.parameter( "quantity", 2 )
						),
				violationOf( MyCrossParameterConstraint.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.crossParameter()
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_METHODCONSTRUCTORVALIDATION, id = "a")
	public void methodParameterValidationIncludesConstraintsFromSuperClass() throws Exception {
		String methodName = "placeOrder";

		Object object = new ExtendedOrderService();
		Method method = ExtendedOrderService.class.getMethod(
				methodName,
				String.class,
				Item.class,
				int.class
		);
		Object[] parameterValues = new Object[] { null, new Item( "" ), 0 };

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateParameters(
				object,
				method,
				parameterValues
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.parameter( "customer", 0 )
						),
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.parameter( "item", 1 )
								.property( "name" )
						),
				violationOf( Min.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.parameter( "quantity", 2 )
						),
				violationOf( MyCrossParameterConstraint.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.crossParameter()
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_METHODCONSTRUCTORVALIDATION, id = "a")
	public void methodParameterValidationIncludesConstraintsFromImplementedInterface()
			throws Exception {
		String methodName = "placeOrder";

		Object object = new OrderServiceImpl();
		Method method = OrderServiceImpl.class.getMethod(
				methodName,
				String.class,
				Item.class,
				int.class
		);
		Object[] parameterValues = new Object[] { null, new Item( "" ), 0 };

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateParameters(
				object,
				method,
				parameterValues
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.parameter( "customer", 0 )
						),
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.parameter( "item", 1 )
								.property( "name" )
						),
				violationOf( Min.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.parameter( "quantity", 2 )
						),
				violationOf( MyCrossParameterConstraint.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.crossParameter()
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_METHODCONSTRUCTORVALIDATION, id = "a")
	public void methodParameterValidationIsAppliedGroupWise() throws Exception {
		String methodName = "placeOrder";

		Object object = new OrderService();
		Method method = OrderService.class.getMethod(
				methodName,
				String.class,
				Item.class,
				Integer.class
		);
		Object[] parameterValues = new Object[] { null, new Item( "" ), 0 };

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateParameters(
				object,
				method,
				parameterValues,
				Basic.class
		);

		//only constraints in Basic group should fail
		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.parameter( "customer", 0 )
						),
				violationOf( MyCrossParameterConstraint.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.crossParameter()
						)
		);

		violations = getExecutableValidator().validateParameters(
				object,
				method,
				parameterValues
		);

		//only constraints in Default group should fail
		assertThat( violations ).containsOnlyViolations(
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.parameter( "item", 1 )
								.property( "name" )
						),
				violationOf( Min.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.parameter( "quantity", 2 )
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_METHODCONSTRUCTORVALIDATION, id = "a")
	public void methodParameterValidationPerformsGroupConversion() throws Exception {
		String methodName = "placeOrder";

		Object object = new OrderService();
		Method method = OrderService.class.getMethod(
				methodName,
				String.class,
				Item.class,
				long.class
		);
		Object[] parameterValues = new Object[] { null, new Item( "" ), 0 };

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateParameters(
				object,
				method,
				parameterValues,
				Basic.class
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.parameter( "item", 1 )
								.property( "name" )
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_METHODCONSTRUCTORVALIDATION, id = "a")
	public void methodParameterValidationValidatesEachConstraintOnlyOnce() throws Exception {
		String methodName = "placeOrder";

		Object object = new OrderService();
		Method method = OrderService.class.getMethod(
				methodName,
				String.class,
				Item.class,
				short.class
		);
		Object[] parameterValues = new Object[] { null, new Item( "" ), (short) 0 };

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateParameters(
				object,
				method,
				parameterValues,
				Basic.class, Default.class
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.parameter( "customer", 0 )
						),
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.parameter( "item", 1 )
								.property( "name" )
						),
				violationOf( Min.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.parameter( "quantity", 2 )
						),
				violationOf( MyCrossParameterConstraint.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.crossParameter()
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_METHODCONSTRUCTORVALIDATION, id = "a")
	public void methodParameterValidationUsingSequence() throws Exception {
		String methodName = "placeOrder";

		Object object = new OrderService();
		Method method = OrderService.class.getMethod(
				methodName,
				String.class,
				Item.class,
				byte.class
		);
		Object[] parameterValues = new Object[] { null, new Item( "" ), (byte) 0 };

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateParameters(
				object,
				method,
				parameterValues,
				OrderServiceSequence.class
		);

		//Only the constraints of the Basic group should fail
		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.parameter( "customer", 0 )
						),
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.parameter( "item", 1 )
								.property( "name" )
						)
		);

		parameterValues = new Object[] { "Bob", new Item( "BV Specification" ), (byte) 0 };

		violations = getExecutableValidator().validateParameters(
				object,
				method,
				parameterValues,
				OrderServiceSequence.class
		);

		//Now the constraints of the Complex group should fail
		assertThat( violations ).containsOnlyViolations(
				violationOf( Min.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.parameter( "quantity", 2 )
						),
				violationOf( MyCrossParameterConstraint.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.crossParameter()
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_METHODCONSTRUCTORVALIDATION, id = "a")
	public void methodParameterValidationWithRedefinedDefaultGroupSequence() throws Exception {
		String methodName = "placeOrder";

		Object object = new OrderServiceWithRedefinedDefaultGroupSequence();
		Method method = OrderServiceWithRedefinedDefaultGroupSequence.class.getMethod(
				methodName,
				String.class,
				Item.class,
				byte.class
		);
		Object[] parameterValues = new Object[] { null, new Item( "" ), (byte) 0 };

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateParameters(
				object,
				method,
				parameterValues
		);

		//Only the constraints of the Basic group on OrderServiceWithRedefinedDefaultGroupSequence and of
		//the Default group on Item should fail
		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.parameter( "customer", 0 )
						),
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.parameter( "item", 1 )
								.property( "name" )
						)
		);

		parameterValues = new Object[] { "Bob", new Item( "" ), (byte) 0 };

		violations = getExecutableValidator().validateParameters(
				object,
				method,
				parameterValues
		);

		//Now the constraints of the Default group on OrderServiceWithRedefinedDefaultGroupSequence and of
		//the Default group on Item should fail
		assertThat( violations ).containsOnlyViolations(
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.parameter( "item", 1 )
								.property( "name" )
						),
				violationOf( Min.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.parameter( "quantity", 2 )
						),
				violationOf( MyCrossParameterConstraint.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.crossParameter()
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_METHODCONSTRUCTORVALIDATION, id = "a")
	public void constructorParameterValidationTargetsParameterCrossParameterAndCascadedConstraints()
			throws Exception {
		Constructor<OrderService> constructor = OrderService.class.getConstructor(
				String.class,
				Item.class,
				int.class
		);
		Object[] parameterValues = new Object[] { null, new Item( "" ), 0 };

		Set<ConstraintViolation<OrderService>> violations = getExecutableValidator().validateConstructorParameters(
				constructor,
				parameterValues
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.constructor( OrderService.class )
								.parameter( "customer", 0 )
						),
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.constructor( OrderService.class )
								.parameter( "item", 1 )
								.property( "name" )
						),
				violationOf( Min.class )
						.withPropertyPath( pathWith()
								.constructor( OrderService.class )
								.parameter( "quantity", 2 )
						),
				violationOf( MyCrossParameterConstraint.class )
						.withPropertyPath( pathWith()
								.constructor( OrderService.class )
								.crossParameter()
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_METHODCONSTRUCTORVALIDATION, id = "a")
	public void constructorParameterValidationDoesNotIncludeConstraintsFromSuperClass()
			throws Exception {
		Constructor<ExtendedOrderService> constructor = ExtendedOrderService.class.getConstructor(
				String.class,
				Item.class,
				int.class
		);
		Object[] parameterValues = new Object[] { null, new Item( "" ), 0 };

		Set<ConstraintViolation<ExtendedOrderService>> violations = getExecutableValidator().validateConstructorParameters(
				constructor,
				parameterValues
		);

		assertNoViolations( violations );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_METHODCONSTRUCTORVALIDATION, id = "a")
	public void constructorParameterValidationIsAppliedGroupWise() throws Exception {
		Constructor<OrderService> constructor = OrderService.class.getConstructor(
				String.class,
				Item.class,
				Integer.class
		);
		Object[] parameterValues = new Object[] { null, new Item( "" ), 0 };

		Set<ConstraintViolation<OrderService>> violations = getExecutableValidator().validateConstructorParameters(
				constructor,
				parameterValues,
				Basic.class
		);

		//only constraints in Basic group should fail
		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.constructor( OrderService.class )
								.parameter( "customer", 0 )
						),
				violationOf( MyCrossParameterConstraint.class )
						.withPropertyPath( pathWith()
								.constructor( OrderService.class )
								.crossParameter()
						)
		);

		violations = getExecutableValidator().validateConstructorParameters(
				constructor,
				parameterValues
		);

		//only constraints in Default group should fail
		assertThat( violations ).containsOnlyViolations(
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.constructor( OrderService.class )
								.parameter( "item", 1 )
								.property( "name" )
						),
				violationOf( Min.class )
						.withPropertyPath( pathWith()
								.constructor( OrderService.class )
								.parameter( "quantity", 2 )
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_METHODCONSTRUCTORVALIDATION, id = "a")
	public void constructorParameterValidationPerformsGroupConversion() throws Exception {
		Constructor<OrderService> constructor = OrderService.class.getConstructor(
				String.class,
				Item.class,
				long.class
		);
		Object[] parameterValues = new Object[] { null, new Item( "" ), 0 };

		Set<ConstraintViolation<OrderService>> violations = getExecutableValidator().validateConstructorParameters(
				constructor,
				parameterValues,
				Basic.class
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.constructor( OrderService.class )
								.parameter( "item", 1 )
								.property( "name" )
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_METHODCONSTRUCTORVALIDATION, id = "a")
	public void constructorParameterValidationValidatesEachConstraintOnlyOnce() throws Exception {
		Constructor<OrderService> constructor = OrderService.class.getConstructor(
				String.class,
				Item.class,
				short.class
		);
		Object[] parameterValues = new Object[] { null, new Item( "" ), (short) 0 };

		Set<ConstraintViolation<OrderService>> violations = getExecutableValidator().validateConstructorParameters(
				constructor,
				parameterValues,
				Basic.class, Default.class
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.constructor( OrderService.class )
								.parameter( "customer", 0 )
						),
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.constructor( OrderService.class )
								.parameter( "item", 1 )
								.property( "name" )
						),
				violationOf( Min.class )
						.withPropertyPath( pathWith()
								.constructor( OrderService.class )
								.parameter( "quantity", 2 )
						),
				violationOf( MyCrossParameterConstraint.class )
						.withPropertyPath( pathWith()
								.constructor( OrderService.class )
								.crossParameter()
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_METHODCONSTRUCTORVALIDATION, id = "a")
	public void constructorParameterValidationUsingSequence() throws Exception {
		Constructor<OrderService> constructor = OrderService.class.getConstructor(
				String.class,
				Item.class,
				byte.class
		);
		Object[] parameterValues = new Object[] { null, new Item( "" ), (byte) 0 };

		Set<ConstraintViolation<OrderService>> violations = getExecutableValidator().validateConstructorParameters(
				constructor,
				parameterValues,
				OrderServiceSequence.class
		);

		//Only the constraints of the Basic group should fail
		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.constructor( OrderService.class )
								.parameter( "customer", 0 )
						),
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.constructor( OrderService.class )
								.parameter( "item", 1 )
								.property( "name" )
						)
		);

		parameterValues = new Object[] { "Bob", new Item( "BV Specification" ), (byte) 0 };

		violations = getExecutableValidator().validateConstructorParameters(
				constructor,
				parameterValues,
				OrderServiceSequence.class
		);

		//Now the constraints of the Complex group should fail
		assertThat( violations ).containsOnlyViolations(
				violationOf( Min.class )
						.withPropertyPath( pathWith()
								.constructor( OrderService.class )
								.parameter( "quantity", 2 )
						),
				violationOf( MyCrossParameterConstraint.class )
						.withPropertyPath( pathWith()
								.constructor( OrderService.class )
								.crossParameter()
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_METHODCONSTRUCTORVALIDATION, id = "a")
	public void constructorParameterValidationWithRedefinedDefaultGroupSequence() throws Exception {
		Constructor<OrderServiceWithRedefinedDefaultGroupSequence> constructor = OrderServiceWithRedefinedDefaultGroupSequence.class
				.getConstructor(
						String.class,
						Item.class,
						byte.class
				);
		Object[] parameterValues = new Object[] { null, new Item( "" ), (byte) 0 };

		Set<ConstraintViolation<OrderServiceWithRedefinedDefaultGroupSequence>> violations = getExecutableValidator()
				.validateConstructorParameters(
						constructor,
						parameterValues
				);

		//Only the constraints of the Basic group on OrderServiceWithRedefinedDefaultGroupSequence and of
		//the Default group on Item should fail
		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.constructor( OrderServiceWithRedefinedDefaultGroupSequence.class )
								.parameter( "customer", 0 )
						),
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.constructor( OrderServiceWithRedefinedDefaultGroupSequence.class )
								.parameter( "item", 1 )
								.property( "name" )
						)
		);

		parameterValues = new Object[] { "Bob", new Item( "" ), (byte) 0 };

		violations = getExecutableValidator().validateConstructorParameters(
				constructor,
				parameterValues
		);

		//Now the constraints of the Default group on OrderServiceWithRedefinedDefaultGroupSequence and of
		//the Default group on Item should fail
		assertThat( violations ).containsOnlyViolations(
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.constructor( OrderServiceWithRedefinedDefaultGroupSequence.class )
								.parameter( "item", 1 )
								.property( "name" )
						),
				violationOf( Min.class )
						.withPropertyPath( pathWith()
								.constructor( OrderServiceWithRedefinedDefaultGroupSequence.class )
								.parameter( "quantity", 2 )
						),
				violationOf( MyCrossParameterConstraint.class )
						.withPropertyPath( pathWith()
								.constructor( OrderServiceWithRedefinedDefaultGroupSequence.class )
								.crossParameter()
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_METHODCONSTRUCTORVALIDATION, id = "b")
	public void methodReturnValueValidationTargetsReturnValueAndCascadedConstraints()
			throws Exception {
		String methodName = "placeOrder";

		Object object = new OrderService();
		Method method = OrderService.class.getMethod(
				methodName,
				String.class,
				Item.class,
				int.class
		);
		Object returnValue = new Order( "" );

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateReturnValue(
				object,
				method,
				returnValue
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.returnValue()
								.property( "name" )
						),
				violationOf( ValidOrder.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.returnValue()
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_METHODCONSTRUCTORVALIDATION, id = "b")
	public void methodReturnValueValidationIncludesConstraintsFromSuperClass() throws Exception {
		String methodName = "placeOrder";

		Object object = new ExtendedOrderService();
		Method method = ExtendedOrderService.class.getMethod(
				methodName,
				String.class,
				Item.class,
				int.class
		);
		Object returnValue = new Order( "" );

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateReturnValue(
				object,
				method,
				returnValue
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.returnValue()
								.property( "name" )
						),
				violationOf( ValidOrder.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.returnValue()
						),
				violationOf( ValidRetailOrder.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.returnValue()
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_METHODCONSTRUCTORVALIDATION, id = "b")
	public void methodReturnValueValidationIncludesConstraintsFromImplementedInterface()
			throws Exception {
		String methodName = "placeOrder";

		Object object = new OrderServiceImpl();
		Method method = OrderServiceImpl.class.getMethod(
				methodName,
				String.class,
				Item.class,
				int.class
		);
		Object returnValue = new Order( "" );

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateReturnValue(
				object,
				method,
				returnValue
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.returnValue()
								.property( "name" )
						),
				violationOf( ValidOrder.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.returnValue()
						),
				violationOf( ValidRetailOrder.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.returnValue()
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_METHODCONSTRUCTORVALIDATION, id = "b")
	public void methodReturnValueValidationIsAppliedGroupWise() throws Exception {
		String methodName = "placeOrder";

		Object object = new OrderService();
		Method method = OrderService.class.getMethod(
				methodName,
				String.class,
				Item.class,
				Integer.class
		);
		Object returnValue = new Order( "" );

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateReturnValue(
				object,
				method,
				returnValue,
				Basic.class
		);

		//only constraints in Basic group should fail
		assertThat( violations ).containsOnlyViolations(
				violationOf( ValidRetailOrder.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.returnValue()
						)
		);

		violations = getExecutableValidator().validateReturnValue(
				object,
				method,
				returnValue
		);

		//only constraints in Default group should fail
		assertThat( violations ).containsOnlyViolations(
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.returnValue()
								.property( "name" )
						),
				violationOf( ValidOrder.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.returnValue()
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_METHODCONSTRUCTORVALIDATION, id = "b")
	public void methodReturnValueValidationPerformsGroupConversion() throws Exception {
		String methodName = "placeOrder";

		Object object = new OrderService();
		Method method = OrderService.class.getMethod(
				methodName,
				String.class,
				Item.class,
				long.class
		);
		Object returnValue = new Order( "" );

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateReturnValue(
				object,
				method,
				returnValue,
				Basic.class
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.returnValue()
								.property( "name" )
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_METHODCONSTRUCTORVALIDATION, id = "b")
	public void methodReturnValueValidationValidatesEachConstraintOnlyOnce() throws Exception {
		String methodName = "placeOrder";

		Object object = new OrderService();
		Method method = OrderService.class.getMethod(
				methodName,
				String.class,
				Item.class,
				short.class
		);
		Object returnValue = new Order( "" );

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateReturnValue(
				object,
				method,
				returnValue,
				Basic.class, Default.class
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( ValidOrder.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.returnValue()
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_METHODCONSTRUCTORVALIDATION, id = "b")
	public void methodReturnValueValidationUsingSequence() throws Exception {
		String methodName = "placeOrder";

		Object object = new OrderService();
		Method method = OrderService.class.getMethod(
				methodName,
				String.class,
				Item.class,
				byte.class
		);
		Object returnValue = new Order( "" );

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateReturnValue(
				object,
				method,
				returnValue,
				OrderServiceSequence.class
		);

		//Only the constraints of the Basic group should fail
		assertThat( violations ).containsOnlyViolations(
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.returnValue()
								.property( "name" )
						),
				violationOf( ValidOrder.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.returnValue()
						)
		);

		returnValue = new Order( "BV Specification" );

		violations = getExecutableValidator().validateReturnValue(
				object,
				method,
				returnValue,
				OrderServiceSequence.class
		);

		//Now the constraints of the Complex group should fail
		assertThat( violations ).containsOnlyViolations(
				violationOf( ValidRetailOrder.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.returnValue()
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_METHODCONSTRUCTORVALIDATION, id = "b")
	public void methodReturnValueValidationWithRedefinedDefaultGroupSequence() throws Exception {
		String methodName = "placeOrder";

		Object object = new OrderServiceWithRedefinedDefaultGroupSequence();
		Method method = OrderServiceWithRedefinedDefaultGroupSequence.class.getMethod(
				methodName,
				String.class,
				Item.class,
				byte.class
		);
		Object returnValue = new Order( "" );

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateReturnValue(
				object,
				method,
				returnValue
		);

		//Only the constraints of the Basic group on OrderServiceWithRedefinedDefaultGroupSequence and of
		//the Default group on Order should fail
		assertThat( violations ).containsOnlyViolations(
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.returnValue()
								.property( "name" )
						),
				violationOf( ValidOrder.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.returnValue()
						)
		);

		returnValue = new Order( "valid" );

		violations = getExecutableValidator().validateReturnValue(
				object,
				method,
				returnValue
		);

		//Now the constraints of the Default group on OrderServiceWithRedefinedDefaultGroupSequence and of
		//the Default group on Order should fail
		assertThat( violations ).containsOnlyViolations(
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.returnValue()
								.property( "name" )
						),
				violationOf( ValidRetailOrder.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.returnValue()
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_METHODCONSTRUCTORVALIDATION, id = "b")
	public void constructorReturnValueValidationTargetsReturnValueAndCascadedConstraints()
			throws Exception {
		Constructor<OrderService> constructor = OrderService.class.getConstructor(
				String.class,
				Item.class,
				int.class
		);
		OrderService returnValue = new OrderService( "" );

		Set<ConstraintViolation<OrderService>> violations = getExecutableValidator().validateConstructorReturnValue(
				constructor,
				returnValue
		);
		assertThat( violations ).containsOnlyViolations(
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.constructor( OrderService.class )
								.returnValue()
								.property( "name" )
						),
				violationOf( ValidOrderService.class )
						.withPropertyPath( pathWith()
								.constructor( OrderService.class )
								.returnValue()
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_METHODCONSTRUCTORVALIDATION, id = "b")
	public void constructorReturnValueValidationDoesNotIncludeConstraintsFromSuperClass()
			throws Exception {
		Constructor<ExtendedOrderService> constructor = ExtendedOrderService.class.getConstructor(
				String.class,
				Item.class,
				int.class
		);
		ExtendedOrderService returnValue = new ExtendedOrderService();

		Set<ConstraintViolation<ExtendedOrderService>> violations = getExecutableValidator().validateConstructorReturnValue(
				constructor,
				returnValue
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( ValidRetailOrderService.class )
						.withPropertyPath( pathWith()
								.constructor( ExtendedOrderService.class )
								.returnValue()
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_METHODCONSTRUCTORVALIDATION, id = "b")
	public void constructorReturnValueValidationIsAppliedGroupWise() throws Exception {
		Constructor<OrderService> constructor = OrderService.class.getConstructor(
				String.class,
				Item.class,
				Integer.class
		);
		OrderService returnValue = new OrderService( "" );

		Set<ConstraintViolation<OrderService>> violations = getExecutableValidator().validateConstructorReturnValue(
				constructor,
				returnValue,
				Basic.class
		);

		//only constraints in Basic group should fail
		assertThat( violations ).containsOnlyViolations(
				violationOf( ValidRetailOrderService.class )
						.withPropertyPath( pathWith()
								.constructor( OrderService.class )
								.returnValue()
						)
		);

		violations = getExecutableValidator().validateConstructorReturnValue(
				constructor,
				returnValue
		);

		//only constraints in Default group should fail
		assertThat( violations ).containsOnlyViolations(
				violationOf( ValidOrderService.class )
						.withPropertyPath( pathWith()
								.constructor( OrderService.class )
								.returnValue()
						),
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.constructor( OrderService.class )
								.returnValue()
								.property( "name" )
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_METHODCONSTRUCTORVALIDATION, id = "b")
	public void constructorReturnValueValidationPerformsGroupConversion() throws Exception {
		Constructor<OrderService> constructor = OrderService.class.getConstructor(
				String.class,
				Item.class,
				long.class
		);
		OrderService returnValue = new OrderService( "" );

		Set<ConstraintViolation<OrderService>> violations = getExecutableValidator().validateConstructorReturnValue(
				constructor,
				returnValue,
				Basic.class
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.constructor( OrderService.class )
								.returnValue()
								.property( "name" )
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_METHODCONSTRUCTORVALIDATION, id = "b")
	public void constructorReturnValueValidationValidatesEachConstraintOnlyOnce() throws Exception {
		Constructor<OrderService> constructor = OrderService.class.getConstructor(
				String.class,
				Item.class,
				short.class
		);
		OrderService returnValue = new OrderService( "" );

		Set<ConstraintViolation<OrderService>> violations = getExecutableValidator().validateConstructorReturnValue(
				constructor,
				returnValue,
				Basic.class, Default.class
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( ValidOrderService.class )
						.withPropertyPath( pathWith()
								.constructor( OrderService.class )
								.returnValue()
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_METHODCONSTRUCTORVALIDATION, id = "b")
	public void constructorReturnValueValidationUsingSequence() throws Exception {
		Constructor<OrderService> constructor = OrderService.class.getConstructor(
				String.class,
				Item.class,
				byte.class
		);
		OrderService returnValue = new OrderService( "" );

		Set<ConstraintViolation<OrderService>> violations = getExecutableValidator().validateConstructorReturnValue(
				constructor,
				returnValue,
				OrderServiceSequence.class
		);

		//Only the constraints of the Basic group should fail
		assertThat( violations ).containsOnlyViolations(
				violationOf( ValidOrderService.class )
						.withPropertyPath( pathWith()
								.constructor( OrderService.class )
								.returnValue()
						),
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.constructor( OrderService.class )
								.returnValue()
								.property( "name" )
						)
		);

		returnValue = new OrderService( "valid order service" );

		violations = getExecutableValidator().validateConstructorReturnValue(
				constructor,
				returnValue,
				OrderServiceSequence.class
		);

		//Now the constraints of the Complex group should fail
		assertThat( violations ).containsOnlyViolations(
				violationOf( ValidRetailOrderService.class )
						.withPropertyPath( pathWith()
								.constructor( OrderService.class )
								.returnValue()
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_METHODCONSTRUCTORVALIDATION, id = "b")
	public void constructorReturnValueValidationWithRedefinedDefaultGroupSequence()
			throws Exception {
		Constructor<OrderServiceWithRedefinedDefaultGroupSequence> constructor = OrderServiceWithRedefinedDefaultGroupSequence.class
				.getConstructor(
						String.class,
						Item.class,
						byte.class
				);
		OrderServiceWithRedefinedDefaultGroupSequence returnValue = new OrderServiceWithRedefinedDefaultGroupSequence(
				""
		);

		Set<ConstraintViolation<OrderServiceWithRedefinedDefaultGroupSequence>> violations = getExecutableValidator()
				.validateConstructorReturnValue(
						constructor,
						returnValue
				);

		//Only the constraints of the Basic group should fail
		assertThat( violations ).containsOnlyViolations(
				violationOf( ValidOrderService.class )
						.withPropertyPath( pathWith()
								.constructor( OrderServiceWithRedefinedDefaultGroupSequence.class )
								.returnValue()
						),
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.constructor( OrderServiceWithRedefinedDefaultGroupSequence.class )
								.returnValue()
								.property( "name" )
						)
		);

		returnValue = new OrderServiceWithRedefinedDefaultGroupSequence( "valid" );

		violations = getExecutableValidator().validateConstructorReturnValue(
				constructor,
				returnValue
		);

		//Now the constraints of the Default group should fail
		assertThat( violations ).containsOnlyViolations(
				violationOf( ValidRetailOrderService.class )
						.withPropertyPath( pathWith()
								.constructor( OrderServiceWithRedefinedDefaultGroupSequence.class )
								.returnValue()
						),
				violationOf( Pattern.class )
						.withPropertyPath( pathWith()
								.constructor( OrderServiceWithRedefinedDefaultGroupSequence.class )
								.returnValue()
								.property( "name" )
						)
		);
	}
}
