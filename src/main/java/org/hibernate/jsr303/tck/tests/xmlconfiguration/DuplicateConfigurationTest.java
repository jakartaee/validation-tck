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
package org.hibernate.jsr303.tck.tests.xmlconfiguration;


import java.util.Set;
import javax.validation.Configuration;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.constraints.Pattern;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstraintDescriptor;

import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.testharness.AbstractTest;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ArtifactType;
import org.jboss.testharness.impl.packaging.Classes;
import org.jboss.testharness.impl.packaging.Resource;
import org.jboss.testharness.impl.packaging.Resources;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;
import org.testng.annotations.Test;

import org.hibernate.jsr303.tck.util.TestUtil;

/**
 * @author Hardy Ferentschik
 */
@Artifact(artifactType = ArtifactType.JSR303)
@Classes({ TestUtil.class, TestUtil.PathImpl.class, TestUtil.NodeImpl.class })
@Resources(
		{
				@Resource(source = DuplicateConfigurationTest.mappingFile1,
						destination = "WEB-INF/classes" + DuplicateConfigurationTest.packageName + DuplicateConfigurationTest.mappingFile1),
				@Resource(source = DuplicateConfigurationTest.mappingFile2,
						destination = "WEB-INF/classes" + DuplicateConfigurationTest.packageName + DuplicateConfigurationTest.mappingFile2),
				@Resource(source = DuplicateConfigurationTest.mappingFile3,
						destination = "WEB-INF/classes" + DuplicateConfigurationTest.packageName + DuplicateConfigurationTest.mappingFile3),
				@Resource(source = DuplicateConfigurationTest.mappingFile4,
						destination = "WEB-INF/classes" + DuplicateConfigurationTest.packageName + DuplicateConfigurationTest.mappingFile4)
		}
)
public class DuplicateConfigurationTest extends AbstractTest {

	public final static String packageName = "/org/hibernate/jsr303/tck/tests/xmlconfiguration/";
	public final static String mappingFile1 = "user-constraints.xml";
	public final static String mappingFile2 = "user-constraints-MultipleBeanDefinitionTest.xml";
	public final static String mappingFile3 = "user-constraints-MultipleFieldDefinitionTest.xml";
	public final static String mappingFile4 = "user-constraints-MultipleGetterDefinitionTest.xml";


	@Test
	@SpecAssertion(section = "7.1", id = "a")
	public void testXmlConfiguredConstraintExposesCorrespondingAnnotationViaMetadata() {
		Configuration<?> config = TestUtil.getConfigurationUnderTest();
		config.addMapping( TestUtil.getInputStreamForPath( packageName + mappingFile1 ) );
		Validator validator = config.buildValidatorFactory().getValidator();


		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( User.class );
		Set<ConstraintDescriptor<?>> constraintDescriptors = beanDescriptor.getConstraintDescriptors();
		assertEquals( constraintDescriptors.size(), 1, "There should be one class level constraint defined in xml" );

		ConstraintDescriptor<?> descriptor = constraintDescriptors.iterator().next();
		assertTrue( descriptor.getAnnotation() instanceof ConsistentUserInformation );


		constraintDescriptors = beanDescriptor.getConstraintsForProperty( "lastname" )
				.getConstraintDescriptors();
		assertEquals( constraintDescriptors.size(), 1, "There should be one constraint defined in xml for 'lastname'" );
		descriptor = constraintDescriptors.iterator().next();
		assertTrue( descriptor.getAnnotation() instanceof Pattern );
	}


	@Test
	@SpecAssertions({
			@SpecAssertion(section = "7.1", id = "b"),
			@SpecAssertion(section = "7.1", id = "e")
	})
	public void testBeanCannotBeDescribedMoreThanOnce() {
		try {
			Configuration<?> config = TestUtil.getConfigurationUnderTest();
			config.addMapping( TestUtil.getInputStreamForPath( packageName + mappingFile1 ) );
			config.addMapping( TestUtil.getInputStreamForPath( packageName + mappingFile2 ) );
			config.buildValidatorFactory().getValidator();
			fail( "You should not be able to define the same bean multiple times." );
		}
		catch ( ValidationException e ) {
			// success
		}
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "7.1", id = "c"),
			@SpecAssertion(section = "7.1", id = "e")
	})
	public void testFieldMappingCannotOccurMoreThanOnce() {
		try {
			Configuration<?> config = TestUtil.getConfigurationUnderTest();
			config.addMapping( TestUtil.getInputStreamForPath( packageName + mappingFile3 ) );
			config.buildValidatorFactory().getValidator();
			fail( "You should not be able to define multiple field mappings per entity" );
		}
		catch ( ValidationException e ) {
			// success
		}
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "7.1", id = "d"),
			@SpecAssertion(section = "7.1", id = "e")
	})
	public void testGetterMappingCannotOccurMoreThanOnce() {
		try {
			Configuration<?> config = TestUtil.getConfigurationUnderTest();
			config.addMapping( TestUtil.getInputStreamForPath( packageName + mappingFile4 ) );
			config.buildValidatorFactory().getValidator();
			fail( "You should not be able to define multiple getter mappings per entity" );
		}
		catch ( ValidationException e ) {
			// success
		}
	}
}
