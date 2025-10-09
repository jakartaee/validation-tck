/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.valueextraction.declaration;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.pathWith;

import java.util.Optional;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.tests.valueextraction.declaration.model.Cinema;
import org.hibernate.beanvalidation.tck.tests.valueextraction.declaration.model.Reference;
import org.hibernate.beanvalidation.tck.tests.valueextraction.declaration.model.ReferenceValueExtractor2;
import org.hibernate.beanvalidation.tck.tests.valueextraction.declaration.model.ReferenceValueExtractor3;
import org.hibernate.beanvalidation.tck.util.IntegrationTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Guillaume Smet
 */
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
@IntegrationTest
public class ValueExtractorsPrecedenceTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( ValueExtractorsPrecedenceTest.class )
				.withPackage( Cinema.class.getPackage() )
				.withValidationXml( "value-extractors-precedence-validation.xml" )
				.withResource(
						"jakarta.validation.valueextraction.ValueExtractor",
						"META-INF/services/jakarta.validation.valueextraction.ValueExtractor",
						true
				)
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_REGISTERING, id = "e")
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "n")
	public void valueExtractorPrecedenceIsAppliedCorrectly() {
		Cinema cinema = Cinema.invalidReference();

		Validator validator = Validation.buildDefaultValidatorFactory()
				.getValidator();

		Set<ConstraintViolation<Cinema>> violations = validator.validate( cinema );

		// validation.xml overrides service loader
		assertThat( violations ).containsOnlyPaths(
				pathWith()
						.property( "visitor" )
						.containerElement( "1", false, null, null, Reference.class, 0 )
		);

		ValidatorFactory validatorFactory = Validation.byDefaultProvider()
				.configure()
				.addValueExtractor( new ReferenceValueExtractor2() )
				.buildValidatorFactory();

		validator = validatorFactory.getValidator();

		violations = validator.validate( cinema );

		// ValidatorFactory overrides validation.xml
		assertThat( violations ).containsOnlyPaths(
				pathWith()
						.property( "visitor" )
						.containerElement( "2", false, null, null, Reference.class, 0 )
		);

		validator = validatorFactory.usingContext()
				.addValueExtractor( new ReferenceValueExtractor3() )
				.getValidator();

		violations = validator.validate( cinema );

		// ValidatorContext overrides ValidatorFactory
		assertThat( violations ).containsOnlyPaths(
				pathWith()
						.property( "visitor" )
						.containerElement( "3", false, null, null, Reference.class, 0 )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_REGISTERING, id = "e")
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "n")
	public void valueExtractorDefinedInXmlHasPrecedenceOverBuiltInValueExtractors() {
		Validator validator = Validation.buildDefaultValidatorFactory()
				.getValidator();

		Set<ConstraintViolation<Cinema>> violations = validator.validate( Cinema.invalidName() );

		assertThat( violations ).containsOnlyPaths(
				pathWith()
						.property( "name" )
						.containerElement( "<optional>", false, null, null, Optional.class, 0 )
		);
	}
}
