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
package org.hibernate.jsr303.tck.tests.xmlconfiguration.constraintdefinition;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;
import javax.validation.Configuration;
import javax.validation.ConstraintValidator;
import javax.validation.Validator;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.PropertyDescriptor;

import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.testharness.AbstractTest;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ArtifactType;
import org.jboss.testharness.impl.packaging.Classes;
import org.jboss.testharness.impl.packaging.Resource;
import org.jboss.testharness.impl.packaging.Resources;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

import org.hibernate.jsr303.tck.util.TestUtil;

/**
 * @author Hardy Ferentschik
 */
@Artifact(artifactType = ArtifactType.JSR303)
@Classes({ TestUtil.class, TestUtil.PathImpl.class, TestUtil.NodeImpl.class })
@Resources({
		@Resource(source = XmlConfiguredConstraintValidatorTest.mappingFile1,
				destination = "WEB-INF/classes" + XmlConfiguredConstraintValidatorTest.packageName + XmlConfiguredConstraintValidatorTest.mappingFile1),
		@Resource(source = XmlConfiguredConstraintValidatorTest.mappingFile2,
				destination = "WEB-INF/classes" + XmlConfiguredConstraintValidatorTest.packageName + XmlConfiguredConstraintValidatorTest.mappingFile2)
})
public class XmlConfiguredConstraintValidatorTest extends AbstractTest {

	public final static String packageName = "/org/hibernate/jsr303/tck/tests/xmlconfiguration/constraintdefinition/";
	public final static String mappingFile1 = "constraint-definition-ExludeExistingValidatorsTest.xml";
	public final static String mappingFile2 = "constraint-definition-IncludeExistingValidatorsTest.xml";

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "7.1.2", id = "b"),
			@SpecAssertion(section = "7.1.2", id = "e")
	})
	public <T extends Annotation> void testExcludeExistingValidators() {
		Configuration<?> config = TestUtil.getConfigurationUnderTest();
		config.addMapping( TestUtil.getInputStreamForPath( packageName + mappingFile1 ) );
		Validator validator = config.buildValidatorFactory().getValidator();

		PropertyDescriptor propDescriptor = validator.getConstraintsForClass( Name.class )
				.getConstraintsForProperty( "name" );

		Set<ConstraintDescriptor<?>> descriptors = propDescriptor.getConstraintDescriptors();
		assertEquals( descriptors.size(), 1, "There should only be one constraint." );

		@SuppressWarnings("unchecked")
		ConstraintDescriptor<T> descriptor = ( ConstraintDescriptor<T> ) descriptors.iterator().next();
		List<Class<? extends ConstraintValidator<T, ?>>> validators = descriptor.getConstraintValidatorClasses();

		assertEquals(
				validators.size(),
				0,
				"No xml defined validator and annotations are ignored -> no validator"
		);
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "7.1.2", id = "c"),
			@SpecAssertion(section = "7.1.2", id = "d"),
			@SpecAssertion(section = "7.1.2", id = "e")
	})
	public <T extends Annotation> void testIncludeExistingValidators() {
		Configuration<?> config = TestUtil.getConfigurationUnderTest();
		config.addMapping( TestUtil.getInputStreamForPath( packageName + mappingFile2 ) );
		Validator validator = config.buildValidatorFactory().getValidator();

		PropertyDescriptor propDescriptor = validator.getConstraintsForClass( Name.class )
				.getConstraintsForProperty( "name" );


		Set<ConstraintDescriptor<?>> descriptors = propDescriptor.getConstraintDescriptors();
		assertEquals( descriptors.size(), 1, "There should only be one constraint." );

		@SuppressWarnings("unchecked")
		ConstraintDescriptor<T> descriptor = ( ConstraintDescriptor<T> ) descriptors.iterator().next();
		List<Class<? extends ConstraintValidator<T, ?>>> validators = descriptor.getConstraintValidatorClasses();


		assertEquals(
				validators.size(),
				2,
				"One validator should be defined in annotation and one in xml"
		);
	}
}
