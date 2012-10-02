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
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.constraintdeclaration;

import javax.validation.Configuration;
import javax.validation.Validator;
import javax.validation.metadata.BeanDescriptor;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "1.1.0")
public class ConstraintDeclarationTest extends Arquillian {

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClass( ConstraintDeclarationTest.class )
				.withClasses( Optional.class, Package.class, PrePosting.class, ValidPackage.class )
				.withValidationXml( "validation-ConstraintDeclarationTest.xml" )
				.withResource( "package-constraints-ConstraintDeclarationTest.xml" )
				.build();
	}

	@Test
	@SpecAssertion(section = "7.1.1", id = "d")
	public void testConstraintAnnotationsArePerDefaultIgnoredForXmlConfiguredEntities() {
		Validator validator = TestUtil.getValidatorUnderTest();
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( Package.class );
		assertFalse( beanDescriptor.isBeanConstrained(), "With xml configuration there should be no constraint." );

		Configuration<?> config = TestUtil.getConfigurationUnderTest();
		config.ignoreXmlConfiguration();
		validator = config.buildValidatorFactory().getValidator();
		beanDescriptor = validator.getConstraintsForClass( Package.class );
		assertTrue(
				beanDescriptor.isBeanConstrained(),
				"If xml configuration is ignored Package should have a single constraint."
		);
	}
}
