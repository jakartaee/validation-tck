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
package org.hibernate.beanvalidation.tck.tests.validatorfactory;

import javax.validation.Configuration;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorFactory;
import javax.validation.ValidationException;
import javax.validation.Validator;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

import static org.testng.Assert.assertTrue;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "1.1.0")
public class CustomConstraintValidatorTest extends Arquillian {

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClassPackage( CustomConstraintValidatorTest.class )
				.build();
	}

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
