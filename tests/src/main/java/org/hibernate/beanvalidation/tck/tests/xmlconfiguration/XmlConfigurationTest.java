/*
* JBoss, Home of Professional Open Source
* Copyright 2009, Red Hat, Inc. and/or its affiliates, and individual contributors
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* http://www.apache.org/licenses/LICENSE-2.0
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration;

import java.io.InputStream;
import java.util.Set;
import javax.validation.Configuration;
import javax.validation.ConstraintViolation;
import javax.validation.Payload;
import javax.validation.Validator;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstraintDescriptor;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectConstraintViolationMessages;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectNumberOfViolations;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "1.1.0")
public class XmlConfigurationTest extends Arquillian {

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
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
						TestGroup.class
				)
				.withValidationXml( "validation-XmlConfigurationTest.xml" )
				.withResource( "user-constraints.xml" )
				.withResource( "order-constraints.xml" )
				.withResource( "order-constraints-XmlConfigurationTest.xml" )
				.build();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.5.6", id = "a"),
			@SpecAssertion(section = "5.5.6", id = "n"),
			@SpecAssertion(section = "5.5.6", id = "m"),
			@SpecAssertion(section = "8.1.1", id = "a"),
			@SpecAssertion(section = "8.1.1", id = "b"),
			@SpecAssertion(section = "8.1.2", id = "a")
	})
	public void testClassConstraintDefinedInXml() {
		Validator validator = TestUtil.getValidatorUnderTest();

		User user = new User();
		Set<ConstraintViolation<User>> constraintViolations = validator.validate( user );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintViolationMessages(
				constraintViolations, "Message from xml"
		);

		ConstraintViolation<User> constraintViolation = constraintViolations.iterator().next();
		Set<Class<? extends Payload>> payloads = constraintViolation.getConstraintDescriptor().getPayload();
		assertTrue( payloads.size() == 1, "One one payload class is defined in xml" );
		assertTrue( Error.class.equals( payloads.iterator().next() ) );

		user.setConsistent( true );
		constraintViolations = validator.validate( user );
		assertCorrectNumberOfViolations( constraintViolations, 0 );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.5.6", id = "a"),
			@SpecAssertion(section = "5.5.6", id = "n"),
			@SpecAssertion(section = "5.5.6", id = "m"),
			@SpecAssertion(section = "8.1.1", id = "a"),
			@SpecAssertion(section = "8.1.1", id = "b"),
			@SpecAssertion(section = "8.1.2", id = "a")
	})
	public void testIgnoreValidationXml() {
		Configuration<?> config = TestUtil.getConfigurationUnderTest();
		Validator validator = config.ignoreXmlConfiguration().buildValidatorFactory().getValidator();

		Order order = new Order();
		Set<ConstraintViolation<Order>> constraintViolations = validator.validate( order );
		assertCorrectNumberOfViolations( constraintViolations, 0 );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.5.6", id = "a"),
			@SpecAssertion(section = "5.5.6", id = "n"),
			@SpecAssertion(section = "5.5.6", id = "m"),
			@SpecAssertion(section = "8.1.1", id = "a"),
			@SpecAssertion(section = "8.1.1", id = "b"),
			@SpecAssertion(section = "8.1.2", id = "a")
	})
	public void testPropertyConstraintDefinedInXml() {
		Validator validator = TestUtil.getValidatorUnderTest();

		User user = new User();
		user.setConsistent( true );
		user.setFirstname( "Wolfeschlegelsteinhausenbergerdorff" );

		Set<ConstraintViolation<User>> constraintViolations = validator.validate( user );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintViolationMessages( constraintViolations, "Size is limited!" );

		user.setFirstname( "Wolfgang" );
		constraintViolations = validator.validate( user );
		assertCorrectNumberOfViolations( constraintViolations, 0 );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.5.6", id = "a"),
			@SpecAssertion(section = "5.5.6", id = "n"),
			@SpecAssertion(section = "5.5.6", id = "m"),
			@SpecAssertion(section = "8.1.1", id = "a"),
			@SpecAssertion(section = "8.1.1", id = "b"),
			@SpecAssertion(section = "8.1.2", id = "a")
	})
	public void testFieldConstraintDefinedInXml() {
		Validator validator = TestUtil.getValidatorUnderTest();

		User user = new User();
		user.setConsistent( true );
		user.setFirstname( "Wolfgang" );
		user.setLastname( "doe" );

		Set<ConstraintViolation<User>> constraintViolations = validator.validate( user );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintViolationMessages(
				constraintViolations, "Last name has to start with with a capital letter."
		);

		user.setLastname( "Doe" );
		constraintViolations = validator.validate( user );
		assertCorrectNumberOfViolations( constraintViolations, 0 );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.5.6", id = "a"),
			@SpecAssertion(section = "5.5.6", id = "n"),
			@SpecAssertion(section = "5.5.6", id = "m"),
			@SpecAssertion(section = "8.1.1", id = "a"),
			@SpecAssertion(section = "8.1.1", id = "b"),
			@SpecAssertion(section = "8.1.2", id = "a")
	})
	public void testAnnotationDefinedConstraintApplies() {
		Validator validator = TestUtil.getValidatorUnderTest();

		User user = new User();
		user.setConsistent( true );
		user.setPhoneNumber( "police" );

		Set<ConstraintViolation<User>> constraintViolations = validator.validate( user );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintViolationMessages(
				constraintViolations, "A phone number can only contain numbers, whitespaces and dashes."
		);

		user.setPhoneNumber( "112" );
		constraintViolations = validator.validate( user );
		assertCorrectNumberOfViolations( constraintViolations, 0 );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.5.6", id = "a"),
			@SpecAssertion(section = "5.5.6", id = "n"),
			@SpecAssertion(section = "5.5.6", id = "m"),
			@SpecAssertion(section = "8.1.1", id = "a"),
			@SpecAssertion(section = "8.1.1", id = "b"),
			@SpecAssertion(section = "8.1.2", id = "a")
	})
	public void testCascadingConfiguredInXml() {
		Validator validator = TestUtil.getValidatorUnderTest();

		User user = new User();
		user.setConsistent( true );
		CreditCard card = new CreditCard();
		card.setNumber( "not a number" );
		user.setCreditcard( card );

		Set<ConstraintViolation<User>> constraintViolations = validator.validate( user );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintViolationMessages( constraintViolations, "Not a credit card number." );

		card.setNumber( "1234567890" );
		constraintViolations = validator.validate( user );
		assertCorrectNumberOfViolations( constraintViolations, 0 );
	}

	@Test
	@SpecAssertion(section = "5.5.6", id = "o")
	public void testMappingFilesAddedViaConfigurationGetAddedToXmlConfiguredMappings() {
		Validator validator = TestUtil.getValidatorUnderTest();

		assertFalse(
				validator.getConstraintsForClass( Order.class ).isBeanConstrained(),
				"Without additional mapping Order should be unconstrained"
		);


		Configuration<?> config = TestUtil.getConfigurationUnderTest();
		config.addMapping(
				getStream(
						"/org/hibernate/beanvalidation/tck/tests/xmlconfiguration/order-constraints-XmlConfigurationTest.xml"
				)
		);
		validator = config.buildValidatorFactory().getValidator();

		assertTrue(
				validator.getConstraintsForClass( Order.class ).isBeanConstrained(),
				"With additional mapping Order should be constrained"
		);
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "8.1.1.6", id = "d"),
			@SpecAssertion(section = "8.1.1.6", id = "e"),
			@SpecAssertion(section = "8.1.1.6", id = "f"),
			@SpecAssertion(section = "8.1.1.6", id = "g"),
			@SpecAssertion(section = "8.1.1.6", id = "h")
	})
	public void testElementConversionInXmlConfiguredConstraint() {
		Validator validator = TestUtil.getValidatorUnderTest();
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( User.class );
		assertTrue( beanDescriptor.isBeanConstrained() );

		Set<ConstraintDescriptor<?>> constraintDescriptors = beanDescriptor.getConstraintDescriptors();
		assertTrue( constraintDescriptors.size() == 1 );

		ConstraintDescriptor<?> descriptor = constraintDescriptors.iterator().next();
		ConsistentUserInformation constraintAnnotation = (ConsistentUserInformation) descriptor.getAnnotation();

		assertEquals( constraintAnnotation.stringParam(), "foobar", "Wrong parameter value" );
		assertEquals( constraintAnnotation.classParam(), String.class, "Wrong parameter value" );
		assertEquals( constraintAnnotation.userType(), UserType.SELLER, "Wrong parameter value" );

		assertEquals( constraintAnnotation.stringArrayParam(), new String[] { "foo", "bar" }, "Wrong parameter value" );

		assertEquals( constraintAnnotation.max().value(), 10, "Wrong parameter value. Default should be used" );
		assertEquals( constraintAnnotation.patterns().length, 2, "Wrong array size" );
	}


	private InputStream getStream(String fileName) {
		return this.getClass().getResourceAsStream( fileName );
	}
}
