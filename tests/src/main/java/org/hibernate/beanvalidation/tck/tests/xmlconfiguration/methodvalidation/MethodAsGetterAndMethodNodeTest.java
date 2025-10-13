/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.methodvalidation;

import jakarta.validation.ValidationException;
import jakarta.validation.Validator;

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
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
public class MethodAsGetterAndMethodNodeTest extends AbstractBootstrapFailureTCKTest {

	@Override
	protected Class<? extends Exception> acceptedDeploymentExceptionType() {
		return ValidationException.class;
	}

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( MethodAsGetterAndMethodNodeTest.class )
				.withValidationXml( "validation-MethodAsGetterAndMethodNodeTest.xml" )
				.withResource( "MethodAsGetterAndMethodNodeTest.xml" )
				.build();
	}

	@Test(expectedExceptions = ValidationException.class)
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_METHODLEVELOVERRIDING, id = "d")
	public void testMethodConfiguredWithMethodAndGetterNodeThrowsException() {
		Validator validator = TestUtil.getValidatorUnderTest();
		validator.getConstraintsForClass( CustomerRepository.class );
	}
}
