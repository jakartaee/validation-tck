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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
public class ContainerElementConstraintMapValueTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( ContainerElementConstraintMapValueTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "c")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "i")
	public void constraint_specified_on_value_type_parameter_of_map_gets_validated() {
		TypeWithMap1 m = new TypeWithMap1();
		m.nameMap = new HashMap<>();
		m.nameMap.put( "first", "Name 1" );
		m.nameMap.put( "second", "" );
		m.nameMap.put( "third", "Name 3" );
		Set<ConstraintViolation<TypeWithMap1>> constraintViolations = getValidator().validate( m );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotBlank.class )
						.withPropertyPath( pathWith()
								.property( "nameMap" )
								.containerElement( "<map value>", true, "second", null, Map.class, 1 )
						)
						.withInvalidValue( "" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "d")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "i")
	public void constraints_specified_on_map_and_on_value_type_parameter_of_map_get_validated() {
		TypeWithMap2 m = new TypeWithMap2();
		m.nameMap = new HashMap<>();
		m.nameMap.put( "first", "Name 1" );
		m.nameMap.put( "second", "" );
		m.nameMap.put( "third", "Name 3" );
		Set<ConstraintViolation<TypeWithMap2>> constraintViolations = getValidator().validate( m );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotBlank.class )
						.withPropertyPath( pathWith()
								.property( "nameMap" )
								.containerElement( "<map value>", true, "second", null, Map.class, 1 )
						)
						.withInvalidValue( "" )
		);

		m = new TypeWithMap2();
		constraintViolations = getValidator().validate( m );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withProperty( "nameMap" )
						.withInvalidValue( m.nameMap )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "c")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "i")
	public void getter_constraint_provided_on_value_type_parameter_of_a_map_gets_validated() {
		TypeWithMap3 m = new TypeWithMap3();
		m.stringMap = new HashMap<>();
		m.stringMap.put( "first", "" );
		m.stringMap.put( "second", "Second" );
		m.stringMap.put( "third", null );

		Set<ConstraintViolation<TypeWithMap3>> constraintViolations = getValidator().validate( m );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotBlank.class )
						.withPropertyPath( pathWith()
								.property( "stringMap" )
								.containerElement( "<map value>", true, "first", null, Map.class, 1 )
						)
						.withInvalidValue( "" ),
				violationOf( NotBlank.class )
						.withPropertyPath( pathWith()
								.property( "stringMap" )
								.containerElement( "<map value>", true, "third", null, Map.class, 1 )
						)
						.withInvalidValue( null ),
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.property( "stringMap" )
								.containerElement( "<map value>", true, "third", null, Map.class, 1 )
						)
						.withInvalidValue( null )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "c")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "i")
	public void return_value_constraint_provided_on_value_type_parameter_of_a_map_gets_validated() throws Exception {
		Method method = TypeWithMap4.class.getDeclaredMethod( "returnStringMap" );

		Map<String, String> parameter = new HashMap<>();
		parameter.put( "first", "First" );
		parameter.put( "second", "" );
		parameter.put( "third", null );

		Set<ConstraintViolation<TypeWithMap4>> constraintViolations = getValidator().forExecutables().validateReturnValue(
				new TypeWithMap4(),
				method,
				parameter
		);

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotBlank.class )
						.withPropertyPath( pathWith()
								.method( "returnStringMap" )
								.returnValue()
								.containerElement( "<map value>", true, "second", null, Map.class, 1 )
						)
						.withInvalidValue( "" ),
				violationOf( NotBlank.class )
						.withPropertyPath( pathWith()
								.method( "returnStringMap" )
								.returnValue()
								.containerElement( "<map value>", true, "third", null, Map.class, 1 )
						)
						.withInvalidValue( null ),
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.method( "returnStringMap" )
								.returnValue()
								.containerElement( "<map value>", true, "third", null, Map.class, 1 )
						)
						.withInvalidValue( null )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "c")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "i")
	public void method_parameter_constraint_provided_as_value_type_parameter_of_a_map_gets_validated() throws Exception {
		Method method = TypeWithMap5.class.getDeclaredMethod( "setValues", Map.class );

		Map<String, String> parameter = new HashMap<>();
		parameter.put( "first", "First" );
		parameter.put( "second", "" );
		parameter.put( "third", null );
		Object[] values = new Object[] { parameter };

		Set<ConstraintViolation<TypeWithMap5>> constraintViolations = getExecutableValidator().validateParameters(
				new TypeWithMap5(),
				method,
				values
		);

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotBlank.class )
						.withPropertyPath( pathWith()
								.method( "setValues" )
								.parameter( "mapParameter", 0 )
								.containerElement( "<map value>", true, "second", null, Map.class, 1 )
						)
						.withInvalidValue( "" ),
				violationOf( NotBlank.class )
						.withPropertyPath( pathWith()
								.method( "setValues" )
								.parameter( "mapParameter", 0 )
								.containerElement( "<map value>", true, "third", null, Map.class, 1 )
						)
						.withInvalidValue( null ),
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.method( "setValues" )
								.parameter( "mapParameter", 0 )
								.containerElement( "<map value>", true, "third", null, Map.class, 1 )
						)
						.withInvalidValue( null )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "c")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "i")
	public void constructor_parameter_constraint_provided_on_value_type_parameter_of_a_map_gets_validated() throws Exception {
		Constructor<TypeWithMap6> constructor = TypeWithMap6.class.getDeclaredConstructor( Map.class );

		Map<String, String> parameter = new HashMap<>();
		parameter.put( "first", "First" );
		parameter.put( "second", "" );
		parameter.put( "third", null );
		Object[] values = new Object[] { parameter };

		Set<ConstraintViolation<TypeWithMap6>> constraintViolations = getExecutableValidator().validateConstructorParameters(
				constructor,
				values
		);

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotBlank.class )
						.withPropertyPath( pathWith()
								.constructor( TypeWithMap6.class )
								.parameter( "mapParameter", 0 )
								.containerElement( "<map value>", true, "second", null, Map.class, 1 )
						)
						.withInvalidValue( "" ),
				violationOf( NotBlank.class )
						.withPropertyPath( pathWith()
								.constructor( TypeWithMap6.class )
								.parameter( "mapParameter", 0 )
								.containerElement( "<map value>", true, "third", null, Map.class, 1 )
						)
						.withInvalidValue( null ),
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.constructor( TypeWithMap6.class )
								.parameter( "mapParameter", 0 )
								.containerElement( "<map value>", true, "third", null, Map.class, 1 )
						)
						.withInvalidValue( null )
		);
	}

	private static class TypeWithMap1 {

		private Map<String, @NotBlank String> nameMap;
	}

	private static class TypeWithMap2 {

		@NotNull
		private Map<String, @NotBlank String> nameMap;
	}

	private static class TypeWithMap3 {

		private Map<String, String> stringMap;

		@SuppressWarnings("unused")
		public Map<String, @NotNull @NotBlank String> getStringMap() {
			return stringMap;
		}
	}

	private static class TypeWithMap4 {

		private Map<String, String> stringMap;

		@SuppressWarnings("unused")
		public Map<String, @NotNull @NotBlank String> returnStringMap() {
			return stringMap;
		}
	}

	private static class TypeWithMap5 {

		@SuppressWarnings("unused")
		public void setValues(Map<String, @NotNull @NotBlank String> mapParameter) {
		}
	}

	private static class TypeWithMap6 {

		@SuppressWarnings("unused")
		public TypeWithMap6(Map<String, @NotNull @NotBlank String> mapParameter) {
		}
	}
}
