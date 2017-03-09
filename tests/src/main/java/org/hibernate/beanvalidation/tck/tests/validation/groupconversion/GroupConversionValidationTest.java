/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation.groupconversion;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ElementKind;
import javax.validation.Path;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.tests.BaseExecutableValidatorTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectNumberOfViolations;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectPropertyPaths;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertDescriptorKinds;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertNodeNames;
import static org.hibernate.beanvalidation.tck.util.TestUtil.webArchiveBuilder;
import static org.testng.Assert.assertTrue;

/**
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class GroupConversionValidationTest extends BaseExecutableValidatorTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( GroupConversionValidationTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = "4.4.5", id = "c")
	@SpecAssertion(section = "4.6", id = "a")
	public void testGroupConversionIsAppliedOnField() {
		Set<ConstraintViolation<User>> constraintViolations = getValidator().validate( TestUsers.withInvalidMainAddress() );

		assertCorrectPropertyPaths(
				constraintViolations,
				"mainAddress.street1",
				"mainAddress.zipCode"
		);
	}

	@Test
	@SpecAssertion(section = "4.4.5", id = "c")
	@SpecAssertion(section = "4.6", id = "a")
	public void testSeveralGroupConversionsAppliedOnField() {
		User userWithInvalidPreferredShipmentAddress = TestUsers.withInvalidPreferredShipmentAddress();

		Set<ConstraintViolation<User>> constraintViolations = getValidator().validate(
				userWithInvalidPreferredShipmentAddress
		);

		assertCorrectPropertyPaths(
				constraintViolations,
				"preferredShipmentAddress.street1",
				"preferredShipmentAddress.zipCode"
		);

		constraintViolations = getValidator().validate(
				userWithInvalidPreferredShipmentAddress,
				Complex.class
		);

		assertCorrectPropertyPaths(
				constraintViolations,
				"preferredShipmentAddress.doorCode"
		);

		constraintViolations = getValidator().validate(
				userWithInvalidPreferredShipmentAddress,
				Default.class,
				Complex.class
		);

		assertCorrectPropertyPaths(
				constraintViolations,
				"preferredShipmentAddress.street1",
				"preferredShipmentAddress.zipCode",
				"preferredShipmentAddress.doorCode"
		);

		constraintViolations = getValidator().validate(
				userWithInvalidPreferredShipmentAddress,
				Complete.class
		);

		assertCorrectPropertyPaths(
				constraintViolations,
				"preferredShipmentAddress.street1",
				"preferredShipmentAddress.zipCode",
				"preferredShipmentAddress.doorCode"
		);
	}

	@Test
	@SpecAssertion(section = "4.4.5", id = "c")
	public void testGroupConversionIsAppliedOnProperty() {
		Set<ConstraintViolation<User>> constraintViolations = getValidator().validate( TestUsers.withInvalidShipmentAddress() );

		assertCorrectPropertyPaths(
				constraintViolations,
				"shipmentAddresses[0].street1",
				"shipmentAddresses[0].zipCode"
		);
	}

	@Test
	@SpecAssertion(section = "4.4.5", id = "c")
	public void testGroupConversionIsAppliedOnMethodReturnValue() throws Exception {
		//given
		User user = TestUsers.validUser();
		Method method = User.class.getMethod( "retrieveMainAddress" );
		Object returnValue = TestAddresses.withInvalidStreet1();

		//when
		Set<ConstraintViolation<User>> constraintViolations = getExecutableValidator()
				.validateReturnValue( user, method, returnValue );

		//then
		assertCorrectNumberOfViolations( constraintViolations, 1 );

		Path propertyPath = constraintViolations.iterator().next().getPropertyPath();

		assertDescriptorKinds( propertyPath, ElementKind.METHOD, ElementKind.RETURN_VALUE, ElementKind.PROPERTY );
		assertNodeNames( propertyPath, "retrieveMainAddress", TestUtil.RETURN_VALUE_NODE_NAME, "street1" );
	}

	@Test
	@SpecAssertion(section = "4.4.5", id = "c")
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
		assertCorrectNumberOfViolations( constraintViolations, 1 );

		Path propertyPath = constraintViolations.iterator().next().getPropertyPath();

		assertDescriptorKinds( propertyPath, ElementKind.METHOD, ElementKind.RETURN_VALUE, ElementKind.PROPERTY );
		assertNodeNames( propertyPath, "retrieveWeekendAddress", TestUtil.RETURN_VALUE_NODE_NAME, "street1" );
	}

	@Test
	@SpecAssertion(section = "4.4.5", id = "c")
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
		assertCorrectNumberOfViolations( constraintViolations, 1 );

		Path propertyPath = constraintViolations.iterator().next().getPropertyPath();

		assertDescriptorKinds( propertyPath, ElementKind.METHOD, ElementKind.RETURN_VALUE, ElementKind.PROPERTY );
		assertNodeNames( propertyPath, "retrieveFallbackAddress", TestUtil.RETURN_VALUE_NODE_NAME, "street1" );
	}

	@Test
	@SpecAssertion(section = "4.4.5", id = "c")
	public void testGroupConversionIsAppliedOnMethodParameter() throws Exception {
		//given
		User user = TestUsers.validUser();
		Method method = User.class.getMethod( "setMainAddress", Address.class );
		Object[] arguments = new Object[] { TestAddresses.withInvalidStreet1() };

		//when
		Set<ConstraintViolation<User>> constraintViolations = getExecutableValidator()
				.validateParameters( user, method, arguments );

		//then
		assertCorrectNumberOfViolations( constraintViolations, 1 );

		Path propertyPath = constraintViolations.iterator().next().getPropertyPath();

		assertDescriptorKinds( propertyPath, ElementKind.METHOD, ElementKind.PARAMETER, ElementKind.PROPERTY );
		assertNodeNames( propertyPath, "setMainAddress", "mainAddress", "street1" );
	}

	@Test
	@SpecAssertion(section = "4.4.5", id = "c")
	public void testGroupConversionIsAppliedOnConstructorReturnValue() throws Exception {
		//given
		Constructor<User> constructor = User.class.getConstructor( Address.class );
		User createdObject = new User( TestAddresses.withInvalidStreet1() );

		//when
		Set<ConstraintViolation<User>> constraintViolations = getExecutableValidator()
				.validateConstructorReturnValue( constructor, createdObject );

		//then
		assertCorrectNumberOfViolations( constraintViolations, 1 );

		Path propertyPath = constraintViolations.iterator().next().getPropertyPath();

		assertDescriptorKinds(
				propertyPath,
				ElementKind.CONSTRUCTOR,
				ElementKind.RETURN_VALUE,
				ElementKind.PROPERTY,
				ElementKind.PROPERTY
		);
		assertNodeNames( propertyPath, "User", TestUtil.RETURN_VALUE_NODE_NAME, "mainAddress", "street1" );
	}

	@Test
	@SpecAssertion(section = "4.4.5", id = "c")
	public void testGroupConversionIsAppliedOnConstructorParameter() throws Exception {
		//given
		Constructor<User> constructor = User.class.getConstructor( Address.class );
		Object[] arguments = new Object[] { TestAddresses.withInvalidStreet1() };

		//when
		Set<ConstraintViolation<User>> constraintViolations = getExecutableValidator()
				.validateConstructorParameters( constructor, arguments );

		//then
		assertCorrectNumberOfViolations( constraintViolations, 1 );

		Path propertyPath = constraintViolations.iterator().next().getPropertyPath();

		assertDescriptorKinds( propertyPath, ElementKind.CONSTRUCTOR, ElementKind.PARAMETER, ElementKind.PROPERTY );
		assertNodeNames( propertyPath, "User", "mainAddress", "street1" );
	}

	@Test
	@SpecAssertion(section = "4.4.5", id = "d")
	public void testGroupConversionIsNotExecutedRecursively() {
		Set<ConstraintViolation<User>> constraintViolations = getValidator().validate( TestUsers.withInvalidOfficeAddress() );

		assertCorrectPropertyPaths(
				constraintViolations,
				"officeAddress.street1",
				"officeAddress.zipCode"
		);

		constraintViolations = getValidator().validate(
				TestUsers.withInvalidOfficeAddress(),
				BasicPostal.class
		);

		assertCorrectPropertyPaths(
				constraintViolations,
				"officeAddress.doorCode"
		);
	}

	@Test
	@SpecAssertion(section = "4.4.5", id = "g")
	public void testGroupConversionWithSequenceAsTo() {
		User user = TestUsers.validUser();

		Set<ConstraintViolation<User>> constraintViolations = getValidator().validate( user );
		assertCorrectNumberOfViolations( constraintViolations, 0 );

		user.getWeekendAddress().setDoorCode( "ABC" );
		constraintViolations = getValidator().validate( user );
		assertCorrectPropertyPaths( constraintViolations, "weekendAddress.doorCode" );

		user.getWeekendAddress().setStreet1( null );
		constraintViolations = getValidator().validate( user );
		assertCorrectPropertyPaths( constraintViolations, "weekendAddress.street1" );
	}

	@Test
	@SpecAssertion(section = "4.4.5", id = "b")
	public void testGroupIsPassedAsIsToNestedElementWithoutConversion() {
		Set<ConstraintViolation<FooHolder>> constraintViolations = getValidator().validate( new FooHolder() );
		assertTrue( constraintViolations.isEmpty(), "No violations expected for default group" );

		constraintViolations = getValidator().validate( new FooHolder(), Complex.class );
		assertCorrectPropertyPaths( constraintViolations, "foo.bar" );
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
