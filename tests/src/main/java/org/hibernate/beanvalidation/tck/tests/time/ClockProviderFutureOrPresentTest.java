/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.time;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertNumberOfViolations;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.pathWith;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

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
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "t")
	public void clockProviderIsUsed() {
		FutureOrPresentDummyEntity dummy = new FutureOrPresentDummyEntity( ZonedDateTime.of( 2099, 1, 12, 5, 0, 0, 0, TZ_BERLIN ) );

		Validator validator = TestUtil.getValidatorUnderTest();

		assertNumberOfViolations( validator.validate( dummy ), 0 );

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
		assertNumberOfViolations( violations, 13 );
		assertThat( violations ).containsOnlyPaths(
				pathWith()
						.property( "date" ),
				pathWith()
						.property( "calendar" ),
				pathWith()
						.property( "instant" ),
				pathWith()
						.property( "hijrahDate" ),
				pathWith()
						.property( "japaneseDate" ),
				pathWith()
						.property( "localDate" ),
				pathWith()
						.property( "localDateTime" ),
				pathWith()
						.property( "minguoDate" ),
				pathWith()
						.property( "offsetDateTime" ),
				pathWith()
						.property( "thaiBuddhistDate" ),
				pathWith()
						.property( "year" ),
				pathWith()
						.property( "yearMonth" ),
				pathWith()
						.property( "zonedDateTime" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "t")
	public void clockProviderIsUsedForRelativePartials() {
		FutureOrPresentRelativePartialDummyEntity dummy = new FutureOrPresentRelativePartialDummyEntity( ZonedDateTime.of( 2016, 6, 6, 14, 45, 0, 0, TZ_BERLIN ) );

		FixedClockProvider clockProvider = new FixedClockProvider( ZonedDateTime.of( 2015, 2, 15, 4, 0, 0, 0, TZ_BERLIN ) );
		ValidatorFactory validatorFactory = TestUtil.getConfigurationUnderTest()
				.clockProvider( clockProvider )
				.buildValidatorFactory();
		Validator validator = validatorFactory.getValidator();

		assertNumberOfViolations( validator.validate( dummy ), 0 );

		clockProvider = new FixedClockProvider( ZonedDateTime.of( 2016, 8, 17, 17, 45, 0, 0, TZ_BERLIN ) );
		validatorFactory = TestUtil.getConfigurationUnderTest()
				.clockProvider( clockProvider )
				.buildValidatorFactory();
		validator = validatorFactory.getValidator();

		Set<ConstraintViolation<FutureOrPresentRelativePartialDummyEntity>> violations = validator.validate( dummy );
		assertNumberOfViolations( violations, 3 );
		assertThat( violations ).containsOnlyPaths(
				pathWith()
						.property( "localTime" ),
				pathWith()
						.property( "monthDay" ),
				pathWith()
						.property( "offsetTime" )
		);
	}

}
