package org.hibernate.beanvalidation.tck.tests.constraints.builtinconstraints;

import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectNumberOfViolations;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectPropertyPaths;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Future;
import javax.validation.constraints.Past;

import org.hibernate.beanvalidation.tck.tests.time.FixedClockProvider;
import org.hibernate.beanvalidation.tck.tests.time.FutureDummyEntity;
import org.hibernate.beanvalidation.tck.tests.time.FutureRelativePartialDummyEntity;
import org.hibernate.beanvalidation.tck.tests.time.PastDummyEntity;
import org.hibernate.beanvalidation.tck.tests.time.PastRelativePartialDummyEntity;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests for {@link Future} and {@link Past} built-in constraints.
 *
 * @author Hardy Ferentschik
 * @author Guillaume Smet
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class FuturePastConstraintsTest {

	private static final ZoneId TZ_BERLIN = ZoneId.of( "Europe/Berlin" );

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClass( FuturePastConstraintsTest.class )
				.build();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "7", id = "a"),
			@SpecAssertion(section = "7", id = "m")
	})
	public void testPastConstraint() {
		Validator validator = TestUtil.getValidatorUnderTest();
		PastDummyEntity dummy = new PastDummyEntity();

		Set<ConstraintViolation<PastDummyEntity>> constraintViolations = validator.validate( dummy );
		assertCorrectNumberOfViolations( constraintViolations, 0 );

		ZonedDateTime reference = ZonedDateTime.now( TZ_BERLIN );

		ZonedDateTime future = reference.plusYears( 1 ).plusMonths( 1 ).plusHours( 1 );
		dummy = new PastDummyEntity( future );

		constraintViolations = validator.validate( dummy );
		assertCorrectNumberOfViolations( constraintViolations, 13 );
		assertCorrectPropertyPaths(
				constraintViolations, "date", "calendar", "instant", "hijrahDate", "japaneseDate", "localDate", "localDateTime",
				"minguoDate", "offsetDateTime", "thaiBuddhistDate", "year", "yearMonth", "zonedDateTime"
		);

		ZonedDateTime past = reference.minusYears( 1 ).minusMonths( 1 ).minusHours( 1 );
		dummy = new PastDummyEntity( past );

		constraintViolations = validator.validate( dummy );
		assertCorrectNumberOfViolations( constraintViolations, 0 );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "7", id = "a"),
			@SpecAssertion(section = "7", id = "m")
	})
	public void testPastConstraintForRelativePartial() {
		// For partials not referencing a precise point in time, we need to use the FixedClockProvider
		// to make sure the tests are working at any date

		ZonedDateTime reference = ZonedDateTime.of( 2016, 6, 6, 14, 0, 0, 0, TZ_BERLIN );

		ValidatorFactory validatorFactory = TestUtil.getConfigurationUnderTest()
				.clockProvider( new FixedClockProvider( reference ) )
				.buildValidatorFactory();
		Validator validator = validatorFactory.getValidator();

		PastRelativePartialDummyEntity dummy = new PastRelativePartialDummyEntity();

		Set<ConstraintViolation<PastRelativePartialDummyEntity>> constraintViolations = validator.validate( dummy );
		assertCorrectNumberOfViolations( constraintViolations, 0 );

		ZonedDateTime future = reference.plusMonths( 1 ).plusHours( 1 );
		dummy = new PastRelativePartialDummyEntity( future );

		constraintViolations = validator.validate( dummy );
		assertCorrectNumberOfViolations( constraintViolations, 3 );
		assertCorrectPropertyPaths(
				constraintViolations, "localTime", "monthDay", "offsetTime"
		);

		ZonedDateTime past = reference.minusMonths( 1 ).minusHours( 1 );
		dummy = new PastRelativePartialDummyEntity( past );

		constraintViolations = validator.validate( dummy );
		assertCorrectNumberOfViolations( constraintViolations, 0 );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "7", id = "a"),
			@SpecAssertion(section = "7", id = "n")
	})
	public void testFutureConstraint() {
		Validator validator = TestUtil.getValidatorUnderTest();
		FutureDummyEntity dummy = new FutureDummyEntity();

		Set<ConstraintViolation<FutureDummyEntity>> constraintViolations = validator.validate( dummy );
		assertCorrectNumberOfViolations( constraintViolations, 0 );

		ZonedDateTime reference = ZonedDateTime.now( TZ_BERLIN );

		ZonedDateTime past = reference.minusYears( 1 ).minusMonths( 1 ).minusHours( 1 );
		dummy = new FutureDummyEntity( past );

		constraintViolations = validator.validate( dummy );
		assertCorrectNumberOfViolations( constraintViolations, 13 );
		assertCorrectPropertyPaths(
				constraintViolations, "date", "calendar", "instant", "hijrahDate", "japaneseDate", "localDate", "localDateTime",
				"minguoDate", "offsetDateTime", "thaiBuddhistDate", "year", "yearMonth", "zonedDateTime"
		);

		ZonedDateTime future = reference.plusYears( 1 ).plusMonths( 1 ).plusHours( 1 );
		dummy = new FutureDummyEntity( future );

		constraintViolations = validator.validate( dummy );
		assertCorrectNumberOfViolations( constraintViolations, 0 );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "7", id = "a"),
			@SpecAssertion(section = "7", id = "n")
	})
	public void testFutureConstraintForRelativePartial() {
		// For partials not referencing a precise point in time, we need to use the FixedClockProvider
		// to make sure the tests are working at any date

		ZonedDateTime reference = ZonedDateTime.of( 2016, 6, 6, 14, 0, 0, 0, TZ_BERLIN );

		ValidatorFactory validatorFactory = TestUtil.getConfigurationUnderTest()
				.clockProvider( new FixedClockProvider( reference ) )
				.buildValidatorFactory();
		Validator validator = validatorFactory.getValidator();

		FutureRelativePartialDummyEntity dummy = new FutureRelativePartialDummyEntity();

		Set<ConstraintViolation<FutureRelativePartialDummyEntity>> constraintViolations = validator.validate( dummy );
		assertCorrectNumberOfViolations( constraintViolations, 0 );

		ZonedDateTime past = reference.minusMonths( 1 ).minusHours( 1 );
		dummy = new FutureRelativePartialDummyEntity( past );

		constraintViolations = validator.validate( dummy );
		assertCorrectNumberOfViolations( constraintViolations, 3 );
		assertCorrectPropertyPaths(
				constraintViolations, "localTime", "monthDay", "offsetTime"
		);

		ZonedDateTime future = reference.plusMonths( 1 ).plusHours( 1 );
		dummy = new FutureRelativePartialDummyEntity( future );

		constraintViolations = validator.validate( dummy );
		assertCorrectNumberOfViolations( constraintViolations, 0 );
	}

}
