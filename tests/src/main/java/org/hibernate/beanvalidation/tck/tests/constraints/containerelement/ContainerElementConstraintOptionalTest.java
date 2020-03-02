/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.containerelement;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.pathWith;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests Java 8 type use annotations.
 *
 * @author Khalid Alqinyah
 * @author Hardy Ferentschik
 * @author Guillaume Smet
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class ContainerElementConstraintOptionalTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( ContainerElementConstraintOptionalTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "c")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "i")
	public void constraint_specified_on_type_parameter_of_optional_gets_validated() {
		TypeWithOptional1 o = new TypeWithOptional1();
		o.stringOptional = Optional.of( "" );

		Set<ConstraintViolation<TypeWithOptional1>> constraintViolations = getValidator().validate( o );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotBlank.class )
						.withProperty( "stringOptional" )
						.withInvalidValue( "" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "d")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "i")
	public void constraints_specified_on_optional_and_on_type_parameter_of_optional_get_validated() {
		TypeWithOptional2 o = new TypeWithOptional2();
		o.stringOptional = Optional.of( "" );
		Set<ConstraintViolation<TypeWithOptional2>> constraintViolations = getValidator().validate( o );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotBlank.class )
						.withProperty( "stringOptional" )
						.withInvalidValue( "" )
		);

		o = new TypeWithOptional2();
		o.stringOptional = null;
		constraintViolations = getValidator().validate( o );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withProperty( "stringOptional" )
						.withInvalidValue( null )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "c")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "i")
	public void getter_constraint_provided_on_type_parameter_of_an_optional_gets_validated() {
		TypeWithOptional3 o = new TypeWithOptional3();
		o.stringOptional = Optional.of( "" );

		Set<ConstraintViolation<TypeWithOptional3>> constraintViolations = getValidator().validate( o );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotBlank.class )
						.withProperty( "stringOptional" )
						.withInvalidValue( "" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "c")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "i")
	public void return_value_constraint_provided_on_type_parameter_of_an_optional_gets_validated() throws Exception {
		Method method = TypeWithOptional4.class.getDeclaredMethod( "returnStringOptional" );
		Set<ConstraintViolation<TypeWithOptional4>> constraintViolations = getValidator().forExecutables().validateReturnValue(
				new TypeWithOptional4(),
				method,
				Optional.of( "" )
		);

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotBlank.class )
						.withPropertyPath( pathWith()
								.method( "returnStringOptional" )
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
	public void method_parameter_constraint_provided_as_type_parameter_of_an_optional_gets_validated()
			throws Exception {
		Method method = TypeWithOptional5.class.getDeclaredMethod( "setValues", Optional.class );
		Object[] values = new Object[] { Optional.of( "" ) };

		Set<ConstraintViolation<TypeWithOptional5>> constraintViolations = getValidator().forExecutables().validateParameters(
				new TypeWithOptional5(),
				method,
				values
		);

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotBlank.class )
						.withPropertyPath( pathWith()
								.method( "setValues" )
								.parameter( "optionalParameter", 0 )
						)
						.withInvalidValue( "" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "c")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "i")
	public void constructor_parameter_constraint_provided_on_type_parameter_of_an_optional_gets_validated()
			throws Exception {
		Constructor<TypeWithOptional6> constructor = TypeWithOptional6.class.getDeclaredConstructor( Optional.class );
		Object[] values = new Object[] { Optional.of( "" ) };

		Set<ConstraintViolation<TypeWithOptional6>> constraintViolations = getValidator().forExecutables().validateConstructorParameters(
				constructor,
				values
		);

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotBlank.class )
						.withPropertyPath( pathWith()
								.constructor( TypeWithOptional6.class )
								.parameter( "optionalParameter", 0 )
						)
						.withInvalidValue( "" )
		);
	}

	private static class TypeWithOptional1 {

		@SuppressWarnings("unused")
		private Optional<@NotBlank String> stringOptional;
	}

	private static class TypeWithOptional2 {

		@NotNull
		private Optional<@NotBlank String> stringOptional;
	}

	private static class TypeWithOptional3 {

		private Optional<String> stringOptional;

		@SuppressWarnings("unused")
		public Optional<@NotBlank String> getStringOptional() {
			return stringOptional;
		}
	}

	private static class TypeWithOptional4 {

		private Optional<String> stringOptional;

		@SuppressWarnings("unused")
		public Optional<@NotBlank String> returnStringOptional() {
			return stringOptional;
		}
	}

	private static class TypeWithOptional5 {

		@SuppressWarnings("unused")
		public void setValues(Optional<@NotBlank String> optionalParameter) {
		}
	}

	private static class TypeWithOptional6 {

		@SuppressWarnings("unused")
		public TypeWithOptional6(Optional<@NotBlank String> optionalParameter) {
		}
	}
}
