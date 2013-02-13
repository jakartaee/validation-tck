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
package org.hibernate.beanvalidation.tck.tests.metadata;

import java.util.List;
import java.util.Set;
import javax.validation.groups.Default;
import javax.validation.metadata.GroupConversionDescriptor;
import javax.validation.metadata.ParameterDescriptor;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.tests.metadata.CustomerService.BasicChecks;
import org.hibernate.beanvalidation.tck.tests.metadata.CustomerService.StrictChecks;
import org.hibernate.beanvalidation.tck.tests.metadata.CustomerService.StrictCustomerServiceChecks;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

/**
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "1.1.0")
public class ParameterDescriptorTest extends Arquillian {

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClass( ParameterDescriptorTest.class )
				.withClasses(
						Account.class,
						Customer.class,
						CustomerService.class,
						Executables.class,
						Person.class
				)
				.build();
	}

	@Test
	@SpecAssertion(section = "6.8", id = "a")
	public void testGetIndexForMethod() {
		List<ParameterDescriptor> parameters = Executables.parameterConstrainedMethod()
				.getParameterDescriptors();

		assertEquals( parameters.get( 0 ).getIndex(), 0, "Wrong parameter index" );
		assertEquals( parameters.get( 1 ).getIndex(), 1, "Wrong parameter index" );
	}

	@Test
	@SpecAssertion(section = "6.8", id = "a")
	public void testGetIndexForConstructor() {
		List<ParameterDescriptor> parameters = Executables.parameterConstrainedConstructor()
				.getParameterDescriptors();

		assertEquals( parameters.get( 0 ).getIndex(), 0, "Wrong parameter index" );
		assertEquals( parameters.get( 1 ).getIndex(), 1, "Wrong parameter index" );
	}

	@Test
	@SpecAssertion(section = "6.8", id = "b")
	public void testGetNameForMethod() {
		List<ParameterDescriptor> parameters = Executables.parameterConstrainedMethod()
				.getParameterDescriptors();

		assertEquals( parameters.get( 0 ).getName(), "arg0", "Wrong parameter name" );
		assertEquals( parameters.get( 1 ).getName(), "arg1", "Wrong parameter name" );
	}

	@Test
	@SpecAssertion(section = "6.8", id = "b")
	public void testGetNameForConstructor() {
		List<ParameterDescriptor> parameters = Executables.parameterConstrainedConstructor()
				.getParameterDescriptors();

		assertEquals( parameters.get( 0 ).getName(), "arg0", "Wrong parameter name" );
		assertEquals( parameters.get( 1 ).getName(), "arg1", "Wrong parameter name" );
	}

	@Test
	@SpecAssertion(section = "6.4", id = "a")
	public void testIsCascadedForMethod() {
		List<ParameterDescriptor> parameters = Executables.parameterConstrainedMethod()
				.getParameterDescriptors();
		assertFalse( parameters.get( 0 ).isCascaded(), "Should not be cascaded" );

		parameters = Executables.cascadedParameterMethod().getParameterDescriptors();
		assertTrue( parameters.get( 0 ).isCascaded(), "Should be cascaded" );
	}

	@Test
	@SpecAssertion(section = "6.4", id = "a")
	public void testIsCascadedForConstructor() {
		List<ParameterDescriptor> parameters = Executables.parameterConstrainedConstructor()
				.getParameterDescriptors();
		assertFalse( parameters.get( 0 ).isCascaded(), "Should not be cascaded" );

		parameters = Executables.cascadedParameterConstructor().getParameterDescriptors();
		assertTrue( parameters.get( 0 ).isCascaded(), "Should be cascaded" );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "6.4", id = "b"),
			@SpecAssertion(section = "6.5", id = "a"),
			@SpecAssertion(section = "6.5", id = "b")
	})
	public void testGetGroupConversionsForConstructorParameter() {
		List<ParameterDescriptor> parameters = Executables.constructorWithGroupConversionOnParameter()
				.getParameterDescriptors();
		Set<GroupConversionDescriptor> groupConversions = parameters.get( 1 ).getGroupConversions();

		assertEquals( groupConversions.size(), 2 );

		for ( GroupConversionDescriptor groupConversionDescriptor : groupConversions ) {
			if ( groupConversionDescriptor.getFrom().equals( Default.class ) ) {
				assertEquals( groupConversionDescriptor.getTo(), BasicChecks.class );
			}
			else if ( groupConversionDescriptor.getFrom().equals( StrictCustomerServiceChecks.class ) ) {
				assertEquals( groupConversionDescriptor.getTo(), StrictChecks.class );
			}
			else {
				fail(
						String.format(
								"Encountered unexpected group conversion from %s to %s",
								groupConversionDescriptor.getFrom().getName(),
								groupConversionDescriptor.getTo().getName()
						)
				);
			}
		}
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "6.4", id = "b"),
			@SpecAssertion(section = "6.5", id = "a"),
			@SpecAssertion(section = "6.5", id = "b")
	})
	public void testGetGroupConversionsForMethodParameter() {
		List<ParameterDescriptor> parameters = Executables.methodWithGroupConversionOnParameter()
				.getParameterDescriptors();
		Set<GroupConversionDescriptor> groupConversions = parameters.get( 0 ).getGroupConversions();

		assertEquals( groupConversions.size(), 2 );

		for ( GroupConversionDescriptor groupConversionDescriptor : groupConversions ) {
			if ( groupConversionDescriptor.getFrom().equals( Default.class ) ) {
				assertEquals( groupConversionDescriptor.getTo(), BasicChecks.class );
			}
			else if ( groupConversionDescriptor.getFrom().equals( StrictCustomerServiceChecks.class ) ) {
				assertEquals( groupConversionDescriptor.getTo(), StrictChecks.class );
			}
			else {
				fail(
						String.format(
								"Encountered unexpected group conversion from %s to %s",
								groupConversionDescriptor.getFrom().getName(),
								groupConversionDescriptor.getTo().getName()
						)
				);
			}
		}
	}

	@Test
	@SpecAssertion(section = "6.4", id = "b")
	public void testGetGroupConversionsReturnsEmptySetForConstructorParameter() {
		ParameterDescriptor parameterDescriptor = Executables.parameterConstrainedConstructor()
				.getParameterDescriptors()
				.get( 0 );
		Set<GroupConversionDescriptor> groupConversions = parameterDescriptor.getGroupConversions();

		assertNotNull( groupConversions );
		assertTrue( groupConversions.isEmpty() );
	}

	@Test
	@SpecAssertion(section = "6.4", id = "b")
	public void testGetGroupConversionsReturnsEmptySetForMethodParameter() {
		ParameterDescriptor parameterDescriptor = Executables.parameterConstrainedMethod()
				.getParameterDescriptors()
				.get( 0 );
		Set<GroupConversionDescriptor> groupConversions = parameterDescriptor.getGroupConversions();

		assertNotNull( groupConversions );
		assertTrue( groupConversions.isEmpty() );
	}
}
