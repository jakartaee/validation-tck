/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.valueextraction.declaration;

import jakarta.validation.Validation;
import jakarta.validation.ValidationException;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractBootstrapFailureTCKTest;
import org.hibernate.beanvalidation.tck.tests.valueextraction.declaration.model.Cinema;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Gunnar Morling
 * @author Guillaume Smet
 */
@SpecVersion(spec = "beanvalidation", version = "3.0.0")
public class ValueExtractorWithNoPublicNoArgConstructorInValidationXmlTest extends AbstractBootstrapFailureTCKTest {

	@Override
	protected Class<? extends Exception> acceptedDeploymentExceptionType() {
		return ValidationException.class;
	}

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( ValueExtractorWithNoPublicNoArgConstructorInValidationXmlTest.class )
				.withPackage( Cinema.class.getPackage() )
				.withValidationXml( "value-extractor-with-no-public-no-arg-constructor-validation.xml" )
				.build();
	}

	@Test(expectedExceptions = ValidationException.class)
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "m")
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "y")
	public void valueExtractorWithNoPublicNoArgConstructorInValidationXmlThrowsException() throws Exception {
		Validation.buildDefaultValidatorFactory();
	}
}
