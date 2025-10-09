/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;

import java.util.Set;

import jakarta.validation.Configuration;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;

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
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
public class MessageInterpolatorSpecifiedInValidationXmlTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( MessageInterpolatorSpecifiedInValidationXmlTest.class )
				.withClasses(
						User.class,
						Optional.class,
						CreditCard.class,
						ConfigurationDefinedMessageInterpolator.class,
						XmlDefinedMessageInterpolator.class
				)
				.withValidationXml( "validation-MessageInterpolatorSpecifiedInValidationXmlTest.xml" )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "f")
	public void testMessageInterpolatorSpecifiedInValidationXml() {
		Validator validator = TestUtil.getValidatorUnderTest();

		User user = new User();
		Set<ConstraintViolation<User>> constraintViolations = validator.validate( user );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class ).withMessage( XmlDefinedMessageInterpolator.STATIC_INTERPOLATION_STRING )
		);
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "d"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "f")
	})
	public void testMessageInterpolatorSpecifiedInValidationXmlCanBeOverridden() {
		Configuration<?> configuration = Validation
				.byDefaultProvider()
				.configure();
		configuration = configuration.messageInterpolator( new ConfigurationDefinedMessageInterpolator() );
		Validator validator = configuration.buildValidatorFactory().getValidator();

		User user = new User();
		Set<ConstraintViolation<User>> constraintViolations = validator.validate( user );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class ).withMessage( ConfigurationDefinedMessageInterpolator.STATIC_INTERPOLATION_STRING )
		);
	}
}
