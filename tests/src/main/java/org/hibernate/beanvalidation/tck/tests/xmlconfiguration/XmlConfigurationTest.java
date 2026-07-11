/*
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertNoViolations;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;

import java.io.InputStream;
import java.util.Set;

import jakarta.validation.Configuration;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Payload;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.metadata.BeanDescriptor;
import jakarta.validation.metadata.ConstraintDescriptor;

import org.assertj.core.api.Assertions;
import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.junit.jupiter.api.Test;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
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
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "r"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "s"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML, id = "a"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML, id = "b"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDEFINITION, id = "a")
	})
	public void testClassConstraintDefinedInXml() {
		User user = new User();
		Set<ConstraintViolation<User>> constraintViolations = getValidator().validate( user, TestGroup.class );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( ConsistentUserInformation.class ).withMessage( "Message from xml" )
		);

		ConstraintViolation<User> constraintViolation = constraintViolations.iterator().next();
		Set<Class<? extends Payload>> payloads = constraintViolation.getConstraintDescriptor().getPayload();
		Assertions.assertThat( payloads.size() == 1 ).as( "One one payload class is defined in xml" ).isTrue();
		Assertions.assertThat( Error.class.equals( payloads.iterator().next() ) ).isTrue();

		user.setConsistent( true );
		constraintViolations = getValidator().validate( user );
		assertNoViolations( constraintViolations );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "a"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "r"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "s"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML, id = "a"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML, id = "b"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDEFINITION, id = "a")
	})
	public void testIgnoreValidationXml() {
		Configuration<?> config = TestUtil.getConfigurationUnderTest();
		//TODO: is this needed? this validator is not used later.
		Validator validator = config.ignoreXmlConfiguration().buildValidatorFactory().getValidator();

		Order order = new Order();
		Set<ConstraintViolation<Order>> constraintViolations = getValidator().validate( order );
		assertNoViolations( constraintViolations );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "a"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "r"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "s"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML, id = "a"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML, id = "b"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDEFINITION, id = "a")
	})
	public void testPropertyConstraintDefinedInXml() {
		User user = new User();
		user.setConsistent( true );
		user.setFirstname( "Wolfeschlegelsteinhausenbergerdorff" );

		Set<ConstraintViolation<User>> constraintViolations = getValidator().validate( user );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Size.class ).withMessage( "Size is limited!" )
		);

		user.setFirstname( "Wolfgang" );
		constraintViolations = getValidator().validate( user );
		assertNoViolations( constraintViolations );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "a"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "r"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "s"),
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
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Pattern.class ).withMessage( "Last name has to start with with a capital letter." )
		);

		user.setLastname( "Doe" );
		constraintViolations = getValidator().validate( user );
		assertNoViolations( constraintViolations );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "a"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "r"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "s"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML, id = "a"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML, id = "b"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDEFINITION, id = "a")
	})
	public void testAnnotationDefinedConstraintApplies() {
		User user = new User();
		user.setConsistent( true );
		user.setPhoneNumber( "police" );

		Set<ConstraintViolation<User>> constraintViolations = getValidator().validate( user );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Pattern.class ).withMessage( "A phone number can only contain numbers, whitespaces and dashes." )
		);

		user.setPhoneNumber( "112" );
		constraintViolations = getValidator().validate( user );
		assertNoViolations( constraintViolations );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "a"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "r"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "s"),
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
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Pattern.class ).withMessage( "Not a credit card number." )
		);

		card.setNumber( "1234567890" );
		constraintViolations = getValidator().validate( user );
		assertNoViolations( constraintViolations );
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "t")
	public void testMappingFilesAddedViaConfigurationGetAddedToXmlConfiguredMappings() {
		Assertions.assertThat( getValidator().getConstraintsForClass( Order.class ).isBeanConstrained() ).as( "Without additional mapping Order should be unconstrained" ).isFalse();

		Configuration<?> config = TestUtil.getConfigurationUnderTest();
		config.addMapping(
				getStream(
						"/org/hibernate/beanvalidation/tck/tests/xmlconfiguration/order-constraints-XmlConfigurationTest.xml"
				)
		);
		Validator validator = config.buildValidatorFactory().getValidator();

		Assertions.assertThat( validator.getConstraintsForClass( Order.class ).isBeanConstrained() ).as( "With additional mapping Order should be constrained" ).isTrue();
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
		Assertions.assertThat( beanDescriptor.isBeanConstrained() ).isTrue();

		Set<ConstraintDescriptor<?>> constraintDescriptors = beanDescriptor.getConstraintDescriptors();
		Assertions.assertThat( constraintDescriptors.size() == 1 ).isTrue();

		ConstraintDescriptor<?> descriptor = constraintDescriptors.iterator().next();
		Assertions.assertThat( descriptor.getMessageTemplate() ).isEqualTo( "Message from xml" );
		Assertions.assertThat( descriptor.getGroups() ).isEqualTo( TestUtil.<Class<?>>asSet( TestGroup.class ) );
		Assertions.assertThat( descriptor.getPayload() ).isEqualTo( TestUtil.<Class<?>>asSet( Error.class  ) );

		ConsistentUserInformation constraintAnnotation = (ConsistentUserInformation) descriptor.getAnnotation();

		Assertions.assertThat( constraintAnnotation.byteParam() ).as( "Wrong parameter value" ).isEqualTo( Byte.MAX_VALUE );
		Assertions.assertThat( constraintAnnotation.shortParam() ).as( "Wrong parameter value" ).isEqualTo( Short.MAX_VALUE );
		Assertions.assertThat( constraintAnnotation.intParam() ).as( "Wrong parameter value" ).isEqualTo( Integer.MAX_VALUE );
		Assertions.assertThat( constraintAnnotation.longParam() ).as( "Wrong parameter value" ).isEqualTo( Long.MAX_VALUE );
		Assertions.assertThat( constraintAnnotation.floatParam() ).as( "Wrong parameter value" ).isEqualTo( Float.MAX_VALUE );
		Assertions.assertThat( constraintAnnotation.doubleParam() ).as( "Wrong parameter value" ).isEqualTo( Double.MAX_VALUE );
		Assertions.assertThat( constraintAnnotation.booleanParam() ).as( "Wrong parameter value" ).isEqualTo( true );
		Assertions.assertThat(  constraintAnnotation.charParam() ).as( "Wrong parameter value" ).isEqualTo( 'A' );

		Assertions.assertThat(  constraintAnnotation.stringParam() ).as( "Wrong parameter value" ).isEqualTo( "foobar" );
		Assertions.assertThat(  constraintAnnotation.classParam() ).as( "Wrong parameter value" ).isEqualTo( String.class );
		Assertions.assertThat(  constraintAnnotation.unqualifiedClassParam() ).as( "Wrong parameter value" ).isEqualTo( UserType.class );

		Assertions.assertThat(  constraintAnnotation.userType() ).as( "Wrong parameter value" ).isEqualTo( UserType.SELLER );

		Assertions.assertThat( constraintAnnotation.stringArrayParam() ).as( "Wrong parameter value" ).isEqualTo( new String[] { "foo", "bar" } );

		Assertions.assertThat(  constraintAnnotation.max().value() ).as( "Wrong parameter value. Default should be used" ).isEqualTo( 10 );
		Assertions.assertThat(  constraintAnnotation.patterns().length ).as( "Wrong array size" ).isEqualTo( 2 );
	}

	@Test
	@SpecAssertion(section = Sections.XML_MAPPING_TYPECONVERSION, id = "l")
	public void testIllegalAnnotationValueInXmlMappingCausesException() {
		Assertions.assertThatThrownBy( () -> {

			Configuration<?> config = TestUtil.getConfigurationUnderTest();
			config.addMapping( getStream( "superuser-constraints.xml" ) );
			Validator validator = config.buildValidatorFactory().getValidator();

			validator.getConstraintsForClass( SuperUser.class );
	
		} ).isInstanceOf( ValidationException.class );
	}

	private InputStream getStream(String fileName) {
		return this.getClass().getResourceAsStream( fileName );
	}
}
