/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.versioning;

import static org.testng.Assert.assertFalse;

import jakarta.validation.Validator;

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
public class Version20InMappingXmlTest extends AbstractTCKTest {

	private static final String MAPPING_FILE = "Version20InMappingXmlTest.xml";

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( Version20InMappingXmlTest.class )
				.withClass( TestEntity.class )
				.withResource( MAPPING_FILE )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.XML_MAPPING_XSD, id = "a")
	public void testValidBeanValidation20Mapping() {
		Validator validator = TestUtil.getConfigurationUnderTest()
				.addMapping( Version20InMappingXmlTest.class.getResourceAsStream( MAPPING_FILE ) )
				.buildValidatorFactory()
				.getValidator();

		assertFalse( validator.getConstraintsForClass( TestEntity.class ).isBeanConstrained() );
	}
}
