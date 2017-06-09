/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.containerelement;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.pathWith;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationWith;

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
public class ContainerElementConstraintMapKeyTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( ContainerElementConstraintMapKeyTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "c")
	public void constraint_specified_on_key_type_parameter_of_map_gets_validated() {
		TypeWithMap1 m = new TypeWithMap1();
		m.nameMap = new HashMap<>();
		m.nameMap.put( "first", "Name 1" );
		m.nameMap.put( "", "Name 2" );
		m.nameMap.put( "third", "Name 3" );
		Set<ConstraintViolation<TypeWithMap1>> constraintViolations = getValidator().validate( m );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationWith()
						.constraintType( NotBlank.class )
						.propertyPath( pathWith()
								.property( "nameMap" )
								.containerElement( "<map key>", true, "", null, Map.class, 0 )
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "d")
	public void constraints_specified_on_map_and_on_key_type_parameter_of_map_get_validated() {
		TypeWithMap2 m = new TypeWithMap2();
		m.nameMap = new HashMap<>();
		m.nameMap.put( "first", "Name 1" );
		m.nameMap.put( "", "Name 2" );
		m.nameMap.put( "third", "Name 3" );
		Set<ConstraintViolation<TypeWithMap2>> constraintViolations = getValidator().validate( m );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationWith()
						.constraintType( NotBlank.class )
						.propertyPath( pathWith()
								.property( "nameMap" )
								.containerElement( "<map key>", true, "", null, Map.class, 0 )
						)
		);

		m = new TypeWithMap2();
		constraintViolations = getValidator().validate( m );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationWith()
						.constraintType( NotNull.class )
						.propertyPath( pathWith()
								.property( "nameMap" )
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "c")
	public void getter_constraint_provided_on_key_type_parameter_of_a_map_gets_validated() {
		TypeWithMap3 m = new TypeWithMap3();
		m.stringMap = new HashMap<>();
		m.stringMap.put( "", "First" );
		m.stringMap.put( "second", "Second" );
		m.stringMap.put( null, "Third" );

		Set<ConstraintViolation<TypeWithMap3>> constraintViolations = getValidator().validate( m );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationWith()
						.constraintType( NotBlank.class )
						.propertyPath( pathWith()
								.property( "stringMap" )
								.containerElement( "<map key>", true, "", null, Map.class, 0 )
						),
				violationWith()
						.constraintType( NotBlank.class )
						.propertyPath( pathWith()
								.property( "stringMap" )
								.containerElement( "<map key>", true, null, null, Map.class, 0 )
						),
				violationWith()
						.constraintType( NotNull.class )
						.propertyPath( pathWith()
								.property( "stringMap" )
								.containerElement( "<map key>", true, null, null, Map.class, 0 )
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "c")
	public void return_value_constraint_provided_on_key_type_parameter_of_a_map_gets_validated() throws Exception {
		Method method = TypeWithMap4.class.getDeclaredMethod( "returnStringMap" );

		Map<String, String> parameter = new HashMap<>();
		parameter.put( "first", "First" );
		parameter.put( "", "Second" );
		parameter.put( null, "Third" );

		Set<ConstraintViolation<TypeWithMap4>> constraintViolations = getValidator().forExecutables().validateReturnValue(
				new TypeWithMap4(),
				method,
				parameter
		);

		assertThat( constraintViolations ).containsOnlyViolations(
				violationWith()
						.constraintType( NotBlank.class )
						.propertyPath( pathWith()
								.method( "returnStringMap" )
								.returnValue()
								.containerElement( "<map key>", true, "", null, Map.class, 0 )
						),
				violationWith()
						.constraintType( NotBlank.class )
						.propertyPath( pathWith()
								.method( "returnStringMap" )
								.returnValue()
								.containerElement( "<map key>", true, null, null, Map.class, 0 )
						),
				violationWith()
						.constraintType( NotNull.class )
						.propertyPath( pathWith()
								.method( "returnStringMap" )
								.returnValue()
								.containerElement( "<map key>", true, null, null, Map.class, 0 )
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "c")
	public void method_parameter_constraint_provided_as_key_type_parameter_of_a_map_gets_validated() throws Exception {
		Method method = TypeWithMap5.class.getDeclaredMethod( "setValues", Map.class );

		Map<String, String> parameter = new HashMap<>();
		parameter.put( "first", "First" );
		parameter.put( "", "Second" );
		parameter.put( null, "Third" );
		Object[] values = new Object[] { parameter };

		Set<ConstraintViolation<TypeWithMap5>> constraintViolations = getExecutableValidator().validateParameters(
				new TypeWithMap5(),
				method,
				values
		);

		assertThat( constraintViolations ).containsOnlyViolations(
				violationWith()
						.constraintType( NotBlank.class )
						.propertyPath( pathWith()
								.method( "setValues" )
								.parameter( "mapParameter", 0 )
								.containerElement( "<map key>", true, "", null, Map.class, 0 )
						),
				violationWith()
						.constraintType( NotBlank.class )
						.propertyPath( pathWith()
								.method( "setValues" )
								.parameter( "mapParameter", 0 )
								.containerElement( "<map key>", true, null, null, Map.class, 0 )
						),
				violationWith()
						.constraintType( NotNull.class )
						.propertyPath( pathWith()
								.method( "setValues" )
								.parameter( "mapParameter", 0 )
								.containerElement( "<map key>", true, null, null, Map.class, 0 )
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "c")
	public void constructor_parameter_constraint_provided_on_key_type_parameter_of_a_map_gets_validated() throws Exception {
		Constructor<TypeWithMap6> constructor = TypeWithMap6.class.getDeclaredConstructor( Map.class );

		Map<String, String> parameter = new HashMap<>();
		parameter.put( "first", "First" );
		parameter.put( "", "Second" );
		parameter.put( null, "Third" );
		Object[] values = new Object[] { parameter };

		Set<ConstraintViolation<TypeWithMap6>> constraintViolations = getExecutableValidator().validateConstructorParameters(
				constructor,
				values
		);

		assertThat( constraintViolations ).containsOnlyViolations(
				violationWith()
						.constraintType( NotBlank.class )
						.propertyPath( pathWith()
								.constructor( TypeWithMap6.class )
								.parameter( "mapParameter", 0 )
								.containerElement( "<map key>", true, "", null, Map.class, 0 )
						),
				violationWith()
						.constraintType( NotBlank.class )
						.propertyPath( pathWith()
								.constructor( TypeWithMap6.class )
								.parameter( "mapParameter", 0 )
								.containerElement( "<map key>", true, null, null, Map.class, 0 )
						),
				violationWith()
						.constraintType( NotNull.class )
						.propertyPath( pathWith()
								.constructor( TypeWithMap6.class )
								.parameter( "mapParameter", 0 )
								.containerElement( "<map key>", true, null, null, Map.class, 0 )
						)
		);
	}

	private static class TypeWithMap1 {

		private Map<@NotBlank String, String> nameMap;
	}

	private static class TypeWithMap2 {

		@NotNull
		private Map<@NotBlank String, String> nameMap;
	}

	private static class TypeWithMap3 {

		private Map<String, String> stringMap;

		@SuppressWarnings("unused")
		public Map<@NotNull @NotBlank String, String> getStringMap() {
			return stringMap;
		}
	}

	private static class TypeWithMap4 {

		private Map<String, String> stringMap;

		@SuppressWarnings("unused")
		public Map<@NotNull @NotBlank String, String> returnStringMap() {
			return stringMap;
		}
	}

	private static class TypeWithMap5 {

		@SuppressWarnings("unused")
		public void setValues(Map<@NotNull @NotBlank String, String> mapParameter) {
		}
	}

	private static class TypeWithMap6 {

		@SuppressWarnings("unused")
		public TypeWithMap6(Map<@NotNull @NotBlank String, String> mapParameter) {
		}
	}
}
