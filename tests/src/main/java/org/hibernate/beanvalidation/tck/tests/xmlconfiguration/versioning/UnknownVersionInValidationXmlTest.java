/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.versioning;

import javax.validation.ValidationException;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Guillaume Smet
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class UnknownVersionInValidationXmlTest extends Arquillian {

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClass( UnknownVersionInValidationXmlTest.class )
				.withValidationXml( "validation-UnknownVersionInValidationXmlTest.xml" )
				.build();
	}

	@Test(expectedExceptions = ValidationException.class)
	@SpecAssertion(section = Sections.XML_CONFIG_XSD, id = "c")
	public void testValidationXmlWithUnknownSchemaVersion() {
		TestUtil.getConfigurationUnderTest()
				.buildValidatorFactory()
				.getValidator();
	}
}
