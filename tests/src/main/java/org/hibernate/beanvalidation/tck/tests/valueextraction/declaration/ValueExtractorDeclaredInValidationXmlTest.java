/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.valueextraction.declaration;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.pathWith;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;

import java.util.Optional;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotBlank;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.tests.valueextraction.declaration.model.Cinema;
import org.hibernate.beanvalidation.tck.tests.valueextraction.declaration.model.Reference;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Gunnar Morling
 * @author Guillaume Smet
 */
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
public class ValueExtractorDeclaredInValidationXmlTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( ValueExtractorDeclaredInValidationXmlTest.class )
				.withPackage( Cinema.class.getPackage() )
				.withValidationXml( "value-extractor-validation.xml" )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_REGISTERING, id = "b")
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "k")
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "l")
	public void canUseValueExtractorGivenInValidationXml() {
		Validator validator = Validation.byDefaultProvider()
				.configure()
				.buildValidatorFactory()
				.getValidator();

		Set<ConstraintViolation<Cinema>> violations = validator.validate( Cinema.invalidVisitor() );

		assertThat( violations ).containsOnlyViolations(
				violationOf( NotBlank.class )
						.withPropertyPath( pathWith()
								.property( "visitor" )
								.property( "name", false, null, null, Reference.class, 0 )
						)
		);

		violations = validator.validate( Cinema.invalidName() );

		assertThat( violations ).containsOnlyPaths(
				pathWith()
						.property( "name" )
						.containerElement( "<optional>", false, null, null, Optional.class, 0 )
		);
	}
}
