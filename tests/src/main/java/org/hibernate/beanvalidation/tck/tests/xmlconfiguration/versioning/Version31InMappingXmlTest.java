/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.versioning;

import static org.testng.Assert.assertFalse;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;

import jakarta.validation.Validator;
import org.testng.annotations.Test;

@SpecVersion(spec = "beanvalidation", version = "3.1.0")
public class Version31InMappingXmlTest extends AbstractTCKTest {

	private static final String MAPPING_FILE = "Version31InMappingXmlTest.xml";

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( Version31InMappingXmlTest.class )
				.withClass( TestEntity.class )
				.withResource( MAPPING_FILE )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.XML_MAPPING_XSD, id = "a")
	public void testValidBeanValidation31Mapping() {
		Validator validator = TestUtil.getConfigurationUnderTest()
				.addMapping( Version31InMappingXmlTest.class.getResourceAsStream( MAPPING_FILE ) )
				.buildValidatorFactory()
				.getValidator();

		assertFalse( validator.getConstraintsForClass( TestEntity.class ).isBeanConstrained() );
	}
}
