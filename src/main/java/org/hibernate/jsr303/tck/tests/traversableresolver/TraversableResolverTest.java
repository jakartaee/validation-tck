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
package org.hibernate.jsr303.tck.tests.traversableresolver;

import java.lang.annotation.ElementType;
import java.util.Set;
import javax.validation.Configuration;
import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.TraversableResolver;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.testharness.AbstractTest;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ArtifactType;
import org.jboss.testharness.impl.packaging.Classes;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

import org.hibernate.jsr303.tck.util.TestUtil;
import static org.hibernate.jsr303.tck.util.TestUtil.assertCorrectNumberOfViolations;

/**
 * @author Emmanuel Bernard
 * @author Hardy Ferentschik
 */
@Artifact(artifactType = ArtifactType.JSR303)
@Classes({ TestUtil.class, TestUtil.PathImpl.class, TestUtil.NodeImpl.class })
public class TraversableResolverTest extends AbstractTest {
	@Test
	@SpecAssertions({
			@SpecAssertion(section = "3.5.2", id = "a"),
			@SpecAssertion(section = "3.5.2", id = "b"),
			@SpecAssertion(section = "3.5.2", id = "c")
	})
	public void testCorrectNumberOfCallsToIsReachableAndIsCascadable() {
		Suit suit = new Suit();
		suit.setTrousers( new Trousers() );
		suit.setJacket( new Jacket() );
		suit.setSize( 3333 );
		suit.getTrousers().setLength( 32321 );
		suit.getJacket().setWidth( 432432 );

		SnifferTraversableResolver resolver = new SnifferTraversableResolver( suit );

		Configuration<?> config = TestUtil.getConfigurationUnderTest().traversableResolver( resolver );

		ValidatorFactory factory = config.buildValidatorFactory();
		Validator v = factory.getValidator();

		v.validate( suit );

		assertEquals( resolver.getReachPaths().size(), 5 );
		assertEquals( resolver.getCascadePaths().size(), 2 );
	}

	@Test
	@SpecAssertion(section = "3.5.2", id = "d")
	public void testCustomTraversableResolverViaConfiguration() {

		// get a new factory using a custom configuration
		Configuration<?> configuration = TestUtil.getConfigurationUnderTest();
		configuration.traversableResolver( new DummyTraversableResolver() );
		ValidatorFactory factory = configuration.buildValidatorFactory();
		Validator validator = factory.getValidator();

		Person person = new Person();
		Set<ConstraintViolation<Person>> constraintViolations = validator.validate( person );
		assertCorrectNumberOfViolations( constraintViolations, 0 );
	}


	@Test(expectedExceptions = ValidationException.class)
	@SpecAssertion(section = "3.5.2", id = "e")
	public void testResolverExceptionsGetWrappedInValidationException() {
		ExceptionThrowingTraversableResolver resolver = new ExceptionThrowingTraversableResolver();
		Configuration<?> config = TestUtil.getConfigurationUnderTest().traversableResolver( resolver );

		ValidatorFactory factory = config.buildValidatorFactory();
		Validator v = factory.getValidator();

		v.validate( new Suit() );
	}

	private static class DummyTraversableResolver implements TraversableResolver {

		public boolean isReachable(Object traversableObject, Path.Node traversableProperty, Class<?> rootBeanType, Path pathToTraversableObject, ElementType elementType) {
			return false;
		}

		public boolean isCascadable(Object traversableObject, Path.Node traversableProperty, Class<?> rootBeanType, Path pathToTraversableObject, ElementType elementType) {
			return false;
		}
	}
}