/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.versioning;

import static org.testng.Assert.assertEquals;

import jakarta.validation.Configuration;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Guillaume Smet
 */
@SpecVersion(spec = "beanvalidation", version = "3.0.0")
public class Version30InValidationXmlTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( Version30InValidationXmlTest.class )
				.withClass( DummyClockProvider.class )
				.withValidationXml( "validation-Version30InValidationXmlTest.xml" )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.XML_CONFIG_XSD, id = "a")
	public void testValidationXmlVersion20() {
		Configuration<?> config = TestUtil.getConfigurationUnderTest();
		assertEquals(
				config.getBootstrapConfiguration().getClockProviderClassName(),
				"org.hibernate.beanvalidation.tck.tests.xmlconfiguration.versioning.DummyClockProvider",
				"Wrong clock provider class name."
		);
	}
}
