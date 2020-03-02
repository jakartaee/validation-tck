/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.valueextraction.declaration;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.pathWith;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.tests.valueextraction.declaration.model.Cinema;
import org.hibernate.beanvalidation.tck.tests.valueextraction.declaration.model.Reference;
import org.hibernate.beanvalidation.tck.util.IntegrationTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Guillaume Smet
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
@IntegrationTest
public class ValueExtractorFromServiceLoaderTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( ValueExtractorFromServiceLoaderTest.class )
				.withPackage( Cinema.class.getPackage() )
				.withResource(
						"jakarta.validation.valueextraction.ValueExtractor",
						"META-INF/services/jakarta.validation.valueextraction.ValueExtractor",
						true
				)
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_REGISTERING, id = "a")
	public void canDeclareValueExtractorViaServiceLoader() {
		Cinema cinema = Cinema.invalidReference();

		Validator validator = Validation.buildDefaultValidatorFactory()
				.getValidator();

		Set<ConstraintViolation<Cinema>> violations = validator.validate( cinema );

		// validation.xml overrides service loader
		assertThat( violations ).containsOnlyPaths(
				pathWith()
						.property( "visitor" )
						.containerElement( "0", false, null, null, Reference.class, 0 )
		);
	}
}
