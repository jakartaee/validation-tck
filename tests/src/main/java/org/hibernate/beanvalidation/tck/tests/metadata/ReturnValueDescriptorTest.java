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


import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ElementDescriptor;
import javax.validation.metadata.ElementDescriptor.Kind;
import javax.validation.metadata.ReturnValueDescriptor;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

/**
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "1.1.0")
public class ReturnValueDescriptorTest extends Arquillian {

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClass( ReturnValueDescriptorTest.class )
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
	@SpecAssertion(section = "6.7", id = "a")
	public void testIsCascadedForMethodReturnValue() {
		ReturnValueDescriptor descriptor = Executables.returnValueConstrainedMethod()
				.getReturnValueDescriptor();
		assertFalse( descriptor.isCascaded(), "Should not be cascaded" );

		descriptor = Executables.cascadedReturnValueMethod().getReturnValueDescriptor();
		assertTrue( descriptor.isCascaded(), "Should be cascaded" );
	}

	@Test
	@SpecAssertion(section = "6.7", id = "a")
	public void testIsCascadedForConstructorReturnValue() {
		ReturnValueDescriptor descriptor = Executables.returnValueConstrainedConstructor()
				.getReturnValueDescriptor();
		assertFalse( descriptor.isCascaded(), "Should not be cascaded" );

		descriptor = Executables.cascadedReturnValueConstructor().getReturnValueDescriptor();
		assertTrue( descriptor.isCascaded(), "Should be cascaded" );
	}

	@Test
	@SpecAssertion(section = "6.2", id = "d")
	public void testGetKind() {
		ReturnValueDescriptor descriptor = Executables.returnValueConstrainedMethod()
				.getReturnValueDescriptor();
		assertEquals(
				descriptor.getKind(),
				Kind.RETURN_VALUE,
				"Descriptor should be of kind RETURN_VALUE"
		);
	}

	@Test
	@SpecAssertion(section = "6.2", id = "e")
	public void testAs() {
		ElementDescriptor elementDescriptor = Executables.returnValueConstrainedMethod()
				.getReturnValueDescriptor();
		ReturnValueDescriptor returnValueDescriptor = elementDescriptor.as( ReturnValueDescriptor.class );
		assertNotNull( returnValueDescriptor, "Descriptor should not be null" );
		assertSame(
				returnValueDescriptor,
				elementDescriptor,
				"as() should return the same object"
		);
	}

	@Test(expectedExceptions = ClassCastException.class)
	@SpecAssertion(section = "6.2", id = "e")
	public void testAsWithWrongType() {
		ElementDescriptor elementDescriptor = Executables.returnValueConstrainedMethod()
				.getReturnValueDescriptor();
		elementDescriptor.as( BeanDescriptor.class );
	}
}
