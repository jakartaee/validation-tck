/*
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration;

import jakarta.validation.Configuration;
import jakarta.validation.Validation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
public class ConstraintValidatorFactorySpecifiedInValidationXmlTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( ConstraintValidatorFactorySpecifiedInValidationXmlTest.class )
				.withClasses(
						User.class,
						Optional.class,
						CreditCard.class,
						ConfigurationDefinedConstraintValidatorFactoryResolver.class,
						XmlDefinedConstraintValidatorFactory.class
				)
				.withValidationXml( "validation-ConstraintValidatorFactorySpecifiedInValidationXmlTest.xml" )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "h")
	public void testConstraintValidatorFactorySpecifiedInValidationXml() {
		try {
			Validator validator = TestUtil.getValidatorUnderTest();
			validator.validate( new User() );
		}
		catch ( ValidationException e ) {
			assertThat( XmlDefinedConstraintValidatorFactory.numberOfIsReachableCalls > 0 ).as( "The factory should have been called at least once if it was properly picked up by xml configuration." ).isTrue();
		}
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "h"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "g")
	})
	public void testConstraintValidatorFactorySpecifiedInValidationXmlCanBeOverridden() {
		try {
			Configuration<?> configuration = Validation
					.byDefaultProvider()
					.configure();

			configuration = configuration.constraintValidatorFactory( new ConfigurationDefinedConstraintValidatorFactoryResolver() );
			Validator validator = configuration.buildValidatorFactory().getValidator();
			validator.validate( new User() );
		}
		catch ( ValidationException e ) {
			assertThat( ConfigurationDefinedConstraintValidatorFactoryResolver.numberOfIsReachableCalls > 0 ).as( "The factory  should have been called at least once if configuration settings were applied." ).isTrue();
		}
	}
}
