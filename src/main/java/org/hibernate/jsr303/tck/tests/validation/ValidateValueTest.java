// $Id$
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
package org.hibernate.jsr303.tck.tests.validation;

import java.util.ArrayList;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.testharness.AbstractTest;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ArtifactType;
import org.jboss.testharness.impl.packaging.Classes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;
import org.testng.annotations.Test;

import org.hibernate.jsr303.tck.util.TestUtil;
import static org.hibernate.jsr303.tck.util.TestUtil.assertConstraintViolation;
import static org.hibernate.jsr303.tck.util.TestUtil.assertCorrectConstraintViolationMessages;
import static org.hibernate.jsr303.tck.util.TestUtil.assertCorrectNumberOfViolations;
import static org.hibernate.jsr303.tck.util.TestUtil.assertCorrectPropertyPaths;

/**
 * Tests for the implementation of <code>Validator</code>.
 *
 * @author Hardy Ferentschik
 */
@Artifact(artifactType = ArtifactType.JSR303)
@Classes({ TestUtil.class, TestUtil.PathImpl.class, TestUtil.NodeImpl.class })
public class ValidateValueTest extends AbstractTest {

	@Test
	@SpecAssertion(section = "4.1.1", id = "h")
	public void testValidateValueSuccess() {
		Validator validator = TestUtil.getValidatorUnderTest();

		Set<ConstraintViolation<Address>> constraintViolations = validator.validateValue(
				Address.class, "city", "Paris"
		);
		assertCorrectNumberOfViolations( constraintViolations, 0 );
	}

	@Test
	@SpecAssertion(section = "4.1.1", id = "h")
	public void testValidateValueFailure() {
		Validator validator = TestUtil.getValidatorUnderTest();

		Set<ConstraintViolation<Address>> constraintViolations = validator.validateValue(
				Address.class, "city", null
		);
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintViolationMessages( constraintViolations, "You have to specify a city." );
	}

	@Test
	@SpecAssertion(section = "4.1.1", id = "h")
	public void testValidateValue() {
		Validator validator = TestUtil.getValidatorUnderTest();

		Set<ConstraintViolation<Order>> constraintViolations = validator.validateValue(
				Order.class, "orderNumber", null
		);
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectPropertyPaths( constraintViolations, "orderNumber" );
		assertCorrectConstraintViolationMessages( constraintViolations, "An order must have an order number." );

		ConstraintViolation<Order> constraintViolation = constraintViolations.iterator().next();
		assertConstraintViolation( constraintViolation, Order.class, null, "orderNumber" );
		assertEquals( constraintViolation.getRootBeanClass(), Order.class, "Wrong root bean class" );

		constraintViolations = validator.validateValue( Order.class, "orderNumber", 1234 );
		assertCorrectNumberOfViolations( constraintViolations, 0 );
	}

	@Test
	@SpecAssertion(section = "4.1.1", id = "i")
	public void testValidateValueWithInvalidPropertyPath() {
		Validator validator = TestUtil.getValidatorUnderTest();

		try {
			validator.validateValue( Customer.class, "foobar", null );
			fail();
		}
		catch ( IllegalArgumentException e ) {
			// success
		}

		// firstname exists, but the capitalisation is wrong
		try {
			validator.validateValue( Customer.class, "FirstName", null );
			fail();
		}
		catch ( IllegalArgumentException e ) {
			// success
		}
	}

	@Test
	@SpecAssertion(section = "4.1.1", id = "i")
	public void testExistingPropertyWoConstraintsNorCascaded() {
		Validator validator = TestUtil.getValidatorUnderTest();
		validator.validateValue( Customer.class, "middleName", new ArrayList<String>() );
	}

	@Test
	@SuppressWarnings("NullArgumentToVariableArgMethod")
	@SpecAssertion(section = "4.1.1", id = "i")
	public void testValidateValuePassingNullAsGroup() {
		Validator validator = TestUtil.getValidatorUnderTest();

		try {
			validator.validateValue( Customer.class, "firstName", "foobar", null );
			fail();
		}
		catch ( IllegalArgumentException e ) {
			// success
		}
	}

	@Test
	@SpecAssertion(section = "4.1.1", id = "i")
	public void testValidateValueWithEmptyPropertyPath() {
		Validator validator = TestUtil.getValidatorUnderTest();

		try {
			validator.validateValue( Customer.class, "", null );
			fail();
		}
		catch ( IllegalArgumentException e ) {
			// success
		}
	}

	@Test
	@SpecAssertion(section = "4.1.1", id = "i")
	public void testValidateValueWithNullObject() {
		Validator validator = TestUtil.getValidatorUnderTest();
		try {
			validator.validateValue( null, "firstName", "foobar" );
			fail();
		}
		catch ( IllegalArgumentException e ) {
			// success
		}
	}

	@Test
	@SpecAssertion(section = "4.1.1", id = "i")
	public void testValidateValueWithNullPropertyName() {
		Validator validator = TestUtil.getValidatorUnderTest();
		try {
			validator.validateValue( Customer.class, null, "foobar" );
			fail();
		}
		catch ( IllegalArgumentException e ) {
			// success
		}
	}

	@Test
	@SpecAssertion(section = "4.1.1", id = "j")
	public void testValidIsNotHonoredValidateValue() {
		Validator validator = TestUtil.getValidatorUnderTest();

		Customer customer = new Customer();
		Order order = new Order();
		customer.addOrder( order );

		Set<ConstraintViolation<Customer>> constraintViolations = validator.validateValue(
				Customer.class, "orders", order
		);
		assertCorrectNumberOfViolations( constraintViolations, 0 );
	}
}
