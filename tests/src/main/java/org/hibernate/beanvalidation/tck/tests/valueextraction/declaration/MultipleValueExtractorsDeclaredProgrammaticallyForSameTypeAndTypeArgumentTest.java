/*
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.valueextraction.declaration;

import jakarta.validation.Validation;
import jakarta.validation.valueextraction.ValueExtractorDeclarationException;

import org.assertj.core.api.Assertions;
import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.tests.valueextraction.declaration.model.Cinema;
import org.hibernate.beanvalidation.tck.tests.valueextraction.declaration.model.ReferenceValueExtractor0;
import org.hibernate.beanvalidation.tck.tests.valueextraction.declaration.model.ReferenceValueExtractor1;
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
public class MultipleValueExtractorsDeclaredProgrammaticallyForSameTypeAndTypeArgumentTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( MultipleValueExtractorsDeclaredProgrammaticallyForSameTypeAndTypeArgumentTest.class )
				.withPackage( Cinema.class.getPackage() )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_CONFIGURATION, id = "h")
	public void configuringMultipleExtractorsForSameTypeAndTypeUseCausesException() throws Exception {
		Assertions.assertThatThrownBy( () -> {

			Validation.byDefaultProvider()
					.configure()
					.addValueExtractor( new ReferenceValueExtractor0() )
					.addValueExtractor( new ReferenceValueExtractor1() );
	
		} ).isInstanceOf( ValueExtractorDeclarationException.class );
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_VALIDATORFACTORY, id = "h")
	public void configuringValidatorWithMultipleExtractorsForSameTypeAndTypeUseCausesException() throws Exception {
		Assertions.assertThatThrownBy( () -> {

			Validation.buildDefaultValidatorFactory()
					.usingContext()
					.addValueExtractor( new ReferenceValueExtractor0() )
					.addValueExtractor( new ReferenceValueExtractor1() );
	
		} ).isInstanceOf( ValueExtractorDeclarationException.class );
	}
}
