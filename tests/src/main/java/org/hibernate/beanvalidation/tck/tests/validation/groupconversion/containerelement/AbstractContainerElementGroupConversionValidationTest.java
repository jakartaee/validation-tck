/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation.groupconversion.containerelement;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertNoViolations;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.pathWith;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.time.Year;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Guillaume Smet
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public abstract class AbstractContainerElementGroupConversionValidationTest extends AbstractTCKTest {

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "c")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION_EXAMPLES, id = "a")
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONTAINERELEMENTCONSTRAINTS, id = "i")
	public void testGroupConversionIsAppliedOnField() {
		Set<ConstraintViolation<RegisteredAddresses>> constraintViolations = getValidator().validate( TestRegisteredAddresses.withInvalidMainAddress() );


		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "mainAddresses" )
						.containerElement( "<map value>", true, TestRegisteredAddresses.REFERENCE_YEAR, null, Map.class, 1 )
						.property( "street1", true, null, 1, List.class, 0 ),
				pathWith()
						.property( "mainAddresses" )
						.containerElement( "<map value>", true, TestRegisteredAddresses.REFERENCE_YEAR, null, Map.class, 1 )
						.property( "zipCode", true, null, 1, List.class, 0 )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "c")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION_EXAMPLES, id = "a")
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONTAINERELEMENTCONSTRAINTS, id = "i")
	public void testSeveralGroupConversionsAppliedOnField() {
		RegisteredAddresses registeredAddressesWithInvalidPreferredShipmentAddress = TestRegisteredAddresses.withInvalidPreferredShipmentAddress();

		Set<ConstraintViolation<RegisteredAddresses>> constraintViolations = getValidator().validate(
				registeredAddressesWithInvalidPreferredShipmentAddress
		);

		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "preferredShipmentAddresses" )
						.containerElement( "<map value>", true, TestRegisteredAddresses.REFERENCE_YEAR, null, Map.class, 1 )
						.property( "street1", true, null, 1, List.class, 0 ),
				pathWith()
						.property( "preferredShipmentAddresses" )
						.containerElement( "<map value>", true, TestRegisteredAddresses.REFERENCE_YEAR, null, Map.class, 1 )
						.property( "zipCode", true, null, 1, List.class, 0 ) );

		constraintViolations = getValidator().validate(
				registeredAddressesWithInvalidPreferredShipmentAddress,
				Complex.class
		);

		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
					.property( "preferredShipmentAddresses" )
					.containerElement( "<map value>", true, TestRegisteredAddresses.REFERENCE_YEAR, null, Map.class, 1 )
					.property("doorCode", true, null, 1, List.class, 0 )
		);

		constraintViolations = getValidator().validate(
				registeredAddressesWithInvalidPreferredShipmentAddress,
				Default.class,
				Complex.class
		);

		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "preferredShipmentAddresses" )
						.containerElement( "<map value>", true, TestRegisteredAddresses.REFERENCE_YEAR, null, Map.class, 1 )
						.property( "street1", true, null, 1, List.class, 0 ),
				pathWith()
						.property( "preferredShipmentAddresses" )
						.containerElement( "<map value>", true, TestRegisteredAddresses.REFERENCE_YEAR, null, Map.class, 1 )
						.property( "zipCode", true, null, 1, List.class, 0 ),
				pathWith()
						.property( "preferredShipmentAddresses" )
						.containerElement( "<map value>", true, TestRegisteredAddresses.REFERENCE_YEAR, null, Map.class, 1 )
						.property( "doorCode", true, null, 1, List.class, 0 )
		);

		constraintViolations = getValidator().validate(
				registeredAddressesWithInvalidPreferredShipmentAddress,
				Complete.class
		);

		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "preferredShipmentAddresses" )
						.containerElement( "<map value>", true, TestRegisteredAddresses.REFERENCE_YEAR, null, Map.class, 1 )
						.property( "street1", true, null, 1, List.class, 0 ),
				pathWith()
						.property( "preferredShipmentAddresses" )
						.containerElement( "<map value>", true, TestRegisteredAddresses.REFERENCE_YEAR, null, Map.class, 1 )
						.property( "zipCode", true, null, 1, List.class, 0 ),
				pathWith()
						.property( "preferredShipmentAddresses" )
						.containerElement( "<map value>", true, TestRegisteredAddresses.REFERENCE_YEAR, null, Map.class, 1 )
						.property( "doorCode", true, null, 1, List.class, 0 )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "c")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION_EXAMPLES, id = "a")
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONTAINERELEMENTCONSTRAINTS, id = "i")
	public void testGroupConversionIsAppliedOnProperty() {
		Set<ConstraintViolation<RegisteredAddresses>> constraintViolations = getValidator().validate( TestRegisteredAddresses.withInvalidShipmentAddress() );

		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "shipmentAddresses" )
						.containerElement( "<map value>", true, TestRegisteredAddresses.REFERENCE_YEAR, null, Map.class, 1 )
						.property( "street1", true, null, 1, List.class, 0 ),
				pathWith()
						.property( "shipmentAddresses" )
						.containerElement( "<map value>", true, TestRegisteredAddresses.REFERENCE_YEAR, null, Map.class, 1 )
						.property( "zipCode", true, null, 1, List.class, 0 )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "c")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION_EXAMPLES, id = "a")
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONTAINERELEMENTCONSTRAINTS, id = "i")
	public void testGroupConversionIsAppliedOnMethodReturnValue() throws Exception {
		//given
		RegisteredAddresses registeredAddresses = TestRegisteredAddresses.validRegisteredAddresses();
		Method method = RegisteredAddresses.class.getMethod( "retrieveMainAddresses" );
		Object returnValue = TestAddresses.wrap( TestAddresses.withInvalidStreet1() );

		//when
		Set<ConstraintViolation<RegisteredAddresses>> constraintViolations = getExecutableValidator()
				.validateReturnValue( registeredAddresses, method, returnValue );

		//then
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
							   .method( "retrieveMainAddresses" )
							   .returnValue()
							   .containerElement( "<map value>", true, TestRegisteredAddresses.REFERENCE_YEAR, null, Map.class, 1 )
							   .property( "street1", true, null, 0, List.class, 0 )
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "c")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION_EXAMPLES, id = "a")
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONTAINERELEMENTCONSTRAINTS, id = "i")
	public void testGroupConversionIsAppliedOnMethodParameter() throws Exception {
		//given
		RegisteredAddresses registeredAddresses = TestRegisteredAddresses.validRegisteredAddresses();
		Method method = RegisteredAddresses.class.getMethod( "setMainAddresses", Map.class );
		Object[] arguments = new Object[] { TestAddresses.wrap( TestAddresses.withInvalidStreet1() ) };

		//when
		Set<ConstraintViolation<RegisteredAddresses>> constraintViolations = getExecutableValidator()
				.validateParameters( registeredAddresses, method, arguments );

		//then
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
							   .method( "setMainAddresses" )
							   .parameter( "mainAddresses", 0 )
							   .containerElement( "<map value>", true, TestRegisteredAddresses.REFERENCE_YEAR, null, Map.class, 1 )
							   .property( "street1", true, null, 0, List.class, 0 )
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "c")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION_EXAMPLES, id = "a")
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONTAINERELEMENTCONSTRAINTS, id = "i")
	public void testGroupConversionIsAppliedOnConstructorReturnValue() throws Exception {
		//given
		Constructor<RegisteredAddresses> constructor = RegisteredAddresses.class.getConstructor( Map.class );
		RegisteredAddresses createdObject = TestRegisteredAddresses.withMainAddressInvalidStreet1();

		//when
		Set<ConstraintViolation<RegisteredAddresses>> constraintViolations = getExecutableValidator()
				.validateConstructorReturnValue( constructor, createdObject );

		//then
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
							   .constructor( RegisteredAddresses.class )
							   .returnValue()
							   .property( "mainAddresses" )
							   .containerElement( "<map value>", true, TestRegisteredAddresses.REFERENCE_YEAR, null, Map.class, 1 )
							   .property( "street1", true, null, 1, List.class, 0 )
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "c")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION_EXAMPLES, id = "a")
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONTAINERELEMENTCONSTRAINTS, id = "i")
	public void testGroupConversionIsAppliedOnConstructorParameter() throws Exception {
		//given
		Constructor<RegisteredAddresses> constructor = RegisteredAddresses.class.getConstructor( Map.class );
		Object[] arguments = new Object[] { TestAddresses.wrap( TestAddresses.withInvalidStreet1() ) };

		//when
		Set<ConstraintViolation<RegisteredAddresses>> constraintViolations = getExecutableValidator()
				.validateConstructorParameters( constructor, arguments );

		//then
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
				.withPropertyPath( pathWith()
					   .constructor( RegisteredAddresses.class )
					   .parameter( "mainAddresses", 0 )
					   .containerElement( "<map value>", true, TestRegisteredAddresses.REFERENCE_YEAR, null, Map.class, 1 )
					   .property( "street1", true, null, 0, List.class, 0 )
				)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "e")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION_EXAMPLES, id = "a")
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONTAINERELEMENTCONSTRAINTS, id = "i")
	public void testGroupConversionIsNotExecutedRecursively() {
		Set<ConstraintViolation<RegisteredAddresses>> constraintViolations = getValidator().validate( TestRegisteredAddresses.withInvalidOfficeAddress() );

		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "officeAddresses" )
						.containerElement( "<map value>", true, TestRegisteredAddresses.REFERENCE_YEAR, null, Map.class, 1 )
						.property( "street1", true, null, 1, List.class, 0 ),
				pathWith()
						.property( "officeAddresses" )
						.containerElement( "<map value>", true, TestRegisteredAddresses.REFERENCE_YEAR, null, Map.class, 1 )
						.property( "zipCode", true, null, 1, List.class, 0 )
		);

		constraintViolations = getValidator().validate(
				TestRegisteredAddresses.withInvalidOfficeAddress(),
				BasicPostal.class
		);

		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "officeAddresses" )
						.containerElement( "<map value>", true, TestRegisteredAddresses.REFERENCE_YEAR, null, Map.class, 1 )
						.property( "doorCode", true, null, 1, List.class, 0 )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "h")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION_EXAMPLES, id = "a")
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONTAINERELEMENTCONSTRAINTS, id = "i")
	public void testGroupConversionWithSequenceAsTo() {
		RegisteredAddresses registeredAddresses = TestRegisteredAddresses.validRegisteredAddresses();

		Set<ConstraintViolation<RegisteredAddresses>> constraintViolations = getValidator().validate( registeredAddresses );
		assertNoViolations( constraintViolations );

		registeredAddresses.getWeekendAddresses().get( TestRegisteredAddresses.REFERENCE_YEAR ).get( 0 ).setDoorCode( "ABC" );
		constraintViolations = getValidator().validate( registeredAddresses );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "weekendAddresses" )
						.containerElement( "<map value>", true, TestRegisteredAddresses.REFERENCE_YEAR, null, Map.class, 1 )
						.property( "doorCode", true, null, 0, List.class, 0 )
		);

		registeredAddresses.getWeekendAddresses().get( TestRegisteredAddresses.REFERENCE_YEAR ).get( 0 ).setStreet1( null );
		constraintViolations = getValidator().validate( registeredAddresses );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "weekendAddresses" )
						.containerElement( "<map value>", true, TestRegisteredAddresses.REFERENCE_YEAR, null, Map.class, 1 )
						.property( "street1", true, null, 0, List.class, 0 ) );
	}

	private static class TestRegisteredAddresses {

		public final static Year REFERENCE_YEAR = Year.of(2016);

		public static RegisteredAddresses validRegisteredAddresses() {
			return new RegisteredAddresses()
					.addMainAddress( REFERENCE_YEAR, TestAddresses.validAddress() )
					.addShipmentAddress( REFERENCE_YEAR, TestAddresses.validAddress() )
					.addPreferredShipmentAddress( REFERENCE_YEAR, TestAddresses.validAddress() )
					.addOfficeAddress( REFERENCE_YEAR, TestAddresses.validAddress() )
					.addWeekendAddress( REFERENCE_YEAR, TestAddresses.validAddress() );
		}

		public static RegisteredAddresses withInvalidMainAddress() {
			RegisteredAddresses registeredAddresses = validRegisteredAddresses();
			registeredAddresses.addMainAddress( REFERENCE_YEAR, TestAddresses.invalidAddress() );
			return registeredAddresses;
		}

		public static RegisteredAddresses withMainAddressInvalidStreet1() {
			RegisteredAddresses registeredAddresses = validRegisteredAddresses();
			registeredAddresses.addMainAddress( REFERENCE_YEAR, TestAddresses.withInvalidStreet1() );
			return registeredAddresses;
		}

		public static RegisteredAddresses withInvalidShipmentAddress() {
			RegisteredAddresses registeredAddresses = validRegisteredAddresses();
			registeredAddresses.addShipmentAddress( REFERENCE_YEAR, TestAddresses.invalidAddress() );
			return registeredAddresses;
		}

		public static RegisteredAddresses withInvalidPreferredShipmentAddress() {
			RegisteredAddresses registeredAddresses = validRegisteredAddresses();
			registeredAddresses.addPreferredShipmentAddress( REFERENCE_YEAR, TestAddresses.invalidAddress() );
			return registeredAddresses;
		}

		public static RegisteredAddresses withInvalidOfficeAddress() {
			RegisteredAddresses registeredAddresses = validRegisteredAddresses();
			registeredAddresses.addOfficeAddress( REFERENCE_YEAR, TestAddresses.invalidAddress() );
			return registeredAddresses;
		}
	}

	private static class TestAddresses {

		public static Address validAddress() {
			return new Address( "Main Street", "c/o Hitchcock", "123", "AB" );
		}

		public static Address invalidAddress() {
			return new Address( null, null, "12", "ABC" );
		}

		public static Address withInvalidStreet1() {
			Address address = validAddress();
			address.setStreet1( null );
			return address;
		}

		public static Map<Year, List<Address>> wrap(Address address) {
			Map<Year, List<Address>> addresses = new HashMap<>();
			addresses.put( TestRegisteredAddresses.REFERENCE_YEAR, Arrays.asList( address ) );
			return addresses;
		}
	}
}
