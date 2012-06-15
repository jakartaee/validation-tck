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
package org.hibernate.jsr303.tck.tests.constraints.constraintcomposition;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintDefinitionException;
import javax.validation.ConstraintViolation;
import javax.validation.Payload;
import javax.validation.UnexpectedTypeException;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstraintDescriptor;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.testng.annotations.Test;

import org.hibernate.jsr303.tck.util.TestUtil;
import org.hibernate.jsr303.tck.util.shrinkwrap.WebArchiveBuilder;

import static org.hibernate.jsr303.tck.util.TestUtil.assertConstraintViolation;
import static org.hibernate.jsr303.tck.util.TestUtil.assertCorrectConstraintTypes;
import static org.hibernate.jsr303.tck.util.TestUtil.assertCorrectConstraintViolationMessages;
import static org.hibernate.jsr303.tck.util.TestUtil.assertCorrectNumberOfViolations;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

/**
 * Tests for composing constraints.
 *
 * @author Hardy Ferentschik
 */
public class ConstraintCompositionTest extends Arquillian {

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClassPackage( ConstraintCompositionTest.class )
				.build();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "2.3", id = "a"),
			@SpecAssertion(section = "2.3", id = "k")
	})
	public void testComposedConstraints() {

		Validator validator = TestUtil.getValidatorUnderTest();
		FrenchAddress address = getFrenchAddressWithoutZipCode();
		Set<ConstraintViolation<FrenchAddress>> constraintViolations = validator.validate( address );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		ConstraintViolation<FrenchAddress> constraintViolation = constraintViolations.iterator().next();
		assertCorrectConstraintTypes( constraintViolations, NotNull.class );
		assertCorrectConstraintViolationMessages( constraintViolations, "may not be null" );
		assertConstraintViolation(
				constraintViolation,
				FrenchAddress.class,
				null,
				"zipCode"
		);
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "2.3", id = "a"),
			@SpecAssertion(section = "2.3", id = "k")
	})
	public void testComposedConstraintsAreRecursive() {
		Validator validator = TestUtil.getValidatorUnderTest();

		GermanAddress address = new GermanAddress();
		address.setAddressline1( "Rathausstrasse 5" );
		address.setAddressline2( "3ter Stock" );
		address.setCity( "Karlsruhe" );
		Set<ConstraintViolation<GermanAddress>> constraintViolations = validator.validate( address );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertConstraintViolation(
				constraintViolations.iterator().next(),
				GermanAddress.class,
				null,
				"zipCode"
		);
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "2.3", id = "b"),
			@SpecAssertion(section = "2.4", id = "m")
	})
	public void testValidationOfMainAnnotationIsAlsoApplied() {

		Validator validator = TestUtil.getValidatorUnderTest();
		FrenchAddress address = getFrenchAddressWithoutZipCode();
		address.setZipCode( "00000" );
		Set<ConstraintViolation<FrenchAddress>> constraintViolations = validator.validate( address );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintTypes( constraintViolations, FrenchZipcode.class );
		assertCorrectConstraintViolationMessages( constraintViolations, "00000 is a reserved code" );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "2.3", id = "c"),
			@SpecAssertion(section = "2.3", id = "i"),
			@SpecAssertion(section = "2.3", id = "l"),
			@SpecAssertion(section = "2.3", id = "m"),
			@SpecAssertion(section = "2.3", id = "n")
	})
	public void testEachFailingConstraintCreatesConstraintViolation() {

		Validator validator = TestUtil.getValidatorUnderTest();
		FrenchAddress address = getFrenchAddressWithoutZipCode();
		address.setZipCode( "abc" );
		Set<ConstraintViolation<FrenchAddress>> constraintViolations = validator.validate( address );
		assertCorrectNumberOfViolations( constraintViolations, 3 );
		assertCorrectConstraintTypes( constraintViolations, Pattern.class, Pattern.class, Size.class );
		for ( ConstraintViolation<FrenchAddress> violation : constraintViolations ) {
			assertConstraintViolation(
					violation,
					FrenchAddress.class,
					"abc",
					"zipCode"
			);
		}

		address.setZipCode( "123" );
		constraintViolations = validator.validate( address );
		assertCorrectNumberOfViolations( constraintViolations, 2 );
		assertCorrectConstraintTypes( constraintViolations, Pattern.class, Size.class );
		for ( ConstraintViolation<FrenchAddress> violation : constraintViolations ) {
			assertConstraintViolation(
					violation,
					FrenchAddress.class,
					"123",
					"zipCode"
			);

		}

		address.setZipCode( "33023" );
		constraintViolations = validator.validate( address );
		assertCorrectNumberOfViolations( constraintViolations, 0 );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "2.3", id = "d"),
			@SpecAssertion(section = "2.3", id = "e")
	})
	public void testGroupsDefinedOnMainAnnotationAreInherited() {
		Validator validator = TestUtil.getValidatorUnderTest();

		FrenchAddress address = getFrenchAddressWithoutZipCode();
		Set<ConstraintViolation<FrenchAddress>> constraintViolations = validator.validate( address );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		ConstraintViolation<FrenchAddress> constraintViolation = constraintViolations.iterator().next();
		assertCorrectConstraintTypes( constraintViolations, NotNull.class );
		NotNull notNull = (NotNull) constraintViolation.getConstraintDescriptor().getAnnotation();
		List<Class<?>> groups = Arrays.asList( notNull.groups() );
		assertTrue( groups.size() == 2, "There should be two groups" );
		assertTrue( groups.contains( Default.class ), "The default group should be in the list." );
		assertTrue(
				groups.contains( FrenchAddress.FullAddressCheck.class ),
				"The FrenchAddress.FullAddressCheck group should be inherited."
		);
	}

	@Test
	@SpecAssertion(section = "2.3", id = "g")
	public void testOnlySingleConstraintViolation() {
		Validator validator = TestUtil.getValidatorUnderTest();

		GermanAddress address = new GermanAddress();
		address.setAddressline1( "Rathausstrasse 5" );
		address.setAddressline2( "3ter Stock" );
		address.setCity( "Karlsruhe" );
		address.setZipCode( "abc" );
		// actually three composing constraints fail, but due to @ReportAsSingleViolation only one will be reported.
		Set<ConstraintViolation<GermanAddress>> constraintViolations = validator.validate( address );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertConstraintViolation(
				constraintViolations.iterator().next(),
				GermanAddress.class,
				"abc",
				"zipCode"
		);
	}

	@Test
	@SpecAssertion(section = "2.3", id = "h")
	public void testAttributesDefinedOnComposingConstraints() {

		Validator validator = TestUtil.getValidatorUnderTest();
		BeanDescriptor descriptor = validator.getConstraintsForClass( FrenchAddress.class );
		Set<ConstraintDescriptor<?>> constraintDescriptors = descriptor.getConstraintsForProperty( "zipCode" )
				.getConstraintDescriptors();
		boolean findPattern = checkForAppropriateAnnotation( constraintDescriptors );
		assertTrue( findPattern, "Could not find @Pattern in composing constraints" );
	}

	private boolean checkForAppropriateAnnotation(Set<ConstraintDescriptor<?>> constraintDescriptors) {
		boolean findPattern = false;
		for ( ConstraintDescriptor<?> constraintDescriptor : constraintDescriptors ) {
			Annotation ann = constraintDescriptor.getAnnotation();
			if ( Pattern.class.getName().equals( ann.annotationType().getName() ) ) {
				String regexp = ( (Pattern) ann ).regexp();
				if ( regexp.equals( "bar" ) ) {
					fail( "The regular expression attributes are defined in the composing constraint." );
				}
				findPattern = true;
			}
			findPattern |= checkForAppropriateAnnotation( constraintDescriptor.getComposingConstraints() );
		}
		return findPattern;
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "2.3", id = "j"),
			@SpecAssertion(section = "2.3", id = "o")
	})
	public void testOverriddenAttributesMustMatchInType() {
		Validator validator = TestUtil.getValidatorUnderTest();
		try {
			validator.validate( new DummyEntityWithZipCode( "foobar" ) );
			fail( "Wrong attribute override in composite constraint. An Exception should have been thrown." );
		}
		catch ( ConstraintDefinitionException e ) {
			// success
		}
	}

	@Test
	@SpecAssertion(section = "2.3", id = "f")
	public void testAllComposingConstraintsMustBeApplicableToAnnotatedType() {
		Validator validator = TestUtil.getValidatorUnderTest();
		try {
			validator.validate( new Shoe( 41 ) );
			fail( "Shoe defines @NotEmpty on an int. Validation should have failed." );
		}
		catch ( UnexpectedTypeException e ) {
			// success
		}
	}

	@Test
	@SpecAssertion(section = "2.3", id = "p")
	public void testPayloadPropagationInComposedConstraints() {
		Friend john = new Friend( "John", "Doe" );

		Validator validator = TestUtil.getValidatorUnderTest();
		Set<ConstraintViolation<Friend>> constraintViolations = validator.validate( john );

		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintTypes( constraintViolations, NotNull.class );

		ConstraintViolation<Friend> constraintViolation = constraintViolations.iterator().next();
		Set<Class<? extends Payload>> payloads = constraintViolation.getConstraintDescriptor().getPayload();

		assertTrue( payloads.size() == 1, "There should be one payload in the set" );
		Class<? extends Payload> payload = payloads.iterator().next();
		assertTrue( payload.getName().equals( Severity.Warn.class.getName() ), "Unexpected payload" );
	}

	private FrenchAddress getFrenchAddressWithoutZipCode() {
		FrenchAddress address = new FrenchAddress();
		address.setAddressline1( "10 rue des Treuils" );
		address.setAddressline2( "BP 12 " );
		address.setCity( "Bordeaux" );
		return address;
	}

	class DummyEntityWithZipCode {
		@FrenchZipcodeWithInvalidOverride
		String zip;

		DummyEntityWithZipCode(String zip) {
			this.zip = zip;
		}
	}
}
