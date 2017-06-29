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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
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
public class ContainerElementConstraintListTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( ContainerElementConstraintListTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "c")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "i")
	public void field_constraint_provided_on_type_parameter_of_a_list_gets_validated() {
		TypeWithList1 l = new TypeWithList1();
		l.names = Arrays.asList( "First", "", null );

		Set<ConstraintViolation<TypeWithList1>> constraintViolations = getValidator().validate( l );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotBlank.class )
						.withPropertyPath( pathWith()
								.property( "names" )
								.containerElement( "<list element>", true, null, 1, List.class, 0 )
						)
						.withInvalidValue( "" ),
				violationOf( NotBlank.class )
						.withPropertyPath( pathWith()
								.property( "names" )
								.containerElement( "<list element>", true, null, 2, List.class, 0 )
						)
						.withInvalidValue( null ),
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.property( "names" )
								.containerElement( "<list element>", true, null, 2, List.class, 0 )
						)
						.withInvalidValue( null )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "d")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "i")
	public void constraints_specified_on_list_and_on_type_parameter_of_list_get_validated() {
		TypeWithList2 l = new TypeWithList2();
		l.names = Arrays.asList( "First", "", null );
		Set<ConstraintViolation<TypeWithList2>> constraintViolations = getValidator().validate( l );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotBlank.class )
						.withPropertyPath( pathWith()
								.property( "names" )
								.containerElement( "<list element>", true, null, 1, List.class, 0 )
						)
						.withInvalidValue( "" ),
				violationOf( NotBlank.class )
						.withPropertyPath( pathWith()
								.property( "names" )
								.containerElement( "<list element>", true, null, 2, List.class, 0 )
						)
						.withInvalidValue( null )
		);

		l = new TypeWithList2();
		l.names = new ArrayList<>();
		constraintViolations = getValidator().validate( l );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf(  Size.class  )
						.withProperty( "names" )
						.withInvalidValue( l.names )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "c")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "i")
	public void getter_constraint_provided_on_type_parameter_of_a_list_gets_validated() {
		TypeWithList3 l = new TypeWithList3();
		l.strings = Arrays.asList( "", "First", null );

		Set<ConstraintViolation<TypeWithList3>> constraintViolations = getValidator().validate( l );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotBlank.class )
						.withPropertyPath( pathWith()
								.property( "strings" )
								.containerElement( "<list element>", true, null, 0, List.class, 0 )
						)
						.withInvalidValue( "" ),
				violationOf( NotBlank.class )
						.withPropertyPath( pathWith()
								.property( "strings" )
								.containerElement( "<list element>", true, null, 2, List.class, 0 )
						)
						.withInvalidValue( null ),
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.property( "strings" )
								.containerElement( "<list element>", true, null, 2, List.class, 0 )
						)
						.withInvalidValue( null )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "c")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "i")
	public void return_value_constraint_provided_on_type_parameter_of_a_list_gets_validated() throws Exception {
		Method method = TypeWithList4.class.getDeclaredMethod( "returnStrings" );
		Set<ConstraintViolation<TypeWithList4>> constraintViolations = getExecutableValidator().validateReturnValue(
				new TypeWithList4(),
				method,
				Arrays.asList( "First", "", null )
		);

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotBlank.class )
						.withPropertyPath( pathWith()
								.method( "returnStrings" )
								.returnValue()
								.containerElement( "<list element>", true, null, 1, List.class, 0 )
						)
						.withInvalidValue( "" ),
				violationOf( NotBlank.class )
						.withPropertyPath( pathWith()
								.method( "returnStrings" )
								.returnValue()
								.containerElement( "<list element>", true, null, 2, List.class, 0 )
						)
						.withInvalidValue( null ),
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.method( "returnStrings" )
								.returnValue()
								.containerElement( "<list element>", true, null, 2, List.class, 0 )
						)
						.withInvalidValue( null )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "c")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "i")
	public void method_parameter_constraint_provided_as_type_parameter_of_a_list_gets_validated() throws Exception {
		Method method = TypeWithList5.class.getDeclaredMethod( "setValues", List.class );
		Object[] values = new Object[] { Arrays.asList( "", "First", null ) };

		Set<ConstraintViolation<TypeWithList5>> constraintViolations = getExecutableValidator().validateParameters(
				new TypeWithList5(),
				method,
				values
		);

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotBlank.class )
						.withPropertyPath( pathWith()
								.method( "setValues" )
								.parameter( "listParameter", 0 )
								.containerElement( "<list element>", true, null, 0, List.class, 0 )
						)
						.withInvalidValue( "" ),
				violationOf( NotBlank.class )
						.withPropertyPath( pathWith()
								.method( "setValues" )
								.parameter( "listParameter", 0 )
								.containerElement( "<list element>", true, null, 2, List.class, 0 )
						)
						.withInvalidValue( null ),
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.method( "setValues" )
								.parameter( "listParameter", 0 )
								.containerElement( "<list element>", true, null, 2, List.class, 0 )
						)
						.withInvalidValue( null )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "c")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "i")
	public void constructor_parameter_constraint_provided_on_type_parameter_of_a_list_gets_validated() throws Exception {
		Constructor<TypeWithList6> constructor = TypeWithList6.class.getDeclaredConstructor( List.class );
		Object[] values = new Object[] { Arrays.asList( "", "First", null ) };

		Set<ConstraintViolation<TypeWithList6>> constraintViolations = getValidator().forExecutables().validateConstructorParameters(
				constructor,
				values
		);

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotBlank.class )
						.withPropertyPath( pathWith()
								.constructor( TypeWithList6.class )
								.parameter( "listParameter", 0 )
								.containerElement( "<list element>", true, null, 0, List.class, 0 )
						)
						.withInvalidValue( "" ),
				violationOf( NotBlank.class )
						.withPropertyPath( pathWith()
								.constructor( TypeWithList6.class )
								.parameter( "listParameter", 0 )
								.containerElement( "<list element>", true, null, 2, List.class, 0 )
						)
						.withInvalidValue( null ),
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.constructor( TypeWithList6.class )
								.parameter( "listParameter", 0 )
								.containerElement( "<list element>", true, null, 2, List.class, 0 )
						)
						.withInvalidValue( null )
		);
	}

	private static class TypeWithList1 {

		@SuppressWarnings("unused")
		private List<@NotNull @NotBlank String> names;
	}

	private static class TypeWithList2 {

		@Size(min = 1)
		private List<@NotBlank String> names;
	}

	private static class TypeWithList3 {

		private List<String> strings;

		@SuppressWarnings("unused")
		public List<@NotNull @NotBlank String> getStrings() {
			return strings;
		}
	}

	private static class TypeWithList4 {

		private List<String> strings;

		@SuppressWarnings("unused")
		public List<@NotNull @NotBlank String> returnStrings() {
			return strings;
		}
	}

	private static class TypeWithList5 {

		@SuppressWarnings("unused")
		public void setValues(List<@NotNull @NotBlank String> listParameter) {
		}
	}

	private static class TypeWithList6 {

		@SuppressWarnings("unused")
		public TypeWithList6(List<@NotNull @NotBlank String> listParameter) {
		}
	}
}
