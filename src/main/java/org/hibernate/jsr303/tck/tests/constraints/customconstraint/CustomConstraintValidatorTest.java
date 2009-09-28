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
package org.hibernate.jsr303.tck.tests.constraints.customconstraint;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.UnexpectedTypeException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.metadata.PropertyDescriptor;

import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.testharness.AbstractTest;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ArtifactType;
import org.jboss.testharness.impl.packaging.Classes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.Test;

import org.hibernate.jsr303.tck.util.TestUtil;
import static org.hibernate.jsr303.tck.util.TestUtil.assertCorrectNumberOfViolations;
import static org.hibernate.jsr303.tck.util.TestUtil.assertCorrectPropertyPaths;

/**
 * @author Emmanuel Bernard
 * @author Hardy Ferentschik
 */
@Artifact(artifactType = ArtifactType.JSR303)
@Classes({ TestUtil.class, TestUtil.PathImpl.class, TestUtil.NodeImpl.class })
public class CustomConstraintValidatorTest extends AbstractTest {

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "2.4", id = "a"),
			@SpecAssertion(section = "2.4", id = "b"),
			@SpecAssertion(section = "2.4", id = "e")
	})
	public void testRightValidatorIsSelectedAndInitializedCalled() {
		Validator validator = TestUtil.getValidatorUnderTest();
		Shoe shoe = new Shoe();
		shoe.size = -2;
		final PropertyDescriptor propertyDescriptor = validator.getConstraintsForClass( Shoe.class )
				.getConstraintsForProperty( "size" );
		assertNotNull( propertyDescriptor );

		BoundariesConstraintValidator.isValidCalls = 0;
		final Set<ConstraintViolation<Shoe>> constraintViolations = validator.validate( shoe );
		assertEquals( 1, constraintViolations.size() );
		assertTrue(
				BoundariesConstraintValidator.isValidCalls >= 1,
				"Ensure the right validator implementation class was picked."
		);
		assertTrue(
				BoundariesConstraintValidator.initializeCalled,
				"Check initialize was called. Note this is not really ensuring that it was called before isValid. That is done in the actual implementation of the validator."
		);
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "2.4", id = "a"),
			@SpecAssertion(section = "2.4", id = "b"),
			@SpecAssertion(section = "2.4", id = "f")
	})
	public void testIsValidIsCalledForEachValidation() {
		Validator validator = TestUtil.getValidatorUnderTest();
		Shoe shoe = new Shoe();
		shoe.size = -2;
		int nbrOfValidCalls = 0;
		BoundariesConstraintValidator.isValidCalls = 0;
		validator.validate( shoe );
		assertTrue(
				BoundariesConstraintValidator.isValidCalls > nbrOfValidCalls,
				"Ensure is valid has been called."
		);
		nbrOfValidCalls = BoundariesConstraintValidator.isValidCalls;

		validator.validate( shoe );
		assertTrue(
				BoundariesConstraintValidator.isValidCalls > nbrOfValidCalls,
				"Ensure is valid has been called."
		);
		nbrOfValidCalls = BoundariesConstraintValidator.isValidCalls;

		validator.validateProperty( shoe, "size" );
		assertTrue(
				BoundariesConstraintValidator.isValidCalls > nbrOfValidCalls,
				"Ensure is valid has been called."
		);
		nbrOfValidCalls = BoundariesConstraintValidator.isValidCalls;

		validator.validateValue( Shoe.class, "size", 41 );
		assertTrue(
				BoundariesConstraintValidator.isValidCalls > nbrOfValidCalls,
				"Ensure is valid has been called."
		);
	}


	@SpecAssertion(section = "2.4", id = "i")
	@Test(expectedExceptions = UnexpectedTypeException.class)
	public void testUnexpectedTypeExceptionIsRaisedForInvalidType() {
		Validator validator = TestUtil.getValidatorUnderTest();
		validator.validate( new OddShoe() );
	}

	@SpecAssertion(section = "2.4", id = "j")
	@Test(expectedExceptions = ValidationException.class)
	public void testRuntimeExceptionFromIsValidIsWrapped() {
		Validator validator = TestUtil.getValidatorUnderTest();
		Shoe shoe = new Shoe();
		shoe.size = -2;
		BoundariesConstraintValidator.throwRuntimeExceptionFromIsValid = true;
		validator.validate( shoe );
	}

	@SpecAssertion(section = "2.4", id = "j")
	@Test(expectedExceptions = ValidationException.class)
	public void testRuntimeExceptionFromInitializeIsWrapped() {
		Validator validator = TestUtil.getValidatorUnderTest();
		validator.validate( new Freezer() );
	}

	@Test
	@SpecAssertion(section = "2.4", id = "l")
	public void testOneConstraintViolationPerFailingConstraint() {
		Validator validator = TestUtil.getValidatorUnderTest();

		Author author = new Author();
		author.setLastName( "" );
		author.setFirstName( "" );
		author.setCompany( "" );


		Set<ConstraintViolation<Author>> constraintViolations = validator.validate( author );
		assertCorrectNumberOfViolations( constraintViolations, 3 );

		author.setFirstName( "John" );
		constraintViolations = validator.validate( author );
		assertCorrectNumberOfViolations( constraintViolations, 2 );

		author.setLastName( "Doe" );
		constraintViolations = validator.validate( author );
		assertCorrectNumberOfViolations( constraintViolations, 1 );

		author.setCompany( "JBoss" );
		constraintViolations = validator.validate( author );
		assertCorrectNumberOfViolations( constraintViolations, 0 );
	}

	@Test
	@SpecAssertion(section = "2.4", id = "n")
	public void testNonInterpolatedMessageParameterIsUsed() {
		Validator validator = TestUtil.getValidatorUnderTest();

		Author author = new Author();
		author.setLastName( "John" );
		author.setFirstName( "Doe" );
		author.setCompany( "" );

		Set<ConstraintViolation<Author>> constraintViolations = validator.validate( author );
		assertCorrectNumberOfViolations( constraintViolations, 1 );

		ConstraintViolation<Author> constraintViolation = constraintViolations.iterator().next();
		assertEquals( constraintViolation.getMessage(), "The company name must be a minimum 3 characters" );
		assertEquals( constraintViolation.getMessageTemplate(), "The company name must be a minimum {min} characters" );
		assertTrue( !constraintViolation.getMessageTemplate().equals( constraintViolation.getMessage() ) );
	}

	@Test
	@SpecAssertion(section = "2.4", id = "o")
	public void testDefaultPropertyPath() {
		Validator validator = TestUtil.getValidatorUnderTest();

		Author author = new Author();
		author.setLastName( "John" );
		author.setFirstName( "Doe" );
		author.setCompany( "" );

		Set<ConstraintViolation<Author>> constraintViolations = validator.validate( author );
		assertCorrectNumberOfViolations( constraintViolations, 1 );

		assertCorrectPropertyPaths( constraintViolations, "company" );
	}


	public static class Shoe {
		@Positive
		public int size;
	}

	public static class OddShoe {
		@Positive
		public String size;
	}

	public static class Freezer {
		@Negative
		public int temperature;
	}
}