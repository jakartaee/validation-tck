/**
 * Bean Validation TCK
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
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Future;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Checks that the {@code ClockProvider} contract is used in {@code @Future} validators.
 *
 * @author Guillaume Smet
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class ClockProviderFutureTest extends AbstractTCKTest {

	private static final ZoneId TZ_BERLIN = ZoneId.of( "Europe/Berlin" );

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( ClockProviderPastTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS_FUTURE, id = "a")
	public void clockProviderIsUsed() {
		FutureDummyEntity dummy = new FutureDummyEntity( ZonedDateTime.of( 2099, 1, 12, 5, 0, 0, 0, TZ_BERLIN ) );

		Validator validator = TestUtil.getValidatorUnderTest();

		assertNoViolations( validator.validate( dummy ) );

		FixedClockProvider clockProvider = new FixedClockProvider(
				ZonedDateTime.of(
						2100, 2, 15, 4, 0, 0, 0,
						TZ_BERLIN
				)
		);
		ValidatorFactory validatorFactory = TestUtil.getConfigurationUnderTest()
				.clockProvider( clockProvider )
				.buildValidatorFactory();
		validator = validatorFactory.getValidator();

		Set<ConstraintViolation<FutureDummyEntity>> violations = validator.validate( dummy );
		assertThat( violations ).containsOnlyViolations(
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
	}

	@Test
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS_FUTURE, id = "a")
	public void clockProviderIsUsedForRelativePartials() {
		FutureRelativePartialDummyEntity dummy = new FutureRelativePartialDummyEntity( ZonedDateTime.of( 2016, 6, 6, 14, 45, 0, 0, TZ_BERLIN ) );

		FixedClockProvider clockProvider = new FixedClockProvider( ZonedDateTime.of( 2015, 2, 15, 4, 0, 0, 0, TZ_BERLIN ) );
		ValidatorFactory validatorFactory = TestUtil.getConfigurationUnderTest()
				.clockProvider( clockProvider )
				.buildValidatorFactory();
		Validator validator = validatorFactory.getValidator();

		assertNoViolations( validator.validate( dummy ) );

		clockProvider = new FixedClockProvider( ZonedDateTime.of( 2016, 8, 17, 17, 45, 0, 0, TZ_BERLIN ) );
		validatorFactory = TestUtil.getConfigurationUnderTest()
				.clockProvider( clockProvider )
				.buildValidatorFactory();
		validator = validatorFactory.getValidator();

		Set<ConstraintViolation<FutureRelativePartialDummyEntity>> violations = validator.validate( dummy );
		assertThat( violations ).containsOnlyViolations(
				violationOf( Future.class ).withProperty( "localTime" ),
				violationOf( Future.class ).withProperty( "monthDay" ),
				violationOf( Future.class ).withProperty( "offsetTime" )
		);
	}

}
