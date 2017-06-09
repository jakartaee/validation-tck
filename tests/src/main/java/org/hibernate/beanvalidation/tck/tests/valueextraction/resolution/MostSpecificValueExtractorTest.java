/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.valueextraction.resolution;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.pathWith;

import java.util.Set;

import javax.validation.ConstraintDeclarationException;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.valueextraction.ExtractedValue;
import javax.validation.valueextraction.ValueExtractor;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.tests.valueextraction.resolution.model.Entity1;
import org.hibernate.beanvalidation.tck.tests.valueextraction.resolution.model.Entity2;
import org.hibernate.beanvalidation.tck.tests.valueextraction.resolution.model.IWrapper11;
import org.hibernate.beanvalidation.tck.tests.valueextraction.resolution.model.IWrapper111;
import org.hibernate.beanvalidation.tck.tests.valueextraction.resolution.model.IWrapper21;
import org.hibernate.beanvalidation.tck.tests.valueextraction.resolution.model.IWrapper211;
import org.hibernate.beanvalidation.tck.tests.valueextraction.resolution.model.IWrapper212;
import org.hibernate.beanvalidation.tck.tests.valueextraction.resolution.model.IWrapper22;
import org.hibernate.beanvalidation.tck.tests.valueextraction.resolution.model.IWrapper221;
import org.hibernate.beanvalidation.tck.tests.valueextraction.resolution.model.Wrapper1;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Guillaume Smet
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class MostSpecificValueExtractorTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( MostSpecificValueExtractorTest.class )
				.withPackage( IWrapper11.class.getPackage() )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_ALGORITHM_CONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_ALGORITHM_CONSTRAINTS, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_ALGORITHM_CONSTRAINTS, id = "d")
	public void mostSpecificValueExtractorFound() throws Exception {
		Validator validator = Validation.byDefaultProvider()
				.configure()
				.addValueExtractor( new IWrapper11ValueExtractor() )
				.addValueExtractor( new IWrapper111ValueExtractor() )
				.buildValidatorFactory()
				.getValidator();

		Set<ConstraintViolation<Entity1>> violations = validator.validate( new Entity1( null ) );

		assertThat( violations ).containsOnlyPaths(
				pathWith()
						.property( "wrapper" )
						.containerElement( "IWrapper11", false, null, null, Wrapper1.class, 0 )
		);
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_ALGORITHM_CONSTRAINTS, id = "e")
	public void parallelValueExtractorDefinitionsCausesException() throws Exception {
		Validator validator = Validation.byDefaultProvider()
				.configure()
				.addValueExtractor( new IWrapper21ValueExtractor() )
				.addValueExtractor( new IWrapper211ValueExtractor() )
				.addValueExtractor( new IWrapper212ValueExtractor() )
				.addValueExtractor( new IWrapper22ValueExtractor() )
				.addValueExtractor( new IWrapper221ValueExtractor() )
				.buildValidatorFactory()
				.getValidator();

		validator.validate( new Entity2( null ) );
	}

	private static class IWrapper11ValueExtractor implements ValueExtractor<IWrapper11<@ExtractedValue ?>> {

		@Override
		public void extractValues(IWrapper11<?> originalValue, ValueReceiver receiver) {
			receiver.value( "IWrapper11", originalValue.getProperty() );
		}
	}

	private static class IWrapper111ValueExtractor implements ValueExtractor<IWrapper111<@ExtractedValue ?>> {

		@Override
		public void extractValues(IWrapper111<?> originalValue, ValueReceiver receiver) {
			receiver.value( "IWrapper111", originalValue.getProperty() );
		}
	}

	private static class IWrapper21ValueExtractor implements ValueExtractor<IWrapper21<@ExtractedValue ?>> {

		@Override
		public void extractValues(IWrapper21<?> originalValue, ValueReceiver receiver) {
			receiver.value( "IWrapper21", originalValue.getProperty() );
		}
	}

	private static class IWrapper211ValueExtractor implements ValueExtractor<IWrapper211<@ExtractedValue ?>> {

		@Override
		public void extractValues(IWrapper211<?> originalValue, ValueReceiver receiver) {
			receiver.value( "IWrapper211", originalValue.getProperty() );
		}
	}

	private static class IWrapper212ValueExtractor implements ValueExtractor<IWrapper212<@ExtractedValue ?>> {

		@Override
		public void extractValues(IWrapper212<?> originalValue, ValueReceiver receiver) {
			receiver.value( "IWrapper212", originalValue.getProperty() );
		}
	}

	private static class IWrapper22ValueExtractor implements ValueExtractor<IWrapper22<@ExtractedValue ?>> {

		@Override
		public void extractValues(IWrapper22<?> originalValue, ValueReceiver receiver) {
			receiver.value( "IWrapper22", originalValue.getProperty() );
		}
	}

	private static class IWrapper221ValueExtractor implements ValueExtractor<IWrapper221<@ExtractedValue ?>> {

		@Override
		public void extractValues(IWrapper221<?> originalValue, ValueReceiver receiver) {
			receiver.value( "IWrapper221", originalValue.getProperty() );
		}
	}
}
