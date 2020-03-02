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
import java.util.HashSet;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.CollectionHelper;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Khalid Alqinyah
 * @author Hardy Ferentschik
 * @author Guillaume Smet
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class ContainerElementConstraintSetTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( ContainerElementConstraintSetTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "c")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "i")
	public void field_constraint_provided_on_type_parameter_of_a_set_gets_validated() {
		TypeWithSet1 s = new TypeWithSet1();
		s.names = CollectionHelper.asSet( "First", "", null );

		Set<ConstraintViolation<TypeWithSet1>> constraintViolations = getValidator().validate( s );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotBlank.class )
						.withPropertyPath( pathWith()
								.property( "names" )
								.containerElement( "<iterable element>", true, null, null, Set.class, 0 )
						)
						.withInvalidValue( "" ),
				violationOf( NotBlank.class )
						.withPropertyPath( pathWith()
								.property( "names" )
								.containerElement( "<iterable element>", true, null, null, Set.class, 0 )
						)
						.withInvalidValue( null ),
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.property( "names" )
								.containerElement( "<iterable element>", true, null, null, Set.class, 0 )
						)
						.withInvalidValue( null )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "d")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "i")
	public void constraints_specified_on_set_and_on_type_parameter_of_set_get_validated() {
		TypeWithSet2 s = new TypeWithSet2();
		s.names = CollectionHelper.asSet( "First", "", null );
		Set<ConstraintViolation<TypeWithSet2>> constraintViolations = getValidator().validate( s );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotBlank.class )
						.withPropertyPath( pathWith()
								.property( "names" )
								.containerElement( "<iterable element>", true, null, null, Set.class, 0 )
						)
						.withInvalidValue( "" ),
				violationOf( NotBlank.class )
						.withPropertyPath( pathWith()
								.property( "names" )
								.containerElement( "<iterable element>", true, null, null, Set.class, 0 )
						)
						.withInvalidValue( null )
		);

		s = new TypeWithSet2();
		s.names = new HashSet<>();
		constraintViolations = getValidator().validate( s );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Size.class )
						.withProperty( "names" )
						.withInvalidValue( s.names )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "c")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "i")
	public void getter_constraint_provided_on_type_parameter_of_a_set_gets_validated() {
		TypeWithSet3 s = new TypeWithSet3();
		s.strings = new HashSet<>();
		s.strings.add( "First" );
		s.strings.add( "" );
		s.strings.add( null );

		Set<ConstraintViolation<TypeWithSet3>> constraintViolations = getValidator().validate( s );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotBlank.class )
						.withPropertyPath( pathWith()
								.property( "strings" )
								.containerElement( "<iterable element>", true, null, null, Set.class, 0 )
						)
						.withInvalidValue( "" ),
				violationOf( NotBlank.class )
						.withPropertyPath( pathWith()
								.property( "strings" )
								.containerElement( "<iterable element>", true, null, null, Set.class, 0 )
						)
						.withInvalidValue( null ),
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.property( "strings" )
								.containerElement( "<iterable element>", true, null, null, Set.class, 0 )
						)
						.withInvalidValue( null )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "c")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "i")
	public void return_value_constraint_provided_on_type_parameter_of_a_set_gets_validated() throws Exception {
		Method method = TypeWithSet4.class.getDeclaredMethod( "returnStrings" );
		Set<ConstraintViolation<TypeWithSet4>> constraintViolations = getExecutableValidator().validateReturnValue(
				new TypeWithSet4(),
				method,
				CollectionHelper.asSet( "First", "", null )
		);

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotBlank.class )
						.withPropertyPath( pathWith()
								.method( "returnStrings" )
								.returnValue()
								.containerElement( "<iterable element>", true, null, null, Set.class, 0 )
						)
						.withInvalidValue( "" ),
				violationOf( NotBlank.class )
						.withPropertyPath( pathWith()
								.method( "returnStrings" )
								.returnValue()
								.containerElement( "<iterable element>", true, null, null, Set.class, 0 )
						)
						.withInvalidValue( null ),
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.method( "returnStrings" )
								.returnValue()
								.containerElement( "<iterable element>", true, null, null, Set.class, 0 )
						)
						.withInvalidValue( null )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "c")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "i")
	public void method_parameter_constraint_provided_as_type_parameter_of_a_set_gets_validated() throws Exception {
		Method method = TypeWithSet5.class.getDeclaredMethod( "setValues", Set.class );
		Object[] values = new Object[] { CollectionHelper.asSet( "", "First", null ) };

		Set<ConstraintViolation<TypeWithSet5>> constraintViolations = getExecutableValidator().validateParameters(
				new TypeWithSet5(),
				method,
				values
		);

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotBlank.class )
						.withPropertyPath( pathWith()
								.method( "setValues" )
								.parameter( "setParameter", 0 )
								.containerElement( "<iterable element>", true, null, null, Set.class, 0 )
						)
						.withInvalidValue( "" ),
				violationOf( NotBlank.class )
						.withPropertyPath( pathWith()
								.method( "setValues" )
								.parameter( "setParameter", 0 )
								.containerElement( "<iterable element>", true, null, null, Set.class, 0 )
						)
						.withInvalidValue( null ),
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.method( "setValues" )
								.parameter( "setParameter", 0 )
								.containerElement( "<iterable element>", true, null, null, Set.class, 0 )
						)
						.withInvalidValue( null )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "c")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "i")
	public void constructor_parameter_constraint_provided_on_type_parameter_of_a_set_gets_validated() throws Exception {
		Constructor<TypeWithSet6> constructor = TypeWithSet6.class.getDeclaredConstructor( Set.class );
		Object[] values = new Object[] { CollectionHelper.asSet( "", "First", null ) };

		Set<ConstraintViolation<TypeWithSet6>> constraintViolations = getExecutableValidator().validateConstructorParameters(
				constructor,
				values
		);

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotBlank.class )
						.withPropertyPath( pathWith()
								.constructor( TypeWithSet6.class )
								.parameter( "setParameter", 0 )
								.containerElement( "<iterable element>", true, null, null, Set.class, 0 )
						)
						.withInvalidValue( "" ),
				violationOf( NotBlank.class )
						.withPropertyPath( pathWith()
								.constructor( TypeWithSet6.class )
								.parameter( "setParameter", 0 )
								.containerElement( "<iterable element>", true, null, null, Set.class, 0 )
						)
						.withInvalidValue( null ),
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.constructor( TypeWithSet6.class )
								.parameter( "setParameter", 0 )
								.containerElement( "<iterable element>", true, null, null, Set.class, 0 )
						)
						.withInvalidValue( null )
		);
	}

	private static class TypeWithSet1 {

		@SuppressWarnings("unused")
		private Set<@NotNull @NotBlank String> names;
	}

	private static class TypeWithSet2 {

		@Size(min = 1)
		private Set<@NotBlank String> names;
	}

	private static class TypeWithSet3 {

		private Set<String> strings;

		@SuppressWarnings("unused")
		public Set<@NotNull @NotBlank String> getStrings() {
			return strings;
		}
	}

	private static class TypeWithSet4 {

		private Set<String> strings;

		@SuppressWarnings("unused")
		public Set<@NotNull @NotBlank String> returnStrings() {
			return strings;
		}
	}

	private static class TypeWithSet5 {

		@SuppressWarnings("unused")
		public void setValues(Set<@NotNull @NotBlank String> setParameter) {
		}
	}

	private static class TypeWithSet6 {

		@SuppressWarnings("unused")
		public TypeWithSet6(Set<@NotNull @NotBlank String> setParameter) {
		}
	}
}
