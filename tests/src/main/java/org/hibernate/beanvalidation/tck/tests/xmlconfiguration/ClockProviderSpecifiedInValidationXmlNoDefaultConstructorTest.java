/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration;

import static org.testng.Assert.fail;

import jakarta.validation.ValidationException;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractBootstrapFailureTCKTest;
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
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
public class ClockProviderSpecifiedInValidationXmlNoDefaultConstructorTest extends AbstractBootstrapFailureTCKTest {

	@Override
	protected Class<? extends Exception> acceptedDeploymentExceptionType() {
		return ValidationException.class;
	}

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( ClockProviderSpecifiedInValidationXmlNoDefaultConstructorTest.class )
				.withValidationXml( "validation-ClockProviderSpecifiedInValidationXmlNoDefaultConstructorTest.xml" )
				.build();
	}

	@Test(expectedExceptions = ValidationException.class)
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "j"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "y")
	})
	public void testClockProviderSpecifiedInValidationXmlHasNoDefaultConstructor() {
		TestUtil.getValidatorUnderTest();
		fail( "Bootstrapping should have failed due to missing no-arg constructor in ClockProvider" );
	}
}
