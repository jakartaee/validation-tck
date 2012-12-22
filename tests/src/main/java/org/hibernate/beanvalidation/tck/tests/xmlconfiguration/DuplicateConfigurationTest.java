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

import java.util.Set;
import javax.validation.Configuration;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.constraints.Pattern;
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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "1.1.0")
public class DuplicateConfigurationTest extends Arquillian {

	public final static String packageName = "/org/hibernate/beanvalidation/tck/tests/xmlconfiguration/";
	public final static String mappingFile1 = "user-constraints.xml";
	public final static String mappingFile2 = "user-constraints-MultipleBeanDefinitionTest.xml";
	public final static String mappingFile3 = "user-constraints-MultipleFieldDefinitionTest.xml";
	public final static String mappingFile4 = "user-constraints-MultipleGetterDefinitionTest.xml";

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClass( DuplicateConfigurationTest.class )
				.withClasses(
						User.class,
						UserType.class,
						Error.class,
						CreditCard.class,
						Optional.class,
						ConsistentUserInformation.class,
						CustomConsistentUserValidator.class,
						ConsistentUserValidator.class,
						TestGroup.class
				)
				.withResource( DuplicateConfigurationTest.mappingFile1 )
				.withResource( DuplicateConfigurationTest.mappingFile2 )
				.withResource( DuplicateConfigurationTest.mappingFile3 )
				.withResource( DuplicateConfigurationTest.mappingFile4 )
				.build();
	}


	@Test
	@SpecAssertion(section = "8.1", id = "a")
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
			@SpecAssertion(section = "8.1", id = "b"),
			@SpecAssertion(section = "8.1", id = "e")
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
			@SpecAssertion(section = "8.1", id = "c"),
			@SpecAssertion(section = "8.1", id = "e")
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
			@SpecAssertion(section = "8.1", id = "d"),
			@SpecAssertion(section = "8.1", id = "e")
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
