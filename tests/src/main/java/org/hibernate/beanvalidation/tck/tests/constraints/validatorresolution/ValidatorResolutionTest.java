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
package org.hibernate.beanvalidation.tck.tests.constraints.validatorresolution;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.UnexpectedTypeException;
import javax.validation.Validator;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

import static org.hibernate.beanvalidation.tck.util.TestUtil.assertConstraintViolation;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectNumberOfViolations;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectPropertyPaths;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

/**
 * Tests for constraint validator resolution.
 *
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "1.1.0")
public class ValidatorResolutionTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClassPackage( ValidatorResolutionTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = "4.6.4", id = "a")
	public void testTargetTypeIsInterface() {
		Validator validator = TestUtil.getValidatorUnderTest();


		assertEquals(
				CustomConstraint.ValidatorForCustomInterface.callCounter,
				0,
				"The validate method of ValidatorForCustomInterface should not have been called yet."
		);

		validator.validate( new CustomInterfaceImpl() );

		assertTrue(
				CustomConstraint.ValidatorForCustomInterface.callCounter > 0,
				"The validate method of ValidatorForCustomInterface should have been called."
		);
	}

	@Test
	@SpecAssertion(section = "4.6.4", id = "a")
	public void testTargetTypeIsClass() {
		Validator validator = TestUtil.getValidatorUnderTest();


		assertEquals(
				CustomConstraint.ValidatorForCustomClass.callCounter,
				0,
				"The validate method of ValidatorForCustomClass should not have been called yet."
		);

		validator.validate( new CustomClass() );

		assertTrue(
				CustomConstraint.ValidatorForCustomClass.callCounter > 0,
				"The validate method of ValidatorForCustomClass should have been called."
		);
	}


	@Test
	@SpecAssertion(section = "4.6.4", id = "b")
	public void testTargetedTypeIsField() {
		Validator validator = TestUtil.getValidatorUnderTest();

		assertEquals(
				CustomConstraint.ValidatorForSubClassA.callCounter,
				0,
				"The validate method of ValidatorForSubClassA should not have been called yet."
		);

		validator.validate( new SubClassAHolder( new SubClassA() ) );

		assertTrue(
				CustomConstraint.ValidatorForSubClassA.callCounter > 0,
				"The validate method of ValidatorForSubClassA should have been called."
		);
	}

	@Test
	@SpecAssertion(section = "4.6.4", id = "c")
	public void testTargetedTypeIsGetter() {
		Validator validator = TestUtil.getValidatorUnderTest();

		assertEquals(
				CustomConstraint.ValidatorForSubClassB.callCounter,
				0,
				"The validate method of ValidatorForSubClassB should not have been called yet."
		);

		validator.validate( new SubClassBHolder( new SubClassB() ) );

		assertTrue(
				CustomConstraint.ValidatorForSubClassB.callCounter > 0,
				"The validate method of ValidatorForSubClassB should have been called."
		);
	}


	@Test
	@SpecAssertions({
			@SpecAssertion(section = "4.6.4", id = "d")
	})
	public void testResolutionOfMultipleSizeValidators() {
		Validator validator = TestUtil.getValidatorUnderTest();

		Suburb suburb = new Suburb();

		// all values are null and should pass
		Set<ConstraintViolation<Suburb>> constraintViolations = validator.validate( suburb );
		assertCorrectNumberOfViolations( constraintViolations, 0 );

		suburb.setName( "" );
		constraintViolations = validator.validate( suburb );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertConstraintViolation(
				constraintViolations.iterator().next(), Suburb.class, "", "name"
		);

		suburb.setName( "Hoegsbo" );
		constraintViolations = validator.validate( suburb );
		assertCorrectNumberOfViolations( constraintViolations, 0 );

		suburb.addFacility( Suburb.Facility.SHOPPING_MALL, false );
		constraintViolations = validator.validate( suburb );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertConstraintViolation(
				constraintViolations.iterator().next(),
				Suburb.class,
				suburb.getFacilities(),
				"facilities"
		);

		suburb.addFacility( Suburb.Facility.BUS_TERMINAL, true );
		constraintViolations = validator.validate( suburb );
		assertCorrectNumberOfViolations( constraintViolations, 0 );

		suburb.addStreetName( "Sikelsgatan" );
		constraintViolations = validator.validate( suburb );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertConstraintViolation(
				constraintViolations.iterator().next(),
				Suburb.class,
				suburb.getStreetNames(),
				"streetNames"
		);

		suburb.addStreetName( "Marklandsgatan" );
		constraintViolations = validator.validate( suburb );
		assertCorrectNumberOfViolations( constraintViolations, 0 );

		Coordinate[] boundingBox = new Coordinate[3];
		boundingBox[0] = new Coordinate( 0l, 0l );
		boundingBox[1] = new Coordinate( 0l, 1l );
		boundingBox[2] = new Coordinate( 1l, 0l );
		suburb.setBoundingBox( boundingBox );
		constraintViolations = validator.validate( suburb );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertConstraintViolation(
				constraintViolations.iterator().next(),
				Suburb.class,
				suburb.getBoundingBox(),
				"boundingBox"
		);

		boundingBox = new Coordinate[4];
		boundingBox[0] = new Coordinate( 0l, 0l );
		boundingBox[1] = new Coordinate( 0l, 1l );
		boundingBox[2] = new Coordinate( 1l, 0l );
		boundingBox[3] = new Coordinate( 1l, 1l );
		suburb.setBoundingBox( boundingBox );
		constraintViolations = validator.validate( suburb );
		assertCorrectNumberOfViolations( constraintViolations, 0 );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "4.6.4", id = "d")
	})
	public void testResolutionOfMinMaxForDifferentTypes() {
		Validator validator = TestUtil.getValidatorUnderTest();
		MinMax minMax = new MinMax( "5", 5 );
		Set<ConstraintViolation<MinMax>> constraintViolations = validator.validate( minMax );
		assertCorrectNumberOfViolations( constraintViolations, 2 );
		assertCorrectPropertyPaths( constraintViolations, "number", "numberAsString" );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "4.6.4", id = "e"),
			@SpecAssertion(section = "3.1", id = "c"),
			@SpecAssertion(section = "3.4", id = "i")
	})
	public void testUnexpectedTypeInValidatorResolution() {
		Validator validator = TestUtil.getValidatorUnderTest();

		Bar bar = new Bar();
		try {
			validator.validate( bar );
			fail();
		}
		catch ( UnexpectedTypeException e ) {
			// success
		}
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "4.6.4", id = "f"),
			@SpecAssertion(section = "9.3", id = "b")
	})
	public void testAmbiguousValidatorResolution() {
		Validator validator = TestUtil.getValidatorUnderTest();

		Foo foo = new Foo( new SerializableBarSubclass() );
		try {
			validator.validate( foo );
			fail( "The test should have failed due to ambiguous validator resolution." );
		}
		catch ( UnexpectedTypeException e ) {
			// success
		}
	}

	public class SubClassAHolder {
		@CustomConstraint
		private final SubClassA subClass;

		SubClassAHolder(SubClassA subClass) {
			this.subClass = subClass;
		}

		public BaseClass getSubClass() {
			return subClass;
		}
	}

	public class SubClassBHolder {
		private final BaseClass baseClass;

		public SubClassBHolder(SubClassB subClass) {
			this.baseClass = subClass;
		}

		@CustomConstraint
		public SubClassB getBaseClass() {
			return (SubClassB) baseClass;
		}
	}

	@CustomConstraint
	public class CustomClass {

	}

	@CustomConstraint
	public interface CustomInterface {
	}

	public class CustomInterfaceImpl implements CustomInterface {

	}
}
