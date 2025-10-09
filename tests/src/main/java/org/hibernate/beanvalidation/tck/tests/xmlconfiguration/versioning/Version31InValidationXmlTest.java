/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.versioning;

import static org.testng.Assert.assertEquals;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;

import jakarta.validation.Configuration;
import org.testng.annotations.Test;

@SpecVersion(spec = "beanvalidation", version = "4.0.0")
public class Version31InValidationXmlTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( Version31InValidationXmlTest.class )
				.withClass( DummyClockProvider.class )
				.withValidationXml( "validation-Version31InValidationXmlTest.xml" )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.XML_CONFIG_XSD, id = "a")
	public void testValidationXmlVersion31() {
		Configuration<?> config = TestUtil.getConfigurationUnderTest();
		assertEquals(
				config.getBootstrapConfiguration().getClockProviderClassName(),
				"org.hibernate.beanvalidation.tck.tests.xmlconfiguration.versioning.DummyClockProvider",
				"Wrong clock provider class name."
		);
	}
}
