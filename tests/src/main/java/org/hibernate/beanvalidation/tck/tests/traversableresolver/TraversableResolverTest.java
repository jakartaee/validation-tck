/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.traversableresolver;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.RETURN_VALUE_NODE_NAME;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertNoViolations;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertSame;

import java.lang.annotation.ElementType;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import jakarta.validation.Configuration;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import jakarta.validation.TraversableResolver;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.executable.ExecutableValidator;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Emmanuel Bernard
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "3.0.0")
public class TraversableResolverTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( TraversableResolverTest.class )
				.build();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE, id = "a"),
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TRAVERSABLE, id = "a"),
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TRAVERSABLE, id = "b"),
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TRAVERSABLE, id = "c"),
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TRAVERSABLE, id = "d"),
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TRAVERSABLE, id = "e"),
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TRAVERSABLE, id = "f"),
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TRAVERSABLE, id = "g"),
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TRAVERSABLE, id = "h"),
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TRAVERSABLE, id = "k"),
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TRAVERSABLE, id = "l")
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
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TRAVERSABLE, id = "a"),
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TRAVERSABLE, id = "b"),
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TRAVERSABLE, id = "c"),
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TRAVERSABLE, id = "d"),
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TRAVERSABLE, id = "e"),
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TRAVERSABLE, id = "f"),
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TRAVERSABLE, id = "g"),
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TRAVERSABLE, id = "h")
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
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TRAVERSABLE, id = "d"),
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TRAVERSABLE, id = "e"),
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TRAVERSABLE, id = "f"),
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TRAVERSABLE, id = "g"),
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TRAVERSABLE, id = "h"),
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TRAVERSABLE, id = "k"),
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TRAVERSABLE, id = "l")
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
						new String[] { "wearSuit", "suit" }
				)
		);
		expectedReachCalls.add(
				new Call(
						suit,
						"trousers",
						Gentleman.class,
						ElementType.FIELD,
						new String[] { "wearSuit", "suit" }
				)
		);
		expectedCascadeCalls.add(
				new Call(
						suit,
						"trousers",
						Gentleman.class,
						ElementType.FIELD,
						new String[] { "wearSuit", "suit" }
				)
		);
		expectedReachCalls.add(
				new Call(
						suit.getTrousers(),
						"length",
						Gentleman.class,
						ElementType.FIELD,
						new String[] { "wearSuit", "suit", "trousers" }
				)
		);
		expectedReachCalls.add(
				new Call(
						suit,
						"jacket",
						Gentleman.class,
						ElementType.METHOD,
						new String[] { "wearSuit", "suit" }
				)
		);
		expectedCascadeCalls.add(
				new Call(
						suit,
						"jacket",
						Gentleman.class,
						ElementType.METHOD,
						new String[] { "wearSuit", "suit" }
				)
		);
		expectedReachCalls.add(
				new Call(
						suit.getJacket(),
						"width",
						Gentleman.class,
						ElementType.METHOD,
						new String[] { "wearSuit", "suit", "jacket" }
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
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TRAVERSABLE, id = "d"),
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TRAVERSABLE, id = "e"),
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TRAVERSABLE, id = "f"),
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TRAVERSABLE, id = "g"),
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TRAVERSABLE, id = "h"),
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TRAVERSABLE, id = "k"),
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TRAVERSABLE, id = "l")
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
						new String[] { "undress", RETURN_VALUE_NODE_NAME }
				)
		);
		expectedReachCalls.add(
				new Call(
						suit,
						"trousers",
						Gentleman.class,
						ElementType.FIELD,
						new String[] { "undress", RETURN_VALUE_NODE_NAME }
				)
		);
		expectedCascadeCalls.add(
				new Call(
						suit,
						"trousers",
						Gentleman.class,
						ElementType.FIELD,
						new String[] { "undress", RETURN_VALUE_NODE_NAME }
				)
		);
		expectedReachCalls.add(
				new Call(
						suit.getTrousers(),
						"length",
						Gentleman.class,
						ElementType.FIELD,
						new String[] { "undress", RETURN_VALUE_NODE_NAME, "trousers" }
				)
		);
		expectedReachCalls.add(
				new Call(
						suit,
						"jacket",
						Gentleman.class,
						ElementType.METHOD,
						new String[] { "undress", RETURN_VALUE_NODE_NAME }
				)
		);
		expectedCascadeCalls.add(
				new Call(
						suit,
						"jacket",
						Gentleman.class,
						ElementType.METHOD,
						new String[] { "undress", RETURN_VALUE_NODE_NAME }
				)
		);
		expectedReachCalls.add(
				new Call(
						suit.getJacket(),
						"width",
						Gentleman.class,
						ElementType.METHOD,
						new String[] { "undress", RETURN_VALUE_NODE_NAME, "jacket" }
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
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TRAVERSABLE, id = "i")
	public void testCustomTraversableResolverViaConfiguration() {
		// get a new factory using a custom configuration
		Configuration<?> configuration = TestUtil.getConfigurationUnderTest();
		configuration.traversableResolver( new DummyTraversableResolver() );
		ValidatorFactory factory = configuration.buildValidatorFactory();
		Validator validator = factory.getValidator();

		Person person = new Person();
		Set<ConstraintViolation<Person>> constraintViolations = validator.validate( person );
		assertNoViolations( constraintViolations );
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_VALIDATORFACTORY, id = "c")
	public void testTraversableResolverFromValidatorFactory() {
		Configuration<?> configuration = TestUtil.getConfigurationUnderTest();
		DummyTraversableResolver traversableResolver = new DummyTraversableResolver();

		configuration.traversableResolver( traversableResolver );
		ValidatorFactory factory = configuration.buildValidatorFactory();

		assertSame( factory.getTraversableResolver(), traversableResolver );
	}

	@Test(expectedExceptions = ValidationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TRAVERSABLE, id = "j")
	public void testResolverExceptionsGetWrappedInValidationException() {
		ExceptionThrowingTraversableResolver resolver = new ExceptionThrowingTraversableResolver();
		Configuration<?> config = TestUtil.getConfigurationUnderTest().traversableResolver( resolver );

		ValidatorFactory factory = config.buildValidatorFactory();
		Validator v = factory.getValidator();

		v.validate( new Suit() );
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_CONFIGURATION, id = "b")
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
