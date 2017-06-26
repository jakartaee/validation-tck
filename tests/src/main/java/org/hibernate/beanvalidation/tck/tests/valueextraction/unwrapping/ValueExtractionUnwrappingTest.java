/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.valueextraction.unwrapping;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;
import static org.testng.Assert.assertEquals;

import java.util.Set;

import javax.validation.ConstraintDeclarationException;
import javax.validation.ConstraintViolation;
import javax.validation.UnexpectedTypeException;
import javax.validation.Validator;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.ValidateUnwrappedValue;
import javax.validation.valueextraction.ExtractedValue;
import javax.validation.valueextraction.UnwrapByDefault;
import javax.validation.valueextraction.Unwrapping;
import javax.validation.valueextraction.ValueExtractor;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.tests.valueextraction.unwrapping.model.IntegerWrapper;
import org.hibernate.beanvalidation.tck.tests.valueextraction.unwrapping.model.UnwrapByDefaultIntegerWrapperValueExtractor;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Test the various scenarios for explicit and implicit unwrapping of values.
 *
 * @author Guillaume Smet
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class ValueExtractionUnwrappingTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( ValueExtractionUnwrappingTest.class )
				.withPackage( IntegerWrapper.class.getPackage() )
				.build();
	}

	private Validator getValidatorWithoutValueExtractor() {
		return getValidator();
	}

	private Validator getValidatorWithValueExtractors() {
		return TestUtil.getConfigurationUnderTest()
				.addValueExtractor( new ValueHolderExtractor() )
				.addValueExtractor( new UnwrapByDefaultWrapperValueExtractor() )
				.addValueExtractor( new UnwrapByDefaultIntegerWrapperValueExtractor() )
				.buildValidatorFactory()
				.getValidator();
	}

	@Test(expectedExceptions = UnexpectedTypeException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS_IMPLICITUNWRAPPING, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_IMPLICITUNWRAPPING, id = "b")
	public void no_constraint_validator_for_unwrapped_value_throws_exception() {
		getValidatorWithValueExtractors().validate( new EntityWithNoContraintValidatorForUnwrappedValue() );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS_IMPLICITUNWRAPPING, id = "b")
	public void skip_and_unwrap_at_the_same_time_throws_exception() {
		getValidatorWithValueExtractors().validate( new EntityWithSkipAndUnwrapAtTheSameTime() );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_IMPLICITUNWRAPPING, id = "c")
	public void missing_value_extractor_throws_exception() {
		getValidatorWithoutValueExtractor().validate( new EntityWithExplicitUnwrapping() );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS_IMPLICITUNWRAPPING, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_IMPLICITUNWRAPPING, id = "b")
	public void validate_wrapped_value_if_explicit_unwrapping() {
		Set<ConstraintViolation<EntityWithExplicitUnwrapping>> constraintViolations = getValidatorWithValueExtractors().validate( new EntityWithExplicitUnwrapping() );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Min.class ).withProperty( "integerHolder" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS_IMPLICITUNWRAPPING, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_IMPLICITUNWRAPPING, id = "e")
	public void validate_wrapper_itself_if_there_is_no_unwrapping() {
		Set<ConstraintViolation<EntityWithNoUnwrapping>> constraintViolations = getValidatorWithValueExtractors().validate( new EntityWithNoUnwrapping() );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Null.class ).withProperty( "integerHolder" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_IMPLICITUNWRAPPING, id = "d")
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION_UNWRAPBYDEFAULT, id = "a")
	public void validate_wrapped_value_if_value_extractor_unwraps_by_default_for_generic_container() {
		Set<ConstraintViolation<WrapperWithImplicitUnwrapping>> constraintViolations = getValidatorWithValueExtractors().validate( new WrapperWithImplicitUnwrapping() );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Min.class ).withProperty( "integerWrapper" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS_IMPLICITUNWRAPPING, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_IMPLICITUNWRAPPING, id = "a")
	public void validate_wrapper_if_unwrapping_disabled_per_constraint_for_generic_container() {
		Set<ConstraintViolation<WrapperWithDisabledUnwrapping>> constraintViolations = getValidatorWithValueExtractors().validate( new WrapperWithDisabledUnwrapping() );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Null.class ).withProperty( "integerWrapper" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS_IMPLICITUNWRAPPING, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_IMPLICITUNWRAPPING, id = "b")
	public void validate_wrapped_value_if_value_extractor_unwraps_by_default_and_unwrapping_enabled_per_constraint_for_generic_container() {
		Set<ConstraintViolation<WrapperWithForcedUnwrapping>> constraintViolations = getValidatorWithValueExtractors().validate( new WrapperWithForcedUnwrapping() );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Min.class ).withProperty( "integerWrapper" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_IMPLICITUNWRAPPING, id = "d")
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION_UNWRAPBYDEFAULT, id = "a")
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION_EXTRACTEDVALUE, id = "e")
	public void validate_wrapped_value_if_value_extractor_unwraps_by_default_for_non_generic_container() {
		Set<ConstraintViolation<IntegerWrapperWithImplicitUnwrapping>> constraintViolations = getValidatorWithValueExtractors().validate( new IntegerWrapperWithImplicitUnwrapping() );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Min.class ).withProperty( "integerWrapper" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS_IMPLICITUNWRAPPING, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_IMPLICITUNWRAPPING, id = "a")
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION_EXTRACTEDVALUE, id = "e")
	public void validate_wrapper_if_unwrapping_disabled_per_constraint_for_non_generic_container() {
		Set<ConstraintViolation<IntegerWrapperWithDisabledUnwrapping>> constraintViolations = getValidatorWithValueExtractors().validate( new IntegerWrapperWithDisabledUnwrapping() );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Null.class ).withProperty( "integerWrapper" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS_IMPLICITUNWRAPPING, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_IMPLICITUNWRAPPING, id = "d")
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION_EXTRACTEDVALUE, id = "e")
	public void validate_wrapped_value_if_value_extractor_unwraps_by_default_and_unwrapping_enabled_per_constraint_for_non_generic_container() {
		Set<ConstraintViolation<IntegerWrapperWithForcedUnwrapping>> constraintViolations = getValidatorWithValueExtractors().validate( new IntegerWrapperWithForcedUnwrapping() );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Min.class ).withProperty( "integerWrapper" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONSTRAINTDESCRIPTOR, id = "p")
	public void validateUnwrappedValue_returns_correct_values() {
		Validator validator = getValidatorWithValueExtractors();
		ConstraintDescriptor<?> minConstraintDescriptor = validator.getConstraintsForClass( WrapperWithImplicitUnwrapping.class )
				.getConstraintsForProperty( "integerWrapper" )
				.getConstraintDescriptors()
				.iterator().next();

		assertEquals( minConstraintDescriptor.getAnnotation().annotationType(), Min.class );
		assertEquals( minConstraintDescriptor.validateUnwrappedValue(), ValidateUnwrappedValue.DEFAULT );

		minConstraintDescriptor = validator.getConstraintsForClass( WrapperWithDisabledUnwrapping.class )
				.getConstraintsForProperty( "integerWrapper" )
				.getConstraintDescriptors()
				.iterator().next();

		assertEquals( minConstraintDescriptor.getAnnotation().annotationType(), Null.class );
		assertEquals( minConstraintDescriptor.validateUnwrappedValue(), ValidateUnwrappedValue.NO );

		minConstraintDescriptor = validator.getConstraintsForClass( WrapperWithForcedUnwrapping.class )
				.getConstraintsForProperty( "integerWrapper" )
				.getConstraintDescriptors()
				.iterator().next();

		assertEquals( minConstraintDescriptor.getAnnotation().annotationType(), Min.class );
		assertEquals( minConstraintDescriptor.validateUnwrappedValue(), ValidateUnwrappedValue.YES );
	}

	private class EntityWithSkipAndUnwrapAtTheSameTime {

		@NotNull(payload = { Unwrapping.Unwrap.class, Unwrapping.Skip.class })
		private final ValueHolder<Integer> integerHolder = new ValueHolder<>( 5 );
	}

	private class EntityWithNoContraintValidatorForUnwrappedValue {

		// no constraint validator for the wrapped value
		@Future(payload = { Unwrapping.Unwrap.class })
		private final ValueHolder<Integer> integerHolder = new ValueHolder<>( 5 );
	}

	private class EntityWithExplicitUnwrapping {

		@Min(value = 10, payload = { Unwrapping.Unwrap.class })
		private final ValueHolder<Integer> integerHolder = new ValueHolder<>( 5 );
	}

	private class EntityWithNoUnwrapping {

		@Null
		private final ValueHolder<Integer> integerHolder = new ValueHolder<>( 5 );
	}

	private class WrapperWithImplicitUnwrapping {

		@Min(10)
		private final Wrapper<Integer> integerWrapper = new Wrapper<>( 5 );
	}

	private class WrapperWithDisabledUnwrapping {

		@Null(payload = { Unwrapping.Skip.class })
		private final Wrapper<Integer> integerWrapper = new Wrapper<>( 5 );
	}

	private class WrapperWithForcedUnwrapping {

		@Min(value = 10, payload = { Unwrapping.Unwrap.class })
		private final Wrapper<Integer> integerWrapper = new Wrapper<>( 5 );
	}

	private class IntegerWrapperWithImplicitUnwrapping {

		@Min(10)
		private final IntegerWrapper integerWrapper = new IntegerWrapper( 5 );
	}

	private class IntegerWrapperWithDisabledUnwrapping {

		@Null(payload = { Unwrapping.Skip.class })
		private final IntegerWrapper integerWrapper = new IntegerWrapper( 5 );
	}

	private class IntegerWrapperWithForcedUnwrapping {

		@Min(value = 10, payload = { Unwrapping.Unwrap.class })
		private final IntegerWrapper integerWrapper = new IntegerWrapper( 5 );
	}

	private class ValueHolder<T> {

		private final T value;

		private ValueHolder(T value) {
			this.value = value;
		}

		@SuppressWarnings("unused")
		public T getValue() {
			return value;
		}
	}

	private class Wrapper<T> {

		private final T value;

		private Wrapper(T value) {
			this.value = value;
		}

		@SuppressWarnings("unused")
		public T getValue() {
			return value;
		}
	}

	private class ValueHolderExtractor implements ValueExtractor<ValueHolder<@ExtractedValue ?>> {

		@Override
		public void extractValues(ValueHolder<@ExtractedValue ?> originalValue, ValueExtractor.ValueReceiver receiver) {
			receiver.value( null, originalValue.value );
		}
	}

	@UnwrapByDefault
	private class UnwrapByDefaultWrapperValueExtractor implements ValueExtractor<Wrapper<@ExtractedValue ?>> {

		@Override
		public void extractValues(Wrapper<@ExtractedValue ?> originalValue, ValueExtractor.ValueReceiver receiver) {
			receiver.value( null, originalValue.value );
		}
	}
}
