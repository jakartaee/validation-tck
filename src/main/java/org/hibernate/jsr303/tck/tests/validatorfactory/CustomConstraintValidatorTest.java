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
package org.hibernate.jsr303.tck.tests.validatorfactory;

import javax.validation.Configuration;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorFactory;
import javax.validation.ValidationException;
import javax.validation.Validator;

import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.testharness.AbstractTest;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ArtifactType;
import org.jboss.testharness.impl.packaging.Classes;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.Test;

import org.hibernate.jsr303.tck.util.TestUtil;

/**
 * @author Hardy Ferentschik
 */
@Artifact(artifactType = ArtifactType.JSR303)
@Classes({TestUtil.class, TestUtil.PathImpl.class, TestUtil.NodeImpl.class})
public class CustomConstraintValidatorTest extends AbstractTest {

	@SpecAssertion(section = "2.5", id = "a")
	@Test
	public void testDefaultConstructorInValidatorCalled() {
		Validator validator = TestUtil.getValidatorUnderTest();
		validator.validate( new Dummy() );
		assertTrue(
				MyConstraintValidator.defaultConstructorCalled,
				"The no-arg default constructor should have been called."
		);
	}

	@SpecAssertion(section = "2.5", id = "b")
	@Test(expectedExceptions = ValidationException.class)
	public void testRuntimeExceptionInValidatorCreationIsWrapped() {
		Validator validator = TestUtil.getValidatorUnderTest();
		validator.validate( new SecondDummy() );
	}

	@SpecAssertion(section = "2.5", id = "c")
	@Test(expectedExceptions = ValidationException.class)
	public void testValidationExceptionIsThrownInCaseFactoryReturnsNull() {
		Configuration<?> config = TestUtil.getConfigurationUnderTest().constraintValidatorFactory(
				new ConstraintValidatorFactory() {
					public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {
						return null;
					}
				}
		);
		Validator validator = config.buildValidatorFactory().getValidator();
		validator.validate( new SecondDummy() );
	}

	public static class Dummy {
		@MyConstraint
		public int value;
	}

	public static class SecondDummy {
		@MySecondConstraint
		public int value;
	}
}
