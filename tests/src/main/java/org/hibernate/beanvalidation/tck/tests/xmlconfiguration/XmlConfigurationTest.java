/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration;

import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectConstraintViolationMessages;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectNumberOfViolations;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.InputStream;
import java.util.Set;

import javax.validation.Configuration;
import javax.validation.ConstraintViolation;
import javax.validation.Payload;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstraintDescriptor;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class XmlConfigurationTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( XmlConfigurationTest.class )
				.withClasses(
						User.class,
						UserType.class,
						Error.class,
						ConsistentUserInformation.class,
						ConsistentUserValidator.class,
						CustomConsistentUserValidator.class,
						Optional.class,
						Order.class,
						OrderLine.class,
						CreditCard.class,
						TestGroup.class,
						SuperUser.class
				)
				.withValidationXml( "validation-XmlConfigurationTest.xml" )
				.withResource( "user-constraints.xml" )
				.withResource( "superuser-constraints.xml" )
				.withResource( "order-constraints.xml" )
				.withResource( "order-constraints-XmlConfigurationTest.xml" )
				.build();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "a"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "n"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "o"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML, id = "a"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML, id = "b"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDEFINITION, id = "a")
	})
	public void testClassConstraintDefinedInXml() {
		User user = new User();
		Set<ConstraintViolation<User>> constraintViolations = getValidator().validate( user, TestGroup.class );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintViolationMessages(
				constraintViolations, "Message from xml"
		);

		ConstraintViolation<User> constraintViolation = constraintViolations.iterator().next();
		Set<Class<? extends Payload>> payloads = constraintViolation.getConstraintDescriptor().getPayload();
		assertTrue( payloads.size() == 1, "One one payload class is defined in xml" );
		assertTrue( Error.class.equals( payloads.iterator().next() ) );

		user.setConsistent( true );
		constraintViolations = getValidator().validate( user );
		assertCorrectNumberOfViolations( constraintViolations, 0 );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "a"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "n"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "o"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML, id = "a"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML, id = "b"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDEFINITION, id = "a")
	})
	public void testIgnoreValidationXml() {
		Configuration<?> config = TestUtil.getConfigurationUnderTest();
		Validator validator = config.ignoreXmlConfiguration().buildValidatorFactory().getValidator();

		Order order = new Order();
		Set<ConstraintViolation<Order>> constraintViolations = getValidator().validate( order );
		assertCorrectNumberOfViolations( constraintViolations, 0 );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "a"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "n"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "o"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML, id = "a"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML, id = "b"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDEFINITION, id = "a")
	})
	public void testPropertyConstraintDefinedInXml() {
		User user = new User();
		user.setConsistent( true );
		user.setFirstname( "Wolfeschlegelsteinhausenbergerdorff" );

		Set<ConstraintViolation<User>> constraintViolations = getValidator().validate( user );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintViolationMessages( constraintViolations, "Size is limited!" );

		user.setFirstname( "Wolfgang" );
		constraintViolations = getValidator().validate( user );
		assertCorrectNumberOfViolations( constraintViolations, 0 );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "a"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "n"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "o"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML, id = "a"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML, id = "b"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDEFINITION, id = "a")
	})
	public void testFieldConstraintDefinedInXml() {
		User user = new User();
		user.setConsistent( true );
		user.setFirstname( "Wolfgang" );
		user.setLastname( "doe" );

		Set<ConstraintViolation<User>> constraintViolations = getValidator().validate( user );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintViolationMessages(
				constraintViolations, "Last name has to start with with a capital letter."
		);

		user.setLastname( "Doe" );
		constraintViolations = getValidator().validate( user );
		assertCorrectNumberOfViolations( constraintViolations, 0 );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "a"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "n"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "o"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML, id = "a"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML, id = "b"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDEFINITION, id = "a")
	})
	public void testAnnotationDefinedConstraintApplies() {
		User user = new User();
		user.setConsistent( true );
		user.setPhoneNumber( "police" );

		Set<ConstraintViolation<User>> constraintViolations = getValidator().validate( user );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintViolationMessages(
				constraintViolations, "A phone number can only contain numbers, whitespaces and dashes."
		);

		user.setPhoneNumber( "112" );
		constraintViolations = getValidator().validate( user );
		assertCorrectNumberOfViolations( constraintViolations, 0 );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "a"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "n"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "o"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML, id = "a"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML, id = "b"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDEFINITION, id = "a")
	})
	public void testCascadingConfiguredInXml() {
		User user = new User();
		user.setConsistent( true );
		CreditCard card = new CreditCard();
		card.setNumber( "not a number" );
		user.setCreditcard( card );

		Set<ConstraintViolation<User>> constraintViolations = getValidator().validate( user );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintViolationMessages( constraintViolations, "Not a credit card number." );

		card.setNumber( "1234567890" );
		constraintViolations = getValidator().validate( user );
		assertCorrectNumberOfViolations( constraintViolations, 0 );
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "p")
	public void testMappingFilesAddedViaConfigurationGetAddedToXmlConfiguredMappings() {
		assertFalse(
				getValidator().getConstraintsForClass( Order.class ).isBeanConstrained(),
				"Without additional mapping Order should be unconstrained"
		);

		Configuration<?> config = TestUtil.getConfigurationUnderTest();
		config.addMapping(
				getStream(
						"/org/hibernate/beanvalidation/tck/tests/xmlconfiguration/order-constraints-XmlConfigurationTest.xml"
				)
		);
		Validator validator = config.buildValidatorFactory().getValidator();

		assertTrue(
				validator.getConstraintsForClass( Order.class ).isBeanConstrained(),
				"With additional mapping Order should be constrained"
		);
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONSTRAINTDECLARATION, id = "a"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONSTRAINTDECLARATION, id = "d"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONSTRAINTDECLARATION, id = "e"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONSTRAINTDECLARATION, id = "f"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONSTRAINTDECLARATION, id = "g"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONSTRAINTDECLARATION, id = "h"),
			@SpecAssertion(section = Sections.XML_MAPPING_TYPECONVERSION, id = "a"),
			@SpecAssertion(section = Sections.XML_MAPPING_TYPECONVERSION, id = "b"),
			@SpecAssertion(section = Sections.XML_MAPPING_TYPECONVERSION, id = "c"),
			@SpecAssertion(section = Sections.XML_MAPPING_TYPECONVERSION, id = "d"),
			@SpecAssertion(section = Sections.XML_MAPPING_TYPECONVERSION, id = "e"),
			@SpecAssertion(section = Sections.XML_MAPPING_TYPECONVERSION, id = "f"),
			@SpecAssertion(section = Sections.XML_MAPPING_TYPECONVERSION, id = "g"),
			@SpecAssertion(section = Sections.XML_MAPPING_TYPECONVERSION, id = "h"),
			@SpecAssertion(section = Sections.XML_MAPPING_TYPECONVERSION, id = "i"),
			@SpecAssertion(section = Sections.XML_MAPPING_TYPECONVERSION, id = "j"),
			@SpecAssertion(section = Sections.XML_MAPPING_TYPECONVERSION, id = "k")
	})
	public void testElementConversionInXmlConfiguredConstraint() {
		BeanDescriptor beanDescriptor = getValidator().getConstraintsForClass( User.class );
		assertTrue( beanDescriptor.isBeanConstrained() );

		Set<ConstraintDescriptor<?>> constraintDescriptors = beanDescriptor.getConstraintDescriptors();
		assertTrue( constraintDescriptors.size() == 1 );

		ConstraintDescriptor<?> descriptor = constraintDescriptors.iterator().next();
		assertEquals( descriptor.getMessageTemplate(), "Message from xml" );
		assertEquals( descriptor.getGroups(), TestUtil.<Class<?>>asSet( TestGroup.class ) );
		assertEquals( descriptor.getPayload(), TestUtil.<Class<?>>asSet( Error.class ) );

		ConsistentUserInformation constraintAnnotation = (ConsistentUserInformation) descriptor.getAnnotation();

		assertEquals( constraintAnnotation.byteParam(), Byte.MAX_VALUE, "Wrong parameter value" );
		assertEquals( constraintAnnotation.shortParam(), Short.MAX_VALUE, "Wrong parameter value" );
		assertEquals( constraintAnnotation.intParam(), Integer.MAX_VALUE, "Wrong parameter value" );
		assertEquals( constraintAnnotation.longParam(), Long.MAX_VALUE, "Wrong parameter value" );
		assertEquals( constraintAnnotation.floatParam(), Float.MAX_VALUE, "Wrong parameter value" );
		assertEquals( constraintAnnotation.doubleParam(), Double.MAX_VALUE, "Wrong parameter value" );
		assertEquals( constraintAnnotation.booleanParam(), true, "Wrong parameter value" );
		assertEquals( constraintAnnotation.charParam(), 'A', "Wrong parameter value" );

		assertEquals( constraintAnnotation.stringParam(), "foobar", "Wrong parameter value" );
		assertEquals( constraintAnnotation.classParam(), String.class, "Wrong parameter value" );
		assertEquals( constraintAnnotation.unqualifiedClassParam(), UserType.class, "Wrong parameter value" );

		assertEquals( constraintAnnotation.userType(), UserType.SELLER, "Wrong parameter value" );

		assertEquals( constraintAnnotation.stringArrayParam(), new String[] { "foo", "bar" }, "Wrong parameter value" );

		assertEquals( constraintAnnotation.max().value(), 10, "Wrong parameter value. Default should be used" );
		assertEquals( constraintAnnotation.patterns().length, 2, "Wrong array size" );
	}

	@Test(expectedExceptions = ValidationException.class)
	@SpecAssertion(section = Sections.XML_MAPPING_TYPECONVERSION, id = "l")
	public void testIllegalAnnotationValueInXmlMappingCausesException() {
		Configuration<?> config = TestUtil.getConfigurationUnderTest();
		config.addMapping( getStream( "superuser-constraints.xml" ) );
		Validator validator = config.buildValidatorFactory().getValidator();

		validator.getConstraintsForClass( SuperUser.class );
	}

	private InputStream getStream(String fileName) {
		return this.getClass().getResourceAsStream( fileName );
	}
}
