/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;
import static org.testng.Assert.assertTrue;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Set;

import jakarta.validation.ClockProvider;
import jakarta.validation.Configuration;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Past;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Guillaume Smet
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class ClockProviderSpecifiedInValidationXmlTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
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
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "j")
	public void testClockProviderSpecifiedInValidationXml() throws Exception {
		assertTrue(
				TestUtil.getValidatorFactoryUnderTest().getClockProvider() instanceof XmlDefinedClockProvider,
				"Clock provider configured in XML wasn't applied"
		);
	}

	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "d"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "j")
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
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Past.class )
		);
	}

	private static class ConfigurationDefinedClockProvider implements ClockProvider {

		@Override
		public Clock getClock() {
			return Clock.fixed( Instant.parse( "1980-11-08T10:15:30.00Z" ), ZoneId.of( "Europe/Paris" ) );
		}
	}
}
