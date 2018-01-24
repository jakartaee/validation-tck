/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.javafx;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertNumberOfViolations;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.pathWith;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.valueextraction.ValueExtractor;

import org.hibernate.beanvalidation.tck.util.TestUtil;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.ReadOnlyMapWrapper;
import javafx.beans.property.ReadOnlySetWrapper;
import javafx.beans.property.SetProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;

/**
 * Tests for JavaFX {@link ValueExtractor}s.
 *
 * @author Khalid Alqinyah
 * @author Hardy Ferentschik
 * @author Guillaume Smet
 */
@SuppressWarnings("restriction")
public class JavaFXValueExtractorsTestImpl {

	public static void testJavaFXBasicProperties() {
		Set<ConstraintViolation<BasicPropertiesEntity>> constraintViolations = TestUtil.getValidatorUnderTest().validate( new BasicPropertiesEntity() );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class ).withProperty( "stringProperty" ),
				violationOf( Max.class ).withProperty( "doubleProperty" ),
				violationOf( Min.class ).withProperty( "integerProperty" ),
				violationOf( AssertTrue.class ).withProperty( "booleanProperty" )
		);
	}

	public static void testValueExtractionForPropertyList() {
		Validator validator = TestUtil.getValidatorUnderTest();

		Set<ConstraintViolation<ListPropertyEntity>> constraintViolations = validator.validate( ListPropertyEntity.valid() );
		assertNumberOfViolations( constraintViolations, 0 );

		constraintViolations = validator.validate( ListPropertyEntity.invalidList() );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Size.class ).withProperty( "listProperty" )
		);

		constraintViolations = validator.validate( ListPropertyEntity.invalidListElement() );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.property( "listProperty" )
								.containerElement( "<list element>", true, null, 0, ListProperty.class, 0 ) )
		);
	}

	public static void testValueExtractionForPropertySet() {
		Validator validator = TestUtil.getValidatorUnderTest();

		Set<ConstraintViolation<SetPropertyEntity>> constraintViolations = validator.validate( SetPropertyEntity.valid() );
		assertNumberOfViolations( constraintViolations, 0 );

		constraintViolations = validator.validate( SetPropertyEntity.invalidSet() );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Size.class ).withProperty( "setProperty" )
		);

		constraintViolations = validator.validate( SetPropertyEntity.invalidSetElement() );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.property( "setProperty" )
								.containerElement( "<iterable element>", true, null, null, SetProperty.class, 0 ) )
		);
	}

	public static void testValueExtractionForPropertyMap() {
		Validator validator = TestUtil.getValidatorUnderTest();

		Set<ConstraintViolation<MapPropertyEntity>> constraintViolations = validator.validate( MapPropertyEntity.valid() );
		assertNumberOfViolations( constraintViolations, 0 );

		constraintViolations = validator.validate( MapPropertyEntity.invalidMap() );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Size.class ).withProperty( "mapProperty" )
		);

		constraintViolations = validator.validate( MapPropertyEntity.invalidMapKey() );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.property( "mapProperty" )
								.containerElement( "<map key>", true, "app", null, MapProperty.class, 0 ) )
		);

		constraintViolations = validator.validate( MapPropertyEntity.invalidMapValue() );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotBlank.class )
						.withPropertyPath( pathWith()
								.property( "mapProperty" )
								.containerElement( "<map value>", true, "pear", null, MapProperty.class, 1 ) )
		);
	}

	public static void testValueExtractionForListOfStringProperty() {
		Validator validator = TestUtil.getValidatorUnderTest();

		Set<ConstraintViolation<ListOfStringPropertyEntity>> constraintViolations = validator.validate( ListOfStringPropertyEntity.valid() );
		assertNumberOfViolations( constraintViolations, 0 );

		constraintViolations = validator.validate( ListOfStringPropertyEntity.invalidListElement() );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.property( "listProperty" )
								.containerElement( "<list element>", true, null, 1, ListProperty.class, 0 )
						)
						.withInvalidValue( "Bob" )
		);
	}

	public static class BasicPropertiesEntity {

		@NotNull
		private StringProperty stringProperty = new SimpleStringProperty( null );

		@Max(value = 3)
		private ReadOnlyDoubleWrapper doubleProperty = new ReadOnlyDoubleWrapper( 4.5 );

		@Min(value = 3)
		private IntegerProperty integerProperty = new SimpleIntegerProperty( 2 );

		@AssertTrue
		private ReadOnlyBooleanProperty booleanProperty = new SimpleBooleanProperty( false );
	}

	public static class ListPropertyEntity {

		@Size(min = 3)
		private ListProperty<@Size(min = 4) String> listProperty;

		private ListPropertyEntity(ObservableList<String> innerList) {
			this.listProperty = new ReadOnlyListWrapper<String>( innerList );
		}

		public static ListPropertyEntity valid() {
			return new ListPropertyEntity( FXCollections.observableArrayList( "apple", "pear", "cherry" ) );
		}

		public static ListPropertyEntity invalidList() {
			return new ListPropertyEntity( FXCollections.observableArrayList( "apple" ) );
		}

		public static ListPropertyEntity invalidListElement() {
			return new ListPropertyEntity( FXCollections.observableArrayList( "app", "pear", "cherry" ) );
		}
	}

	public static class SetPropertyEntity {

		@Size(min = 3)
		private SetProperty<@Size(min = 4) String> setProperty;

		private SetPropertyEntity(ObservableSet<String> innerList) {
			this.setProperty = new ReadOnlySetWrapper<String>( innerList );
		}

		public static SetPropertyEntity valid() {
			return new SetPropertyEntity( FXCollections.observableSet( "apple", "pear", "cherry" ) );
		}

		public static SetPropertyEntity invalidSet() {
			return new SetPropertyEntity( FXCollections.observableSet( "apple" ) );
		}

		public static SetPropertyEntity invalidSetElement() {
			return new SetPropertyEntity( FXCollections.observableSet( "app", "pear", "cherry" ) );
		}
	}

	public static class MapPropertyEntity {

		@Size(min = 3)
		private MapProperty<@Size(min = 4) String, @NotBlank String> mapProperty = new ReadOnlyMapWrapper<String, String>();

		private MapPropertyEntity(ObservableMap<String, String> innerMap) {
			this.mapProperty = new ReadOnlyMapWrapper<String, String>( innerMap );
		}

		public static MapPropertyEntity valid() {
			ObservableMap<String, String> innerMap = FXCollections.observableHashMap();
			innerMap.put( "apple", "apple@example.com" );
			innerMap.put( "pear", "pear@example.com" );
			innerMap.put( "cherry", "cherry@example.com" );

			return new MapPropertyEntity( innerMap );
		}

		public static MapPropertyEntity invalidMap() {
			return new MapPropertyEntity( FXCollections.observableHashMap() );
		}

		public static MapPropertyEntity invalidMapKey() {
			ObservableMap<String, String> innerMap = FXCollections.observableHashMap();
			innerMap.put( "app", "apple@example.com" );
			innerMap.put( "pear", "pear@example.com" );
			innerMap.put( "cherry", "cherry@example.com" );

			return new MapPropertyEntity( innerMap );
		}

		public static MapPropertyEntity invalidMapValue() {
			ObservableMap<String, String> innerMap = FXCollections.observableHashMap();
			innerMap.put( "apple", "apple@example.com" );
			innerMap.put( "pear", " " );
			innerMap.put( "cherry", "cherry@example.com" );

			return new MapPropertyEntity( innerMap );
		}
	}

	public static class ListOfStringPropertyEntity {

		@SuppressWarnings("unused")
		private final ListProperty<@Size(min = 4) StringProperty> listProperty;

		private ListOfStringPropertyEntity(ObservableList<StringProperty> innerList) {
			this.listProperty = new SimpleListProperty<>( innerList );
		}

		public static ListOfStringPropertyEntity valid() {
			return new ListOfStringPropertyEntity( FXCollections.observableArrayList( new SimpleStringProperty( "Billy" ), new SimpleStringProperty( "Bruce" ) ) );
		}

		public static ListOfStringPropertyEntity invalidListElement() {
			return new ListOfStringPropertyEntity( FXCollections.observableArrayList( new SimpleStringProperty( "Billy" ), new SimpleStringProperty( "Bob" ) ) );
		}
	}
}
