// $Id$
/*
* JBoss, Home of Professional Open Source
* Copyright 2008, Red Hat Middleware LLC, and individual contributors
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
package org.hibernate.jsr303.tck.tests.metadata;

import java.util.Set;
import javax.validation.Validator;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.PropertyDescriptor;

import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.testharness.AbstractTest;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ArtifactType;
import org.jboss.testharness.impl.packaging.Classes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.Test;

import org.hibernate.jsr303.tck.util.TestUtil;


/**
 * @author Hardy Ferentschik
 */
@Artifact(artifactType = ArtifactType.JSR303)
@Classes({ TestUtil.class, TestUtil.PathImpl.class, TestUtil.NodeImpl.class })
public class BeanDescriptorTest extends AbstractTest {

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.1", id = "b"),
			@SpecAssertion(section = "5.3", id = "a")
	})
	public void testIsBeanConstrainedDueToValidAnnotation() {
		Validator validator = TestUtil.getValidatorUnderTest();
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( Customer.class );

		// constraint via @Valid
		assertFalse( beanDescriptor.hasConstraints(), "There should be no direct constraints on the specified bean." );
		assertTrue( beanDescriptor.isBeanConstrained(), "Bean should be constrained due to @valid " );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.1", id = "b"),
			@SpecAssertion(section = "5.3", id = "a")
	})
	public void testIsBeanConstrainedDueToConstraintOnEntity() {
		Validator validator = TestUtil.getValidatorUnderTest();

		// constraint hosted on bean itself
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( Account.class );
		assertTrue( beanDescriptor.hasConstraints(), "There should be direct constraints on the specified bean." );
		assertTrue( beanDescriptor.isBeanConstrained(), "Bean should be constrained due to @valid" );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.1", id = "b"),
			@SpecAssertion(section = "5.3", id = "a")
	})
	public void testIsBeanConstrainedDueToConstraintProperty() {
		Validator validator = TestUtil.getValidatorUnderTest();

		// constraint on bean property
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( Order.class );
		assertFalse( beanDescriptor.hasConstraints(), "There should be no direct constraints on the specified bean." );
		assertTrue( beanDescriptor.isBeanConstrained(), "Bean should be constrained due to @NotNull" );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.1", id = "b"),
			@SpecAssertion(section = "5.3", id = "a")
	})
	public void testIsBeanConstrainedDueToConstraintOnInterface() {
		Validator validator = TestUtil.getValidatorUnderTest();

		// constraint on implemented interface
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( Man.class );
		assertFalse( beanDescriptor.hasConstraints(), "There should be no direct constraints on the specified bean." );
		assertTrue( beanDescriptor.isBeanConstrained(), "Bean should be constrained due to constraints on Person." );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.1", id = "b"),
			@SpecAssertion(section = "5.3", id = "a")
	})
	public void testUnconstrainedClass() {
		Validator validator = TestUtil.getValidatorUnderTest();
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( UnconstraintEntity.class );
		assertFalse( beanDescriptor.hasConstraints(), "There should be no direct constraints on the specified bean." );
		assertFalse( beanDescriptor.isBeanConstrained(), "Bean should be unconstrained." );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.1", id = "b"),
			@SpecAssertion(section = "5.3", id = "b")
	})
	public void testGetConstraintForConstrainedProperty() {
		Validator validator = TestUtil.getValidatorUnderTest();
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( Order.class );
		PropertyDescriptor propertyDescriptor = beanDescriptor.getConstraintsForProperty( "orderNumber" );
		assertEquals(
				propertyDescriptor.getConstraintDescriptors().size(), 1, "There should be one constraint descriptor"
		);
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.3", id = "b"),
			@SpecAssertion(section = "5.3", id = "b"),
			@SpecAssertion(section = "5.4", id = "a")
	})
	public void testGetConstraintForUnConstrainedProperty() {
		Validator validator = TestUtil.getValidatorUnderTest();
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( Customer.class );
		PropertyDescriptor propertyDescriptor = beanDescriptor.getConstraintsForProperty( "orderList" );
		assertEquals(
				propertyDescriptor.getConstraintDescriptors().size(), 0, "There should be no constraint descriptors"
		);
		assertTrue( propertyDescriptor.isCascaded(), "The property should be cascaded" );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.1", id = "b"),
			@SpecAssertion(section = "5.3", id = "b")
	})
	public void testGetConstraintsForNonExistingProperty() {
		Validator validator = TestUtil.getValidatorUnderTest();
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( Order.class );
		assertNull( beanDescriptor.getConstraintsForProperty( "foobar" ), "There should be no descriptor" );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.1", id = "b"),
			@SpecAssertion(section = "5.3", id = "b")
	})
	public void testGetConstrainedProperties() {
		Validator validator = TestUtil.getValidatorUnderTest();
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
			@SpecAssertion(section = "5.1", id = "b"),
			@SpecAssertion(section = "5.3", id = "b")
	})
	public void testGetConstrainedPropertiesForUnconstrainedEntity() {
		Validator validator = TestUtil.getValidatorUnderTest();
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( UnconstraintEntity.class );
		Set<PropertyDescriptor> constraintProperties = beanDescriptor.getConstrainedProperties();
		assertEquals( constraintProperties.size(), 0, "We should get the empty set." );
	}


	@Test(expectedExceptions = IllegalArgumentException.class)
	@SpecAssertion(section = "5.3", id = "c")
	public void testGetConstraintsForNullProperty() {
		Validator validator = TestUtil.getValidatorUnderTest();
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( Order.class );
		beanDescriptor.getConstraintsForProperty( null );
	}
}
