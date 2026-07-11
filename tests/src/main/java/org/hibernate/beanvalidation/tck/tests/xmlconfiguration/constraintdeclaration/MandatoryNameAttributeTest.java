/*
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.constraintdeclaration;

import jakarta.validation.ValidationException;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.ExpectBootstrapFailure;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.junit.jupiter.api.Test;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
@ExpectBootstrapFailure(ValidationException.class)
public class MandatoryNameAttributeTest extends AbstractTCKTest {
	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( MandatoryNameAttributeTest.class )
				.withClasses( User.class )
				.withValidationXml( "validation-MandatoryNameAttributeTest.xml" )
				.withResource( "constraints-MandatoryNameAttributeTest.xml" )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONSTRAINTDECLARATION, id = "b")
	public void testNameAttributeIsMandatory() {
		TestUtil.getValidatorUnderTest();
	}
}
