/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.time;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertNoViolations;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Set;
import java.util.TimeZone;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Past;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Checks that the {@code ClockProvider} contract is used in {@code @Past} validators.
 *
 * @author Gunnar Morling
 * @author Guillaume Smet
 */
@SpecVersion(spec = "beanvalidation", version = "3.0.0")
public class ClockProviderPastTest extends AbstractTCKTest {

	private static final ZoneId TZ_BERLIN = ZoneId.of( "Europe/Berlin" );

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( ClockProviderPastTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS_PAST, id = "a")
	public void clockProviderIsUsed() {
		PastDummyEntity dummy = new PastDummyEntity( ZonedDateTime.of( 1985, 6, 12, 3, 0, 0, 0, TZ_BERLIN ) );

		Validator validator = TestUtil.getValidatorUnderTest();

		assertNoViolations( validator.validate( dummy ) );

		FixedClockProvider clockProvider = new FixedClockProvider( ZonedDateTime.of( 1984, 2, 15, 4, 0, 0, 0, TZ_BERLIN ) );
		ValidatorFactory validatorFactory = TestUtil.getConfigurationUnderTest()
				.clockProvider( clockProvider )
				.buildValidatorFactory();
		validator = validatorFactory.getValidator();

		Set<ConstraintViolation<PastDummyEntity>> violations = validator.validate( dummy );
		assertThat( violations ).containsOnlyViolations(
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
	}

	@Test
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS_PAST, id = "a")
	public void clockProviderIsUsedForRelativePartials() {
		Calendar cal = GregorianCalendar.getInstance( TimeZone.getTimeZone( TZ_BERLIN ) );
		cal.set( 2016, 6, 6 );
		cal.set( Calendar.HOUR_OF_DAY, 14 );
		cal.set( Calendar.MINUTE, 45 );

		PastRelativePartialDummyEntity dummy = new PastRelativePartialDummyEntity( ZonedDateTime.of( 2016, 6, 6, 14, 45, 0, 0, TZ_BERLIN ) );

		FixedClockProvider clockProvider = new FixedClockProvider( ZonedDateTime.of( 2016, 8, 15, 16, 15, 0, 0, TZ_BERLIN ) );
		ValidatorFactory validatorFactory = TestUtil.getConfigurationUnderTest()
				.clockProvider( clockProvider )
				.buildValidatorFactory();
		Validator validator = validatorFactory.getValidator();

		assertNoViolations( validator.validate( dummy ) );

		clockProvider = new FixedClockProvider( ZonedDateTime.of( 2014, 4, 4, 9, 45, 0, 0, TZ_BERLIN ) );
		validatorFactory = TestUtil.getConfigurationUnderTest()
				.clockProvider( clockProvider )
				.buildValidatorFactory();
		validator = validatorFactory.getValidator();

		Set<ConstraintViolation<PastRelativePartialDummyEntity>> violations = validator.validate( dummy );
		assertThat( violations ).containsOnlyViolations(
				violationOf( Past.class ).withProperty( "localTime" ),
				violationOf( Past.class ).withProperty( "monthDay" ),
				violationOf( Past.class ).withProperty( "offsetTime" )
		);
	}

}
