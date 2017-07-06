/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.messageinterpolation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;
import static org.hibernate.beanvalidation.tck.util.TestUtil.getDefaultMessageInterpolator;
import static org.hibernate.beanvalidation.tck.util.TestUtil.getValidatorUnderTest;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import javax.validation.MessageInterpolator;
import javax.validation.Payload;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import javax.validation.metadata.ConstraintDescriptor;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Hardy Ferentschik
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class MessageInterpolationTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( MessageInterpolationTest.class )
				.withResource( "ValidationMessages.properties", "ValidationMessages.properties", true )
				.withResource( "ValidationMessages_de.properties", "ValidationMessages_de.properties", true )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_DEFAULTMESSAGEINTERPOLATION, id = "a")
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_CUSTOMRESOLUTION, id = "f")
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_CONFIGURATION, id = "a")
	public void testDefaultMessageInterpolatorIsNotNull() {
		MessageInterpolator interpolator = getDefaultMessageInterpolator();
		assertNotNull( interpolator, "Each bean validation provider must provide a default message interpolator." );
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_DEFAULTMESSAGEINTERPOLATION, id = "e")
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_DEFAULTMESSAGEINTERPOLATION_RESOLUTIONALGORITHM, id = "a")
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_CUSTOMRESOLUTION, id = "f")
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_CONFIGURATION, id = "a")
	public void testSuccessfulInterpolationOfValidationMessagesValue() {
		MessageInterpolator interpolator = getDefaultMessageInterpolator();
		ConstraintDescriptor<?> descriptor = getDescriptorFor( DummyEntity.class, "foo" );
		MessageInterpolator.Context context = new TestContext( descriptor );

		String expected = "replacement worked";
		String actual = interpolator.interpolate( "{foo}", context );
		assertEquals( actual, expected, "Wrong substitution" );

		expected = "replacement worked replacement worked";
		actual = interpolator.interpolate( "{foo} {foo}", context );
		assertEquals( actual, expected, "Wrong substitution" );

		expected = "This replacement worked just fine";
		actual = interpolator.interpolate( "This {foo} just fine", context );
		assertEquals( actual, expected, "Wrong substitution" );

		expected = "{} replacement worked {unknown}";
		actual = interpolator.interpolate( "{} {foo} {unknown}", context );
		assertEquals( actual, expected, "Wrong substitution" );
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_DEFAULTMESSAGEINTERPOLATION_RESOLUTIONALGORITHM, id = "b")
	public void testRecursiveMessageInterpolation() {
		MessageInterpolator interpolator = getDefaultMessageInterpolator();
		ConstraintDescriptor<?> descriptor = getDescriptorFor( DummyEntity.class, "fubar" );
		MessageInterpolator.Context context = new TestContext( descriptor );

		String expected = "recursion worked";
		String actual = interpolator.interpolate( descriptor.getMessageTemplate(), context );
		assertEquals(
				actual, expected, "Expansion should be recursive"
		);
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_DEFAULTMESSAGEINTERPOLATION, id = "d")
	public void testMessagesCanBeOverriddenAtConstraintLevel() {
		Validator validator = TestUtil.getValidatorUnderTest();
		Set<ConstraintViolation<DummyEntity>> constraintViolations = validator.validateProperty(
				new DummyEntity(), "snafu"
		);
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class ).withMessage( "messages can also be overridden at constraint declaration." )
		);
	}


	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_DEFAULTMESSAGEINTERPOLATION, id = "f")
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_DEFAULTMESSAGEINTERPOLATION, id = "g")
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_DEFAULTMESSAGEINTERPOLATION, id = "h")
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_DEFAULTMESSAGEINTERPOLATION, id = "i")
	public void testEscapedCharactersAreConsideredAsLiterals() {
		MessageInterpolator interpolator = getDefaultMessageInterpolator();
		ConstraintDescriptor<?> descriptor = getDescriptorFor( DummyEntity.class, "foo" );
		MessageInterpolator.Context context = new TestContext( descriptor );

		String expected = "{";
		String actual = interpolator.interpolate( "\\{", context );
		assertEquals( actual, expected, "Wrong substitution" );

		expected = "}";
		actual = interpolator.interpolate( "\\}", context );
		assertEquals( actual, expected, "Wrong substitution" );

		expected = "\\";
		actual = interpolator.interpolate( "\\", context );
		assertEquals( actual, expected, "Wrong substitution" );

		expected = "$";
		actual = interpolator.interpolate( "\\$", context );
		assertEquals( actual, expected, "Wrong substitution" );
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_DEFAULTMESSAGEINTERPOLATION_RESOLUTIONALGORITHM, id = "a")
	public void testUnSuccessfulInterpolation() {
		MessageInterpolator interpolator = getDefaultMessageInterpolator();
		ConstraintDescriptor<?> descriptor = getDescriptorFor( DummyEntity.class, "foo" );
		MessageInterpolator.Context context = new TestContext( descriptor );

		String expected = "foo";  // missing {}
		String actual = interpolator.interpolate( "foo", context );
		assertEquals( actual, expected, "Wrong substitution" );

		expected = "#{foo  {}";
		actual = interpolator.interpolate( "#{foo  {}", context );
		assertEquals( actual, expected, "Wrong substitution" );
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_DEFAULTMESSAGEINTERPOLATION_RESOLUTIONALGORITHM, id = "a")
	public void testUnknownTokenInterpolation() {
		MessageInterpolator interpolator = getDefaultMessageInterpolator();
		ConstraintDescriptor<?> descriptor = getDescriptorFor( DummyEntity.class, "foo" );
		MessageInterpolator.Context context = new TestContext( descriptor );

		String expected = "{bar}";  // unknown token {}
		String actual = interpolator.interpolate( "{bar}", context );
		assertEquals( actual, expected, "Wrong substitution" );
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_DEFAULTMESSAGEINTERPOLATION_RESOLUTIONALGORITHM, id = "c")
	public void testParametersAreExtractedFromBeanValidationProviderBundle() {
		MessageInterpolator interpolator = getDefaultMessageInterpolator();
		ConstraintDescriptor<?> descriptor = getDescriptorFor( Person.class, "birthday" );
		MessageInterpolator.Context context = new TestContext( descriptor );

		String key = "{javax.validation.constraints.Past.message}"; // Past is a built-in constraint so the provider must provide a default message
		String actual = interpolator.interpolate( key, context );
		assertFalse(
				key.equals( actual ),
				"There should have been a message interpolation from the bean validator provider bundle."
		);
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_DEFAULTMESSAGEINTERPOLATION_RESOLUTIONALGORITHM, id = "f")
	public void testConstraintAttributeValuesAreInterpolated() {
		MessageInterpolator interpolator = getDefaultMessageInterpolator();
		ConstraintDescriptor<?> descriptor = getDescriptorFor( DummyEntity.class, "bar" );
		MessageInterpolator.Context context = new TestContext( descriptor );

		String expected = "size must be between 5 and 10";
		String actual = interpolator.interpolate( descriptor.getMessageTemplate(), context );
		assertEquals( actual, expected, "Wrong substitution" );
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_DEFAULTMESSAGEINTERPOLATION_RESOLUTIONALGORITHM, id = "f")
	public void testParameterInterpolationHasPrecedenceOverExpressionEvaluation() {
		MessageInterpolator interpolator = getDefaultMessageInterpolator();
		ConstraintDescriptor<?> descriptor = getDescriptorFor( DummyEntity.class, "amount" );
		MessageInterpolator.Context context = new TestContext( descriptor );

		//if EL evaluation kicked in first, the "$" would be gone
		String expected = "must be $5 at least";
		String actual = interpolator.interpolate( descriptor.getMessageTemplate(), context );
		assertEquals( actual, expected, "Wrong substitution" );
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_DEFAULTMESSAGEINTERPOLATION_RESOLUTIONALGORITHM, id = "g")
	public void testElExpressionsAreInterpolated() {
		MessageInterpolator interpolator = getDefaultMessageInterpolator();
		ConstraintDescriptor<?> descriptor = getDescriptorFor( DummyEntity.class, "doubleAmount" );
		MessageInterpolator.Context context = new TestContext( descriptor );

		String expected = "must be 10 at least";
		String actual = interpolator.interpolate( descriptor.getMessageTemplate(), context );
		assertEquals( actual, expected, "Wrong substitution" );
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_DEFAULTMESSAGEINTERPOLATION_LOCALE, id = "a")
	public void testMessageInterpolationWithLocale() {
		MessageInterpolator interpolator = getDefaultMessageInterpolator();
		ConstraintDescriptor<?> descriptor = getDescriptorFor( DummyEntity.class, "foo" );
		MessageInterpolator.Context context = new TestContext( descriptor );

		String expected = "kann nicht null sein";
		String actual = interpolator.interpolate(
				descriptor.getMessageTemplate(), context, Locale.GERMAN
		);
		assertEquals( actual, expected, "Wrong substitution" );
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_DEFAULTMESSAGEINTERPOLATION_LOCALE, id = "b")
	public void testIfNoLocaleIsSpecifiedTheDefaultLocaleIsAssumed() {
		MessageInterpolator interpolator = getDefaultMessageInterpolator();
		ConstraintDescriptor<?> descriptor = getDescriptorFor( DummyEntity.class, "foo" );
		String messageTemplate = descriptor.getMessageTemplate();
		MessageInterpolator.Context context = new TestContext( descriptor );

		String messageInterpolatedWithNoLocale = interpolator.interpolate( messageTemplate, context );
		String messageInterpolatedWithDefaultLocale = interpolator.interpolate(
				messageTemplate, context, Locale.getDefault()
		);

		assertEquals( messageInterpolatedWithNoLocale, messageInterpolatedWithDefaultLocale, "Wrong substitution" );
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_CUSTOMRESOLUTION, id = "a")
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_CUSTOMRESOLUTION, id = "b")
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_CUSTOMRESOLUTION, id = "c")
	public void testCorrectValuesArePassedToInterpolateForPropertyConstraint() {
		TestMessageInterpolator messageInterpolator = new TestMessageInterpolator();
		Validator validator = TestUtil.getConfigurationUnderTest()
				.messageInterpolator( messageInterpolator )
				.buildValidatorFactory()
				.getValidator();

		String name = "Bob";
		validator.validate( new TestBeanWithPropertyConstraint( name ) );

		assertEquals( messageInterpolator.messageTemplate, TestBeanWithPropertyConstraint.MESSAGE );

		ConstraintDescriptor<?> constraintDescriptor = messageInterpolator.constraintDescriptor;
		assertEquals( constraintDescriptor.getAnnotation().annotationType(), Size.class );
		assertEquals( constraintDescriptor.getMessageTemplate(), TestBeanWithPropertyConstraint.MESSAGE );

		assertEquals( messageInterpolator.validatedValue, name );
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_CUSTOMRESOLUTION, id = "a")
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_CUSTOMRESOLUTION, id = "b")
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_CUSTOMRESOLUTION, id = "c")
	public void testCorrectValuesArePassedToInterpolateForClassLevelConstraint() {
		TestMessageInterpolator messageInterpolator = new TestMessageInterpolator();
		Validator validator = TestUtil.getConfigurationUnderTest()
				.messageInterpolator( messageInterpolator )
				.buildValidatorFactory()
				.getValidator();

		TestBeanWithClassLevelConstraint testBean = new TestBeanWithClassLevelConstraint();
		validator.validate( testBean );

		assertEquals( messageInterpolator.messageTemplate, TestBeanWithClassLevelConstraint.MESSAGE );

		ConstraintDescriptor<?> constraintDescriptor = messageInterpolator.constraintDescriptor;
		assertEquals( constraintDescriptor.getAnnotation().annotationType(), ValidTestBean.class );
		assertEquals( constraintDescriptor.getMessageTemplate(), TestBeanWithClassLevelConstraint.MESSAGE );

		assertEquals( messageInterpolator.validatedValue, testBean );
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_CUSTOMRESOLUTION, id = "g")
	public void testExceptionDuringMessageInterpolationIsWrappedIntoValidationException() {
		try ( ValidatorFactory factory = Validation.buildDefaultValidatorFactory() ) {
			ExceptionThrowingMessageInterpolator interpolator = new ExceptionThrowingMessageInterpolator();
			Validator validator = factory.usingContext().messageInterpolator( interpolator ).getValidator();

			try {
				validator.validate( new TestBeanWithPropertyConstraint( "Bob" ) );
				fail( "Expected exception wasn't thrown." );
			}
			catch (ValidationException ve) {
				assertEquals( ve.getCause(), interpolator.exception );
			}
		}
	}

	private ConstraintDescriptor<?> getDescriptorFor(Class<?> clazz, String propertyName) {
		Validator validator = getValidatorUnderTest();
		return validator.getConstraintsForClass( clazz )
				.getConstraintsForProperty( propertyName )
				.getConstraintDescriptors()
				.iterator()
				.next();
	}

	public class TestContext implements MessageInterpolator.Context {
		ConstraintDescriptor<?> descriptor;

		TestContext(ConstraintDescriptor<?> descriptor) {
			this.descriptor = descriptor;
		}

		@Override
		public ConstraintDescriptor<?> getConstraintDescriptor() {
			return descriptor;
		}

		@Override
		public Object getValidatedValue() {
			return null;
		}

		@Override
		public <T> T unwrap(Class<T> type) {
			throw new RuntimeException( "ups" );
		}
	}

	public class DummyEntity {
		@NotNull
		String foo;

		@Size(min = 5, max = 10, message = "size must be between {min} and {max}")
		String bar;

		@Max(value = 10, message = "{replace.in.user.bundle1}")
		String fubar;

		@NotNull(message = "messages can also be overridden at constraint declaration.")
		String snafu;

		@Min(value = 5, message = "must be ${value} at least")
		Integer amount = 3;

		@Min(value = 5, message = "must be ${value * 2} at least")
		Integer doubleAmount = 3;
	}

	public class Person {

		String name;

		@Past
		Date birthday;
	}

	private static class TestBeanWithPropertyConstraint {

		private static final String MESSAGE = "name must not be null";

		@Size(message = MESSAGE, min = 5)
		private final String name;

		public TestBeanWithPropertyConstraint(String name) {
			this.name = name;
		}
	}

	@ValidTestBean(message = TestBeanWithClassLevelConstraint.MESSAGE)
	private static class TestBeanWithClassLevelConstraint {

		public static final String MESSAGE = "Invalid test bean";
	}

	private static class TestMessageInterpolator implements MessageInterpolator {

		public String messageTemplate;
		public ConstraintDescriptor<?> constraintDescriptor;
		public Object validatedValue;

		@Override
		public String interpolate(String messageTemplate, Context context) {
			this.messageTemplate = messageTemplate;
			this.constraintDescriptor = context.getConstraintDescriptor();
			this.validatedValue = context.getValidatedValue();

			return null;
		}

		@Override
		public String interpolate(String messageTemplate, Context context, Locale locale) {
			throw new UnsupportedOperationException( "No specific locale is possible" );
		}
	}

	@Documented
	@Constraint(validatedBy = { ValidTestBean.Validator.class })
	@Target({ TYPE })
	@Retention(RUNTIME)
	public @interface ValidTestBean {
		String message() default "default message";

		Class<?>[] groups() default { };

		Class<? extends Payload>[] payload() default { };

		public static class Validator implements ConstraintValidator<ValidTestBean, TestBeanWithClassLevelConstraint> {

			@Override
			public boolean isValid(TestBeanWithClassLevelConstraint object, ConstraintValidatorContext constraintValidatorContext) {
				return false;
			}
		}
	}

	private static class ExceptionThrowingMessageInterpolator implements MessageInterpolator {

		private final RuntimeException exception = new MyInterpolationException();

		@Override
		public String interpolate(String messageTemplate, Context context) {
			throw exception;
		}

		@Override
		public String interpolate(String messageTemplate, Context context, Locale locale) {
			throw new UnsupportedOperationException( "No specific locale is possible" );
		}
	}

	private static class MyInterpolationException extends RuntimeException {
	}
}
