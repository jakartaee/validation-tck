/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.time;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectNumberOfViolations;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectPropertyPaths;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertSame;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Set;

import javax.validation.ClockProvider;
import javax.validation.Configuration;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import javax.validation.Payload;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Past;

import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Guillaume Smet
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class ClockProviderTest extends Arquillian {

	/**
	 * Maximum deviation accepted between 2 {@link Clock}s to consider them as equal.
	 */
	private static final long ACCEPTABLE_CLOCK_DEVIATION_IN_MS = 10;

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClassPackage( ClockProviderTest.class )
				.build();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.5.3", id = "e")
	})
	public void testDefaultClockProviderProvidedByConfiguration() {
		Configuration<?> config = TestUtil.getConfigurationUnderTest();
		assertNotNull( config.getDefaultClockProvider() );
		checkClockProviderHasDefaultProperties( config.getDefaultClockProvider() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "3.4.1", id = "a"),
			@SpecAssertion(section = "3.4.1", id = "b")
	})
	public void testClockProviderProviderByConstraintValidatorContext() {
		TestUtil.getValidatorUnderTest().validate( new DefaultClockProviderIsValidEntity() );
	}

	@Test
	@SpecAssertion(section = "5.5.2", id = "f")
	public void testCustomClockProviderFromValidatorFactory() {
		Configuration<?> configuration = TestUtil.getConfigurationUnderTest();
		CustomClockProvider clockProvider = new CustomClockProvider();

		configuration.clockProvider( clockProvider );
		ValidatorFactory factory = configuration.buildValidatorFactory();

		assertSame( factory.getClockProvider(), clockProvider );
	}

	@Test
	@SpecAssertion(section = "5.5.2", id = "f")
	public void testCustomClockProviderViaConfiguration() {
		// use the default configuration
		Validator validator = TestUtil.getValidatorUnderTest();

		Person person = new Person();

		person.setBirthday( Instant.now().minus( Duration.ofDays( 15 ) ) );
		Set<ConstraintViolation<Person>> constraintViolations = validator.validate( person );
		assertCorrectNumberOfViolations( constraintViolations, 0 );

		person.setBirthday( Instant.now().plus( Duration.ofDays( 15 ) ) );
		constraintViolations = validator.validate( person );
		assertCorrectPropertyPaths( constraintViolations, "birthday" );

		// get a new validator with a custom configuration
		validator = TestUtil.getConfigurationUnderTest()
				.clockProvider( new FixedClockProvider( ZonedDateTime.now().plus( Duration.ofDays( 60 ) ) ) )
				.buildValidatorFactory()
				.getValidator();

		person.setBirthday( Instant.now().plus( Duration.ofDays( 15 ) ) );
		constraintViolations = validator.validate( person );
		assertCorrectNumberOfViolations( constraintViolations, 0 );

		person.setBirthday( Instant.now().plus( Duration.ofDays( 90 ) ) );
		constraintViolations = validator.validate( person );
		assertCorrectPropertyPaths( constraintViolations, "birthday" );
	}

	@Test(expectedExceptions = ValidationException.class)
	@SpecAssertion(section = "3.4", id = "n")
	public void testClockProviderExceptionsGetWrappedInValidationException() {
		ExceptionThrowingClockProvider clockProvider = new ExceptionThrowingClockProvider();
		Configuration<?> config = TestUtil.getConfigurationUnderTest().clockProvider( clockProvider );

		ValidatorFactory factory = config.buildValidatorFactory();
		Validator v = factory.getValidator();

		Person person = new Person();
		person.setBirthday( Instant.now().minus( Duration.ofDays( 3 ) ) );

		v.validate( person );
	}

	@Test
	@SpecAssertion(section = "5.5.2", id = "g")
	public void canConfigureClockProviderForValidator() {
		Validator validator = TestUtil.getValidatorUnderTest();

		Person person = new Person();
		person.setBirthday( Instant.now().plus( Duration.ofDays( 15 ) ) );

		Set<ConstraintViolation<Person>> constraintViolations = validator.validate( person );
		assertCorrectPropertyPaths( constraintViolations, "birthday" );

		validator = TestUtil.getValidatorFactoryUnderTest()
				.usingContext()
						.clockProvider( new FixedClockProvider( ZonedDateTime.now().plus( Duration.ofDays( 60 ) ) ) )
				.getValidator();

		constraintViolations = validator.validate( person );
		assertCorrectNumberOfViolations( constraintViolations, 0 );
	}

	private static void checkClockProviderHasDefaultProperties(ClockProvider clockProvider) {
		Clock clock = clockProvider.getClock();
		Clock systemClock = Clock.systemDefaultZone();

		if ( !systemClock.getZone().equals( clock.getZone() ) ) {
			throw new AssertionError( "The default clock provider should use the default system time zone." );
		}
		if ( Math.abs( clock.millis() - systemClock.millis() ) > ACCEPTABLE_CLOCK_DEVIATION_IN_MS ) {
			throw new AssertionError( "The default clock provider should return the system time." );
		}
	}

	private static class Person {

		@Past
		private Instant birthday;

		public void setBirthday(Instant birthday) {
			this.birthday = birthday;
		}
	}

	private static class CustomClockProvider implements ClockProvider {

		@Override
		public Clock getClock() {
			return Clock.systemDefaultZone();
		}
	}

	private static class ExceptionThrowingClockProvider implements ClockProvider {

		@Override
		public Clock getClock() {
			throw new RuntimeException( "This clock provider throws an exception that should be wrapped in a ValidationException" );
		}
	}

	private static class DefaultClockProviderIsValidEntity {

		@DefaultClockProviderIsValid
		private LocalDateTime localDateTime;
	}

	@Constraint(validatedBy = DefaultClockProviderValidator.class)
	@Documented
	@Target({ METHOD, FIELD, TYPE })
	@Retention(RUNTIME)
	public @interface DefaultClockProviderIsValid {

		String message() default "Default clock provider is invalid.";

		Class<?>[] groups() default { };

		Class<? extends Payload>[] payload() default {};
	}

	public static class DefaultClockProviderValidator implements ConstraintValidator<DefaultClockProviderIsValid, LocalDateTime> {

		@Override
		public boolean isValid(LocalDateTime localDateTime, ConstraintValidatorContext constraintValidatorContext) {
			checkClockProviderHasDefaultProperties( constraintValidatorContext.getClockProvider() );
			return true;
		}
	}
}
