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
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration;

import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectConstraintTypes;
import static org.testng.Assert.assertTrue;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Set;

import javax.validation.ClockProvider;
import javax.validation.Configuration;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
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
public class ClockProviderSpecifiedInValidationXmlTest extends Arquillian {

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClass( ClockProviderSpecifiedInValidationXmlTest.class )
				.withClasses(
						XmlDefinedClockProvider.class
				)
				.withValidationXml(
						"validation-ClockProviderSpecifiedInValidationXmlTest.xml"
				)
				.build();
	}

	@Test
	@SpecAssertion(section = "5.5.6", id = "j")
	public void testClockProviderSpecifiedInValidationXml() throws Exception {
		assertTrue(
				TestUtil.getValidatorFactoryUnderTest().getClockProvider() instanceof XmlDefinedClockProvider,
				"Clock provider configured in XML wasn't applied"
		);
	}

	@SpecAssertions({
			@SpecAssertion(section = "5.5.6", id = "d"),
			@SpecAssertion(section = "5.5.6", id = "j")
	})
	public void testClockProviderSpecifiedInValidationXmlCanBeOverridden() {
		Configuration<?> configuration = Validation
				.byDefaultProvider()
				.configure();
		configuration = configuration.clockProvider( new ConfigurationDefinedClockProvider() );
		Validator validator = configuration.buildValidatorFactory().getValidator();

		User user = new User();
		user.setBirthday( ZonedDateTime.of( 1980, 11, 10, 9, 16, 0, 0, ZoneId.of( "Europe/Paris" ) ) );
		Set<ConstraintViolation<User>> constraintViolations = validator.validate( user );
		assertCorrectConstraintTypes( constraintViolations, Past.class );
	}

	private static class ConfigurationDefinedClockProvider implements ClockProvider {

		@Override
		public Clock getClock() {
			return Clock.fixed( Instant.parse( "1980-11-08T10:15:30.00Z" ), ZoneId.of( "Europe/Paris" ) );
		}
	}
}
