/*
* JBoss, Home of Professional Open Source
* Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual contributors
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
package org.hibernate.beanvalidation.tck.tests.methodvalidation.constructorvalidation;

import java.util.List;
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.ConstructorDescriptor;
import javax.validation.metadata.GroupConversionDescriptor;
import javax.validation.metadata.ParameterDescriptor;
import javax.validation.metadata.ReturnValueDescriptor;

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
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "1.1.0")
public class ConstructorValidationTest extends Arquillian {

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClassPackage( ConstructorValidationTest.class )
				.withValidationXml( "validation-ConstructorValidationTest.xml" )
				.withResource( "customer-repository-constraints-ConstructorValidationTest.xml" )
				.build();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "8.1.1.4", id = "a"),
			@SpecAssertion(section = "8.1.1.4", id = "c"),
			@SpecAssertion(section = "8.1.1.4", id = "e")
	})
	public void testXmlConfiguredConstructors() throws Exception {
		ConstructorDescriptor descriptor = TestUtil.getConstructorDescriptor( CustomerRepository.class );
		assertNotNull( descriptor, "the specified constructor should be configured in xml" );
		assertTrue( descriptor.isReturnValueConstrained() );

		descriptor = TestUtil.getConstructorDescriptor( CustomerRepository.class, String.class );
		assertNotNull( descriptor, "the specified constructor should be configured in xml" );
		assertTrue( descriptor.areParametersConstrained() );

		descriptor = TestUtil.getConstructorDescriptor( CustomerRepository.class, CustomerRepository.class );
		assertNotNull( descriptor, "the specified constructor should be configured in xml" );
		assertTrue( descriptor.areParametersConstrained() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "8.1.1.4", id = "b"),
			@SpecAssertion(section = "8.1.1.4", id = "c")
	})
	public void testVarargsConstructorParameter() throws Exception {
		ConstructorDescriptor descriptor = TestUtil.getConstructorDescriptor(
				CustomerRepository.class,
				String.class,
				Customer[].class
		);
		assertNotNull( descriptor, "the specified constructor should be configured in xml" );
		assertTrue( descriptor.areParametersConstrained() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "8.1.1.4", id = "c"),
			@SpecAssertion(section = "8.1.1.4", id = "f"),
			@SpecAssertion(section = "8.1.1.4", id = "j")
	})
	public void tesConstructorCrossParameterParameter() throws Exception {
		ConstructorDescriptor descriptor = TestUtil.getConstructorDescriptor(
				CustomerRepository.class,
				CustomerRepository.class,
				CustomerRepository.class
		);
		assertNotNull( descriptor, "the specified constructor should be configured in xml" );
		assertTrue( descriptor.getCrossParameterDescriptor().hasConstraints() );
	}

	@Test
	@SpecAssertion(section = "8.1.1.4", id = "g")
	public void testConstraintOnConstructorReturnValueAndParameter() throws Exception {
		ConstructorDescriptor descriptor = TestUtil.getConstructorDescriptor(
				CustomerRepository.class,
				String.class
		);
		assertNotNull( descriptor, "the specified constructor should be configured in xml" );

		ReturnValueDescriptor returnValueDescriptor = descriptor.getReturnValueDescriptor();
		Set<ConstraintDescriptor<?>> constraintDescriptors = returnValueDescriptor.getConstraintDescriptors();
		assertTrue( constraintDescriptors.size() == 1 );

		ConstraintDescriptor<?> constraintDescriptor = constraintDescriptors.iterator().next();
		assertEquals(
				constraintDescriptor.getAnnotation().annotationType(),
				NotNull.class,
				"Unexpected constraint type"
		);

		List<ParameterDescriptor> parameterDescriptors = descriptor.getParameterDescriptors();
		assertTrue( parameterDescriptors.size() == 1 );

		ParameterDescriptor parameterDescriptor = parameterDescriptors.get( 0 );
		constraintDescriptors = parameterDescriptor.getConstraintDescriptors();
		assertTrue( constraintDescriptors.size() == 1 );

		constraintDescriptor = constraintDescriptors.iterator().next();
		assertEquals(
				constraintDescriptor.getAnnotation().annotationType(),
				NotNull.class,
				"Unexpected constraint type"
		);
	}

	@Test
	@SpecAssertion(section = "8.1.1.4", id = "h")
	public void testCascadingOnReturnValueAndParameter() throws Exception {
		ConstructorDescriptor descriptor = TestUtil.getConstructorDescriptor(
				CustomerRepository.class,
				CustomerRepository.class
		);
		assertNotNull( descriptor, "the specified constructor should be configured in xml" );

		ReturnValueDescriptor returnValueDescriptor = descriptor.getReturnValueDescriptor();
		assertTrue(returnValueDescriptor.isCascaded(), "<valid/> is used to configure cascading");


		List<ParameterDescriptor> parameterDescriptors = descriptor.getParameterDescriptors();
		assertTrue( parameterDescriptors.size() == 1 );

		ParameterDescriptor parameterDescriptor = parameterDescriptors.get( 0 );
		assertTrue(parameterDescriptor.isCascaded(), "<valid/> is used to configure cascading");
	}

	@Test
	@SpecAssertion(section = "8.1.1.4", id = "i")
	public void testGroupConversionOnReturnValueAndParameter() throws Exception {
		ConstructorDescriptor descriptor = TestUtil.getConstructorDescriptor(
				CustomerRepository.class,
				CustomerRepository.class
		);
		assertNotNull( descriptor, "the specified constructor should be configured in xml" );

		ReturnValueDescriptor returnValueDescriptor = descriptor.getReturnValueDescriptor();
		Set<GroupConversionDescriptor> groupConversionDescriptors = returnValueDescriptor.getGroupConversions();
		assertTrue( groupConversionDescriptors.size() == 1 );

		GroupConversionDescriptor groupConversionDescriptor = groupConversionDescriptors.iterator().next();
		assertEquals( groupConversionDescriptor.getFrom(), Default.class, "Wrong from class for group conversion" );

		List<ParameterDescriptor> parameterDescriptors = descriptor.getParameterDescriptors();
		assertTrue( parameterDescriptors.size() == 1 );

		ParameterDescriptor parameterDescriptor = parameterDescriptors.get( 0 );
		groupConversionDescriptors = parameterDescriptor.getGroupConversions();
		assertTrue( groupConversionDescriptors.size() == 1 );

		groupConversionDescriptor = groupConversionDescriptors.iterator().next();
		assertEquals( groupConversionDescriptor.getFrom(), Default.class, "Wrong from class for group conversion" );
	}
}
