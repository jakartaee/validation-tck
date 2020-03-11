/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.valueextraction.unwrapping;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.pathWith;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;
import static org.testng.Assert.assertEquals;

import java.util.Set;

import jakarta.validation.ConstraintDeclarationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.UnexpectedTypeException;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.metadata.ConstraintDescriptor;
import jakarta.validation.metadata.ValidateUnwrappedValue;
import jakarta.validation.valueextraction.ExtractedValue;
import jakarta.validation.valueextraction.UnwrapByDefault;
import jakarta.validation.valueextraction.Unwrapping;
import jakarta.validation.valueextraction.ValueExtractor;

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
@SpecVersion(spec = "beanvalidation", version = "3.0.0")
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
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "k")
	public void validate_wrapped_value_if_explicit_unwrapping() {
		Set<ConstraintViolation<EntityWithExplicitUnwrapping>> constraintViolations = getValidatorWithValueExtractors().validate( new EntityWithExplicitUnwrapping() );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Min.class )
						.withProperty( "integerHolder" )
						.withInvalidValue( 5 )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS_IMPLICITUNWRAPPING, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_IMPLICITUNWRAPPING, id = "e")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "i")
	public void validate_wrapper_itself_if_there_is_no_unwrapping() {
		EntityWithNoUnwrapping entity = new EntityWithNoUnwrapping();
		Set<ConstraintViolation<EntityWithNoUnwrapping>> constraintViolations = getValidatorWithValueExtractors().validate( entity );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Null.class )
						.withProperty( "integerHolder" )
						.withInvalidValue( entity.integerHolder )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_IMPLICITUNWRAPPING, id = "d")
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION_UNWRAPBYDEFAULT, id = "a")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "k")
	public void validate_wrapped_value_if_value_extractor_unwraps_by_default_for_generic_container() {
		Set<ConstraintViolation<WrapperWithImplicitUnwrapping>> constraintViolations = getValidatorWithValueExtractors().validate( new WrapperWithImplicitUnwrapping() );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Min.class )
						.withProperty( "integerWrapper" )
						.withInvalidValue( 5 )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS_IMPLICITUNWRAPPING, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_IMPLICITUNWRAPPING, id = "a")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "i")
	public void validate_wrapper_if_unwrapping_disabled_per_constraint_for_generic_container() {
		WrapperWithDisabledUnwrapping entity = new WrapperWithDisabledUnwrapping();
		Set<ConstraintViolation<WrapperWithDisabledUnwrapping>> constraintViolations = getValidatorWithValueExtractors().validate( entity );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Null.class )
						.withProperty( "integerWrapper" )
						.withInvalidValue( entity.integerWrapper )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS_IMPLICITUNWRAPPING, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_IMPLICITUNWRAPPING, id = "b")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "k")
	public void validate_wrapped_value_if_value_extractor_unwraps_by_default_and_unwrapping_enabled_per_constraint_for_generic_container() {
		Set<ConstraintViolation<WrapperWithForcedUnwrapping>> constraintViolations = getValidatorWithValueExtractors().validate( new WrapperWithForcedUnwrapping() );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Min.class )
						.withProperty( "integerWrapper" )
						.withInvalidValue( 5 )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_IMPLICITUNWRAPPING, id = "d")
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION_UNWRAPBYDEFAULT, id = "a")
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION_EXTRACTEDVALUE, id = "e")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "k")
	public void validate_wrapped_value_if_value_extractor_unwraps_by_default_for_non_generic_container() {
		Set<ConstraintViolation<IntegerWrapperWithImplicitUnwrapping>> constraintViolations = getValidatorWithValueExtractors().validate( new IntegerWrapperWithImplicitUnwrapping() );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Min.class )
						.withPropertyPath( pathWith()
								.property( "integerWrapper" )
								.containerElement( "wrapper", false, null, null, IntegerWrapper.class, null )
						)
						.withInvalidValue( 5 )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS_IMPLICITUNWRAPPING, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_IMPLICITUNWRAPPING, id = "a")
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION_EXTRACTEDVALUE, id = "e")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "i")
	public void validate_wrapper_if_unwrapping_disabled_per_constraint_for_non_generic_container() {
		IntegerWrapperWithDisabledUnwrapping entity = new IntegerWrapperWithDisabledUnwrapping();
		Set<ConstraintViolation<IntegerWrapperWithDisabledUnwrapping>> constraintViolations = getValidatorWithValueExtractors().validate( entity );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Null.class )
						.withProperty( "integerWrapper" )
						.withInvalidValue( entity.integerWrapper )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS_IMPLICITUNWRAPPING, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_IMPLICITUNWRAPPING, id = "d")
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION_EXTRACTEDVALUE, id = "e")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "k")
	public void validate_wrapped_value_if_value_extractor_unwraps_by_default_and_unwrapping_enabled_per_constraint_for_non_generic_container() {
		Set<ConstraintViolation<IntegerWrapperWithForcedUnwrapping>> constraintViolations = getValidatorWithValueExtractors().validate( new IntegerWrapperWithForcedUnwrapping() );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Min.class )
						.withPropertyPath( pathWith()
								.property( "integerWrapper" )
								.containerElement( "wrapper", false, null, null, IntegerWrapper.class, null )
						)
						.withInvalidValue( 5 )
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
		assertEquals( minConstraintDescriptor.getValueUnwrapping(), ValidateUnwrappedValue.DEFAULT );

		minConstraintDescriptor = validator.getConstraintsForClass( WrapperWithDisabledUnwrapping.class )
				.getConstraintsForProperty( "integerWrapper" )
				.getConstraintDescriptors()
				.iterator().next();

		assertEquals( minConstraintDescriptor.getAnnotation().annotationType(), Null.class );
		assertEquals( minConstraintDescriptor.getValueUnwrapping(), ValidateUnwrappedValue.SKIP );

		minConstraintDescriptor = validator.getConstraintsForClass( WrapperWithForcedUnwrapping.class )
				.getConstraintsForProperty( "integerWrapper" )
				.getConstraintDescriptors()
				.iterator().next();

		assertEquals( minConstraintDescriptor.getAnnotation().annotationType(), Min.class );
		assertEquals( minConstraintDescriptor.getValueUnwrapping(), ValidateUnwrappedValue.UNWRAP );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_IMPLICITUNWRAPPING, id = "c")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "k")
	public void validate_implicit_unwrapping_having_two_type_parameters_and_only_one_maximally_specific_value_extractor_is_ok() {
		Validator validator = TestUtil.getConfigurationUnderTest()
				.addValueExtractor( new UnwrapByDefaultWrapperWithTwoTypeArgumentsFirstValueExtractor() )
				.buildValidatorFactory()
				.getValidator();

		Set<ConstraintViolation<BeanWithWrapperWithTwoTypeArguments>> constraintViolations = validator.validate( new BeanWithWrapperWithTwoTypeArguments() );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Min.class )
						.withPropertyPath( pathWith()
								.property( "wrapper" )
								.containerElement( "first", false, null, null, WrapperWithTwoTypeArguments.class, 0 )
						)
						.withInvalidValue( 5L )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_IMPLICITUNWRAPPING, id = "c")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "k")
	public void validate_implicit_unwrapping_having_two_type_parameters_and_only_one_maximally_specific_value_extractor_marked_with_unwrap_by_default_is_ok() {
		Validator validator = TestUtil.getConfigurationUnderTest()
				.addValueExtractor( new UnwrapByDefaultWrapperWithTwoTypeArgumentsFirstValueExtractor() )
				.addValueExtractor( new WrapperWithTwoTypeArgumentsSecondValueExtractor() )
				.buildValidatorFactory()
				.getValidator();

		Set<ConstraintViolation<BeanWithWrapperWithTwoTypeArguments>> constraintViolations = validator.validate( new BeanWithWrapperWithTwoTypeArguments() );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Min.class )
						.withPropertyPath( pathWith()
								.property( "wrapper" )
								.containerElement( "first", false, null, null, WrapperWithTwoTypeArguments.class, 0 )
						)
						.withInvalidValue( 5L )
		);
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_IMPLICITUNWRAPPING, id = "c")
	public void validate_implicit_unwrapping_having_two_type_parameters_and_two_maximally_specific_value_extractors_marked_with_unwrap_by_default_raises_exception() {
		Validator validator = TestUtil.getConfigurationUnderTest()
				.addValueExtractor( new UnwrapByDefaultWrapperWithTwoTypeArgumentsFirstValueExtractor() )
				.addValueExtractor( new UnwrapByDefaultWrapperWithTwoTypeArgumentsSecondValueExtractor() )
				.buildValidatorFactory()
				.getValidator();

		validator.validate( new BeanWithWrapperWithTwoTypeArguments() );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_IMPLICITUNWRAPPING, id = "c")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "k")
	public void validate_forced_unwrapping_having_two_type_parameters_and_only_one_maximally_specific_value_extractor_is_ok() {
		Validator validator = TestUtil.getConfigurationUnderTest()
				.addValueExtractor( new WrapperWithTwoTypeArgumentsFirstValueExtractor() )
				.buildValidatorFactory()
				.getValidator();

		Set<ConstraintViolation<BeanWithWrapperWithTwoTypeArgumentsAndForcedUnwrapping>> constraintViolations =
				validator.validate( new BeanWithWrapperWithTwoTypeArgumentsAndForcedUnwrapping() );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Min.class )
						.withPropertyPath( pathWith()
								.property( "wrapper" )
								.containerElement( "first", false, null, null, WrapperWithTwoTypeArguments.class, 0 )
						)
						.withInvalidValue( 5L )
		);
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_IMPLICITUNWRAPPING, id = "c")
	public void validate_forced_unwrapping_having_two_type_parameters_and_two_maximally_specific_value_extractors_raises_exception() {
		Validator validator = TestUtil.getConfigurationUnderTest()
				.addValueExtractor( new WrapperWithTwoTypeArgumentsFirstValueExtractor() )
				.addValueExtractor( new WrapperWithTwoTypeArgumentsSecondValueExtractor() )
				.buildValidatorFactory()
				.getValidator();

		validator.validate( new BeanWithWrapperWithTwoTypeArgumentsAndForcedUnwrapping() );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_IMPLICITUNWRAPPING, id = "c")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "k")
	public void validate_forced_unwrapping_having_two_reverted_type_parameters_correctly_identifies_type_parameter() {
		Validator validator = TestUtil.getConfigurationUnderTest()
				.addValueExtractor( new WrapperWithTwoTypeArgumentsSecondValueExtractor() )
				.buildValidatorFactory()
				.getValidator();

		Set<ConstraintViolation<BeanWithWrapperWithRevertedTwoTypeArgumentsAndForcedUnwrapping>> constraintViolations =
				validator.validate( new BeanWithWrapperWithRevertedTwoTypeArgumentsAndForcedUnwrapping() );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Min.class )
						.withPropertyPath( pathWith()
								.property( "wrapper" )
								.containerElement( "second", false, null, null, WrapperWithRevertedTwoTypeArguments.class, 0 )
						)
						.withInvalidValue( 5L )
		);
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

	private class BeanWithWrapperWithTwoTypeArguments {

		@Min(value = 10)
		private final WrapperWithTwoTypeArguments<Long, String> wrapper = new WrapperWithTwoTypeArguments<>( 5L, "value" );
	}

	private class BeanWithWrapperWithTwoTypeArgumentsAndForcedUnwrapping {

		@Min(value = 10, payload = Unwrapping.Unwrap.class)
		private final WrapperWithTwoTypeArguments<Long, String> wrapper = new WrapperWithTwoTypeArguments<>( 5L, "value" );
	}

	private class BeanWithWrapperWithRevertedTwoTypeArgumentsAndForcedUnwrapping {

		@Min(value = 10, payload = Unwrapping.Unwrap.class)
		private final WrapperWithRevertedTwoTypeArguments<Long, String> wrapper = new WrapperWithRevertedTwoTypeArguments<>( 5L, "value" );
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

	private class WrapperWithTwoTypeArguments<T, U> {

		private final T value1;
		private final U value2;

		private WrapperWithTwoTypeArguments(T value1, U value2) {
			this.value1 = value1;
			this.value2 = value2;
		}
	}

	private class WrapperWithRevertedTwoTypeArguments<V, W> extends WrapperWithTwoTypeArguments<W, V> {

		private WrapperWithRevertedTwoTypeArguments(V value1, W value2) {
			super( value2, value1 );
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

	@UnwrapByDefault
	private class UnwrapByDefaultWrapperWithTwoTypeArgumentsFirstValueExtractor implements ValueExtractor<WrapperWithTwoTypeArguments<@ExtractedValue ?, ?>> {

		@Override
		public void extractValues(WrapperWithTwoTypeArguments<?, ?> originalValue, ValueExtractor.ValueReceiver receiver) {
			receiver.value( "first", originalValue.value1 );
		}
	}

	@UnwrapByDefault
	private class UnwrapByDefaultWrapperWithTwoTypeArgumentsSecondValueExtractor implements ValueExtractor<WrapperWithTwoTypeArguments<?, @ExtractedValue ?>> {

		@Override
		public void extractValues(WrapperWithTwoTypeArguments<?, ?> originalValue, ValueExtractor.ValueReceiver receiver) {
			receiver.value( "second", originalValue.value2 );
		}
	}

	private class WrapperWithTwoTypeArgumentsFirstValueExtractor implements ValueExtractor<WrapperWithTwoTypeArguments<@ExtractedValue ?, ?>> {

		@Override
		public void extractValues(WrapperWithTwoTypeArguments<?, ?> originalValue, ValueExtractor.ValueReceiver receiver) {
			receiver.value( "first", originalValue.value1 );
		}
	}

	private class WrapperWithTwoTypeArgumentsSecondValueExtractor implements ValueExtractor<WrapperWithTwoTypeArguments<?, @ExtractedValue ?>> {

		@Override
		public void extractValues(WrapperWithTwoTypeArguments<?, ?> originalValue, ValueExtractor.ValueReceiver receiver) {
			receiver.value( "second", originalValue.value2 );
		}
	}
}
