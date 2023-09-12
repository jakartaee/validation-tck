/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation.graphnavigation.containerelement;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertNoViolations;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.pathWith;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.tests.validation.graphnavigation.containerelement.model.Address;
import org.hibernate.beanvalidation.tck.tests.validation.graphnavigation.containerelement.model.AddressType;
import org.hibernate.beanvalidation.tck.tests.validation.graphnavigation.containerelement.model.Cinema;
import org.hibernate.beanvalidation.tck.tests.validation.graphnavigation.containerelement.model.EmailAddress;
import org.hibernate.beanvalidation.tck.tests.validation.graphnavigation.containerelement.model.Reference;
import org.hibernate.beanvalidation.tck.tests.validation.graphnavigation.containerelement.model.ReferenceValueExtractor;
import org.hibernate.beanvalidation.tck.tests.validation.graphnavigation.containerelement.model.SomeReference;
import org.hibernate.beanvalidation.tck.tests.validation.graphnavigation.containerelement.model.Visitor;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Guillaume Smet
 */
@SpecVersion(spec = "beanvalidation", version = "3.0.0")
public class NestedCascadingOnContainerElementsTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( NestedCascadingOnContainerElementsTest.class )
				.withPackage( Cinema.class.getPackage() )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "l")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "m")
	public void testNestedValid() {
		Validator validator = getValidator();

		Set<ConstraintViolation<EmailAddressMap>> constraintViolations = validator.validate( EmailAddressMap.validEmailAddressMap() );

		assertNoViolations( constraintViolations );

		constraintViolations = validator.validate( EmailAddressMap.invalidEmailAddressMap() );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotBlank.class )
						.withPropertyPath( pathWith()
								.property( "map" )
								.containerElement( "<map value>", true, "invalid", null, Map.class, 1 )
								.property( "email", true, null, 1, List.class, 0 )
						),
				violationOf( NotBlank.class )
						.withPropertyPath( pathWith()
								.property( "map" )
								.containerElement( "<map value>", true, "invalid", null, Map.class, 1 )
								.property( "email", true, null, 2, List.class, 0 )
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "l")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "m")
	public void testNestedValidWithTwoInnerExtractions() {
		Validator validator = getValidator();

		Set<ConstraintViolation<AddressBook>> constraintViolations = validator.validate( AddressBook.valid() );

		assertNoViolations( constraintViolations );

		constraintViolations = validator.validate( AddressBook.invalid() );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotBlank.class )
						.withPropertyPath( pathWith()
								.property( "addressesPerTypePerPerson" )
								.containerElement( "<map value>", true, "Firstname Lastname", null, Map.class, 1 )
								.property( "type", true, AddressType.invalid(), null, Map.class, 0 )
						),
				violationOf( NotBlank.class )
						.withPropertyPath( pathWith()
								.property( "addressesPerTypePerPerson" )
								.containerElement( "<map value>", true, "Firstname Lastname", null, Map.class, 1 )
								.property( "zipCode", true, AddressType.invalid(), null, Map.class, 1 )
						),
				violationOf( NotBlank.class )
						.withPropertyPath( pathWith()
								.property( "addressesPerTypePerPerson" )
								.containerElement( "<map value>", true, "Firstname Lastname", null, Map.class, 1 )
								.property( "zipCode", true, AddressType.valid(), null, Map.class, 1 )
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "l")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "m")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_REGISTERING, id = "c")
	public void testMultipleNestedValidWithCustomExtractor() {
		Validator validator = Validation.byDefaultProvider()
				.configure()
				.addValueExtractor( new ReferenceValueExtractor() )
				.buildValidatorFactory()
				.getValidator();

		Set<ConstraintViolation<CinemaEmailAddresses>> constraintViolations = validator.validate( CinemaEmailAddresses.validCinemaEmailAddresses() );

		assertNoViolations( constraintViolations );

		CinemaEmailAddresses invalidCinemaEmailAddresses = CinemaEmailAddresses.invalidCinemaEmailAddresses();
		constraintViolations = validator.validate( invalidCinemaEmailAddresses );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotBlank.class )
						.withPropertyPath( pathWith()
								.property( "map" )
								.containerElement( "<map value>", true, invalidCinemaEmailAddresses.map.keySet().toArray()[1], null, Map.class, 1 )
								.property( "email", true, null, 1, List.class, 0 )
						),
				violationOf( NotBlank.class )
						.withPropertyPath( pathWith()
								.property( "map" )
								.containerElement( "<map value>", true, invalidCinemaEmailAddresses.map.keySet().toArray()[1], null, Map.class, 1 )
								.property( "email", true, null, 2, List.class, 0 )
						),
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.property( "map" )
								.containerElement( "<map value>", true, invalidCinemaEmailAddresses.map.keySet().toArray()[2], null, Map.class, 1 )
								.containerElement( "<list element>", true, null, 0, List.class, 0 )
						)
		);

		CinemaEmailAddresses invalidKeyCinemaEmailAddresses = CinemaEmailAddresses.invalidKey();
		constraintViolations = validator.validate( invalidKeyCinemaEmailAddresses );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.property( "map" )
								.containerElement( "<map key>", true, invalidKeyCinemaEmailAddresses.map.keySet().toArray()[1], null, Map.class, 0 )
								.property( "visitor", Optional.class, 0 )
								.property( "name", Reference.class, 0 )
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "l")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "m")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_VALUEEXTRACTORRESOLUTION_REGISTERING, id = "c")
	public void testNestedNullValue() {
		Validator validator = Validation.byDefaultProvider()
				.configure()
				.addValueExtractor( new ReferenceValueExtractor() )
				.buildValidatorFactory()
				.getValidator();

		Set<ConstraintViolation<NestedCascadingListWithValidAllAlongTheWay>> constraintViolations = validator
				.validate( NestedCascadingListWithValidAllAlongTheWay.valid() );

		assertNoViolations( constraintViolations );

		constraintViolations = validator.validate( NestedCascadingListWithValidAllAlongTheWay.withNullList() );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.property( "list" )
								.containerElement( "<list element>", true, null, 0, List.class, 0 )
						)
		);
	}

	private static class EmailAddressMap {

		private final Map<String, List<@Valid EmailAddress>> map = new LinkedHashMap<>();

		private static EmailAddressMap validEmailAddressMap() {
			List<EmailAddress> validEmailAddresses = Arrays.asList( new EmailAddress( "valid-email-1@example.com" ), new EmailAddress( "valid-email-2@example.com" ) );

			EmailAddressMap emailAddressMap = new EmailAddressMap();
			emailAddressMap.map.put( "valid", validEmailAddresses );

			return emailAddressMap;
		}

		private static EmailAddressMap invalidEmailAddressMap() {
			List<EmailAddress> validEmailAddresses = Arrays.asList( new EmailAddress( "valid-email-1@example.com" ), new EmailAddress( "valid-email-2@example.com" ) );
			List<EmailAddress> emailAddressesContainingInvalidEmails = Arrays.asList( new EmailAddress( "valid-email-3@example.com" ),
					new EmailAddress( " " ), new EmailAddress( "  " ) );

			EmailAddressMap emailAddressMap = new EmailAddressMap();
			emailAddressMap.map.put( "valid", validEmailAddresses );
			emailAddressMap.map.put( "invalid", emailAddressesContainingInvalidEmails );

			return emailAddressMap;
		}
	}

	private static class CinemaEmailAddresses {

		private final Map<@NotNull Optional<@Valid Cinema>, List<@NotNull @Valid EmailAddress>> map = new LinkedHashMap<>();

		private static CinemaEmailAddresses validCinemaEmailAddresses() {
			CinemaEmailAddresses cinemaEmailAddresses = new CinemaEmailAddresses();
			cinemaEmailAddresses.map.put(
					Optional.of( new Cinema( "cinema1", new SomeReference<>( new Visitor( "Name 1" ) ) ) ),
					Arrays.asList( new EmailAddress( "valid-email-1@example.com" ), new EmailAddress( "valid-email-2@example.com" ) )
			);

			return cinemaEmailAddresses;
		}

		private static CinemaEmailAddresses invalidCinemaEmailAddresses() {
			CinemaEmailAddresses cinemaEmailAddresses = validCinemaEmailAddresses();
			cinemaEmailAddresses.map.put(
					Optional.of( new Cinema( "cinema2", new SomeReference<>( new Visitor( "Name 2" ) ) ) ),
					Arrays.asList( new EmailAddress( "valid-email-3@example.com" ), new EmailAddress( " " ), new EmailAddress( "  " ) )
			);
			cinemaEmailAddresses.map.put(
					Optional.of( new Cinema( "cinema3", new SomeReference<>( new Visitor( "Name 3" ) ) ) ),
					Arrays.asList( (EmailAddress) null )
			);

			return cinemaEmailAddresses;
		}

		private static CinemaEmailAddresses invalidKey() {
			CinemaEmailAddresses cinemaEmailAddresses = validCinemaEmailAddresses();
			cinemaEmailAddresses.map.put(
					Optional.of( new Cinema( "cinema4", new SomeReference<>( new Visitor() ) ) ),
					Arrays.asList( new EmailAddress( "valid-email-4@example.com" ) )
			);

			return cinemaEmailAddresses;
		}
	}

	@SuppressWarnings({ "unused", "unchecked" })
	private static class CinemaArray {

		private List<@NotNull @Valid Cinema>[] array;

		private static CinemaArray validCinemaArray() {
			CinemaArray cinemaArray = new CinemaArray();

			cinemaArray.array = new List[] { Arrays.asList( new Cinema( "cinema1", new SomeReference<>( new Visitor( "Name 1" ) ) ) ) };

			return cinemaArray;
		}

		private static CinemaArray invalidCinemaArray() {
			CinemaArray cinemaArray = new CinemaArray();

			cinemaArray.array = new List[] { Arrays.asList( null, new Cinema( "cinema2", new SomeReference<>( new Visitor() ) ) ) };

			return cinemaArray;
		}
	}

	@SuppressWarnings({ "unused" })
	private static class NestedCascadingListWithValidAllAlongTheWay {

		private List<@NotNull List<@NotNull @Valid Cinema>> list;

		private static NestedCascadingListWithValidAllAlongTheWay valid() {
			NestedCascadingListWithValidAllAlongTheWay valid = new NestedCascadingListWithValidAllAlongTheWay();

			valid.list = Arrays.asList( Arrays.asList( new Cinema( "cinema1", new SomeReference<>( new Visitor( "Name 1" ) ) ) ) );

			return valid;
		}

		private static NestedCascadingListWithValidAllAlongTheWay withNullList() {
			NestedCascadingListWithValidAllAlongTheWay valid = new NestedCascadingListWithValidAllAlongTheWay();

			valid.list = Arrays.asList( (List<Cinema>) null );

			return valid;
		}
	}

	private static class AddressBook {

		private Map<String, Map<@Valid AddressType, @Valid Address>> addressesPerTypePerPerson = new HashMap<>();

		private static AddressBook valid() {
			Map<AddressType, Address> addressesPerType = new HashMap<>();
			addressesPerType.put( AddressType.valid(), Address.valid() );

			AddressBook addressBook = new AddressBook();
			addressBook.addressesPerTypePerPerson.put( "Firstname Lastname", addressesPerType );

			return addressBook;
		}

		private static AddressBook invalid() {
			Map<AddressType, Address> addressesPerType = new HashMap<>();
			addressesPerType.put( AddressType.valid(), Address.valid() );
			addressesPerType.put( AddressType.valid(), Address.invalid() );
			addressesPerType.put( AddressType.invalid(), Address.invalid() );

			AddressBook addressBook = new AddressBook();
			addressBook.addressesPerTypePerPerson.put( "Firstname Lastname", addressesPerType );

			return addressBook;
		}
	}
}
