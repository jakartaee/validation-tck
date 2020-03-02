/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration;

import jakarta.validation.ValidationException;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractBootstrapFailureTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class MissingClassNameOnBeanNodeTest extends AbstractBootstrapFailureTCKTest {

	@Override
	protected Class<? extends Exception> acceptedDeploymentExceptionType() {
		return ValidationException.class;
	}

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( MissingClassNameOnBeanNodeTest.class )
				.withValidationXml( "validation-MissingClassNameOnBeanNodeTest.xml" )
				.withResource( "user-constraints-MissingClassNameOnBeanNodeTest.xml" )
				.build();
	}

	@Test(expectedExceptions = ValidationException.class)
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML, id = "c")
	public void testClassNameIsMandatory() {
		TestUtil.getValidatorUnderTest();
	}
}
