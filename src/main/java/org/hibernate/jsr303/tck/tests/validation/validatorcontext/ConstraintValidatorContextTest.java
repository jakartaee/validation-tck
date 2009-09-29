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
package org.hibernate.jsr303.tck.tests.validation.validatorcontext;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.Validator;

import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.testharness.AbstractTest;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ArtifactType;
import org.jboss.testharness.impl.packaging.Classes;
import org.testng.annotations.Test;

import org.hibernate.jsr303.tck.util.TestUtil;
import static org.hibernate.jsr303.tck.util.TestUtil.assertCorrectConstraintViolationMessages;
import static org.hibernate.jsr303.tck.util.TestUtil.assertCorrectNumberOfViolations;
import static org.hibernate.jsr303.tck.util.TestUtil.assertCorrectPropertyPaths;

/**
 * @author Hardy Ferentschik
 */
@Artifact(artifactType = ArtifactType.JSR303)
@Classes({ TestUtil.class, TestUtil.PathImpl.class, TestUtil.NodeImpl.class })
public class ConstraintValidatorContextTest extends AbstractTest {

	@Test
	@SpecAssertion(section = "2.4", id = "l")
	public void testDefaultError() {
		Validator validator = TestUtil.getValidatorUnderTest();

		DummyValidator.disableDefaultError( false );
		DummyValidator.setCustomErrorMessages( null );

		DummyBean bean = new DummyBean( "foobar" );

		Set<ConstraintViolation<DummyBean>> constraintViolations = validator.validate( bean );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintViolationMessages( constraintViolations, "dummy message" );
	}

	@Test(expectedExceptions = ValidationException.class)
	@SpecAssertion(section = "2.4", id = "r")
	public void testDisableDefaultErrorWithoutAddingCustomError() {
		Validator validator = TestUtil.getValidatorUnderTest();

		DummyValidator.disableDefaultError( true );
		Map<String, String> errors = new HashMap<String, String>();
		DummyValidator.setCustomErrorMessages( errors );

		DummyBean bean = new DummyBean( "foobar" );
		validator.validate( bean );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "2.4", id = "m")
	})
	public void testDisableDefaultErrorWithCustomErrorNoSubNode() {
		Validator validator = TestUtil.getValidatorUnderTest();

		DummyValidator.disableDefaultError( true );
		Map<String, String> errors = new HashMap<String, String>();
		errors.put( null, "message1" );
		DummyValidator.setCustomErrorMessages( errors );

		DummyBean bean = new DummyBean( "foobar" );

		Set<ConstraintViolation<DummyBean>> constraintViolations = validator.validate( bean );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintViolationMessages( constraintViolations, "message1" );
		assertCorrectPropertyPaths( constraintViolations, "value" );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "2.4", id = "m"),
			@SpecAssertion(section = "2.4", id = "q")
	})
	public void testDisableDefaultErrorWithCustomErrorWithSubNode() {
		Validator validator = TestUtil.getValidatorUnderTest();

		DummyValidator.disableDefaultError( true );
		Map<String, String> errors = new HashMap<String, String>();
		errors.put( "subnode", "subnode message" );
		DummyValidator.setCustomErrorMessages( errors );

		DummyBean bean = new DummyBean( "foobar" );

		Set<ConstraintViolation<DummyBean>> constraintViolations = validator.validate( bean );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintViolationMessages( constraintViolations, "subnode message" );
		assertCorrectPropertyPaths( constraintViolations, "value.subnode" );
	}
}
