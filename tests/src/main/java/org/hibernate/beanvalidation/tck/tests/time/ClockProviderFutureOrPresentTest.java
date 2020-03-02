/**
 * Jakarta Bean Validation TCK
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

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.FutureOrPresent;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Checks that the {@code ClockProvider} contract is used in {@code @FutureOrPresent} validators.
 *
 * @author Guillaume Smet
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class ClockProviderFutureOrPresentTest extends AbstractTCKTest {

	private static final ZoneId TZ_BERLIN = ZoneId.of( "Europe/Berlin" );

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( ClockProviderPastTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS_FUTUREORPRESENT, id = "a")
	public void clockProviderIsUsed() {
		FutureOrPresentDummyEntity dummy = new FutureOrPresentDummyEntity( ZonedDateTime.of( 2099, 1, 12, 5, 0, 0, 0, TZ_BERLIN ) );

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

		Set<ConstraintViolation<FutureOrPresentDummyEntity>> violations = validator.validate( dummy );
		assertThat( violations ).containsOnlyViolations(
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
	public void clockProviderIsUsedForRelativePartials() {
		FutureOrPresentRelativePartialDummyEntity dummy = new FutureOrPresentRelativePartialDummyEntity( ZonedDateTime.of( 2016, 6, 6, 14, 45, 0, 0, TZ_BERLIN ) );

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

		Set<ConstraintViolation<FutureOrPresentRelativePartialDummyEntity>> violations = validator.validate( dummy );
		assertThat( violations ).containsOnlyViolations(
				violationOf( FutureOrPresent.class ).withProperty( "localTime" ),
				violationOf( FutureOrPresent.class ).withProperty( "monthDay" ),
				violationOf( FutureOrPresent.class ).withProperty( "offsetTime" )
		);
	}

}
