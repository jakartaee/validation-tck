/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.containerelement;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.pathWith;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;
import static org.hibernate.beanvalidation.tck.util.TestUtil.getConfigurationUnderTest;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.valueextraction.ExtractedValue;
import javax.validation.valueextraction.ValueExtractor;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Guillaume Smet
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class ContainerElementConstraintCustomContainerTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( ContainerElementConstraintCustomContainerTest.class )
				.build();
	}

	private static Validator getValidatorWithValueExtractor() {
		return getConfigurationUnderTest()
				.addValueExtractor( new CustomContainerValueExtractor() )
				.buildValidatorFactory()
				.getValidator();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "c")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "i")
	public void constraint_specified_on_type_parameter_of_custom_container_gets_validated() {
		TypeWithCustomContainer1 o = new TypeWithCustomContainer1();
		o.container = new CustomContainer<String>( "" );

		Set<ConstraintViolation<TypeWithCustomContainer1>> constraintViolations = getValidatorWithValueExtractor().validate( o );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotBlank.class )
						.withProperty( "container" )
						.withInvalidValue( "" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "d")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "i")
	public void constraints_specified_on_custom_container_and_on_type_parameter_of_custom_container_get_validated() {
		TypeWithCustomContainer2 o = new TypeWithCustomContainer2();
		o.container = new CustomContainer<String>( "" );
		Set<ConstraintViolation<TypeWithCustomContainer2>> constraintViolations = getValidatorWithValueExtractor().validate( o );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotBlank.class )
						.withProperty( "container" )
						.withInvalidValue( "" )
		);

		o = new TypeWithCustomContainer2();
		o.container = null;
		constraintViolations = getValidatorWithValueExtractor().validate( o );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withProperty( "container" )
						.withInvalidValue( null )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "c")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "i")
	public void getter_constraint_provided_on_type_parameter_of_an_custom_container_gets_validated() {
		TypeWithCustomContainer3 o = new TypeWithCustomContainer3();
		o.container = new CustomContainer<String>( "" );

		Set<ConstraintViolation<TypeWithCustomContainer3>> constraintViolations = getValidatorWithValueExtractor().validate( o );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotBlank.class )
						.withProperty( "container" )
						.withInvalidValue( "" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "c")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "i")
	public void return_value_constraint_provided_on_type_parameter_of_an_custom_container_gets_validated() throws Exception {
		Method method = TypeWithCustomContainer4.class.getDeclaredMethod( "returnContainer" );
		Set<ConstraintViolation<TypeWithCustomContainer4>> constraintViolations = getValidatorWithValueExtractor().forExecutables().validateReturnValue(
				new TypeWithCustomContainer4(),
				method,
				new CustomContainer<String>( "" )
		);

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotBlank.class )
						.withPropertyPath( pathWith()
								.method( "returnContainer" )
								.returnValue()
						)
						.withInvalidValue( "" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "c")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "i")
	public void method_parameter_constraint_provided_as_type_parameter_of_an_custom_container_gets_validated()
			throws Exception {
		Method method = TypeWithCustomContainer5.class.getDeclaredMethod( "setContainer", CustomContainer.class );
		Object[] values = new Object[] { new CustomContainer<String>( "" ) };

		Set<ConstraintViolation<TypeWithCustomContainer5>> constraintViolations = getValidatorWithValueExtractor().forExecutables().validateParameters(
				new TypeWithCustomContainer5(),
				method,
				values
		);

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotBlank.class )
						.withPropertyPath( pathWith()
								.method( "setContainer" )
								.parameter( "parameter", 0 )
						)
						.withInvalidValue( "" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "c")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "i")
	public void constructor_parameter_constraint_provided_on_type_parameter_of_an_custom_container_gets_validated()
			throws Exception {
		Constructor<TypeWithCustomContainer6> constructor = TypeWithCustomContainer6.class.getDeclaredConstructor( CustomContainer.class );
		Object[] values = new Object[] { new CustomContainer<String>( "" ) };

		Set<ConstraintViolation<TypeWithCustomContainer6>> constraintViolations = getValidatorWithValueExtractor().forExecutables().validateConstructorParameters(
				constructor,
				values
		);

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotBlank.class )
						.withPropertyPath( pathWith()
								.constructor( TypeWithCustomContainer6.class )
								.parameter( "parameter", 0 )
						)
						.withInvalidValue( "" )
		);
	}

	private class CustomContainer<T> {

		private T property;

		private CustomContainer(T property) {
			this.property = property;
		}
	}

	private static class CustomContainerValueExtractor implements ValueExtractor<CustomContainer<@ExtractedValue ?>> {

		@Override
		public void extractValues(CustomContainer<?> originalValue, ValueExtractor.ValueReceiver receiver) {
			receiver.value( null, originalValue.property );
		}
	}

	private static class TypeWithCustomContainer1 {

		@SuppressWarnings("unused")
		private CustomContainer<@NotBlank String> container;
	}

	private static class TypeWithCustomContainer2 {

		@NotNull
		private CustomContainer<@NotBlank String> container;
	}

	private static class TypeWithCustomContainer3 {

		private CustomContainer<String> container;

		@SuppressWarnings("unused")
		public CustomContainer<@NotBlank String> getContainer() {
			return container;
		}
	}

	private static class TypeWithCustomContainer4 {

		private CustomContainer<String> container;

		@SuppressWarnings("unused")
		public CustomContainer<@NotBlank String> returnContainer() {
			return container;
		}
	}

	private static class TypeWithCustomContainer5 {

		@SuppressWarnings("unused")
		public void setContainer(CustomContainer<@NotBlank String> parameter) {
		}
	}

	private static class TypeWithCustomContainer6 {

		@SuppressWarnings("unused")
		public TypeWithCustomContainer6(CustomContainer<@NotBlank String> parameter) {
		}
	}
}
