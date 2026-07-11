/*
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.valueextraction.declaration;

import jakarta.validation.Validation;
import jakarta.validation.valueextraction.ValueExtractorDeclarationException;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.ExpectBootstrapFailure;
import org.hibernate.beanvalidation.tck.tests.valueextraction.declaration.model.Cinema;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.junit.jupiter.api.Test;

/**
 * @author Gunnar Morling
 * @author Guillaume Smet
 */
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
@ExpectBootstrapFailure(ValueExtractorDeclarationException.class)
public class MultipleValueExtractorsInValidationXmlForSameTypeAndTypeArgumentTest extends AbstractTCKTest {
	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( MultipleValueExtractorsInValidationXmlForSameTypeAndTypeArgumentTest.class )
				.withPackage( Cinema.class.getPackage() )
				.withValidationXml( "multiple-value-extractors-for-same-type-and-type-argument-validation.xml" )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.EXCEPTION_VALUEEXTRACTORDECLARATION, id = "a")
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "o")
	public void configuringMultipleExtractorsForSameTypeAndTypeUseInValidationXmlCausesException() throws Exception {
				Validation.buildDefaultValidatorFactory();
	}
}
