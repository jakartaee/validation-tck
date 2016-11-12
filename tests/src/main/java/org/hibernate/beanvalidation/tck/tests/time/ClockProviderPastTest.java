/*
* JBoss, Home of Professional Open Source
* Copyright 2016, Red Hat, Inc. and/or its affiliates, and individual contributors
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
package org.hibernate.beanvalidation.tck.tests.time;

import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectNumberOfViolations;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectPropertyPaths;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
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
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class ClockProviderPastTest extends Arquillian {

	private static final ZoneId TZ_BERLIN = ZoneId.of( "Europe/Berlin" );

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClass( ClockProviderPastTest.class )
				.build();
	}

	@Test
	// XXX BVAL-496 update specification references
	@SpecAssertion(section = "", id = "")
	public void clockProviderIsUsed() {
		PastDummyEntity dummy = new PastDummyEntity( ZonedDateTime.of( 1985, 6, 12, 3, 0, 0, 0, TZ_BERLIN ) );

		Validator validator = TestUtil.getValidatorUnderTest();

		assertCorrectNumberOfViolations( validator.validate( dummy ), 0 );

		FixedClockProvider clockProvider = new FixedClockProvider( ZonedDateTime.of( 1984, 2, 15, 4, 0, 0, 0, TZ_BERLIN ) );
		ValidatorFactory validatorFactory = TestUtil.getConfigurationUnderTest()
				.clockProvider( clockProvider )
				.buildValidatorFactory();
		validator = validatorFactory.getValidator();

		assertCorrectNumberOfViolations( validator.validate( dummy ), 13 );
		assertCorrectPropertyPaths(
				validator.validate( dummy ), "date", "calendar", "instant", "hijrahDate", "japaneseDate", "localDate", "localDateTime",
				"minguoDate", "offsetDateTime", "thaiBuddhistDate", "year", "yearMonth", "zonedDateTime"
		);
	}

	@Test
	// XXX BVAL-496 update specification references
	@SpecAssertion(section = "", id = "")
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

		assertCorrectNumberOfViolations( validator.validate( dummy ), 0 );

		clockProvider = new FixedClockProvider( ZonedDateTime.of( 2014, 4, 4, 9, 45, 0, 0, TZ_BERLIN ) );
		validatorFactory = TestUtil.getConfigurationUnderTest()
				.clockProvider( clockProvider )
				.buildValidatorFactory();
		validator = validatorFactory.getValidator();

		assertCorrectNumberOfViolations( validator.validate( dummy ), 3 );
		assertCorrectPropertyPaths(
				validator.validate( dummy ), "localTime", "monthDay", "offsetTime"
		);
	}

}
