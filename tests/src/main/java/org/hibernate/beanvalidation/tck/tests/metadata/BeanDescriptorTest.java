/*
* JBoss, Home of Professional Open Source
* Copyright 2009, Red Hat, Inc. and/or its affiliates, and individual contributors
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
package org.hibernate.beanvalidation.tck.tests.metadata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.validation.Validator;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstructorDescriptor;
import javax.validation.metadata.ElementDescriptor;
import javax.validation.metadata.ElementDescriptor.Kind;
import javax.validation.metadata.MethodDescriptor;
import javax.validation.metadata.ParameterDescriptor;
import javax.validation.metadata.PropertyDescriptor;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

import static org.hibernate.beanvalidation.tck.util.TestUtil.asSet;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "1.1.0")
public class BeanDescriptorTest extends Arquillian {

	private Validator validator;

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClass( BeanDescriptorTest.class )
				.withClasses(
						Customer.class,
						Person.class,
						Man.class,
						Account.class,
						Order.class,
						UnconstraintEntity.class,
						Severity.class,
						NotEmpty.class,
						AccountChecker.class,
						AccountValidator.class,
						CustomerService.class,
						Executables.class
				)
				.build();
	}

	@BeforeMethod
	private void setupValidator() {
		validator = TestUtil.getValidatorUnderTest();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "6.1", id = "a"),
			@SpecAssertion(section = "6.3", id = "a")
	})
	public void testIsBeanConstrainedDueToValidAnnotation() {
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( Customer.class );

		// constraint via @Valid
		assertFalse(
				beanDescriptor.hasConstraints(),
				"There should be no direct constraints on the specified bean."
		);
		assertTrue(
				beanDescriptor.isBeanConstrained(),
				"Bean should be constrained due to @valid "
		);
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "6.1", id = "a"),
			@SpecAssertion(section = "6.3", id = "a")
	})
	public void testIsBeanConstrainedDueToConstraintOnEntity() {
		// constraint hosted on bean itself
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( Account.class );
		assertTrue(
				beanDescriptor.hasConstraints(),
				"There should be direct constraints on the specified bean."
		);
		assertTrue(
				beanDescriptor.isBeanConstrained(),
				"Bean should be constrained due to @valid"
		);
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "6.1", id = "a"),
			@SpecAssertion(section = "6.3", id = "a")
	})
	public void testIsBeanConstrainedDueToConstraintProperty() {
		// constraint on bean property
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( Order.class );
		assertFalse(
				beanDescriptor.hasConstraints(),
				"There should be no direct constraints on the specified bean."
		);
		assertTrue(
				beanDescriptor.isBeanConstrained(),
				"Bean should be constrained due to @NotNull"
		);
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "6.1", id = "a"),
			@SpecAssertion(section = "6.3", id = "a")
	})
	public void testIsBeanConstrainedDueToConstraintOnInterface() {
		// constraint on implemented interface
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( Man.class );
		assertFalse(
				beanDescriptor.hasConstraints(),
				"There should be no direct constraints on the specified bean."
		);
		assertTrue(
				beanDescriptor.isBeanConstrained(),
				"Bean should be constrained due to constraints on Person."
		);
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "6.1", id = "a"),
			@SpecAssertion(section = "6.3", id = "a")
	})
	public void testUnconstrainedClass() {
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( UnconstraintEntity.class );
		assertFalse(
				beanDescriptor.hasConstraints(),
				"There should be no direct constraints on the specified bean."
		);
		assertFalse( beanDescriptor.isBeanConstrained(), "Bean should be unconstrained." );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "6.1", id = "a"),
			@SpecAssertion(section = "6.3", id = "b")
	})
	public void testGetConstraintsForConstrainedProperty() {
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( Order.class );
		PropertyDescriptor propertyDescriptor = beanDescriptor.getConstraintsForProperty(
				"orderNumber"
		);
		assertEquals(
				propertyDescriptor.getConstraintDescriptors().size(),
				1,
				"There should be one constraint descriptor"
		);
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "6.3", id = "b"),
			@SpecAssertion(section = "6.4", id = "a")
	})
	public void testGetConstraintsForUnConstrainedProperty() {
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( Customer.class );
		PropertyDescriptor propertyDescriptor = beanDescriptor.getConstraintsForProperty(
				"orderList"
		);
		assertEquals(
				propertyDescriptor.getConstraintDescriptors().size(),
				0,
				"There should be no constraint descriptors"
		);
		assertTrue( propertyDescriptor.isCascaded(), "The property should be cascaded" );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "6.1", id = "a"),
			@SpecAssertion(section = "6.3", id = "b")
	})
	public void testGetConstraintsForNonExistingProperty() {
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( Order.class );
		assertNull(
				beanDescriptor.getConstraintsForProperty( "foobar" ),
				"There should be no descriptor"
		);
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "6.1", id = "a"),
			@SpecAssertion(section = "6.3", id = "b")
	})
	public void testGetConstrainedProperties() {
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( Order.class );
		Set<PropertyDescriptor> constraintProperties = beanDescriptor.getConstrainedProperties();
		assertEquals( constraintProperties.size(), 1, "There should be only one property" );
		boolean hasOrderNumber = false;
		for ( PropertyDescriptor pd : constraintProperties ) {
			hasOrderNumber |= pd.getPropertyName().equals( "orderNumber" );
		}
		assertTrue( hasOrderNumber, "Wrong property" );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "6.1", id = "a"),
			@SpecAssertion(section = "6.3", id = "b")
	})
	public void testGetConstrainedPropertiesForUnconstrainedEntity() {
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( UnconstraintEntity.class );
		Set<PropertyDescriptor> constraintProperties = beanDescriptor.getConstrainedProperties();
		assertEquals( constraintProperties.size(), 0, "We should get the empty set." );
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	@SpecAssertion(section = "6.3", id = "c")
	public void testGetConstraintsForNullProperty() {
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( Order.class );
		beanDescriptor.getConstraintsForProperty( null );
	}

	@Test
	@SpecAssertion(section = "6.3", id = "d")
	public void testGetConstraintsForParameterConstrainedMethod() {
		MethodDescriptor methodDescriptor = Executables.parameterConstrainedMethod();
		assertNotNull( methodDescriptor, "Descriptor should not be null" );
	}

	@Test
	@SpecAssertion(section = "6.3", id = "d")
	public void testGetConstraintsForCrossParameterConstrainedMethod() {
		MethodDescriptor methodDescriptor = Executables.crossParameterConstrainedMethod();
		assertNotNull( methodDescriptor, "Descriptor should not be null" );
	}

	@Test
	@SpecAssertion(section = "6.3", id = "d")
	public void testGetConstraintsForCascadedParameterMethod() {
		MethodDescriptor methodDescriptor = Executables.cascadedParameterMethod();
		assertNotNull( methodDescriptor, "Descriptor should not be null" );
	}

	@Test
	@SpecAssertion(section = "6.3", id = "d")
	public void testGetConstraintsForReturnValueConstrainedMethod() {
		MethodDescriptor methodDescriptor = Executables.returnValueConstrainedMethod();
		assertNotNull( methodDescriptor, "Descriptor should not be null" );
	}

	@Test
	@SpecAssertion(section = "6.3", id = "d")
	public void testGetConstraintsForCascadedReturnValueMethod() {
		MethodDescriptor methodDescriptor = Executables.cascadedReturnValueMethod();
		assertNotNull( methodDescriptor, "Descriptor should not be null" );
	}

	@Test
	@SpecAssertion(section = "6.3", id = "d")
	public void testGetConstraintsForUnconstrainedMethod() {
		MethodDescriptor methodDescriptor = Executables.unconstrainedMethod();
		assertNull( methodDescriptor, "Descriptor should be null" );
	}

	@Test
	@SpecAssertion(section = "6.3", id = "d")
	public void testGetConstraintsForNonExistingMethod() {
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( CustomerService.class );
		MethodDescriptor methodDescriptor = beanDescriptor.getConstraintsForMethod( "foo" );
		assertNull( methodDescriptor, "Descriptor should be null" );
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	@SpecAssertion(section = "6.3", id = "d")
	public void testGetConstraintsForNullMethod() {
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( CustomerService.class );
		beanDescriptor.getConstraintsForMethod( null );
	}

	@Test
	@SpecAssertion(section = "6.3", id = "e")
	public void testGetConstrainedMethods() {
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( CustomerService.class );
		Set<MethodDescriptor> methodDescriptors = beanDescriptor.getConstrainedMethods();
		assertEquals( methodDescriptors.size(), 5, "Wrong number of descriptors" );

		Set<String> actualMethodNames = new HashSet<String>();
		for ( MethodDescriptor methodDescriptor : methodDescriptors ) {
			actualMethodNames.add( methodDescriptor.getName() );
		}

		assertEquals(
				actualMethodNames,
				asSet(
						"createCustomer",
						"reset",
						"removeCustomer",
						"findCustomer",
						"updateAccount"
				),
				"Wrong methods"
		);
	}

	@Test
	@SpecAssertion(section = "6.3", id = "e")
	public void testGetConstrainedMethodsForUnconstrainedEntity() {
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( UnconstraintEntity.class );
		Set<MethodDescriptor> methodDescriptors = beanDescriptor.getConstrainedMethods();
		assertEquals( methodDescriptors.size(), 0, "We should get the empty set." );
	}

	@Test
	@SpecAssertion(section = "6.3", id = "f")
	public void testGetConstraintsForParameterConstrainedConstructor() {
		ConstructorDescriptor constructorDescriptor = Executables.parameterConstrainedConstructor();
		assertNotNull( constructorDescriptor, "Descriptor should not be null" );
	}

	@Test
	@SpecAssertion(section = "6.3", id = "f")
	public void testGetConstraintsForCrossParameterConstrainedConstructor() {
		ConstructorDescriptor constructorDescriptor = Executables.crossParameterConstrainedConstructor();
		assertNotNull( constructorDescriptor, "Descriptor should not be null" );
	}

	@Test
	@SpecAssertion(section = "6.3", id = "f")
	public void testGetConstraintsForCascadedParameterConstructor() {
		ConstructorDescriptor constructorDescriptor = Executables.cascadedParameterConstructor();
		assertNotNull( constructorDescriptor, "Descriptor should not be null" );
	}

	@Test
	@SpecAssertion(section = "6.3", id = "f")
	public void testGetConstraintsForReturnValueConstrainedConstructor() {
		ConstructorDescriptor constructorDescriptor = Executables.returnValueConstrainedConstructor();
		assertNotNull( constructorDescriptor, "Descriptor should not be null" );
	}

	@Test
	@SpecAssertion(section = "6.3", id = "f")
	public void testGetConstraintsForCascadedReturnValueConstructor() {
		ConstructorDescriptor constructorDescriptor = Executables.cascadedReturnValueConstructor();
		assertNotNull( constructorDescriptor, "Descriptor should not be null" );
	}

	@Test
	@SpecAssertion(section = "6.3", id = "f")
	public void testGetConstraintsForUnconstrainedConstructor() {
		ConstructorDescriptor constructorDescriptor = Executables.unconstrainedConstructor();
		assertNull( constructorDescriptor, "Descriptor should be null" );
	}

	@Test
	@SpecAssertion(section = "6.3", id = "f")
	public void testGetConstraintsForNonExistingConstructorConstructor() {
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( CustomerService.class );
		ConstructorDescriptor constructorDescriptor = beanDescriptor.getConstraintsForConstructor(
				Short.class
		);
		assertNull( constructorDescriptor, "Descriptor should be null" );
	}

	@Test
	@SpecAssertion(section = "6.3", id = "g")
	public void testGetConstrainedConstructors() {
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( CustomerService.class );
		Set<ConstructorDescriptor> constructorDescriptors = beanDescriptor.getConstrainedConstructors();
		assertEquals( constructorDescriptors.size(), 5, "Wrong number of descriptors" );

		Set<List<Class<?>>> actualParameterTypes = getParameterTypes( constructorDescriptors );

		@SuppressWarnings("unchecked")
		Set<List<Class<?>>> expectedParameterTypes = asSet(
				Collections.<Class<?>>emptyList(),
				Arrays.<Class<?>>asList( String.class, String.class ),
				Arrays.<Class<?>>asList( Customer.class ),
				Arrays.<Class<?>>asList( Account.class ),
				Arrays.<Class<?>>asList( long.class )
		);
		assertEquals( actualParameterTypes, expectedParameterTypes, "Wrong constructors" );
	}

	@Test
	@SpecAssertion(section = "6.3", id = "g")
	public void testGetConstrainedConstructorsForUnconstrainedEntity() {
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( UnconstraintEntity.class );
		Set<ConstructorDescriptor> constructorDescriptors = beanDescriptor.getConstrainedConstructors();
		assertEquals( constructorDescriptors.size(), 0, "We should get the empty set." );
	}

	@Test
	@SpecAssertion(section = "6.2", id = "d")
	public void testGetKind() {
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( Order.class );
		assertEquals( beanDescriptor.getKind(), Kind.BEAN, "Descriptor should be of kind BEAN" );
	}

	@Test
	@SpecAssertion(section = "6.2", id = "e")
	public void testAs() {
		ElementDescriptor elementDescriptor = validator.getConstraintsForClass( Order.class );
		BeanDescriptor beanDescriptor = elementDescriptor.as( BeanDescriptor.class );
		assertNotNull( beanDescriptor, "Descriptor should not be null" );
		assertSame( beanDescriptor, elementDescriptor, "as() should return the same object" );
	}

	@Test(expectedExceptions = ClassCastException.class)
	@SpecAssertion(section = "6.2", id = "e")
	public void testAsWithWrongType() {
		ElementDescriptor elementDescriptor = validator.getConstraintsForClass( Order.class );
		elementDescriptor.as( PropertyDescriptor.class );
	}

	private Set<List<Class<?>>> getParameterTypes(Set<ConstructorDescriptor> constructorDescriptors) {
		Set<List<Class<?>>> parameterTypes = new HashSet<List<Class<?>>>();

		for ( ConstructorDescriptor constructorDescriptor : constructorDescriptors ) {
			List<Class<?>> types = new ArrayList<Class<?>>();
			for ( ParameterDescriptor parameterDescriptor : constructorDescriptor.getParameterDescriptors() ) {
				types.add( parameterDescriptor.getElementClass() );
			}
			parameterTypes.add( types );
		}

		return parameterTypes;
	}
}
