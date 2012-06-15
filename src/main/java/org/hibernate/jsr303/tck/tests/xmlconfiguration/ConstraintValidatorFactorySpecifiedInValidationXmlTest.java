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
package org.hibernate.jsr303.tck.tests.xmlconfiguration;

import javax.validation.Configuration;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.testng.annotations.Test;

import org.hibernate.jsr303.tck.util.TestUtil;
import org.hibernate.jsr303.tck.util.shrinkwrap.WebArchiveBuilder;

import static org.testng.Assert.assertTrue;

/**
 * @author Hardy Ferentschik
 */
public class ConstraintValidatorFactorySpecifiedInValidationXmlTest extends Arquillian {

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClass( ConstraintValidatorFactorySpecifiedInValidationXmlTest.class )
				.withClasses(
						User.class,
						Optional.class,
						CreditCard.class,
						ConfigurationDefinedConstraintValidatorFactoryResolver.class,
						XmlDefinedConstraintValidatorFactory.class
				)
				.withValidationXml( "validation-ConstraintValidatorFactorySpecifiedInValidationXmlTest.xml" )
				.build();
	}

	@Test
	@SpecAssertion(section = "4.4.6", id = "i")
	public void testConstraintValidatorFactorySpecifiedInValidationXml() {
		try {
			Validator validator = TestUtil.getValidatorUnderTest();
			validator.validate( new User() );
		}
		catch ( ValidationException e ) {
			assertTrue(
					XmlDefinedConstraintValidatorFactory.numberOfIsReachableCalls > 0,
					"The factory should have been called at least once if it was properly picked up by xml configuration."
			);
		}
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "4.4.6", id = "i"),
			@SpecAssertion(section = "4.4.6", id = "h")
	})
	public void testConstraintValidatorFactorySpecifiedInValidationXmlCanBeOverridden() {
		try {
			Configuration<?> configuration = Validation
					.byDefaultProvider()
					.configure();

			configuration = configuration.constraintValidatorFactory( new ConfigurationDefinedConstraintValidatorFactoryResolver() );
			Validator validator = configuration.buildValidatorFactory().getValidator();
			validator.validate( new User() );
		}
		catch ( ValidationException e ) {
			assertTrue(
					ConfigurationDefinedConstraintValidatorFactoryResolver.numberOfIsReachableCalls > 0,
					"The factory  should have been called at least once if configuration settings were applied."
			);
		}
	}
}
