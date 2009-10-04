// $Id$
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
package org.hibernate.jsr303.tck.tests.xmlconfiguration.constraintdeclaration;

import java.util.Set;
import javax.validation.Configuration;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import javax.validation.metadata.BeanDescriptor;

import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.testharness.AbstractTest;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ArtifactType;
import org.jboss.testharness.impl.packaging.Classes;
import org.jboss.testharness.impl.packaging.Resource;
import org.jboss.testharness.impl.packaging.jsr303.ValidationXml;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.Test;

import org.hibernate.jsr303.tck.util.TestUtil;
import static org.hibernate.jsr303.tck.util.TestUtil.assertCorrectConstraintViolationMessages;
import static org.hibernate.jsr303.tck.util.TestUtil.assertCorrectNumberOfViolations;

/**
 * @author Hardy Ferentschik
 */
@Artifact(artifactType = ArtifactType.JSR303)
@Classes({ TestUtil.class, TestUtil.PathImpl.class, TestUtil.NodeImpl.class })
@ValidationXml(value = "validation-ConfigurationViaXmlAndAnnotationsTest.xml")
@Resource(source = "package-constraints-ConfigurationViaXmlAndAnnotationsTest.xml",
		destination = "WEB-INF/classes/org/hibernate/jsr303/tck/tests/xmlconfiguration/constraintdeclaration/package-constraints-ConfigurationViaXmlAndAnnotationsTest.xml")
public class ConfigurationViaXmlAndAnnotationsTest extends AbstractTest {

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "7.1.1", id = "e"),
			@SpecAssertion(section = "7.1.1", id = "a")
	}
	)
	public void testEntityConfiguredViaAnnotationsAndXml() {
		Validator validator = TestUtil.getValidatorUnderTest();
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( Package.class );
		assertTrue( beanDescriptor.isBeanConstrained(), "The bean should be constrained" );

		assertEquals(
				beanDescriptor.getConstraintsForProperty( "maxWeight" ).getConstraintDescriptors().size(),
				2,
				"With xml configuration there should be two constraints."
		);

		Configuration<?> config = TestUtil.getConfigurationUnderTest();
		config.ignoreXmlConfiguration();
		validator = config.buildValidatorFactory().getValidator();
		beanDescriptor = validator.getConstraintsForClass( Package.class );

		assertTrue(
				beanDescriptor.isBeanConstrained(),
				"Without xml there should be only one constraint."
		);

		assertEquals(
				beanDescriptor.getConstraintsForProperty( "maxWeight" ).getConstraintDescriptors().size(),
				1,
				"Without xml there should be only one constraint."
		);
	}

	@Test
	@SpecAssertion(section = "7.1.1.1", id = "e")
	public void testDefaultGroupDefinitionDefinedInEntityApplies() {
		Validator validator = TestUtil.getValidatorUnderTest();
		Package p = new Package();
		p.setMaxWeight(30);
		Set<ConstraintViolation<Package>> violations = validator.validate( p, Default.class );

		assertCorrectNumberOfViolations( violations, 1 );
		assertCorrectConstraintViolationMessages( violations, "ValidPackage defined as annotation" );
	}
}
