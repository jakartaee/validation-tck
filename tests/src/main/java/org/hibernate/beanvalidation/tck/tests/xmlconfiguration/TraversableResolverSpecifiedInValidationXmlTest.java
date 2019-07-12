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

import java.util.Set;

import javax.validation.Configuration;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;

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
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class TraversableResolverSpecifiedInValidationXmlTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( TraversableResolverSpecifiedInValidationXmlTest.class )
				.withClasses(
						User.class,
						Optional.class,
						CreditCard.class,
						ConfigurationDefinedTraversableResolver.class,
						XmlDefinedTraversableResolver.class
				)
				.withValidationXml( "validation-TraversableResolverSpecifiedInValidationXmlTest.xml" )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "g")
	public void testTraversableResolverSpecifiedInValidationXml() {
		Validator validator = TestUtil.getValidatorUnderTest();

		User user = new User();
		Set<ConstraintViolation<User>> constraintViolations = validator.validate( user );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
		);
		assertTrue(
				XmlDefinedTraversableResolver.numberOfIsReachableCalls > 0,
				"The resolver should have been called at least once if it was properly picked up by xml configuration."
		);

	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "d"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "g")
	})
	public void testTraversableResolverSpecifiedInValidationXmlCanBeOverridden() {
		Configuration<?> configuration = Validation
				.byDefaultProvider()
				.configure();

		configuration = configuration.traversableResolver( new ConfigurationDefinedTraversableResolver() );
		Validator validator = configuration.buildValidatorFactory().getValidator();

		User user = new User();
		Set<ConstraintViolation<User>> constraintViolations = validator.validate( user );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
		);
		assertTrue(
				ConfigurationDefinedTraversableResolver.numberOfIsReachableCalls > 0,
				"The resolver should have been called at least once if configuration settings were applied."
		);
	}
}
