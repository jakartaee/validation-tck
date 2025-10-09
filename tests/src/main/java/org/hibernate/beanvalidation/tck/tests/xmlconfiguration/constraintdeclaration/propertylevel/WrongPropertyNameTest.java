/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.constraintdeclaration.propertylevel;

import jakarta.validation.ValidationException;
import jakarta.validation.Validator;

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
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
public class WrongPropertyNameTest extends AbstractBootstrapFailureTCKTest {

	@Override
	protected Class<? extends Exception> acceptedDeploymentExceptionType() {
		return ValidationException.class;
	}

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( WrongPropertyNameTest.class )
				.withClasses( User.class, CreditCard.class )
				.withValidationXml( "validation-WrongPropertyNameTest.xml" )
				.withResource( "user-constraints-WrongPropertyNameTest.xml" )
				.build();
	}

	@Test(expectedExceptions = ValidationException.class)
	@SpecAssertions({
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_PROPERTYLEVELOVERRIDING, id = "g")
	})
	public void testWrongPropertyNameThrowsException() {
		Validator validator = TestUtil.getValidatorUnderTest();
		validator.getConstraintsForClass( User.class );
	}
}
