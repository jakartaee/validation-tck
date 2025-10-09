/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.containerelement;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertNoViolations;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.pathWith;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.valueextraction.Unwrapping;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "beanvalidation", version = "4.0.0")
public class NestedContainerElementConstraintsTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( NestedContainerElementConstraintsTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "f")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "i")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ad")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ae")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ag")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ah")
	public void validation_of_nested_type_arguments_works_with_map_of_list_of_optional() {
		Set<ConstraintViolation<MapOfLists>> constraintViolations = getValidator().validate( MapOfLists.valid() );
		assertNoViolations( constraintViolations );

		constraintViolations = getValidator().validate( MapOfLists.invalidKey() );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.property( "map" )
								.containerElement( "<map key>", true, "k", null, Map.class, 0 )
						)
						.withInvalidValue( MapOfLists.INVALID_KEY )
		);

		constraintViolations = getValidator().validate( MapOfLists.invalidList() );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.property( "map" )
								.containerElement( "<map value>", true, "key1", null, Map.class, 1 )
						)
						.withInvalidValue( MapOfLists.INVALID_LIST )
		);

		constraintViolations = getValidator().validate( MapOfLists.invalidString() );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.property( "map" )
								.containerElement( "<map value>", true, "key1", null, Map.class, 1 )
								.containerElement( "<list element>", true, null, 0, List.class, 0 )
						)
						.withInvalidValue( MapOfLists.INVALID_STRING_1 ),
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.property( "map" )
								.containerElement( "<map value>", true, "key1", null, Map.class, 1 )
								.containerElement( "<list element>", true, null, 1, List.class, 0 )
						)
						.withInvalidValue( MapOfLists.INVALID_STRING_2 )
		);

		constraintViolations = getValidator().validate( MapOfLists.reallyInvalid() );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.property( "map" )
								.containerElement( "<map key>", true, "k", null, Map.class, 0 )
						)
						.withInvalidValue( MapOfLists.INVALID_KEY ),
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.property( "map" )
								.containerElement( "<map value>", true, "k", null, Map.class, 1 )
						)
						.withInvalidValue( MapOfLists.REALLY_INVALID_LIST ),
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.property( "map" )
								.containerElement( "<map value>", true, "k", null, Map.class, 1 )
								.containerElement( "<list element>", true, null, 0, List.class, 0 )
						)
						.withInvalidValue( MapOfLists.INVALID_STRING_1 )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "f")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ad")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ae")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "af")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ag")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ah")
	public void validation_of_nested_type_arguments_works_with_map_of_list_of_stringproperty() {
		Set<ConstraintViolation<MapOfListsWithAutomaticUnwrapping>> constraintViolations = getValidator().validate( MapOfListsWithAutomaticUnwrapping.valid() );
		assertNoViolations( constraintViolations );

		constraintViolations = getValidator().validate( MapOfListsWithAutomaticUnwrapping.invalidStringProperty() );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Min.class )
						.withPropertyPath( pathWith()
								.property( "map" )
								.containerElement( "<map value>", true, "key", null, Map.class, 1 )
								.containerElement( "<list element>", true, null, 1, List.class, 0 )
						)
		);

		constraintViolations = getValidator().validate( MapOfListsWithAutomaticUnwrapping.invalidListElement() );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
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
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.property( "map" )
								.containerElement( "<map value>", true, "key1", null, Map.class, 1 )
								.containerElement( "<list element>", true, null, 0, List.class, 0 )
						),
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.property( "map" )
								.containerElement( "<map value>", true, "key1", null, Map.class, 1 )
								.containerElement( "<list element>", true, null, 1, List.class, 0 )
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "f")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ad")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "af")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ag")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ah")
	public void validation_of_nested_type_arguments_works_with_list_of_maps() {
		Set<ConstraintViolation<ListOfMaps>> constraintViolations = getValidator().validate( ListOfMaps.valid() );
		assertNoViolations( constraintViolations );

		constraintViolations = getValidator().validate( ListOfMaps.invalidValue() );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.property( "list" )
								.containerElement( "<list element>", true, null, 0, List.class, 0 )
								.containerElement( "<map value>", true, "key", null, Map.class, 1 )
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "f")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ad")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ag")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ah")
	public void validation_of_nested_type_arguments_works_with_list_of_iterables() {
		Set<ConstraintViolation<ListOfIterables>> constraintViolations = getValidator().validate( ListOfIterables.valid() );
		assertNoViolations( constraintViolations );

		constraintViolations = getValidator().validate( ListOfIterables.invalid() );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.property( "list" )
								.containerElement( "<list element>", true, null, 0, List.class, 0 )
								.containerElement( "<iterable element>", true, null, null, Set.class, 0 )
						)
		);
	}

	private static class MapOfLists {

		private static final String INVALID_KEY = "k";

		private static final List<Optional<String>> INVALID_LIST = Arrays.asList( Optional.of( "only one value" ) );

		private static final String INVALID_STRING_1 = "1";

		private static final String INVALID_STRING_2 = "2";

		private static final List<Optional<String>> REALLY_INVALID_LIST = Arrays.asList( Optional.of( INVALID_STRING_1 ) );

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
			foo.map.put( INVALID_KEY, list );

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

			List<Optional<String>> list = Arrays.asList( Optional.of( INVALID_STRING_1 ), Optional.of( INVALID_STRING_2 ) );
			foo.map = new HashMap<>();
			foo.map.put( "key1", list );

			return foo;
		}

		private static MapOfLists reallyInvalid() {
			MapOfLists foo = new MapOfLists();

			foo.map = new HashMap<>();
			foo.map.put( INVALID_KEY, REALLY_INVALID_LIST );

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

		private Map<@Size(min = 2) String, List<@NotNull(payload = { Unwrapping.Skip.class }) @Min(2) OptionalInt>> map;

		private static MapOfListsWithAutomaticUnwrapping valid() {
			MapOfListsWithAutomaticUnwrapping bar = new MapOfListsWithAutomaticUnwrapping();

			List<OptionalInt> list = Arrays.asList( OptionalInt.of( 3 ), OptionalInt.of( 4 ),
					OptionalInt.of( 5 ) );
			bar.map = new HashMap<>();
			bar.map.put( "key", list );

			return bar;
		}

		private static MapOfListsWithAutomaticUnwrapping invalidStringProperty() {
			MapOfListsWithAutomaticUnwrapping bar = new MapOfListsWithAutomaticUnwrapping();

			List<OptionalInt> list = Arrays.asList( OptionalInt.of( 3 ), OptionalInt.of( 1 ),
					OptionalInt.of( 5 ) );
			bar.map = new HashMap<>();
			bar.map.put( "key", list );

			return bar;
		}

		private static MapOfListsWithAutomaticUnwrapping invalidListElement() {
			MapOfListsWithAutomaticUnwrapping bar = new MapOfListsWithAutomaticUnwrapping();

			List<OptionalInt> list = Arrays.asList( null, OptionalInt.of( 3 ) );
			bar.map = new HashMap<>();
			bar.map.put( "key", list );

			return bar;
		}
	}

	private static class ListOfMaps {

		private List<Map<@Size(min = 2) String, @NotNull @Size(min = 2) String>> list;

		private static ListOfMaps valid() {
			ListOfMaps foo = new ListOfMaps();

			Map<String, String> map = new HashMap<>();
			map.put( "key", "value" );

			foo.list = new ArrayList<>();
			foo.list.add( map );

			return foo;
		}

		private static ListOfMaps invalidValue() {
			ListOfMaps foo = new ListOfMaps();

			Map<String, String> map = new HashMap<>();
			map.put( "key", "v" );

			foo.list = new ArrayList<>();
			foo.list.add( map );

			return foo;
		}
	}

	private static class ListOfIterables {

		private List<Set<@Size(min = 2) String>> list;

		private static ListOfIterables valid() {
			ListOfIterables foo = new ListOfIterables();

			Set<String> set = new HashSet<>();
			set.add( "value" );

			foo.list = new ArrayList<>();
			foo.list.add( set );

			return foo;
		}

		private static ListOfIterables invalid() {
			ListOfIterables foo = new ListOfIterables();

			Set<String> set = new HashSet<>();
			set.add( "v" );

			foo.list = new ArrayList<>();
			foo.list.add( set );

			return foo;
		}
	}
}
