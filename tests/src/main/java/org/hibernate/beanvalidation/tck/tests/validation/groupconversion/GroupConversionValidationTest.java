/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation.groupconversion;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertNoViolations;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.pathWith;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class GroupConversionValidationTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( GroupConversionValidationTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "c")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE, id = "a")
	public void testGroupConversionIsAppliedOnField() {
		Set<ConstraintViolation<User>> constraintViolations = getValidator().validate( TestUsers.withInvalidMainAddress() );

		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "mainAddress" )
						.property( "street1" ),
				pathWith()
						.property( "mainAddress" )
						.property( "zipCode" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "c")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "d")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE, id = "a")
	public void testSeveralGroupConversionsAppliedOnField() {
		User userWithInvalidPreferredShipmentAddress = TestUsers.withInvalidPreferredShipmentAddress();

		Set<ConstraintViolation<User>> constraintViolations = getValidator().validate(
				userWithInvalidPreferredShipmentAddress
		);

		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "preferredShipmentAddress" )
						.property( "street1" ),
				pathWith()
						.property( "preferredShipmentAddress" )
						.property( "zipCode" )
		);

		constraintViolations = getValidator().validate(
				userWithInvalidPreferredShipmentAddress,
				Complex.class
		);

		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "preferredShipmentAddress" )
						.property( "doorCode" )
		);

		constraintViolations = getValidator().validate(
				userWithInvalidPreferredShipmentAddress,
				Default.class,
				Complex.class
		);

		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "preferredShipmentAddress" )
						.property( "street1" ),
				pathWith()
						.property( "preferredShipmentAddress" )
						.property( "zipCode" ),
				pathWith()
						.property( "preferredShipmentAddress" )
						.property( "doorCode" )
		);

		constraintViolations = getValidator().validate(
				userWithInvalidPreferredShipmentAddress,
				Complete.class
		);

		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "preferredShipmentAddress" )
						.property( "street1" ),
				pathWith()
						.property( "preferredShipmentAddress" )
						.property( "zipCode" ),
				pathWith()
						.property( "preferredShipmentAddress" )
						.property( "doorCode" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "c")
	public void testGroupConversionIsAppliedOnProperty() {
		Set<ConstraintViolation<User>> constraintViolations = getValidator().validate( TestUsers.withInvalidShipmentAddress() );

		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "shipmentAddresses" )
						.property( "street1", true, null, 0, List.class, 0 ),
				pathWith()
						.property( "shipmentAddresses" )
						.property( "zipCode", true, null, 0, List.class, 0 )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "c")
	public void testGroupConversionIsAppliedOnMethodReturnValue() throws Exception {
		//given
		User user = TestUsers.validUser();
		Method method = User.class.getMethod( "retrieveMainAddress" );
		Object returnValue = TestAddresses.withInvalidStreet1();

		//when
		Set<ConstraintViolation<User>> constraintViolations = getExecutableValidator()
				.validateReturnValue( user, method, returnValue );

		//then
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
							   .method( "retrieveMainAddress" )
							   .returnValue()
							   .property( "street1" )
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "c")
	public void testGroupConversionDefinedInSubClassIsAppliedOnMethodReturnValue()
			throws Exception {
		//given
		EndUserImpl user = TestUsers.validEndUser();
		Method method = EndUserImpl.class.getMethod( "retrieveWeekendAddress" );
		Object returnValue = TestAddresses.withInvalidStreet1();

		//when
		Set<ConstraintViolation<EndUserImpl>> constraintViolations = getExecutableValidator()
				.validateReturnValue( user, method, returnValue );

		//then
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
							   .method( "retrieveWeekendAddress" )
							   .returnValue()
							   .property( "street1" )
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "c")
	public void testGroupConversionDefinedInImplementedClassIsAppliedOnMethodReturnValue()
			throws Exception {
		//given
		EndUserImpl user = TestUsers.validEndUser();
		Method method = EndUserImpl.class.getMethod( "retrieveFallbackAddress" );
		Object returnValue = TestAddresses.withInvalidStreet1();

		//when
		Set<ConstraintViolation<EndUserImpl>> constraintViolations = getExecutableValidator()
				.validateReturnValue( user, method, returnValue );

		//then
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
							   .method( "retrieveFallbackAddress" )
							   .returnValue()
							   .property( "street1" )
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "c")
	public void testGroupConversionIsAppliedOnMethodParameter() throws Exception {
		//given
		User user = TestUsers.validUser();
		Method method = User.class.getMethod( "setMainAddress", Address.class );
		Object[] arguments = new Object[] { TestAddresses.withInvalidStreet1() };

		//when
		Set<ConstraintViolation<User>> constraintViolations = getExecutableValidator()
				.validateParameters( user, method, arguments );

		//then
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
							   .method( "setMainAddress" )
							   .parameter( "mainAddress", 0 )
							   .property( "street1" )
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "c")
	public void testGroupConversionIsAppliedOnConstructorReturnValue() throws Exception {
		//given
		Constructor<User> constructor = User.class.getConstructor( Address.class );
		User createdObject = new User( TestAddresses.withInvalidStreet1() );

		//when
		Set<ConstraintViolation<User>> constraintViolations = getExecutableValidator()
				.validateConstructorReturnValue( constructor, createdObject );

		//then
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
							   .constructor( User.class )
							   .returnValue()
							   .property( "mainAddress" )
							   .property( "street1" )
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "c")
	public void testGroupConversionIsAppliedOnConstructorParameter() throws Exception {
		//given
		Constructor<User> constructor = User.class.getConstructor( Address.class );
		Object[] arguments = new Object[] { TestAddresses.withInvalidStreet1() };

		//when
		Set<ConstraintViolation<User>> constraintViolations = getExecutableValidator()
				.validateConstructorParameters( constructor, arguments );

		//then
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
							   .constructor( User.class )
							   .parameter( "mainAddress", 0 )
							   .property( "street1" )
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "e")
	public void testGroupConversionIsNotExecutedRecursively() {
		Set<ConstraintViolation<User>> constraintViolations = getValidator().validate( TestUsers.withInvalidOfficeAddress() );

		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "officeAddress" )
						.property( "street1" ),
				pathWith()
						.property( "officeAddress" )
						.property( "zipCode" )
		);

		constraintViolations = getValidator().validate(
				TestUsers.withInvalidOfficeAddress(),
				BasicPostal.class
		);

		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "officeAddress" )
						.property( "doorCode" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "h")
	public void testGroupConversionWithSequenceAsTo() {
		User user = TestUsers.validUser();

		Set<ConstraintViolation<User>> constraintViolations = getValidator().validate( user );
		assertNoViolations( constraintViolations );

		user.getWeekendAddress().setDoorCode( "ABC" );
		constraintViolations = getValidator().validate( user );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "weekendAddress" )
						.property( "doorCode" )
		);

		user.getWeekendAddress().setStreet1( null );
		constraintViolations = getValidator().validate( user );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "weekendAddress" )
						.property( "street1" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "b")
	public void testGroupIsPassedAsIsToNestedElementWithoutConversion() {
		Set<ConstraintViolation<FooHolder>> constraintViolations = getValidator().validate( new FooHolder() );
		assertTrue( constraintViolations.isEmpty(), "No violations expected for default group" );

		constraintViolations = getValidator().validate( new FooHolder(), Complex.class );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "foo" )
						.property( "bar" )
		);
	}

	private static class TestUsers {

		public static User validUser() {
			return new User(
					TestAddresses.validAddress(),
					Arrays.asList( TestAddresses.validAddress() ),
					TestAddresses.validAddress(),
					TestAddresses.validAddress(),
					TestAddresses.validAddress()
			);
		}

		public static EndUserImpl validEndUser() {
			return new EndUserImpl(
					TestAddresses.validAddress(),
					Arrays.asList( TestAddresses.validAddress() ),
					TestAddresses.validAddress(),
					TestAddresses.validAddress(),
					TestAddresses.validAddress()
			);
		}

		public static User withInvalidMainAddress() {
			User user = validUser();
			user.setMainAddress( TestAddresses.invalidAddress() );
			return user;
		}

		public static User withInvalidShipmentAddress() {
			User user = validUser();
			user.setShipmentAddresses( Arrays.asList( TestAddresses.invalidAddress() ) );
			return user;
		}

		public static User withInvalidPreferredShipmentAddress() {
			User user = validUser();
			user.setPreferredShipmentAddress( TestAddresses.invalidAddress() );
			return user;
		}

		public static User withInvalidOfficeAddress() {
			User user = validUser();
			user.setOfficeAddress( TestAddresses.invalidAddress() );
			return user;
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
	}

	private static class FooHolder {

		@Valid
		private final Foo foo = new Foo();
	}

	private static class Foo {

		@NotNull(groups = Complex.class)
		private String bar;
	}
}
