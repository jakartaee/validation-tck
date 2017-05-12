/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.invalid;

import javax.validation.ValidationException;

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
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class InvalidMappingXmlTest extends AbstractTCKTest {

	private static final String MAPPING_FILE = "InvalidMappingXmlTest.xml";

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( InvalidMappingXmlTest.class )
				.withResource( MAPPING_FILE )
				.build();
	}

	@Test(expectedExceptions = ValidationException.class)
	@SpecAssertion(section = Sections.XML, id = "a")
	public void testInvalidConstraintMappingXml() {
		TestUtil.getConfigurationUnderTest()
				.addMapping( InvalidMappingXmlTest.class.getResourceAsStream( MAPPING_FILE ) )
				.buildValidatorFactory()
				.getValidator();
	}
}
