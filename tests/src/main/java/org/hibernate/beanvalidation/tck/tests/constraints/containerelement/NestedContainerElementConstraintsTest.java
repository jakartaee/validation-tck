/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.containerelement;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertNumberOfViolations;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.pathWith;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationWith;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.valueextraction.Unwrapping;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@SuppressWarnings("restriction")
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class NestedContainerElementConstraintsTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( NestedContainerElementConstraintsTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "f")
	public void validation_of_nested_type_arguments_works_with_map_of_list_of_optional() {
		Set<ConstraintViolation<MapOfLists>> constraintViolations = getValidator().validate( MapOfLists.valid() );
		assertNumberOfViolations( constraintViolations, 0 );

		constraintViolations = getValidator().validate( MapOfLists.invalidKey() );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationWith()
						.constraintType( Size.class )
						.propertyPath( pathWith()
								.property( "map" )
								.containerElement( "<map key>", true, "k", null, Map.class, 0 )
						)
		);

		constraintViolations = getValidator().validate( MapOfLists.invalidList() );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationWith()
						.constraintType( Size.class )
						.propertyPath( pathWith()
								.property( "map" )
								.containerElement( "<map value>", true, "key1", null, Map.class, 1 )
						)
		);

		constraintViolations = getValidator().validate( MapOfLists.invalidString() );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationWith()
						.constraintType( Size.class )
						.propertyPath( pathWith()
								.property( "map" )
								.containerElement( "<map value>", true, "key1", null, Map.class, 1 )
								.containerElement( "<list element>", true, null, 0, List.class, 0 )
						),
				violationWith()
						.constraintType( Size.class )
						.propertyPath( pathWith()
								.property( "map" )
								.containerElement( "<map value>", true, "key1", null, Map.class, 1 )
								.containerElement( "<list element>", true, null, 1, List.class, 0 )
						)
		);

		constraintViolations = getValidator().validate( MapOfLists.reallyInvalid() );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationWith()
						.constraintType( Size.class )
						.propertyPath( pathWith()
								.property( "map" )
								.containerElement( "<map key>", true, "k", null, Map.class, 0 )
						),
				violationWith()
						.constraintType( Size.class )
						.propertyPath( pathWith()
								.property( "map" )
								.containerElement( "<map value>", true, "k", null, Map.class, 1 )
						),
				violationWith()
						.constraintType( Size.class )
						.propertyPath( pathWith()
								.property( "map" )
								.containerElement( "<map value>", true, "k", null, Map.class, 1 )
								.containerElement( "<list element>", true, null, 0, List.class, 0 )
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "f")
	public void validation_of_nested_type_arguments_works_with_map_of_list_of_stringproperty() {
		Set<ConstraintViolation<MapOfListsWithAutomaticUnwrapping>> constraintViolations = getValidator().validate( MapOfListsWithAutomaticUnwrapping.valid() );
		assertNumberOfViolations( constraintViolations, 0 );

		constraintViolations = getValidator().validate( MapOfListsWithAutomaticUnwrapping.invalidStringProperty() );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationWith()
						.constraintType( Size.class )
						.propertyPath( pathWith()
								.property( "map" )
								.containerElement( "<map value>", true, "key", null, Map.class, 1 )
								.containerElement( "<list element>", true, null, 1, List.class, 0 )
						)
		);

		constraintViolations = getValidator().validate( MapOfListsWithAutomaticUnwrapping.invalidListElement() );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationWith()
						.constraintType( NotNull.class )
						.propertyPath( pathWith()
								.property( "map" )
								.containerElement( "<map value>", true, "key", null, Map.class, 1 )
								.containerElement( "<list element>", true, null, 0, List.class, 0 )
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "f")
	public void validation_of_nested_type_arguments_works_on_getter_with_map_of_list_of_optional() {
		Set<ConstraintViolation<MapOfListsUsingGetter>> constraintViolations = getValidator().validate( MapOfListsUsingGetter.invalidString() );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationWith()
						.constraintType( Size.class )
						.propertyPath( pathWith()
								.property( "map" )
								.containerElement( "<map value>", true, "key1", null, Map.class, 1 )
								.containerElement( "<list element>", true, null, 0, List.class, 0 )
						),
				violationWith()
						.constraintType( Size.class )
						.propertyPath( pathWith()
								.property( "map" )
								.containerElement( "<map value>", true, "key1", null, Map.class, 1 )
								.containerElement( "<list element>", true, null, 1, List.class, 0 )
						)
		);
	}

	private static class MapOfLists {

		private Map<@Size(min = 2) String, @NotNull @Size(min = 2) List<Optional<@Size(min = 3) String>>> map;

		private static MapOfLists valid() {
			MapOfLists foo = new MapOfLists();

			List<Optional<String>> list = Arrays.asList( Optional.of( "one" ), Optional.of( "two" ) );
			foo.map = new HashMap<>();
			foo.map.put( "key", list );

			return foo;
		}

		private static MapOfLists invalidKey() {
			MapOfLists foo = new MapOfLists();

			List<Optional<String>> list = Arrays.asList( Optional.of( "one" ), Optional.of( "two" ) );
			foo.map = new HashMap<>();
			foo.map.put( "k", list );

			return foo;
		}

		private static MapOfLists invalidList() {
			MapOfLists foo = new MapOfLists();

			List<Optional<String>> list = Arrays.asList( Optional.of( "only one value" ) );
			foo.map = new HashMap<>();
			foo.map.put( "key1", list );

			return foo;
		}

		private static MapOfLists invalidString() {
			MapOfLists foo = new MapOfLists();

			List<Optional<String>> list = Arrays.asList( Optional.of( "1" ), Optional.of( "2" ) );
			foo.map = new HashMap<>();
			foo.map.put( "key1", list );

			return foo;
		}

		private static MapOfLists reallyInvalid() {
			MapOfLists foo = new MapOfLists();

			List<Optional<String>> list = Arrays.asList( Optional.of( "1" ) );
			foo.map = new HashMap<>();
			foo.map.put( "k", list );

			return foo;
		}
	}

	private static class MapOfListsUsingGetter {

		private Map<String, List<Optional<String>>> map;

		static MapOfListsUsingGetter invalidString() {
			MapOfListsUsingGetter mapOfListsUsingGetter = new MapOfListsUsingGetter();
			mapOfListsUsingGetter.map = MapOfLists.invalidString().map;
			return mapOfListsUsingGetter;
		}

		@SuppressWarnings("unused")
		Map<@Size(min = 2) String, @NotNull @Size(min = 2) List<Optional<@Size(min = 3) String>>> getMap() {
			return map;
		}
	}

	private static class MapOfListsWithAutomaticUnwrapping {

		private Map<@Size(min = 2) String, List<@NotNull(payload = { Unwrapping.Skip.class }) @Size(min = 2) StringProperty>> map;

		private static MapOfListsWithAutomaticUnwrapping valid() {
			MapOfListsWithAutomaticUnwrapping bar = new MapOfListsWithAutomaticUnwrapping();

			List<StringProperty> list = Arrays.asList( new SimpleStringProperty( "one" ), new SimpleStringProperty( "tw" ),
					new SimpleStringProperty( "three" ) );
			bar.map = new HashMap<>();
			bar.map.put( "key", list );

			return bar;
		}

		private static MapOfListsWithAutomaticUnwrapping invalidStringProperty() {
			MapOfListsWithAutomaticUnwrapping bar = new MapOfListsWithAutomaticUnwrapping();

			List<StringProperty> list = Arrays.asList( new SimpleStringProperty( "one" ), new SimpleStringProperty( "t" ),
					new SimpleStringProperty( "three" ) );
			bar.map = new HashMap<>();
			bar.map.put( "key", list );

			return bar;
		}

		private static MapOfListsWithAutomaticUnwrapping invalidListElement() {
			MapOfListsWithAutomaticUnwrapping bar = new MapOfListsWithAutomaticUnwrapping();

			List<StringProperty> list = Arrays.asList( null, new SimpleStringProperty( "two" ) );
			bar.map = new HashMap<>();
			bar.map.put( "key", list );

			return bar;
		}
	}

	@SuppressWarnings({ "unchecked", "unused" })
	private static class ArrayOfOptionalsWithAutomaticUnwrapping {

		private Optional<@Size(min = 3) StringProperty> @NotNull [] array;

		private static ArrayOfOptionalsWithAutomaticUnwrapping valid() {
			ArrayOfOptionalsWithAutomaticUnwrapping baz = new ArrayOfOptionalsWithAutomaticUnwrapping();

			baz.array = new Optional[] { Optional.of( new SimpleStringProperty( "string1" ) ), Optional.of( new SimpleStringProperty( "string2" ) ) };

			return baz;
		}

		private static ArrayOfOptionalsWithAutomaticUnwrapping invalidArray() {
			ArrayOfOptionalsWithAutomaticUnwrapping baz = new ArrayOfOptionalsWithAutomaticUnwrapping();

			baz.array = new Optional[] { null, Optional.of( new SimpleStringProperty( "st" ) ) };

			return baz;
		}
	}
}
