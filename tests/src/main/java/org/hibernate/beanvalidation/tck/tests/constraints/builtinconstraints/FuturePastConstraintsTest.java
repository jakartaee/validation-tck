/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.builtinconstraints;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertNoViolations;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.HijrahDate;
import java.time.chrono.JapaneseDate;
import java.time.chrono.MinguoDate;
import java.time.chrono.ThaiBuddhistDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;

import jakarta.validation.ClockProvider;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests for {@link Future} and {@link Past} built-in constraints.
 *
 * @author Hardy Ferentschik
 * @author Guillaume Smet
 */
@SpecVersion(spec = "beanvalidation", version = "3.0.0")
public class FuturePastConstraintsTest extends AbstractTCKTest {

	private static final ZoneId TZ_BERLIN = ZoneId.of( "Europe/Berlin" );

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( FuturePastConstraintsTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS_PAST, id = "a")
	public void testPastConstraint() {
		Validator validator = TestUtil.getValidatorUnderTest();
		PastDummyEntity dummy = new PastDummyEntity();

		Set<ConstraintViolation<PastDummyEntity>> constraintViolations = validator.validate( dummy );
		assertNoViolations( constraintViolations );

		ZonedDateTime reference = ZonedDateTime.now( TZ_BERLIN );

		ZonedDateTime future = reference.plusYears( 1 ).plusMonths( 1 ).plusHours( 1 );
		dummy = new PastDummyEntity( future );

		constraintViolations = validator.validate( dummy );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Past.class ).withProperty( "date" ),
				violationOf( Past.class ).withProperty( "calendar" ),
				violationOf( Past.class ).withProperty( "instant" ),
				violationOf( Past.class ).withProperty( "hijrahDate" ),
				violationOf( Past.class ).withProperty( "japaneseDate" ),
				violationOf( Past.class ).withProperty( "localDate" ),
				violationOf( Past.class ).withProperty( "localDateTime" ),
				violationOf( Past.class ).withProperty( "minguoDate" ),
				violationOf( Past.class ).withProperty( "offsetDateTime" ),
				violationOf( Past.class ).withProperty( "thaiBuddhistDate" ),
				violationOf( Past.class ).withProperty( "year" ),
				violationOf( Past.class ).withProperty( "yearMonth" ),
				violationOf( Past.class ).withProperty( "zonedDateTime" )
		);

		ZonedDateTime past = reference.minusYears( 1 ).minusMonths( 1 ).minusHours( 1 );
		dummy = new PastDummyEntity( past );

		constraintViolations = validator.validate( dummy );
		assertNoViolations( constraintViolations );
	}

	@Test
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS_PAST, id = "a")
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
		assertNoViolations( constraintViolations );

		ZonedDateTime future = reference.plusMonths( 1 ).plusHours( 1 );
		dummy = new PastRelativePartialDummyEntity( future );

		constraintViolations = validator.validate( dummy );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Past.class ).withProperty( "localTime" ),
				violationOf( Past.class ).withProperty( "monthDay" ),
				violationOf( Past.class ).withProperty( "offsetTime" )
		);

		ZonedDateTime past = reference.minusMonths( 1 ).minusHours( 1 );
		dummy = new PastRelativePartialDummyEntity( past );

		constraintViolations = validator.validate( dummy );
		assertNoViolations( constraintViolations );
	}

	@Test
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS_FUTURE, id = "a")
	public void testFutureConstraint() {
		Validator validator = TestUtil.getValidatorUnderTest();
		FutureDummyEntity dummy = new FutureDummyEntity();

		Set<ConstraintViolation<FutureDummyEntity>> constraintViolations = validator.validate( dummy );
		assertNoViolations( constraintViolations );

		ZonedDateTime reference = ZonedDateTime.now( TZ_BERLIN );

		ZonedDateTime past = reference.minusYears( 1 ).minusMonths( 1 ).minusHours( 1 );
		dummy = new FutureDummyEntity( past );

		constraintViolations = validator.validate( dummy );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Future.class ).withProperty( "date" ),
				violationOf( Future.class ).withProperty( "calendar" ),
				violationOf( Future.class ).withProperty( "instant" ),
				violationOf( Future.class ).withProperty( "hijrahDate" ),
				violationOf( Future.class ).withProperty( "japaneseDate" ),
				violationOf( Future.class ).withProperty( "localDate" ),
				violationOf( Future.class ).withProperty( "localDateTime" ),
				violationOf( Future.class ).withProperty( "minguoDate" ),
				violationOf( Future.class ).withProperty( "offsetDateTime" ),
				violationOf( Future.class ).withProperty( "thaiBuddhistDate" ),
				violationOf( Future.class ).withProperty( "year" ),
				violationOf( Future.class ).withProperty( "yearMonth" ),
				violationOf( Future.class ).withProperty( "zonedDateTime" )
		);

		ZonedDateTime future = reference.plusYears( 1 ).plusMonths( 1 ).plusHours( 1 );
		dummy = new FutureDummyEntity( future );

		constraintViolations = validator.validate( dummy );
		assertNoViolations( constraintViolations );
	}

	@Test
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS_FUTURE, id = "a")
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
		assertNoViolations( constraintViolations );

		ZonedDateTime past = reference.minusMonths( 1 ).minusHours( 1 );
		dummy = new FutureRelativePartialDummyEntity( past );

		constraintViolations = validator.validate( dummy );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Future.class ).withProperty( "localTime" ),
				violationOf( Future.class ).withProperty( "monthDay" ),
				violationOf( Future.class ).withProperty( "offsetTime" )
		);

		ZonedDateTime future = reference.plusMonths( 1 ).plusHours( 1 );
		dummy = new FutureRelativePartialDummyEntity( future );

		constraintViolations = validator.validate( dummy );
		assertNoViolations( constraintViolations );
	}

	@Test
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS_PASTORPRESENT, id = "a")
	public void testPastOrPresentConstraint() {
		ZonedDateTime reference = ZonedDateTime.of( 2016, 6, 6, 14, 26, 0, 0, TZ_BERLIN );

		ValidatorFactory validatorFactory = TestUtil.getConfigurationUnderTest()
				.clockProvider( new FixedClockProvider( reference ) )
				.buildValidatorFactory();
		Validator validator = validatorFactory.getValidator();

		PastDummyEntity pastDummy = new PastDummyEntity( reference );

		Set<ConstraintViolation<PastDummyEntity>> pastConstraintViolations = validator.validate( pastDummy );
		assertThat( pastConstraintViolations ).containsOnlyViolations(
				violationOf( Past.class ).withProperty( "date" ),
				violationOf( Past.class ).withProperty( "calendar" ),
				violationOf( Past.class ).withProperty( "instant" ),
				violationOf( Past.class ).withProperty( "hijrahDate" ),
				violationOf( Past.class ).withProperty( "japaneseDate" ),
				violationOf( Past.class ).withProperty( "localDate" ),
				violationOf( Past.class ).withProperty( "localDateTime" ),
				violationOf( Past.class ).withProperty( "minguoDate" ),
				violationOf( Past.class ).withProperty( "offsetDateTime" ),
				violationOf( Past.class ).withProperty( "thaiBuddhistDate" ),
				violationOf( Past.class ).withProperty( "year" ),
				violationOf( Past.class ).withProperty( "yearMonth" ),
				violationOf( Past.class ).withProperty( "zonedDateTime" )
		);

		PastOrPresentDummyEntity pastOrPresentDummy = new PastOrPresentDummyEntity();
		Set<ConstraintViolation<PastOrPresentDummyEntity>> pastOrPresentConstraintViolations = validator.validate( pastOrPresentDummy );
		assertNoViolations( pastOrPresentConstraintViolations );

		pastOrPresentDummy = new PastOrPresentDummyEntity( reference );
		pastOrPresentConstraintViolations = validator.validate( pastOrPresentDummy );
		assertNoViolations( pastOrPresentConstraintViolations );

		ZonedDateTime pastDate = ZonedDateTime.of( 2015, 5, 5, 13, 14, 0, 0, TZ_BERLIN );
		pastOrPresentDummy = new PastOrPresentDummyEntity( pastDate );
		pastOrPresentConstraintViolations = validator.validate( pastOrPresentDummy );
		assertNoViolations( pastOrPresentConstraintViolations );

		ZonedDateTime futureDate = ZonedDateTime.of( 2017, 7, 7, 15, 32, 0, 0, TZ_BERLIN );
		pastOrPresentDummy = new PastOrPresentDummyEntity( futureDate );
		pastOrPresentConstraintViolations = validator.validate( pastOrPresentDummy );
		assertThat( pastOrPresentConstraintViolations ).containsOnlyViolations(
				violationOf( PastOrPresent.class ).withProperty( "date" ),
				violationOf( PastOrPresent.class ).withProperty( "calendar" ),
				violationOf( PastOrPresent.class ).withProperty( "instant" ),
				violationOf( PastOrPresent.class ).withProperty( "hijrahDate" ),
				violationOf( PastOrPresent.class ).withProperty( "japaneseDate" ),
				violationOf( PastOrPresent.class ).withProperty( "localDate" ),
				violationOf( PastOrPresent.class ).withProperty( "localDateTime" ),
				violationOf( PastOrPresent.class ).withProperty( "minguoDate" ),
				violationOf( PastOrPresent.class ).withProperty( "offsetDateTime" ),
				violationOf( PastOrPresent.class ).withProperty( "thaiBuddhistDate" ),
				violationOf( PastOrPresent.class ).withProperty( "year" ),
				violationOf( PastOrPresent.class ).withProperty( "yearMonth" ),
				violationOf( PastOrPresent.class ).withProperty( "zonedDateTime" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS_PASTORPRESENT, id = "a")
	public void testPastOrPresentConstraintForRelativePartial() {
		ZonedDateTime reference = ZonedDateTime.of( 2016, 6, 6, 14, 26, 0, 0, TZ_BERLIN );

		ValidatorFactory validatorFactory = TestUtil.getConfigurationUnderTest()
				.clockProvider( new FixedClockProvider( reference ) )
				.buildValidatorFactory();
		Validator validator = validatorFactory.getValidator();

		PastRelativePartialDummyEntity pastDummy = new PastRelativePartialDummyEntity( reference );

		Set<ConstraintViolation<PastRelativePartialDummyEntity>> pastConstraintViolations = validator.validate( pastDummy );
		assertThat( pastConstraintViolations ).containsOnlyViolations(
				violationOf( Past.class ).withProperty( "localTime" ),
				violationOf( Past.class ).withProperty( "monthDay" ),
				violationOf( Past.class ).withProperty( "offsetTime" )
		);

		PastOrPresentRelativePartialDummyEntity pastOrPresentDummy = new PastOrPresentRelativePartialDummyEntity();
		Set<ConstraintViolation<PastOrPresentRelativePartialDummyEntity>> pastOrPresentConstraintViolations = validator.validate( pastOrPresentDummy );
		assertNoViolations( pastOrPresentConstraintViolations );

		pastOrPresentDummy = new PastOrPresentRelativePartialDummyEntity( reference );
		pastOrPresentConstraintViolations = validator.validate( pastOrPresentDummy );
		assertNoViolations( pastOrPresentConstraintViolations );

		ZonedDateTime pastDate = ZonedDateTime.of( 2015, 5, 5, 13, 14, 0, 0, TZ_BERLIN );
		pastOrPresentDummy = new PastOrPresentRelativePartialDummyEntity( pastDate );
		pastOrPresentConstraintViolations = validator.validate( pastOrPresentDummy );
		assertNoViolations( pastOrPresentConstraintViolations );

		ZonedDateTime futureDate = ZonedDateTime.of( 2017, 7, 7, 15, 32, 0, 0, TZ_BERLIN );
		pastOrPresentDummy = new PastOrPresentRelativePartialDummyEntity( futureDate );
		pastOrPresentConstraintViolations = validator.validate( pastOrPresentDummy );
		assertThat( pastOrPresentConstraintViolations ).containsOnlyViolations(
				violationOf( PastOrPresent.class ).withProperty( "localTime" ),
				violationOf( PastOrPresent.class ).withProperty( "monthDay" ),
				violationOf( PastOrPresent.class ).withProperty( "offsetTime" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS_FUTUREORPRESENT, id = "a")
	public void testFutureOrPresentConstraint() {
		ZonedDateTime reference = ZonedDateTime.of( 2016, 6, 6, 14, 26, 0, 0, TZ_BERLIN );

		ValidatorFactory validatorFactory = TestUtil.getConfigurationUnderTest()
				.clockProvider( new FixedClockProvider( reference ) )
				.buildValidatorFactory();
		Validator validator = validatorFactory.getValidator();

		FutureDummyEntity futureDummy = new FutureDummyEntity( reference );

		Set<ConstraintViolation<FutureDummyEntity>> futureConstraintViolations = validator.validate( futureDummy );
		assertThat( futureConstraintViolations ).containsOnlyViolations(
				violationOf( Future.class ).withProperty( "date" ),
				violationOf( Future.class ).withProperty( "calendar" ),
				violationOf( Future.class ).withProperty( "instant" ),
				violationOf( Future.class ).withProperty( "hijrahDate" ),
				violationOf( Future.class ).withProperty( "japaneseDate" ),
				violationOf( Future.class ).withProperty( "localDate" ),
				violationOf( Future.class ).withProperty( "localDateTime" ),
				violationOf( Future.class ).withProperty( "minguoDate" ),
				violationOf( Future.class ).withProperty( "offsetDateTime" ),
				violationOf( Future.class ).withProperty( "thaiBuddhistDate" ),
				violationOf( Future.class ).withProperty( "year" ),
				violationOf( Future.class ).withProperty( "yearMonth" ),
				violationOf( Future.class ).withProperty( "zonedDateTime" )
		);

		FutureOrPresentDummyEntity futureOrPresentDummy = new FutureOrPresentDummyEntity();
		Set<ConstraintViolation<FutureOrPresentDummyEntity>> futureOrPresentConstraintViolations = validator.validate( futureOrPresentDummy );
		assertNoViolations( futureOrPresentConstraintViolations );

		futureOrPresentDummy = new FutureOrPresentDummyEntity( reference );
		futureOrPresentConstraintViolations = validator.validate( futureOrPresentDummy );
		assertNoViolations( futureOrPresentConstraintViolations );

		ZonedDateTime futureDate = ZonedDateTime.of( 2017, 7, 7, 15, 32, 0, 0, TZ_BERLIN );
		futureOrPresentDummy = new FutureOrPresentDummyEntity( futureDate );
		futureOrPresentConstraintViolations = validator.validate( futureOrPresentDummy );
		assertNoViolations( futureOrPresentConstraintViolations );

		ZonedDateTime pastDate = ZonedDateTime.of( 2015, 4, 3, 12, 20, 0, 0, TZ_BERLIN );
		futureOrPresentDummy = new FutureOrPresentDummyEntity( pastDate );
		futureOrPresentConstraintViolations = validator.validate( futureOrPresentDummy );
		assertThat( futureOrPresentConstraintViolations ).containsOnlyViolations(
				violationOf( FutureOrPresent.class ).withProperty( "date" ),
				violationOf( FutureOrPresent.class ).withProperty( "calendar" ),
				violationOf( FutureOrPresent.class ).withProperty( "instant" ),
				violationOf( FutureOrPresent.class ).withProperty( "hijrahDate" ),
				violationOf( FutureOrPresent.class ).withProperty( "japaneseDate" ),
				violationOf( FutureOrPresent.class ).withProperty( "localDate" ),
				violationOf( FutureOrPresent.class ).withProperty( "localDateTime" ),
				violationOf( FutureOrPresent.class ).withProperty( "minguoDate" ),
				violationOf( FutureOrPresent.class ).withProperty( "offsetDateTime" ),
				violationOf( FutureOrPresent.class ).withProperty( "thaiBuddhistDate" ),
				violationOf( FutureOrPresent.class ).withProperty( "year" ),
				violationOf( FutureOrPresent.class ).withProperty( "yearMonth" ),
				violationOf( FutureOrPresent.class ).withProperty( "zonedDateTime" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS_FUTUREORPRESENT, id = "a")
	public void testFutureOrPresentConstraintForRelativePartial() {
		ZonedDateTime reference = ZonedDateTime.of( 2016, 6, 6, 14, 26, 0, 0, TZ_BERLIN );

		ValidatorFactory validatorFactory = TestUtil.getConfigurationUnderTest()
				.clockProvider( new FixedClockProvider( reference ) )
				.buildValidatorFactory();
		Validator validator = validatorFactory.getValidator();

		FutureRelativePartialDummyEntity futureDummy = new FutureRelativePartialDummyEntity( reference );

		Set<ConstraintViolation<FutureRelativePartialDummyEntity>> futureConstraintViolations = validator.validate( futureDummy );
		assertThat( futureConstraintViolations ).containsOnlyViolations(
				violationOf( Future.class ).withProperty( "localTime" ),
				violationOf( Future.class ).withProperty( "monthDay" ),
				violationOf( Future.class ).withProperty( "offsetTime" )
		);

		FutureOrPresentRelativePartialDummyEntity futureOrPresentDummy = new FutureOrPresentRelativePartialDummyEntity();
		Set<ConstraintViolation<FutureOrPresentRelativePartialDummyEntity>> futureOrPresentConstraintViolations = validator.validate( futureOrPresentDummy );
		assertNoViolations( futureOrPresentConstraintViolations );

		futureOrPresentDummy = new FutureOrPresentRelativePartialDummyEntity( reference );
		futureOrPresentConstraintViolations = validator.validate( futureOrPresentDummy );
		assertNoViolations( futureOrPresentConstraintViolations );

		ZonedDateTime futureDate = ZonedDateTime.of( 2017, 7, 7, 15, 32, 0, 0, TZ_BERLIN );
		futureOrPresentDummy = new FutureOrPresentRelativePartialDummyEntity( futureDate );
		futureOrPresentConstraintViolations = validator.validate( futureOrPresentDummy );
		assertNoViolations( futureOrPresentConstraintViolations );

		ZonedDateTime pastDate = ZonedDateTime.of( 2015, 4, 3, 12, 20, 0, 0, TZ_BERLIN );
		futureOrPresentDummy = new FutureOrPresentRelativePartialDummyEntity( pastDate );
		futureOrPresentConstraintViolations = validator.validate( futureOrPresentDummy );
		assertThat( futureOrPresentConstraintViolations ).containsOnlyViolations(
				violationOf( FutureOrPresent.class ).withProperty( "localTime" ),
				violationOf( FutureOrPresent.class ).withProperty( "monthDay" ),
				violationOf( FutureOrPresent.class ).withProperty( "offsetTime" )
		);
	}

	public class PastDummyEntity {

		@Past
		private Calendar calendar;

		@Past
		private Date date;

		@Past
		private HijrahDate hijrahDate;

		@Past
		private Instant instant;

		@Past
		private JapaneseDate japaneseDate;

		@Past
		private LocalDate localDate;

		@Past
		private LocalDateTime localDateTime;

		@Past
		private MinguoDate minguoDate;

		@Past
		private OffsetDateTime offsetDateTime;

		@Past
		private ThaiBuddhistDate thaiBuddhistDate;

		@Past
		private Year year;

		@Past
		private YearMonth yearMonth;

		@Past
		private ZonedDateTime zonedDateTime;

		public PastDummyEntity() {
		}

		public PastDummyEntity(ZonedDateTime dateTime) {
			calendar = GregorianCalendar.from( dateTime );
			date = calendar.getTime();

			instant = dateTime.toInstant();
			localDateTime = dateTime.toLocalDateTime();

			hijrahDate = HijrahDate.from( dateTime );
			japaneseDate = JapaneseDate.from( dateTime );
			localDate = LocalDate.from( dateTime );
			minguoDate = MinguoDate.from( dateTime );
			offsetDateTime = dateTime.toOffsetDateTime();
			thaiBuddhistDate = ThaiBuddhistDate.from( dateTime );
			year = Year.from( dateTime );
			yearMonth = YearMonth.from( dateTime );
			zonedDateTime = dateTime;
		}
	}

	private static class PastOrPresentDummyEntity {

		@PastOrPresent
		private Calendar calendar;

		@PastOrPresent
		private Date date;

		@PastOrPresent
		private HijrahDate hijrahDate;

		@PastOrPresent
		private Instant instant;

		@PastOrPresent
		private JapaneseDate japaneseDate;

		@PastOrPresent
		private LocalDate localDate;

		@PastOrPresent
		private LocalDateTime localDateTime;

		@PastOrPresent
		private MinguoDate minguoDate;

		@PastOrPresent
		private OffsetDateTime offsetDateTime;

		@PastOrPresent
		private ThaiBuddhistDate thaiBuddhistDate;

		@PastOrPresent
		private Year year;

		@PastOrPresent
		private YearMonth yearMonth;

		@PastOrPresent
		private ZonedDateTime zonedDateTime;

		public PastOrPresentDummyEntity() {
		}

		public PastOrPresentDummyEntity(ZonedDateTime dateTime) {
			calendar = GregorianCalendar.from( dateTime );
			date = calendar.getTime();

			instant = dateTime.toInstant();
			localDateTime = dateTime.toLocalDateTime();

			hijrahDate = HijrahDate.from( dateTime );
			japaneseDate = JapaneseDate.from( dateTime );
			localDate = LocalDate.from( dateTime );
			minguoDate = MinguoDate.from( dateTime );
			offsetDateTime = dateTime.toOffsetDateTime();
			thaiBuddhistDate = ThaiBuddhistDate.from( dateTime );
			year = Year.from( dateTime );
			yearMonth = YearMonth.from( dateTime );
			zonedDateTime = dateTime;
		}
	}

	public class PastRelativePartialDummyEntity {

		@Past
		private LocalTime localTime;

		@Past
		private MonthDay monthDay;

		@Past
		private OffsetTime offsetTime;

		public PastRelativePartialDummyEntity() {
		}

		public PastRelativePartialDummyEntity(ZonedDateTime dateTime) {
			localTime = dateTime.toLocalTime();
			monthDay = MonthDay.from( dateTime );
			offsetTime = OffsetTime.from( dateTime );
		}
	}


	private static class PastOrPresentRelativePartialDummyEntity {

		@PastOrPresent
		private LocalTime localTime;

		@PastOrPresent
		private MonthDay monthDay;

		@PastOrPresent
		private OffsetTime offsetTime;

		public PastOrPresentRelativePartialDummyEntity() {
		}

		public PastOrPresentRelativePartialDummyEntity(ZonedDateTime dateTime) {
			localTime = dateTime.toLocalTime();
			monthDay = MonthDay.from( dateTime );
			offsetTime = OffsetTime.from( dateTime );
		}
	}

	public class FutureDummyEntity {

		@Future
		private Calendar calendar;

		@Future
		private Date date;

		@Future
		private HijrahDate hijrahDate;

		@Future
		private Instant instant;

		@Future
		private JapaneseDate japaneseDate;

		@Future
		private LocalDate localDate;

		@Future
		private LocalDateTime localDateTime;

		@Future
		private MinguoDate minguoDate;

		@Future
		private OffsetDateTime offsetDateTime;

		@Future
		private ThaiBuddhistDate thaiBuddhistDate;

		@Future
		private Year year;

		@Future
		private YearMonth yearMonth;

		@Future
		private ZonedDateTime zonedDateTime;

		public FutureDummyEntity() {
		}

		public FutureDummyEntity(ZonedDateTime dateTime) {
			calendar = GregorianCalendar.from( dateTime );
			date = calendar.getTime();

			instant = dateTime.toInstant();
			localDateTime = dateTime.toLocalDateTime();

			hijrahDate = HijrahDate.from( dateTime );
			japaneseDate = JapaneseDate.from( dateTime );
			localDate = LocalDate.from( dateTime );
			minguoDate = MinguoDate.from( dateTime );
			offsetDateTime = dateTime.toOffsetDateTime();
			thaiBuddhistDate = ThaiBuddhistDate.from( dateTime );
			year = Year.from( dateTime );
			yearMonth = YearMonth.from( dateTime );
			zonedDateTime = dateTime;
		}
	}

	private static class FutureOrPresentDummyEntity {

		@FutureOrPresent
		private Calendar calendar;

		@FutureOrPresent
		private Date date;

		@FutureOrPresent
		private HijrahDate hijrahDate;

		@FutureOrPresent
		private Instant instant;

		@FutureOrPresent
		private JapaneseDate japaneseDate;

		@FutureOrPresent
		private LocalDate localDate;

		@FutureOrPresent
		private LocalDateTime localDateTime;

		@FutureOrPresent
		private MinguoDate minguoDate;

		@FutureOrPresent
		private OffsetDateTime offsetDateTime;

		@FutureOrPresent
		private ThaiBuddhistDate thaiBuddhistDate;

		@FutureOrPresent
		private Year year;

		@FutureOrPresent
		private YearMonth yearMonth;

		@FutureOrPresent
		private ZonedDateTime zonedDateTime;

		private FutureOrPresentDummyEntity() {
		}

		private FutureOrPresentDummyEntity(ZonedDateTime dateTime) {
			calendar = GregorianCalendar.from( dateTime );
			date = calendar.getTime();

			instant = dateTime.toInstant();
			localDateTime = dateTime.toLocalDateTime();

			hijrahDate = HijrahDate.from( dateTime );
			japaneseDate = JapaneseDate.from( dateTime );
			localDate = LocalDate.from( dateTime );
			minguoDate = MinguoDate.from( dateTime );
			offsetDateTime = dateTime.toOffsetDateTime();
			thaiBuddhistDate = ThaiBuddhistDate.from( dateTime );
			year = Year.from( dateTime );
			yearMonth = YearMonth.from( dateTime );
			zonedDateTime = dateTime;
		}
	}

	public class FutureRelativePartialDummyEntity {

		@Future
		private LocalTime localTime;

		@Future
		private MonthDay monthDay;

		@Future
		private OffsetTime offsetTime;

		public FutureRelativePartialDummyEntity() {
		}

		public FutureRelativePartialDummyEntity(ZonedDateTime dateTime) {
			localTime = dateTime.toLocalTime();
			monthDay = MonthDay.from( dateTime );
			offsetTime = OffsetTime.from( dateTime );
		}
	}

	private static class FutureOrPresentRelativePartialDummyEntity {

		@FutureOrPresent
		private LocalTime localTime;

		@FutureOrPresent
		private MonthDay monthDay;

		@FutureOrPresent
		private OffsetTime offsetTime;

		public FutureOrPresentRelativePartialDummyEntity() {
		}

		public FutureOrPresentRelativePartialDummyEntity(ZonedDateTime dateTime) {
			localTime = dateTime.toLocalTime();
			monthDay = MonthDay.from( dateTime );
			offsetTime = OffsetTime.from( dateTime );
		}
	}

	private static class FixedClockProvider implements ClockProvider {

		private final Clock clock;

		public FixedClockProvider(ZonedDateTime dateTime) {
			clock = Clock.fixed( dateTime.toInstant(), dateTime.getZone() );
		}

		@Override
		public Clock getClock() {
			return clock;
		}

	}
}
