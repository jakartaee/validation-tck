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
import org.hibernate.beanvalidation.tck.tests.valueextraction.resolution.model.CascadingEntity1;
import org.hibernate.beanvalidation.tck.tests.valueextraction.resolution.model.CascadingEntity2;
import org.hibernate.beanvalidation.tck.tests.valueextraction.resolution.model.IWrapper11;
import org.hibernate.beanvalidation.tck.tests.valueextraction.resolution.model.IWrapper111;
import org.hibernate.beanvalidation.tck.tests.valueextraction.resolution.model.IWrapper21;
import org.hibernate.beanvalidation.tck.tests.valueextraction.resolution.model.IWrapper211;
import org.hibernate.beanvalidation.tck.tests.valueextraction.resolution.model.IWrapper212;
import org.hibernate.beanvalidation.tck.tests.valueextraction.resolution.model.Wrapper2;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Guillaume Smet
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class CascadingValueExtractorResolutionAlgorithmTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( CascadingValueExtractorResolutionAlgorithmTest.class )
				.withPackage( IWrapper11.class.getPackage() )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_ALGORITHM_CASCADED, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_ALGORITHM_CASCADED, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_ALGORITHM_CASCADED, id = "d")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_ALGORITHM_CASCADED, id = "e")
	public void mostSpecificValueExtractorFound() {
		Validator validator = Validation.byDefaultProvider()
				.configure()
				.addValueExtractor( new IWrapper11ValueExtractor0() )
				.addValueExtractor( new IWrapper111ValueExtractor0() )
				.addValueExtractor( new IWrapper111ValueExtractor1() )
				.buildValidatorFactory()
				.getValidator();

		Set<ConstraintViolation<CascadingEntity1>> violations = validator.validate( new CascadingEntity1( null ) );

		assertThat( violations ).containsOnlyPaths(
				pathWith()
						.property( "wrapper" )
						.property( "property", false, null, null, IWrapper111.class, 0 )
		);
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_ALGORITHM_CASCADED, id = "c")
	public void customGenericTypeWithCascadingButNoValueExtractorThrowsException() {
		Validator validator = Validation.byDefaultProvider()
				.configure()
				.addValueExtractor( new IWrapper111ValueExtractor1() )
				.buildValidatorFactory()
				.getValidator();

		validator.validate( new CascadingEntity1( null ) );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_ALGORITHM_CASCADED, id = "f")
	public void parallelValueExtractorDefinitionsCausesException() {
		Validator validator = Validation.byDefaultProvider()
				.configure()
				.addValueExtractor( new IWrapper211ValueExtractor0() )
				.addValueExtractor( new IWrapper212ValueExtractor0() )
				.addValueExtractor( new Wrapper2ValueExtractor1() )
				.buildValidatorFactory()
				.getValidator();

		validator.validate( new CascadingEntity2( null ) );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_ALGORITHM_CASCADED, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_ALGORITHM_CASCADED, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_ALGORITHM_CASCADED, id = "d")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_ALGORITHM_CASCADED, id = "e")
	public void mostSpecificValueExtractorFoundWithParallelHierarchiesAsRuntimeTypeIsConsidered() {
		Validator validator = Validation.byDefaultProvider()
				.configure()
				.addValueExtractor( new IWrapper211ValueExtractor0() )
				.addValueExtractor( new IWrapper212ValueExtractor0() )
				.addValueExtractor( new Wrapper2ValueExtractor0() )
				.addValueExtractor( new Wrapper2ValueExtractor1() )
				.buildValidatorFactory()
				.getValidator();

		Set<ConstraintViolation<CascadingEntity2>> violations = validator.validate( new CascadingEntity2( null ) );

		assertThat( violations ).containsOnlyPaths(
				pathWith()
						.property( "wrapper" )
						.property( "property", false, null, null, IWrapper21.class, 0 ),
				pathWith()
						.property( "wrapper" )
						.property( "property", false, null, null, IWrapper21.class, 1 )
		);
	}

	private static class IWrapper11ValueExtractor0 implements ValueExtractor<IWrapper11<@ExtractedValue ?, ?>> {

		@Override
		public void extractValues(IWrapper11<?, ?> originalValue, ValueReceiver receiver) {
			receiver.value( "IWrapper11-0", originalValue.getProperty1() );
		}
	}

	private static class IWrapper111ValueExtractor0 implements ValueExtractor<IWrapper111<@ExtractedValue ?, ?>> {

		@Override
		public void extractValues(IWrapper111<?, ?> originalValue, ValueReceiver receiver) {
			receiver.value( "IWrapper111-0", originalValue.getProperty1() );
		}
	}

	private static class IWrapper111ValueExtractor1 implements ValueExtractor<IWrapper111<?, @ExtractedValue ?>> {

		@Override
		public void extractValues(IWrapper111<?, ?> originalValue, ValueReceiver receiver) {
			receiver.value( "IWrapper111-1", originalValue.getProperty2() );
		}
	}

	private static class IWrapper211ValueExtractor0 implements ValueExtractor<IWrapper211<@ExtractedValue ?, ?>> {

		@Override
		public void extractValues(IWrapper211<?, ?> originalValue, ValueReceiver receiver) {
			receiver.value( "IWrapper211-0", originalValue.getProperty1() );
		}
	}

	private static class IWrapper212ValueExtractor0 implements ValueExtractor<IWrapper212<@ExtractedValue ?, ?>> {

		@Override
		public void extractValues(IWrapper212<?, ?> originalValue, ValueReceiver receiver) {
			receiver.value( "IWrapper212-0", originalValue.getProperty1() );
		}
	}

	private static class Wrapper2ValueExtractor0 implements ValueExtractor<Wrapper2<@ExtractedValue ?, ?>> {

		@Override
		public void extractValues(Wrapper2<?, ?> originalValue, ValueReceiver receiver) {
			receiver.value( "Wrapper2-0", originalValue.getProperty1() );
		}
	}

	private static class Wrapper2ValueExtractor1 implements ValueExtractor<Wrapper2<?, @ExtractedValue ?>> {

		@Override
		public void extractValues(Wrapper2<?, ?> originalValue, ValueReceiver receiver) {
			receiver.value( "Wrapper2-1", originalValue.getProperty2() );
		}
	}
}
