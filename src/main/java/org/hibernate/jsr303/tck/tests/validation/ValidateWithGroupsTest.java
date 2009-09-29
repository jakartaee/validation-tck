// $Id$
/*
* JBoss, Home of Professional Open Source
* Copyright 2008, Red Hat, Inc. and/or its affiliates, and individual contributors
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

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.testharness.AbstractTest;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ArtifactType;
import org.jboss.testharness.impl.packaging.Classes;
import org.testng.annotations.Test;

import org.hibernate.jsr303.tck.util.TestUtil;
import static org.hibernate.jsr303.tck.util.TestUtil.assertCorrectConstraintTypes;
import static org.hibernate.jsr303.tck.util.TestUtil.assertCorrectNumberOfViolations;
import static org.hibernate.jsr303.tck.util.TestUtil.assertCorrectPropertyPaths;

/**
 * Tests for the implementation of {@code Validator}.
 *
 * @author Hardy Ferentschik
 */
@Artifact(artifactType = ArtifactType.JSR303)
@Classes({ TestUtil.class, TestUtil.PathImpl.class, TestUtil.NodeImpl.class })
public class ValidateWithGroupsTest extends AbstractTest {

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "4.1.2", id = "a"),
			@SpecAssertion(section = "4.1.2", id = "b")
	})
	public void testCorrectGroupsAreAppliedForValidate() {
		Validator validator = TestUtil.getValidatorUnderTest();

		Set<ConstraintViolation<Address>> constraintViolations = validator.validate( new Address() );
		assertCorrectNumberOfViolations( constraintViolations, 2 );
		assertCorrectConstraintTypes( constraintViolations, NotNull.class, NotEmpty.class );
		assertCorrectPropertyPaths( constraintViolations, "city", "zipCode" );

		constraintViolations = validator.validate( new Address(), Default.class );
		assertCorrectNumberOfViolations( constraintViolations, 2 );
		assertCorrectConstraintTypes( constraintViolations, NotNull.class, NotEmpty.class );
		assertCorrectPropertyPaths( constraintViolations, "city", "zipCode" );

		constraintViolations = validator.validate( new Address(), Address.Minimal.class );
		assertCorrectNumberOfViolations( constraintViolations, 2 );
		assertCorrectConstraintTypes( constraintViolations, NotEmpty.class, NotEmpty.class );
		assertCorrectPropertyPaths( constraintViolations, "street", "zipCode" );

		constraintViolations = validator.validate( new Address(), Default.class, Address.Minimal.class );
		assertCorrectNumberOfViolations( constraintViolations, 3 );
		assertCorrectConstraintTypes( constraintViolations, NotNull.class, NotEmpty.class, NotEmpty.class );
		assertCorrectPropertyPaths( constraintViolations, "city", "street", "zipCode" );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "4.1.2", id = "a"),
			@SpecAssertion(section = "4.1.2", id = "b")
	})
	public void testCorrectGroupsAreAppliedForValidateProperty() {
		Validator validator = TestUtil.getValidatorUnderTest();

		Set<ConstraintViolation<Address>> constraintViolations = validator.validateProperty( new Address(), "city" );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintTypes( constraintViolations, NotNull.class );
		assertCorrectPropertyPaths( constraintViolations, "city" );

		constraintViolations = validator.validateProperty( new Address(), "city", Default.class );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintTypes( constraintViolations, NotNull.class );
		assertCorrectPropertyPaths( constraintViolations, "city" );

		constraintViolations = validator.validateProperty( new Address(), "city", Address.Minimal.class );
		assertCorrectNumberOfViolations( constraintViolations, 0 );

		constraintViolations = validator.validateProperty( new Address(), "street", Address.Minimal.class );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintTypes( constraintViolations, NotEmpty.class );
		assertCorrectPropertyPaths( constraintViolations, "street" );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "4.1.2", id = "a"),
			@SpecAssertion(section = "4.1.2", id = "b")
	})
	public void testCorrectGroupsAreAppliedForValidateValue() {
		Validator validator = TestUtil.getValidatorUnderTest();

		Set<ConstraintViolation<Address>> constraintViolations = validator.validateValue( Address.class, "city", null );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintTypes( constraintViolations, NotNull.class );
		assertCorrectPropertyPaths( constraintViolations, "city" );

		constraintViolations = validator.validateValue( Address.class, "city", null, Default.class );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintTypes( constraintViolations, NotNull.class );
		assertCorrectPropertyPaths( constraintViolations, "city" );

		constraintViolations = validator.validateValue( Address.class, "city", null, Address.Minimal.class );
		assertCorrectNumberOfViolations( constraintViolations, 0 );

		constraintViolations = validator.validateValue( Address.class, "street", null, Address.Minimal.class );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintTypes( constraintViolations, NotEmpty.class );
		assertCorrectPropertyPaths( constraintViolations, "street" );
	}
}