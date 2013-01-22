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
package org.hibernate.beanvalidation.tck.tests.methodvalidation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.MethodValidator;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;
import javax.validation.metadata.ElementDescriptor.Kind;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

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
import org.hibernate.beanvalidation.tck.util.Groups;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectConstraintTypes;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectNumberOfViolations;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectPathDescriptorKinds;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectPathNodeNames;
import static org.hibernate.beanvalidation.tck.util.TestUtil.kinds;
import static org.hibernate.beanvalidation.tck.util.TestUtil.names;

/**
 * @author Gunnar Morling
 */
//TODO Add tests for not reachable/cascadable parameters/return values
@SpecVersion(spec = "beanvalidation", version = "1.1.0")
public class MethodValidationTest extends Arquillian {

	private MethodValidator executableValidator;

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClass( ValidateConstructorParametersTest.class )
				.withPackage( MyCrossParameterConstraint.class.getPackage() )
				.withPackage( ExtendedOrderService.class.getPackage() )
				.withClass( Item.class )
				.withClass( Order.class )
				.build();
	}

	@BeforeMethod
	public void setupValidator() {
		executableValidator = TestUtil.getValidatorUnderTest().forMethods();
	}

	//fails due to https://hibernate.onjira.com/browse/HV-673
	@Test(groups = Groups.FAILING_IN_RI)
	@SpecAssertion(section = "4.6.2", id = "a")
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

		Set<ConstraintViolation<Object>> violations = executableValidator.validateParameters(
				object,
				method,
				parameterValues
		);

		assertCorrectNumberOfViolations( violations, 4 );

		assertCorrectConstraintTypes(
				violations,
				NotNull.class,
				Min.class,
				Size.class,
				MyCrossParameterConstraint.class
		);
		assertCorrectPathNodeNames(
				violations,
				names( methodName ),
				names( methodName, "arg0" ),
				names( methodName, "arg1", "name" ),
				names( methodName, "arg2" )
		);
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.METHOD ),
				kinds( Kind.METHOD, Kind.PARAMETER ),
				kinds( Kind.METHOD, Kind.PARAMETER, Kind.PROPERTY ),
				kinds( Kind.METHOD, Kind.PARAMETER )
		);
	}

	//fails due to https://hibernate.onjira.com/browse/HV-673
	@Test(groups = Groups.FAILING_IN_RI)
	@SpecAssertion(section = "4.6.2", id = "a")
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

		Set<ConstraintViolation<Object>> violations = executableValidator.validateParameters(
				object,
				method,
				parameterValues
		);

		assertCorrectNumberOfViolations( violations, 4 );

		assertCorrectConstraintTypes(
				violations,
				NotNull.class,
				Min.class,
				Size.class,
				MyCrossParameterConstraint.class
		);
		assertCorrectPathNodeNames(
				violations,
				names( methodName ),
				names( methodName, "arg0" ),
				names( methodName, "arg1", "name" ),
				names( methodName, "arg2" )
		);
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.METHOD ),
				kinds( Kind.METHOD, Kind.PARAMETER ),
				kinds( Kind.METHOD, Kind.PARAMETER, Kind.PROPERTY ),
				kinds( Kind.METHOD, Kind.PARAMETER )
		);
	}

	//fails due to https://hibernate.onjira.com/browse/HV-673
	@Test(groups = Groups.FAILING_IN_RI)
	@SpecAssertion(section = "4.6.2", id = "a")
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

		Set<ConstraintViolation<Object>> violations = executableValidator.validateParameters(
				object,
				method,
				parameterValues
		);

		assertCorrectNumberOfViolations( violations, 4 );

		assertCorrectConstraintTypes(
				violations,
				NotNull.class,
				Min.class,
				Size.class,
				MyCrossParameterConstraint.class
		);
		assertCorrectPathNodeNames(
				violations,
				names( methodName ),
				names( methodName, "arg0" ),
				names( methodName, "arg1", "name" ),
				names( methodName, "arg2" )
		);
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.METHOD ),
				kinds( Kind.METHOD, Kind.PARAMETER ),
				kinds( Kind.METHOD, Kind.PARAMETER, Kind.PROPERTY ),
				kinds( Kind.METHOD, Kind.PARAMETER )
		);
	}

	//fails due to https://hibernate.onjira.com/browse/HV-673
	@Test(groups = Groups.FAILING_IN_RI)
	@SpecAssertion(section = "4.6.2", id = "a")
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

		Set<ConstraintViolation<Object>> violations = executableValidator.validateParameters(
				object,
				method,
				parameterValues,
				Basic.class
		);

		//only constraints in Basic group should fail
		assertCorrectNumberOfViolations( violations, 2 );

		assertCorrectConstraintTypes(
				violations,
				NotNull.class,
				MyCrossParameterConstraint.class
		);
		assertCorrectPathNodeNames(
				violations,
				names( methodName ),
				names( methodName, "arg0" )
		);
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.METHOD ),
				kinds( Kind.METHOD, Kind.PARAMETER )
		);

		violations = executableValidator.validateParameters(
				object,
				method,
				parameterValues
		);

		//only constraints in Default group should fail
		assertCorrectNumberOfViolations( violations, 2 );

		assertCorrectConstraintTypes(
				violations,
				Min.class,
				Size.class
		);
		assertCorrectPathNodeNames(
				violations,
				names( methodName, "arg1", "name" ),
				names( methodName, "arg2" )
		);
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.METHOD, Kind.PARAMETER, Kind.PROPERTY ),
				kinds( Kind.METHOD, Kind.PARAMETER )
		);
	}

	//fails due to https://hibernate.onjira.com/browse/HV-673
	@Test(groups = Groups.FAILING_IN_RI)
	@SpecAssertion(section = "4.6.2", id = "a")
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

		Set<ConstraintViolation<Object>> violations = executableValidator.validateParameters(
				object,
				method,
				parameterValues,
				Basic.class
		);

		assertCorrectNumberOfViolations( violations, 1 );

		assertCorrectConstraintTypes( violations, Size.class );
		assertCorrectPathNodeNames( violations, names( methodName, "arg1", "name" ) );
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.METHOD, Kind.PARAMETER, Kind.PROPERTY )
		);
	}

	//fails due to https://hibernate.onjira.com/browse/HV-678
	@Test(groups = Groups.FAILING_IN_RI)
	@SpecAssertions({
			@SpecAssertion(section = "4.6.2", id = "a"),
			@SpecAssertion(section = "4.6.2", id = "c")
	})
	public void methodParameterValidationValidatesEachConstraintOnlyOnce() throws Exception {
		String methodName = "placeOrder";

		Object object = new OrderService();
		Method method = OrderService.class.getMethod(
				methodName,
				String.class,
				Item.class,
				short.class
		);
		Object[] parameterValues = new Object[] { null, new Item( "" ), 0 };

		Set<ConstraintViolation<Object>> violations = executableValidator.validateParameters(
				object,
				method,
				parameterValues,
				Basic.class, Default.class
		);

		assertCorrectNumberOfViolations( violations, 4 );

		assertCorrectConstraintTypes(
				violations,
				NotNull.class,
				Min.class,
				Size.class,
				MyCrossParameterConstraint.class
		);
		assertCorrectPathNodeNames(
				violations,
				names( methodName ),
				names( methodName, "arg0" ),
				names( methodName, "arg1", "name" ),
				names( methodName, "arg2" )
		);
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.METHOD ),
				kinds( Kind.METHOD, Kind.PARAMETER ),
				kinds( Kind.METHOD, Kind.PARAMETER, Kind.PROPERTY ),
				kinds( Kind.METHOD, Kind.PARAMETER )
		);
	}

	//fails due to https://hibernate.onjira.com/browse/HV-673
	@Test(groups = Groups.FAILING_IN_RI)
	@SpecAssertions({
			@SpecAssertion(section = "4.6.2", id = "a"),
			@SpecAssertion(section = "4.6.2", id = "d")
	})
	public void methodParameterValidationUsingSequence() throws Exception {
		String methodName = "placeOrder";

		Object object = new OrderService();
		Method method = OrderService.class.getMethod(
				methodName,
				String.class,
				Item.class,
				byte.class
		);
		Object[] parameterValues = new Object[] { null, new Item( "" ), 0 };

		Set<ConstraintViolation<Object>> violations = executableValidator.validateParameters(
				object,
				method,
				parameterValues,
				OrderServiceSequence.class
		);

		assertCorrectNumberOfViolations( violations, 2 );

		//Only the constraints of the Basic group should fail
		assertCorrectConstraintTypes( violations, Size.class, NotNull.class );
		assertCorrectPathNodeNames(
				violations,
				names( methodName, "arg0" ),
				names( methodName, "arg1", "name" )
		);
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.METHOD, Kind.PARAMETER ),
				kinds( Kind.METHOD, Kind.PARAMETER, Kind.PROPERTY )
		);

		parameterValues = new Object[] { "Bob", new Item( "BV Specification" ), 0 };

		violations = executableValidator.validateParameters(
				object,
				method,
				parameterValues,
				OrderServiceSequence.class
		);

		assertCorrectNumberOfViolations( violations, 2 );

		//Now the constraints of the Complex group should fail
		assertCorrectConstraintTypes( violations, MyCrossParameterConstraint.class, Min.class );
		assertCorrectPathNodeNames( violations, names( methodName ), names( methodName, "arg2" ) );
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.METHOD ),
				kinds( Kind.METHOD, Kind.PARAMETER )
		);
	}

	//fails due to https://hibernate.onjira.com/browse/HV-679
	@Test(groups = Groups.FAILING_IN_RI)
	@SpecAssertions({
			@SpecAssertion(section = "4.6.2", id = "a"),
			@SpecAssertion(section = "4.6.2", id = "d")
	})
	public void methodParameterValidationWithRedefinedDefaultGroupSequence() throws Exception {
		String methodName = "placeOrder";

		Object object = new OrderServiceWithRedefinedDefaultGroupSequence();
		Method method = OrderServiceWithRedefinedDefaultGroupSequence.class.getMethod(
				methodName,
				String.class,
				Item.class,
				byte.class
		);
		Object[] parameterValues = new Object[] { null, new Item( "" ), 0 };

		Set<ConstraintViolation<Object>> violations = executableValidator.validateParameters(
				object,
				method,
				parameterValues
		);

		assertCorrectNumberOfViolations( violations, 2 );

		//Only the constraints of the Basic group on OrderServiceWithRedefinedDefaultGroupSequence and of
		//the Default group on Item should fail
		assertCorrectConstraintTypes( violations, Size.class, NotNull.class );
		assertCorrectPathNodeNames(
				violations,
				names( methodName, "arg0" ),
				names( methodName, "arg1", "name" )
		);
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.METHOD, Kind.PARAMETER ),
				kinds( Kind.METHOD, Kind.PARAMETER, Kind.PROPERTY )
		);

		parameterValues = new Object[] { "Bob", new Item( "BV Specification" ), 0 };

		violations = executableValidator.validateParameters(
				object,
				method,
				parameterValues
		);

		assertCorrectNumberOfViolations( violations, 3 );

		//Now the constraints of the Default group on OrderServiceWithRedefinedDefaultGroupSequence and of
		//the Default group on Item should fail
		assertCorrectConstraintTypes(
				violations,
				MyCrossParameterConstraint.class,
				Size.class,
				Min.class
		);
		assertCorrectPathNodeNames(
				violations,
				names( methodName ),
				names( methodName, "arg1", "name" ),
				names( methodName, "arg2" )
		);
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.METHOD ),
				kinds( Kind.METHOD, Kind.PARAMETER, Kind.PROPERTY ),
				kinds( Kind.METHOD, Kind.PARAMETER )
		);
	}

	//fails due to https://hibernate.onjira.com/browse/HV-673
	@Test(groups = Groups.FAILING_IN_RI)
	@SpecAssertion(section = "4.6.2", id = "a")
	public void constructorParameterValidationTargetsParameterCrossParameterAndCascadedConstraints()
			throws Exception {
		String className = "OrderService";

		Constructor<OrderService> constructor = OrderService.class.getConstructor(
				String.class,
				Item.class,
				int.class
		);
		Object[] parameterValues = new Object[] { null, new Item( "" ), 0 };

		Set<ConstraintViolation<OrderService>> violations = executableValidator.validateConstructorParameters(
				constructor,
				parameterValues
		);

		assertCorrectNumberOfViolations( violations, 4 );

		assertCorrectConstraintTypes(
				violations,
				NotNull.class,
				Min.class,
				Size.class,
				MyCrossParameterConstraint.class
		);
		assertCorrectPathNodeNames(
				violations,
				names( className ),
				names( className, "arg0" ),
				names( className, "arg1", "name" ),
				names( className, "arg2" )
		);
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.CONSTRUCTOR ),
				kinds( Kind.CONSTRUCTOR, Kind.PARAMETER ),
				kinds( Kind.CONSTRUCTOR, Kind.PARAMETER, Kind.PROPERTY ),
				kinds( Kind.CONSTRUCTOR, Kind.PARAMETER )
		);
	}

	@Test
	@SpecAssertion(section = "4.6.2", id = "a")
	public void constructorParameterValidationDoesNotIncludeConstraintsFromSuperClass()
			throws Exception {
		Constructor<ExtendedOrderService> constructor = ExtendedOrderService.class.getConstructor(
				String.class,
				Item.class,
				int.class
		);
		Object[] parameterValues = new Object[] { null, new Item( "" ), 0 };

		Set<ConstraintViolation<ExtendedOrderService>> violations = executableValidator.validateConstructorParameters(
				constructor,
				parameterValues
		);

		assertCorrectNumberOfViolations( violations, 0 );
	}

	//fails due to https://hibernate.onjira.com/browse/HV-673
	@Test(groups = Groups.FAILING_IN_RI)
	@SpecAssertion(section = "4.6.2", id = "a")
	public void constructorParameterValidationIsAppliedGroupWise() throws Exception {
		String className = "OrderService";

		Constructor<OrderService> constructor = OrderService.class.getConstructor(
				String.class,
				Item.class,
				Integer.class
		);
		Object[] parameterValues = new Object[] { null, new Item( "" ), 0 };

		Set<ConstraintViolation<OrderService>> violations = executableValidator.validateConstructorParameters(
				constructor,
				parameterValues,
				Basic.class
		);

		//only constraints in Basic group should fail
		assertCorrectNumberOfViolations( violations, 2 );

		assertCorrectConstraintTypes(
				violations,
				NotNull.class,
				MyCrossParameterConstraint.class
		);
		assertCorrectPathNodeNames(
				violations,
				names( className ),
				names( className, "arg0" )
		);
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.CONSTRUCTOR ),
				kinds( Kind.CONSTRUCTOR, Kind.PARAMETER )
		);

		violations = executableValidator.validateConstructorParameters(
				constructor,
				parameterValues
		);

		//only constraints in Default group should fail
		assertCorrectNumberOfViolations( violations, 2 );

		assertCorrectConstraintTypes(
				violations,
				Min.class,
				Size.class
		);
		assertCorrectPathNodeNames(
				violations,
				names( className, "arg1", "name" ),
				names( className, "arg2" )
		);
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.CONSTRUCTOR, Kind.PARAMETER, Kind.PROPERTY ),
				kinds( Kind.CONSTRUCTOR, Kind.PARAMETER )
		);
	}

	//fails due to https://hibernate.onjira.com/browse/HV-673
	@Test(groups = Groups.FAILING_IN_RI)
	@SpecAssertion(section = "4.6.2", id = "a")
	public void constructorParameterValidationPerformsGroupConversion() throws Exception {
		String className = "OrderService";

		Constructor<OrderService> constructor = OrderService.class.getConstructor(
				String.class,
				Item.class,
				long.class
		);
		Object[] parameterValues = new Object[] { null, new Item( "" ), 0 };

		Set<ConstraintViolation<OrderService>> violations = executableValidator.validateConstructorParameters(
				constructor,
				parameterValues,
				Basic.class
		);

		assertCorrectNumberOfViolations( violations, 1 );

		assertCorrectConstraintTypes( violations, Size.class );
		assertCorrectPathNodeNames( violations, names( className, "arg1", "name" ) );
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.CONSTRUCTOR, Kind.PARAMETER, Kind.PROPERTY )
		);
	}

	//fails due to https://hibernate.onjira.com/browse/HV-678
	@Test(groups = Groups.FAILING_IN_RI)
	@SpecAssertions({
			@SpecAssertion(section = "4.6.2", id = "a"),
			@SpecAssertion(section = "4.6.2", id = "c")
	})
	public void constructorParameterValidationValidatesEachConstraintOnlyOnce() throws Exception {
		String className = "OrderService";

		Constructor<OrderService> constructor = OrderService.class.getConstructor(
				String.class,
				Item.class,
				short.class
		);
		Object[] parameterValues = new Object[] { null, new Item( "" ), 0 };

		Set<ConstraintViolation<OrderService>> violations = executableValidator.validateConstructorParameters(
				constructor,
				parameterValues,
				Basic.class, Default.class
		);

		assertCorrectNumberOfViolations( violations, 4 );

		assertCorrectConstraintTypes(
				violations,
				NotNull.class,
				Min.class,
				Size.class,
				MyCrossParameterConstraint.class
		);
		assertCorrectPathNodeNames(
				violations,
				names( className ),
				names( className, "arg0" ),
				names( className, "arg1", "name" ),
				names( className, "arg2" )
		);
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.CONSTRUCTOR ),
				kinds( Kind.CONSTRUCTOR, Kind.PARAMETER ),
				kinds( Kind.CONSTRUCTOR, Kind.PARAMETER, Kind.PROPERTY ),
				kinds( Kind.CONSTRUCTOR, Kind.PARAMETER )
		);
	}

	//fails due to https://hibernate.onjira.com/browse/HV-673
	@Test(groups = Groups.FAILING_IN_RI)
	@SpecAssertions({
			@SpecAssertion(section = "4.6.2", id = "a"),
			@SpecAssertion(section = "4.6.2", id = "d")
	})
	public void constructorParameterValidationUsingSequence() throws Exception {
		String className = "OrderService";

		Constructor<OrderService> constructor = OrderService.class.getConstructor(
				String.class,
				Item.class,
				byte.class
		);
		Object[] parameterValues = new Object[] { null, new Item( "" ), 0 };

		Set<ConstraintViolation<OrderService>> violations = executableValidator.validateConstructorParameters(
				constructor,
				parameterValues,
				OrderServiceSequence.class
		);

		assertCorrectNumberOfViolations( violations, 2 );

		//Only the constraints of the Basic group should fail
		assertCorrectConstraintTypes( violations, Size.class, NotNull.class );
		assertCorrectPathNodeNames(
				violations,
				names( className, "arg0" ),
				names( className, "arg1", "name" )
		);
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.CONSTRUCTOR, Kind.PARAMETER ),
				kinds( Kind.CONSTRUCTOR, Kind.PARAMETER, Kind.PROPERTY )
		);

		parameterValues = new Object[] { "Bob", new Item( "BV Specification" ), 0 };

		violations = executableValidator.validateConstructorParameters(
				constructor,
				parameterValues,
				OrderServiceSequence.class
		);

		assertCorrectNumberOfViolations( violations, 2 );

		//Now the constraints of the Complex group should fail
		assertCorrectConstraintTypes( violations, MyCrossParameterConstraint.class, Min.class );
		assertCorrectPathNodeNames( violations, names( className ), names( className, "arg2" ) );
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.CONSTRUCTOR ),
				kinds( Kind.CONSTRUCTOR, Kind.PARAMETER )
		);
	}

	//fails due to https://hibernate.onjira.com/browse/HV-679
	@Test(groups = Groups.FAILING_IN_RI)
	@SpecAssertions({
			@SpecAssertion(section = "4.6.2", id = "a"),
			@SpecAssertion(section = "4.6.2", id = "d")
	})
	public void constructorParameterValidationWithRedefinedDefaultGroupSequence() throws Exception {
		String className = "OrderServiceWithRedefinedDefaultGroupSequence";

		Constructor<OrderServiceWithRedefinedDefaultGroupSequence> constructor = OrderServiceWithRedefinedDefaultGroupSequence.class
				.getConstructor(
						String.class,
						Item.class,
						byte.class
				);
		Object[] parameterValues = new Object[] { null, new Item( "" ), 0 };

		Set<ConstraintViolation<OrderServiceWithRedefinedDefaultGroupSequence>> violations = executableValidator
				.validateConstructorParameters(
						constructor,
						parameterValues
				);

		assertCorrectNumberOfViolations( violations, 2 );

		//Only the constraints of the Basic group on OrderServiceWithRedefinedDefaultGroupSequence and of
		//the Default group on Item should fail
		assertCorrectConstraintTypes( violations, Size.class, NotNull.class );
		assertCorrectPathNodeNames(
				violations,
				names( className, "arg0" ),
				names( className, "arg1", "name" )
		);
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.CONSTRUCTOR, Kind.PARAMETER ),
				kinds( Kind.CONSTRUCTOR, Kind.PARAMETER, Kind.PROPERTY )
		);

		parameterValues = new Object[] { "Bob", new Item( "BV Specification" ), 0 };

		violations = executableValidator.validateConstructorParameters(
				constructor,
				parameterValues
		);

		assertCorrectNumberOfViolations( violations, 3 );

		//Now the constraints of the Default group on OrderServiceWithRedefinedDefaultGroupSequence and of
		//the Default group on Item should fail
		assertCorrectConstraintTypes(
				violations,
				MyCrossParameterConstraint.class,
				Size.class,
				Min.class
		);
		assertCorrectPathNodeNames(
				violations,
				names( className ),
				names( className, "arg1", "name" ),
				names( className, "arg2" )
		);
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.CONSTRUCTOR ),
				kinds( Kind.CONSTRUCTOR, Kind.PARAMETER, Kind.PROPERTY ),
				kinds( Kind.CONSTRUCTOR, Kind.PARAMETER )
		);
	}

	//fails due to https://hibernate.onjira.com/browse/HV-680
	@Test(groups = Groups.FAILING_IN_RI)
	@SpecAssertion(section = "4.6.2", id = "b")
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

		Set<ConstraintViolation<Object>> violations = executableValidator.validateReturnValue(
				object,
				method,
				returnValue
		);

		assertCorrectNumberOfViolations( violations, 2 );

		assertCorrectConstraintTypes(
				violations,
				ValidOrder.class,
				Size.class
		);
		assertCorrectPathNodeNames(
				violations,
				names( methodName, "$retval" ),
				names( methodName, "$retval", "name" )
		);
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.METHOD, Kind.RETURN_VALUE ),
				kinds( Kind.METHOD, Kind.RETURN_VALUE, Kind.PROPERTY )
		);
	}

	//fails due to https://hibernate.onjira.com/browse/HV-680
	@Test(groups = Groups.FAILING_IN_RI)
	@SpecAssertion(section = "4.6.2", id = "b")
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

		Set<ConstraintViolation<Object>> violations = executableValidator.validateReturnValue(
				object,
				method,
				returnValue
		);

		assertCorrectNumberOfViolations( violations, 3 );

		assertCorrectConstraintTypes(
				violations,
				ValidOrder.class,
				ValidRetailOrder.class,
				Size.class
		);
		assertCorrectPathNodeNames(
				violations,
				names( methodName, "$retval" ),
				names( methodName, "$retval" ),
				names( methodName, "$retval", "name" )
		);
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.METHOD, Kind.RETURN_VALUE ),
				kinds( Kind.METHOD, Kind.RETURN_VALUE ),
				kinds( Kind.METHOD, Kind.RETURN_VALUE, Kind.PROPERTY )
		);
	}

	//fails due to https://hibernate.onjira.com/browse/HV-680
	@Test(groups = Groups.FAILING_IN_RI)
	@SpecAssertion(section = "4.6.2", id = "b")
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

		Set<ConstraintViolation<Object>> violations = executableValidator.validateReturnValue(
				object,
				method,
				returnValue
		);

		assertCorrectNumberOfViolations( violations, 3 );

		assertCorrectConstraintTypes(
				violations,
				ValidOrder.class,
				ValidRetailOrder.class,
				Size.class
		);
		assertCorrectPathNodeNames(
				violations,
				names( methodName, "$retval" ),
				names( methodName, "$retval" ),
				names( methodName, "$retval", "name" )
		);
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.METHOD, Kind.RETURN_VALUE ),
				kinds( Kind.METHOD, Kind.RETURN_VALUE ),
				kinds( Kind.METHOD, Kind.RETURN_VALUE, Kind.PROPERTY )
		);
	}

	//fails due to https://hibernate.onjira.com/browse/HV-680
	@Test(groups = Groups.FAILING_IN_RI)
	@SpecAssertion(section = "4.6.2", id = "b")
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

		Set<ConstraintViolation<Object>> violations = executableValidator.validateReturnValue(
				object,
				method,
				returnValue,
				Basic.class
		);

		//only constraints in Basic group should fail
		assertCorrectNumberOfViolations( violations, 1 );

		assertCorrectConstraintTypes(
				violations,
				ValidRetailOrder.class
		);
		assertCorrectPathNodeNames(
				violations,
				names( methodName, "$retval" )
		);
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.METHOD, Kind.RETURN_VALUE )
		);

		violations = executableValidator.validateReturnValue(
				object,
				method,
				returnValue
		);

		//only constraints in Default group should fail
		assertCorrectNumberOfViolations( violations, 2 );

		assertCorrectConstraintTypes(
				violations,
				ValidOrder.class,
				Size.class
		);
		assertCorrectPathNodeNames(
				violations,
				names( methodName, "$retval", "name" ),
				names( methodName, "$retval" )
		);
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.METHOD, Kind.RETURN_VALUE, Kind.PROPERTY ),
				kinds( Kind.METHOD, Kind.RETURN_VALUE )
		);
	}

	//fails due to https://hibernate.onjira.com/browse/HV-673
	@Test(groups = Groups.FAILING_IN_RI)
	@SpecAssertion(section = "4.6.2", id = "b")
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

		Set<ConstraintViolation<Object>> violations = executableValidator.validateReturnValue(
				object,
				method,
				returnValue,
				Basic.class
		);

		assertCorrectNumberOfViolations( violations, 1 );

		assertCorrectConstraintTypes( violations, Size.class );
		assertCorrectPathNodeNames( violations, names( methodName, "$retval", "name" ) );
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.METHOD, Kind.RETURN_VALUE, Kind.PROPERTY )
		);
	}

	//fails due to https://hibernate.onjira.com/browse/HV-678
	@Test(groups = Groups.FAILING_IN_RI)
	@SpecAssertions({
			@SpecAssertion(section = "4.6.2", id = "b"),
			@SpecAssertion(section = "4.6.2", id = "c")
	})
	public void methodReturnValueValidationValidatesEachConstraintOnlyOnce() throws Exception {
		String methodName = "placeOrder";

		Object object = new OrderService();
		Method method = OrderService.class.getMethod(
				methodName,
				String.class,
				Item.class,
				short.class
		);
		Object returnValue = null;

		Set<ConstraintViolation<Object>> violations = executableValidator.validateReturnValue(
				object,
				method,
				returnValue,
				Basic.class, Default.class
		);

		assertCorrectNumberOfViolations( violations, 1 );

		assertCorrectConstraintTypes(
				violations,
				ValidOrder.class
		);
		assertCorrectPathNodeNames(
				violations,
				names( methodName, "$retval" )
		);
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.METHOD, Kind.RETURN_VALUE )
		);
	}

	//fails due to https://hibernate.onjira.com/browse/HV-680
	@Test(groups = Groups.FAILING_IN_RI)
	@SpecAssertions({
			@SpecAssertion(section = "4.6.2", id = "b"),
			@SpecAssertion(section = "4.6.2", id = "d")
	})
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

		Set<ConstraintViolation<Object>> violations = executableValidator.validateReturnValue(
				object,
				method,
				returnValue,
				OrderServiceSequence.class
		);

		assertCorrectNumberOfViolations( violations, 2 );

		//Only the constraints of the Basic group should fail
		assertCorrectConstraintTypes( violations, ValidOrder.class );
		assertCorrectPathNodeNames(
				violations,
				names( methodName, "$retval" ),
				names( methodName, "$retval", "name" )
		);
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.METHOD, Kind.RETURN_VALUE ),
				kinds( Kind.METHOD, Kind.RETURN_VALUE, Kind.PROPERTY )
		);

		returnValue = new Order( "BV Specification" );

		violations = executableValidator.validateReturnValue(
				object,
				method,
				returnValue,
				OrderServiceSequence.class
		);

		assertCorrectNumberOfViolations( violations, 1 );

		//Now the constraints of the Complex group should fail
		assertCorrectConstraintTypes( violations, ValidRetailOrder.class );
		assertCorrectPathNodeNames(
				violations,
				names( methodName ),
				names( methodName, "$retval" )
		);
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.METHOD, Kind.RETURN_VALUE )
		);
	}

	//fails due to https://hibernate.onjira.com/browse/HV-679
	@Test(groups = Groups.FAILING_IN_RI)
	@SpecAssertions({
			@SpecAssertion(section = "4.6.2", id = "b"),
			@SpecAssertion(section = "4.6.2", id = "d")
	})
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

		Set<ConstraintViolation<Object>> violations = executableValidator.validateReturnValue(
				object,
				method,
				returnValue
		);

		assertCorrectNumberOfViolations( violations, 2 );

		//Only the constraints of the Basic group on OrderServiceWithRedefinedDefaultGroupSequence and of
		//the Default group on Order should fail
		assertCorrectConstraintTypes( violations, Size.class, ValidOrder.class );
		assertCorrectPathNodeNames(
				violations,
				names( methodName, "$retval" ),
				names( methodName, "$retval", "name" )
		);
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.METHOD, Kind.RETURN_VALUE ),
				kinds( Kind.METHOD, Kind.RETURN_VALUE, Kind.PROPERTY )
		);

		returnValue = new Order( "valid" );

		violations = executableValidator.validateReturnValue(
				object,
				method,
				returnValue
		);

		assertCorrectNumberOfViolations( violations, 2 );

		//Now the constraints of the Default group on OrderServiceWithRedefinedDefaultGroupSequence and of
		//the Default group on Order should fail
		assertCorrectConstraintTypes( violations, Size.class, ValidRetailOrder.class );
		assertCorrectPathNodeNames(
				violations,
				names( methodName, "$retval" ),
				names( methodName, "$retval", "name" )
		);
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.METHOD, Kind.RETURN_VALUE ),
				kinds( Kind.METHOD, Kind.RETURN_VALUE, Kind.PROPERTY )
		);
	}

	//fails due to https://hibernate.onjira.com/browse/HV-680
	@Test(groups = Groups.FAILING_IN_RI)
	@SpecAssertion(section = "4.6.2", id = "b")
	public void constructorReturnValueValidationTargetsReturnValueAndCascadedConstraints()
			throws Exception {
		String className = "OrderService";

		Constructor<OrderService> constructor = OrderService.class.getConstructor(
				String.class,
				Item.class,
				int.class
		);
		OrderService returnValue = new OrderService( "" );

		Set<ConstraintViolation<OrderService>> violations = executableValidator.validateConstructorReturnValue(
				constructor,
				returnValue
		);

		assertCorrectNumberOfViolations( violations, 2 );

		assertCorrectConstraintTypes(
				violations,
				ValidOrderService.class,
				Size.class
		);
		assertCorrectPathNodeNames(
				violations,
				names( className, "$retval" ),
				names( className, "$retval", "name" )
		);
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.CONSTRUCTOR, Kind.RETURN_VALUE ),
				kinds( Kind.CONSTRUCTOR, Kind.RETURN_VALUE, Kind.PROPERTY )
		);
	}

	@Test
	@SpecAssertion(section = "4.6.2", id = "b")
	public void constructorReturnValueValidationDoesNotIncludeConstraintsFromSuperClass()
			throws Exception {
		String className = "ExtendedOrderService";

		Constructor<ExtendedOrderService> constructor = ExtendedOrderService.class.getConstructor(
				String.class,
				Item.class,
				int.class
		);
		ExtendedOrderService returnValue = new ExtendedOrderService();

		Set<ConstraintViolation<ExtendedOrderService>> violations = executableValidator.validateConstructorReturnValue(
				constructor,
				returnValue
		);

		assertCorrectNumberOfViolations( violations, 1 );

		assertCorrectConstraintTypes(
				violations,
				ValidRetailOrderService.class
		);
		assertCorrectPathNodeNames(
				violations,
				names( className, "$retval" )
		);
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.CONSTRUCTOR, Kind.RETURN_VALUE )
		);
	}

	//fails due to https://hibernate.onjira.com/browse/HV-680
	@Test(groups = Groups.FAILING_IN_RI)
	@SpecAssertion(section = "4.6.2", id = "b")
	public void constructorReturnValueValidationIsAppliedGroupWise() throws Exception {
		String className = "OrderService";

		Constructor<OrderService> constructor = OrderService.class.getConstructor(
				String.class,
				Item.class,
				Integer.class
		);
		OrderService returnValue = new OrderService( "" );

		Set<ConstraintViolation<OrderService>> violations = executableValidator.validateConstructorReturnValue(
				constructor,
				returnValue,
				Basic.class
		);

		//only constraints in Basic group should fail
		assertCorrectNumberOfViolations( violations, 1 );

		assertCorrectConstraintTypes(
				violations,
				ValidRetailOrderService.class
		);
		assertCorrectPathNodeNames(
				violations,
				names( className, "$retval" )
		);
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.CONSTRUCTOR, Kind.RETURN_VALUE )
		);

		violations = executableValidator.validateConstructorReturnValue(
				constructor,
				returnValue
		);

		//only constraints in Default group should fail
		assertCorrectNumberOfViolations( violations, 2 );

		assertCorrectConstraintTypes(
				violations,
				ValidOrderService.class,
				Size.class
		);
		assertCorrectPathNodeNames(
				violations,
				names( className, "$retval", "name" ),
				names( className, "$retval" )
		);
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.CONSTRUCTOR, Kind.RETURN_VALUE, Kind.PROPERTY ),
				kinds( Kind.CONSTRUCTOR, Kind.RETURN_VALUE )
		);
	}

	//fails due to https://hibernate.onjira.com/browse/HV-673
	@Test(groups = Groups.FAILING_IN_RI)
	@SpecAssertion(section = "4.6.2", id = "b")
	public void constructorReturnValueValidationPerformsGroupConversion() throws Exception {
		String className = "OrderService";

		Constructor<OrderService> constructor = OrderService.class.getConstructor(
				String.class,
				Item.class,
				long.class
		);
		OrderService returnValue = new OrderService( "" );

		Set<ConstraintViolation<OrderService>> violations = executableValidator.validateConstructorReturnValue(
				constructor,
				returnValue,
				Basic.class
		);

		assertCorrectNumberOfViolations( violations, 1 );

		assertCorrectConstraintTypes( violations, Size.class );
		assertCorrectPathNodeNames( violations, names( className, "$retval", "name" ) );
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.CONSTRUCTOR, Kind.RETURN_VALUE, Kind.PROPERTY )
		);
	}

	//fails due to https://hibernate.onjira.com/browse/HV-678
	@Test(groups = Groups.FAILING_IN_RI)
	@SpecAssertions({
			@SpecAssertion(section = "4.6.2", id = "b"),
			@SpecAssertion(section = "4.6.2", id = "c")
	})
	public void constructorReturnValueValidationValidatesEachConstraintOnlyOnce() throws Exception {
		String className = "OrderService";

		Constructor<OrderService> constructor = OrderService.class.getConstructor(
				String.class,
				Item.class,
				short.class
		);
		OrderService returnValue = new OrderService( "" );

		Set<ConstraintViolation<OrderService>> violations = executableValidator.validateConstructorReturnValue(
				constructor,
				returnValue,
				Basic.class, Default.class
		);

		assertCorrectNumberOfViolations( violations, 1 );

		assertCorrectConstraintTypes(
				violations,
				ValidOrderService.class
		);
		assertCorrectPathNodeNames(
				violations,
				names( className, "$retval" )
		);
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.CONSTRUCTOR, Kind.RETURN_VALUE )
		);
	}

	//fails due to https://hibernate.onjira.com/browse/HV-680
	@Test(groups = Groups.FAILING_IN_RI)
	@SpecAssertions({
			@SpecAssertion(section = "4.6.2", id = "b"),
			@SpecAssertion(section = "4.6.2", id = "d")
	})
	public void constructorReturnValueValidationUsingSequence() throws Exception {
		String className = "OrderService";

		Constructor<OrderService> constructor = OrderService.class.getConstructor(
				String.class,
				Item.class,
				byte.class
		);
		OrderService returnValue = new OrderService( "" );

		Set<ConstraintViolation<OrderService>> violations = executableValidator.validateConstructorReturnValue(
				constructor,
				returnValue,
				OrderServiceSequence.class
		);

		assertCorrectNumberOfViolations( violations, 2 );

		//Only the constraints of the Basic group should fail
		assertCorrectConstraintTypes( violations, ValidOrderService.class );
		assertCorrectPathNodeNames(
				violations,
				names( className, "$retval" ),
				names( className, "$retval", "name" )
		);
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.CONSTRUCTOR, Kind.RETURN_VALUE ),
				kinds( Kind.CONSTRUCTOR, Kind.RETURN_VALUE, Kind.PROPERTY )
		);

		returnValue = new OrderService( "valid" );

		violations = executableValidator.validateConstructorReturnValue(
				constructor,
				returnValue,
				OrderServiceSequence.class
		);

		assertCorrectNumberOfViolations( violations, 1 );

		//Now the constraints of the Complex group should fail
		assertCorrectConstraintTypes( violations, ValidRetailOrderService.class );
		assertCorrectPathNodeNames(
				violations,
				names( className ),
				names( className, "$retval" )
		);
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.CONSTRUCTOR, Kind.RETURN_VALUE )
		);
	}

	//fails due to https://hibernate.onjira.com/browse/HV-680
	@Test(groups = Groups.FAILING_IN_RI)
	@SpecAssertions({
			@SpecAssertion(section = "4.6.2", id = "b"),
			@SpecAssertion(section = "4.6.2", id = "d")
	})
	public void constructorReturnValueValidationWithRedefinedDefaultGroupSequence()
			throws Exception {
		String className = "OrderServiceWithRedefinedDefaultGroupSequence";

		Constructor<OrderServiceWithRedefinedDefaultGroupSequence> constructor = OrderServiceWithRedefinedDefaultGroupSequence.class
				.getConstructor(
						String.class,
						Item.class,
						byte.class
				);
		OrderServiceWithRedefinedDefaultGroupSequence returnValue = new OrderServiceWithRedefinedDefaultGroupSequence(
				""
		);

		Set<ConstraintViolation<OrderServiceWithRedefinedDefaultGroupSequence>> violations = executableValidator
				.validateConstructorReturnValue(
						constructor,
						returnValue
				);

		assertCorrectNumberOfViolations( violations, 2 );

		//Only the constraints of the Basic group should fail
		assertCorrectConstraintTypes( violations, Size.class, ValidOrderService.class );
		assertCorrectPathNodeNames(
				violations,
				names( className, "$retval" ),
				names( className, "$retval", "name" )
		);
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.CONSTRUCTOR, Kind.RETURN_VALUE ),
				kinds( Kind.CONSTRUCTOR, Kind.RETURN_VALUE, Kind.PROPERTY )
		);

		returnValue = new OrderServiceWithRedefinedDefaultGroupSequence( "valid" );

		violations = executableValidator.validateConstructorReturnValue(
				constructor,
				returnValue
		);

		assertCorrectNumberOfViolations( violations, 2 );

		//Now the constraints of the Default group should fail
		assertCorrectConstraintTypes( violations, Pattern.class, ValidRetailOrderService.class );
		assertCorrectPathNodeNames(
				violations,
				names( className, "$retval" ),
				names( className, "$retval", "name" )
		);
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.CONSTRUCTOR, Kind.RETURN_VALUE ),
				kinds( Kind.CONSTRUCTOR, Kind.RETURN_VALUE, Kind.PROPERTY )
		);
	}
}
