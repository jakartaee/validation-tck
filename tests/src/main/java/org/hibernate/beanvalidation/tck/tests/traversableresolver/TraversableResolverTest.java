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
package org.hibernate.beanvalidation.tck.tests.traversableresolver;

import java.lang.annotation.ElementType;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import javax.validation.Configuration;
import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.TraversableResolver;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectNumberOfViolations;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertSame;

/**
 * @author Emmanuel Bernard
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class TraversableResolverTest extends Arquillian {

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClassPackage( TraversableResolverTest.class )
				.build();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "4.6", id = "a"),
			@SpecAssertion(section = "4.6.3", id = "a"),
			@SpecAssertion(section = "4.6.3", id = "b"),
			@SpecAssertion(section = "4.6.3", id = "c"),
			@SpecAssertion(section = "4.6.3", id = "d"),
			@SpecAssertion(section = "4.6.3", id = "e"),
			@SpecAssertion(section = "4.6.3", id = "f"),
			@SpecAssertion(section = "4.6.3", id = "g"),
			@SpecAssertion(section = "4.6.3", id = "h"),
			@SpecAssertion(section = "4.6.3", id = "k"),
			@SpecAssertion(section = "4.6.3", id = "l")
	})
	public void testCorrectCallsToIsReachableAndIsCascadable() {
		Suit suit = new Suit();
		suit.setTrousers( new Trousers() );
		suit.setJacket( new Jacket() );
		suit.setSize( 3333 );
		suit.getTrousers().setLength( 32321 );
		suit.getJacket().setWidth( 432432 );

		Set<Call> expectedReachCalls = new HashSet<Call>();
		Set<Call> expectedCascadeCalls = new HashSet<Call>();
		expectedReachCalls.add(
				new Call(
						suit,
						"size",
						Suit.class,
						ElementType.FIELD,
						new String[] { null }
				)
		);
		expectedReachCalls.add(
				new Call(
						suit,
						"trousers",
						Suit.class,
						ElementType.FIELD,
						new String[] { null }
				)
		);
		expectedCascadeCalls.add(
				new Call(
						suit,
						"trousers",
						Suit.class,
						ElementType.FIELD,
						new String[] { null }
				)
		);
		expectedReachCalls.add(
				new Call(
						suit.getTrousers(),
						"length",
						Suit.class,
						ElementType.FIELD,
						new String[] { "trousers" }
				)
		);
		expectedReachCalls.add(
				new Call(
						suit,
						"jacket",
						Suit.class,
						ElementType.METHOD,
						new String[] { null }
				)
		);
		expectedCascadeCalls.add(
				new Call(
						suit,
						"jacket",
						Suit.class,
						ElementType.METHOD,
						new String[] { null }
				)
		);
		expectedReachCalls.add(
				new Call(
						suit.getJacket(),
						"width",
						Suit.class,
						ElementType.METHOD,
						new String[] { "jacket" }
				)
		);

		SnifferTraversableResolver resolver = new SnifferTraversableResolver(
				expectedReachCalls,
				expectedCascadeCalls
		);

		Configuration<?> config = TestUtil.getConfigurationUnderTest().traversableResolver( resolver );

		ValidatorFactory factory = config.buildValidatorFactory();
		Validator v = factory.getValidator();

		v.validate( suit );

		assertEquals( resolver.getReachableCallCount(), 5 );
		assertEquals( resolver.getCascadableCallCount(), 2 );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "4.6.3", id = "a"),
			@SpecAssertion(section = "4.6.3", id = "b"),
			@SpecAssertion(section = "4.6.3", id = "c"),
			@SpecAssertion(section = "4.6.3", id = "d"),
			@SpecAssertion(section = "4.6.3", id = "e"),
			@SpecAssertion(section = "4.6.3", id = "f"),
			@SpecAssertion(section = "4.6.3", id = "g"),
			@SpecAssertion(section = "4.6.3", id = "h")
	})
	public void testCorrectCallsToIsReachableAndIsCascadableForValidateValue() {
		Suit suit = new Suit();
		suit.setTrousers( new Trousers() );
		suit.setJacket( new Jacket() );
		suit.setSize( 3333 );
		suit.getTrousers().setLength( 32321 );
		suit.getJacket().setWidth( 432432 );

		Set<Call> expectedReachCalls = new HashSet<Call>();
		Set<Call> expectedCascadeCalls = new HashSet<Call>();
		expectedReachCalls.add(
				new Call(
						null,
						"size",
						Suit.class,
						ElementType.FIELD,
						new String[] { null }
				)
		);

		SnifferTraversableResolver resolver = new SnifferTraversableResolver(
				expectedReachCalls,
				expectedCascadeCalls
		);

		Configuration<?> config = TestUtil.getConfigurationUnderTest().traversableResolver( resolver );

		ValidatorFactory factory = config.buildValidatorFactory();
		Validator v = factory.getValidator();

		v.validateValue( Suit.class, "size", 3333 );

		assertEquals( resolver.getReachableCallCount(), 1 );
		assertEquals( resolver.getCascadableCallCount(), 0 );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "4.6.3", id = "d"),
			@SpecAssertion(section = "4.6.3", id = "e"),
			@SpecAssertion(section = "4.6.3", id = "f"),
			@SpecAssertion(section = "4.6.3", id = "g"),
			@SpecAssertion(section = "4.6.3", id = "h"),
			@SpecAssertion(section = "4.6.3", id = "k"),
			@SpecAssertion(section = "4.6.3", id = "l")
	})
	public void testCorrectCallsToIsReachableAndIsCascadableForParameterValidation() throws Exception {
		Suit suit = new Suit();
		suit.setTrousers( new Trousers() );
		suit.setJacket( new Jacket() );
		suit.setSize( 3333 );
		suit.getTrousers().setLength( 32321 );
		suit.getJacket().setWidth( 432432 );

		Set<Call> expectedReachCalls = new HashSet<Call>();
		Set<Call> expectedCascadeCalls = new HashSet<Call>();
		expectedReachCalls.add(
				new Call(
						suit,
						"size",
						Gentleman.class,
						ElementType.FIELD,
						new String[] { "wearSuit", "arg0" }
				)
		);
		expectedReachCalls.add(
				new Call(
						suit,
						"trousers",
						Gentleman.class,
						ElementType.FIELD,
						new String[] { "wearSuit", "arg0" }
				)
		);
		expectedCascadeCalls.add(
				new Call(
						suit,
						"trousers",
						Gentleman.class,
						ElementType.FIELD,
						new String[] { "wearSuit", "arg0" }
				)
		);
		expectedReachCalls.add(
				new Call(
						suit.getTrousers(),
						"length",
						Gentleman.class,
						ElementType.FIELD,
						new String[] { "wearSuit", "arg0", "trousers" }
				)
		);
		expectedReachCalls.add(
				new Call(
						suit,
						"jacket",
						Gentleman.class,
						ElementType.METHOD,
						new String[] { "wearSuit", "arg0" }
				)
		);
		expectedCascadeCalls.add(
				new Call(
						suit,
						"jacket",
						Gentleman.class,
						ElementType.METHOD,
						new String[] { "wearSuit", "arg0" }
				)
		);
		expectedReachCalls.add(
				new Call(
						suit.getJacket(),
						"width",
						Gentleman.class,
						ElementType.METHOD,
						new String[] { "wearSuit", "arg0", "jacket" }
				)
		);

		SnifferTraversableResolver resolver = new SnifferTraversableResolver(
				expectedReachCalls,
				expectedCascadeCalls
		);

		Configuration<?> config = TestUtil.getConfigurationUnderTest().traversableResolver( resolver );

		ValidatorFactory factory = config.buildValidatorFactory();
		ExecutableValidator executableValidator = factory.getValidator().forExecutables();

		Object gentleman = new Gentleman();
		Method method = Gentleman.class.getMethod( "wearSuit", Suit.class );
		Object[] parameterValues = new Object[] { suit };

		executableValidator.validateParameters(
				gentleman,
				method,
				parameterValues
		);

		assertEquals( resolver.getReachableCallCount(), 5 );
		assertEquals( resolver.getCascadableCallCount(), 2 );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "4.6.3", id = "d"),
			@SpecAssertion(section = "4.6.3", id = "e"),
			@SpecAssertion(section = "4.6.3", id = "f"),
			@SpecAssertion(section = "4.6.3", id = "g"),
			@SpecAssertion(section = "4.6.3", id = "h"),
			@SpecAssertion(section = "4.6.3", id = "k"),
			@SpecAssertion(section = "4.6.3", id = "l")
	})
	public void testCorrectCallsToIsReachableAndIsCascadableForReturnValueValidation() throws Exception {
		Suit suit = new Suit();
		suit.setTrousers( new Trousers() );
		suit.setJacket( new Jacket() );
		suit.setSize( 3333 );
		suit.getTrousers().setLength( 32321 );
		suit.getJacket().setWidth( 432432 );

		Set<Call> expectedReachCalls = new HashSet<Call>();
		Set<Call> expectedCascadeCalls = new HashSet<Call>();
		expectedReachCalls.add(
				new Call(
						suit,
						"size",
						Gentleman.class,
						ElementType.FIELD,
						new String[] { "undress", TestUtil.RETURN_VALUE_NODE_NAME }
				)
		);
		expectedReachCalls.add(
				new Call(
						suit,
						"trousers",
						Gentleman.class,
						ElementType.FIELD,
						new String[] { "undress", TestUtil.RETURN_VALUE_NODE_NAME }
				)
		);
		expectedCascadeCalls.add(
				new Call(
						suit,
						"trousers",
						Gentleman.class,
						ElementType.FIELD,
						new String[] { "undress", TestUtil.RETURN_VALUE_NODE_NAME }
				)
		);
		expectedReachCalls.add(
				new Call(
						suit.getTrousers(),
						"length",
						Gentleman.class,
						ElementType.FIELD,
						new String[] { "undress", TestUtil.RETURN_VALUE_NODE_NAME, "trousers" }
				)
		);
		expectedReachCalls.add(
				new Call(
						suit,
						"jacket",
						Gentleman.class,
						ElementType.METHOD,
						new String[] { "undress", TestUtil.RETURN_VALUE_NODE_NAME }
				)
		);
		expectedCascadeCalls.add(
				new Call(
						suit,
						"jacket",
						Gentleman.class,
						ElementType.METHOD,
						new String[] { "undress", TestUtil.RETURN_VALUE_NODE_NAME }
				)
		);
		expectedReachCalls.add(
				new Call(
						suit.getJacket(),
						"width",
						Gentleman.class,
						ElementType.METHOD,
						new String[] { "undress", TestUtil.RETURN_VALUE_NODE_NAME, "jacket" }
				)
		);

		SnifferTraversableResolver resolver = new SnifferTraversableResolver(
				expectedReachCalls,
				expectedCascadeCalls
		);

		Configuration<?> config = TestUtil.getConfigurationUnderTest().traversableResolver( resolver );

		ValidatorFactory factory = config.buildValidatorFactory();
		ExecutableValidator executableValidator = factory.getValidator().forExecutables();

		Gentleman gentleman = new Gentleman();
		gentleman.wearSuit( suit );
		Method method = Gentleman.class.getMethod( "undress" );

		executableValidator.validateReturnValue(
				gentleman,
				method,
				suit
		);

		assertEquals( resolver.getReachableCallCount(), 5 );
		assertEquals( resolver.getCascadableCallCount(), 2 );
	}

	@Test
	@SpecAssertion(section = "4.6.3", id = "i")
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

	@Test
	@SpecAssertion(section = "5.5.2", id = "c")
	public void testTraversableResolverFromValidatorFactory() {
		Configuration<?> configuration = TestUtil.getConfigurationUnderTest();
		DummyTraversableResolver traversableResolver = new DummyTraversableResolver();

		configuration.traversableResolver( traversableResolver );
		ValidatorFactory factory = configuration.buildValidatorFactory();

		assertSame( factory.getTraversableResolver(), traversableResolver );
	}

	@Test(expectedExceptions = ValidationException.class)
	@SpecAssertion(section = "4.6.3", id = "j")
	public void testResolverExceptionsGetWrappedInValidationException() {
		ExceptionThrowingTraversableResolver resolver = new ExceptionThrowingTraversableResolver();
		Configuration<?> config = TestUtil.getConfigurationUnderTest().traversableResolver( resolver );

		ValidatorFactory factory = config.buildValidatorFactory();
		Validator v = factory.getValidator();

		v.validate( new Suit() );
	}

	@Test
	@SpecAssertion(section = "5.5.3", id = "b")
	public void testDefaultTraversableResolverIsNotNull() {
		Configuration<?> config = TestUtil.getConfigurationUnderTest();
		assertNotNull( config.getDefaultTraversableResolver() );
	}

	private static class DummyTraversableResolver implements TraversableResolver {

		@Override
		public boolean isReachable(Object traversableObject, Path.Node traversableProperty, Class<?> rootBeanType, Path pathToTraversableObject, ElementType elementType) {
			return false;
		}

		@Override
		public boolean isCascadable(Object traversableObject, Path.Node traversableProperty, Class<?> rootBeanType, Path pathToTraversableObject, ElementType elementType) {
			return false;
		}
	}
}
