/**
 * Jakarta Bean Validation TCK
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
import org.hibernate.beanvalidation.tck.tests.valueextraction.resolution.model.ContainerElementEntity1;
import org.hibernate.beanvalidation.tck.tests.valueextraction.resolution.model.ContainerElementEntity2;
import org.hibernate.beanvalidation.tck.tests.valueextraction.resolution.model.ContainerElementEntity3;
import org.hibernate.beanvalidation.tck.tests.valueextraction.resolution.model.IWrapper11;
import org.hibernate.beanvalidation.tck.tests.valueextraction.resolution.model.IWrapper111;
import org.hibernate.beanvalidation.tck.tests.valueextraction.resolution.model.IWrapper21;
import org.hibernate.beanvalidation.tck.tests.valueextraction.resolution.model.IWrapper211;
import org.hibernate.beanvalidation.tck.tests.valueextraction.resolution.model.IWrapper212;
import org.hibernate.beanvalidation.tck.tests.valueextraction.resolution.model.IWrapper22;
import org.hibernate.beanvalidation.tck.tests.valueextraction.resolution.model.IWrapper221;
import org.hibernate.beanvalidation.tck.tests.valueextraction.resolution.model.Wrapper1;
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
public class ContainerElementValueExtractorResolutionAlgorithmTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( ContainerElementValueExtractorResolutionAlgorithmTest.class )
				.withPackage( IWrapper11.class.getPackage() )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_ALGORITHM_CONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_ALGORITHM_CONSTRAINTS, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_ALGORITHM_CONSTRAINTS, id = "d")
	public void mostSpecificValueExtractorFound() {
		Validator validator = Validation.byDefaultProvider()
				.configure()
				.addValueExtractor( new IWrapper11ValueExtractor0() )
				.addValueExtractor( new IWrapper111ValueExtractor0() )
				.addValueExtractor( new IWrapper111ValueExtractor1() )
				.buildValidatorFactory()
				.getValidator();

		Set<ConstraintViolation<ContainerElementEntity1>> violations = validator.validate( new ContainerElementEntity1( null, 4l ) );

		assertThat( violations ).containsOnlyPaths(
				pathWith()
						.property( "wrapper" )
						.containerElement( "IWrapper11-0", false, null, null, Wrapper1.class, 0 )
		);

		violations = validator.validate( new ContainerElementEntity1( "string", null ) );

		assertThat( violations ).containsOnlyPaths(
				pathWith()
						.property( "wrapper" )
						.containerElement( "IWrapper111-1", false, null, null, Wrapper1.class, 1 )
		);
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_ALGORITHM_CONSTRAINTS, id = "c")
	public void customGenericTypeWithContainerElementConstraintButNoValueExtractorThrowsException() {
		getValidator().validate( new ContainerElementEntity1( null, 4l ) );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_ALGORITHM_CONSTRAINTS, id = "e")
	public void parallelValueExtractorDefinitionsCausesException() {
		Validator validator = Validation.byDefaultProvider()
				.configure()
				.addValueExtractor( new IWrapper21ValueExtractor0() )
				.addValueExtractor( new IWrapper211ValueExtractor0() )
				.addValueExtractor( new IWrapper212ValueExtractor0() )
				.addValueExtractor( new IWrapper22ValueExtractor0() )
				.addValueExtractor( new IWrapper221ValueExtractor0() )
				.buildValidatorFactory()
				.getValidator();

		validator.validate( new ContainerElementEntity2( null, null ) );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_ALGORITHM_CONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_ALGORITHM_CONSTRAINTS, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_ALGORITHM_CONSTRAINTS, id = "d")
	public void parallelValueExtractorDefinitionsIsOKIfOnlyOneMaximallySpecific() {
		Validator validator = Validation.byDefaultProvider()
				.configure()
				.addValueExtractor( new IWrapper21ValueExtractor0() )
				.addValueExtractor( new IWrapper211ValueExtractor0() )
				.addValueExtractor( new IWrapper212ValueExtractor0() )
				.addValueExtractor( new IWrapper22ValueExtractor0() )
				.addValueExtractor( new IWrapper221ValueExtractor0() )
				.addValueExtractor( new Wrapper2ValueExtractor0() )
				.buildValidatorFactory()
				.getValidator();

		Set<ConstraintViolation<ContainerElementEntity2>> violations = validator.validate( new ContainerElementEntity2( null, 4l ) );

		assertThat( violations ).containsOnlyPaths(
				pathWith()
						.property( "wrapper" )
						.containerElement( "Wrapper2-0", false, null, null, Wrapper2.class, 0 )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_ALGORITHM_CONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_ALGORITHM_CONSTRAINTS, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_ALGORITHM_CONSTRAINTS, id = "d")
	public void declaringTypeIsConsideredAndNotRuntimeType() {
		Validator validator = Validation.byDefaultProvider()
				.configure()
				.addValueExtractor( new IWrapper21ValueExtractor0() )
				.addValueExtractor( new IWrapper211ValueExtractor0() )
				.addValueExtractor( new IWrapper212ValueExtractor0() )
				.addValueExtractor( new Wrapper2ValueExtractor0() )
				.buildValidatorFactory()
				.getValidator();

		Set<ConstraintViolation<ContainerElementEntity3>> violations = validator.validate( new ContainerElementEntity3( null, 4l ) );

		assertThat( violations ).containsOnlyPaths(
				pathWith()
						.property( "wrapper" )
						.containerElement( "IWrapper21-0", false, null, null, IWrapper21.class, 0 )
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

	private static class IWrapper21ValueExtractor0 implements ValueExtractor<IWrapper21<@ExtractedValue ?, ?>> {

		@Override
		public void extractValues(IWrapper21<?, ?> originalValue, ValueReceiver receiver) {
			receiver.value( "IWrapper21-0", originalValue.getProperty1() );
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

	private static class IWrapper22ValueExtractor0 implements ValueExtractor<IWrapper22<@ExtractedValue ?, ?>> {

		@Override
		public void extractValues(IWrapper22<?, ?> originalValue, ValueReceiver receiver) {
			receiver.value( "IWrapper22-0", originalValue.getProperty1() );
		}
	}

	private static class IWrapper221ValueExtractor0 implements ValueExtractor<IWrapper221<@ExtractedValue ?, ?>> {

		@Override
		public void extractValues(IWrapper221<?, ?> originalValue, ValueReceiver receiver) {
			receiver.value( "IWrapper221-0", originalValue.getProperty1() );
		}
	}

	private static class Wrapper2ValueExtractor0 implements ValueExtractor<Wrapper2<@ExtractedValue ?, ?>> {

		@Override
		public void extractValues(Wrapper2<?, ?> originalValue, ValueReceiver receiver) {
			receiver.value( "Wrapper2-0", originalValue.getProperty1() );
		}
	}
}
