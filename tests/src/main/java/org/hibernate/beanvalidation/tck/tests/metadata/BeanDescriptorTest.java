/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.metadata;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.validation.metadata.BeanDescriptor;
import jakarta.validation.metadata.ConstructorDescriptor;
import jakarta.validation.metadata.MethodDescriptor;
import jakarta.validation.metadata.MethodType;
import jakarta.validation.metadata.ParameterDescriptor;
import jakarta.validation.metadata.PropertyDescriptor;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
public class BeanDescriptorTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( BeanDescriptorTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "a")
	public void testGetElementClassReturnsBeanClass() {
		BeanDescriptor beanDescriptor = getValidator().getConstraintsForClass( Customer.class );
		assertEquals( beanDescriptor.getElementClass(), Customer.class, "Wrong element class" );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_VALIDATOR, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_BEANDESCRIPTOR, id = "a")
	public void testIsBeanConstrainedDueToValidAnnotation() {
		BeanDescriptor beanDescriptor = getValidator().getConstraintsForClass( Customer.class );

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
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_VALIDATOR, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_BEANDESCRIPTOR, id = "a")
	public void testIsBeanConstrainedDueToConstraintOnEntity() {
		// constraint hosted on bean itself
		BeanDescriptor beanDescriptor = getValidator().getConstraintsForClass( Account.class );
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
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_VALIDATOR, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_BEANDESCRIPTOR, id = "a")
	public void testIsBeanConstrainedDueToConstraintProperty() {
		// constraint on bean property
		BeanDescriptor beanDescriptor = getValidator().getConstraintsForClass( Order.class );
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
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_VALIDATOR, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_BEANDESCRIPTOR, id = "a")
	public void testIsBeanConstrainedDueToConstraintOnInterface() {
		// constraint on implemented interface
		BeanDescriptor beanDescriptor = getValidator().getConstraintsForClass( Man.class );
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
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_VALIDATOR, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_BEANDESCRIPTOR, id = "a")
	public void testUnconstrainedClass() {
		BeanDescriptor beanDescriptor = getValidator().getConstraintsForClass( UnconstrainedEntity.class );
		assertFalse(
				beanDescriptor.hasConstraints(),
				"There should be no direct constraints on the specified bean."
		);
		assertFalse( beanDescriptor.isBeanConstrained(), "Bean should be unconstrained." );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_VALIDATOR, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_BEANDESCRIPTOR, id = "b")
	public void testGetConstraintsForConstrainedProperty() {
		BeanDescriptor beanDescriptor = getValidator().getConstraintsForClass( Order.class );
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
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_BEANDESCRIPTOR, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CASCADABLEDESCRIPTOR, id = "a")
	public void testGetConstraintsForUnConstrainedProperty() {
		BeanDescriptor beanDescriptor = getValidator().getConstraintsForClass( Customer.class );
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
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_VALIDATOR, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_BEANDESCRIPTOR, id = "b")
	public void testGetConstraintsForNonExistingProperty() {
		BeanDescriptor beanDescriptor = getValidator().getConstraintsForClass( Order.class );
		assertNull(
				beanDescriptor.getConstraintsForProperty( "foobar" ),
				"There should be no descriptor"
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_VALIDATOR, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_BEANDESCRIPTOR, id = "d")
	public void testGetConstrainedProperties() {
		BeanDescriptor beanDescriptor = getValidator().getConstraintsForClass( Order.class );
		Set<PropertyDescriptor> constraintProperties = beanDescriptor.getConstrainedProperties();
		assertEquals( constraintProperties.size(), 1, "There should be only one property" );
		boolean hasOrderNumber = false;
		for ( PropertyDescriptor pd : constraintProperties ) {
			hasOrderNumber |= pd.getPropertyName().equals( "orderNumber" );
		}
		assertTrue( hasOrderNumber, "Wrong property" );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_VALIDATOR, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_BEANDESCRIPTOR, id = "d")
	public void testGetConstrainedPropertiesForUnconstrainedEntity() {
		BeanDescriptor beanDescriptor = getValidator().getConstraintsForClass( UnconstrainedEntity.class );
		Set<PropertyDescriptor> constraintProperties = beanDescriptor.getConstrainedProperties();
		assertEquals( constraintProperties.size(), 0, "We should get the empty set." );
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_BEANDESCRIPTOR, id = "c")
	public void testGetConstraintsForNullProperty() {
		BeanDescriptor beanDescriptor = getValidator().getConstraintsForClass( Order.class );
		beanDescriptor.getConstraintsForProperty( null );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_BEANDESCRIPTOR, id = "e")
	public void testGetConstraintsForParameterConstrainedMethod() {
		MethodDescriptor methodDescriptor = Executables.parameterConstrainedMethod();
		assertNotNull( methodDescriptor, "Descriptor should not be null" );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_BEANDESCRIPTOR, id = "e")
	public void testGetConstraintsForCrossParameterConstrainedMethod() {
		MethodDescriptor methodDescriptor = Executables.crossParameterConstrainedMethod();
		assertNotNull( methodDescriptor, "Descriptor should not be null" );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_BEANDESCRIPTOR, id = "e")
	public void testGetConstraintsForCascadedParameterMethod() {
		MethodDescriptor methodDescriptor = Executables.cascadedParameterMethod();
		assertNotNull( methodDescriptor, "Descriptor should not be null" );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_BEANDESCRIPTOR, id = "e")
	public void testGetConstraintsForReturnValueConstrainedMethod() {
		MethodDescriptor methodDescriptor = Executables.returnValueConstrainedMethod();
		assertNotNull( methodDescriptor, "Descriptor should not be null" );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_BEANDESCRIPTOR, id = "e")
	public void testGetConstraintsForCascadedReturnValueMethod() {
		MethodDescriptor methodDescriptor = Executables.cascadedReturnValueMethod();
		assertNotNull( methodDescriptor, "Descriptor should not be null" );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_BEANDESCRIPTOR, id = "e")
	public void testGetConstraintsForUnconstrainedMethod() {
		MethodDescriptor methodDescriptor = Executables.unconstrainedMethod();
		assertNull( methodDescriptor, "Descriptor should be null" );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_BEANDESCRIPTOR, id = "e")
	public void testGetConstraintsForNonExistingMethod() {
		BeanDescriptor beanDescriptor = getValidator().getConstraintsForClass( CustomerService.class );
		MethodDescriptor methodDescriptor = beanDescriptor.getConstraintsForMethod( "foo" );
		assertNull( methodDescriptor, "Descriptor should be null" );
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_BEANDESCRIPTOR, id = "e")
	public void testGetConstraintsForNullMethod() {
		BeanDescriptor beanDescriptor = getValidator().getConstraintsForClass( CustomerService.class );
		beanDescriptor.getConstraintsForMethod( null );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_BEANDESCRIPTOR, id = "f")
	public void testGetConstrainedMethodsTypeNON_GETTER() {
		BeanDescriptor beanDescriptor = getValidator().getConstraintsForClass( CustomerService.class );
		Set<MethodDescriptor> methodDescriptors = beanDescriptor.getConstrainedMethods( MethodType.NON_GETTER );

		assertThat( methodDescriptors ).extracting( MethodDescriptor::getName ).containsExactlyInAnyOrder(
				"createCustomer",
				"reset",
				"removeCustomer",
				"findCustomer",
				"findCustomer",
				"updateAccount",
				"updateAccountStrictly",
				"updateCustomer",
				"createOrder",
				"getOrderContent"
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_BEANDESCRIPTOR, id = "f")
	public void testGetConstrainedMethodsTypeGETTER() {
		BeanDescriptor beanDescriptor = getValidator().getConstraintsForClass( CustomerService.class );
		Set<MethodDescriptor> methodDescriptors = beanDescriptor.getConstrainedMethods( MethodType.GETTER );

		assertEquals( methodDescriptors.size(), 1 );
		assertEquals( methodDescriptors.iterator().next().getName(), "getBestCustomer" );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_BEANDESCRIPTOR, id = "f")
	public void testGetConstrainedMethodsTypesGETTERAndNON_GETTER() {
		BeanDescriptor beanDescriptor = getValidator().getConstraintsForClass( CustomerService.class );
		Set<MethodDescriptor> methodDescriptors = beanDescriptor.getConstrainedMethods(
				MethodType.GETTER,
				MethodType.NON_GETTER
		);

		assertThat( methodDescriptors ).extracting( MethodDescriptor::getName ).containsExactlyInAnyOrder(
				"createCustomer",
				"reset",
				"removeCustomer",
				"findCustomer",
				"findCustomer",
				"updateAccount",
				"updateAccountStrictly",
				"updateCustomer",
				"createOrder",
				"getOrderContent",
				"getBestCustomer"
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_BEANDESCRIPTOR, id = "f")
	public void testGetConstrainedMethodsForUnconstrainedEntity() {
		BeanDescriptor beanDescriptor = getValidator().getConstraintsForClass( UnconstrainedEntity.class );
		Set<MethodDescriptor> methodDescriptors = beanDescriptor.getConstrainedMethods(
				MethodType.GETTER,
				MethodType.NON_GETTER
		);
		assertEquals( methodDescriptors.size(), 0, "We should get the empty set." );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_BEANDESCRIPTOR, id = "g")
	public void testGetConstraintsForParameterConstrainedConstructor() {
		ConstructorDescriptor constructorDescriptor = Executables.parameterConstrainedConstructor();
		assertNotNull( constructorDescriptor, "Descriptor should not be null" );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_BEANDESCRIPTOR, id = "g")
	public void testGetConstraintsForCrossParameterConstrainedConstructor() {
		ConstructorDescriptor constructorDescriptor = Executables.crossParameterConstrainedConstructor();
		assertNotNull( constructorDescriptor, "Descriptor should not be null" );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_BEANDESCRIPTOR, id = "g")
	public void testGetConstraintsForCascadedParameterConstructor() {
		ConstructorDescriptor constructorDescriptor = Executables.cascadedParameterConstructor();
		assertNotNull( constructorDescriptor, "Descriptor should not be null" );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_BEANDESCRIPTOR, id = "g")
	public void testGetConstraintsForReturnValueConstrainedConstructor() {
		ConstructorDescriptor constructorDescriptor = Executables.returnValueConstrainedConstructor();
		assertNotNull( constructorDescriptor, "Descriptor should not be null" );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_BEANDESCRIPTOR, id = "g")
	public void testGetConstraintsForCascadedReturnValueConstructor() {
		ConstructorDescriptor constructorDescriptor = Executables.cascadedReturnValueConstructor();
		assertNotNull( constructorDescriptor, "Descriptor should not be null" );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_BEANDESCRIPTOR, id = "g")
	public void testGetConstraintsForUnconstrainedConstructor() {
		ConstructorDescriptor constructorDescriptor = Executables.unconstrainedConstructor();
		assertNull( constructorDescriptor, "Descriptor should be null" );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_BEANDESCRIPTOR, id = "g")
	public void testGetConstraintsForNonExistingConstructorConstructor() {
		BeanDescriptor beanDescriptor = getValidator().getConstraintsForClass( CustomerService.class );
		ConstructorDescriptor constructorDescriptor = beanDescriptor.getConstraintsForConstructor(
				Short.class
		);
		assertNull( constructorDescriptor, "Descriptor should be null" );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_BEANDESCRIPTOR, id = "h")
	public void testGetConstrainedConstructors() {
		BeanDescriptor beanDescriptor = getValidator().getConstraintsForClass( CustomerService.class );
		Set<ConstructorDescriptor> constructorDescriptors = beanDescriptor.getConstrainedConstructors();

		Set<List<Class<?>>> actualParameterTypes = getParameterTypes( constructorDescriptors );

		assertThat( actualParameterTypes ).describedAs( "Wrong constructors" ).containsExactlyInAnyOrder(
				Collections.<Class<?>>emptyList(),
				Arrays.<Class<?>>asList( String.class, String.class ),
				Arrays.<Class<?>>asList( Customer.class ),
				Arrays.<Class<?>>asList( Account.class ),
				Arrays.<Class<?>>asList( int.class, Account.class ),
				Arrays.<Class<?>>asList( long.class ),
				Arrays.<Class<?>>asList( long.class, int.class ),
				Arrays.<Class<?>>asList( Map.class )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_BEANDESCRIPTOR, id = "h")
	public void testGetConstrainedConstructorsForUnconstrainedEntity() {
		BeanDescriptor beanDescriptor = getValidator().getConstraintsForClass( UnconstrainedEntity.class );
		Set<ConstructorDescriptor> constructorDescriptors = beanDescriptor.getConstrainedConstructors();
		assertEquals( constructorDescriptors.size(), 0, "We should get the empty set." );
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
