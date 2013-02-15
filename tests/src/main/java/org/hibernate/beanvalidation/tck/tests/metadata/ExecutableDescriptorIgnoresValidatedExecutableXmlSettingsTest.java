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

import java.util.Collections;
import javax.validation.Validation;
import javax.validation.metadata.ConstructorDescriptor;
import javax.validation.metadata.MethodDescriptor;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

import static org.hibernate.beanvalidation.tck.util.TestUtil.getConstructorDescriptor;
import static org.hibernate.beanvalidation.tck.util.TestUtil.getMethodDescriptor;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "1.1.0")
public class ExecutableDescriptorIgnoresValidatedExecutableXmlSettingsTest extends Arquillian {

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClass( ExecutableDescriptorIgnoresValidatedExecutableXmlSettingsTest.class )
				.withClasses(
						StockItem.class
				)
				.withValidationXml( "validation-ExecutableDescriptorIgnoresValidatedExecutableXmlSettingsTest.xml" )
				.build();
	}

	@Test
	@SpecAssertion(section = "6.7", id = "h")
	public void testMethodDescriptorCanBeRetrievedAlsoIfValidateExecutableIsSetToNONEInXml() {
		assertEquals(
				Validation.byDefaultProvider().configure().getBootstrapConfiguration().getValidatedExecutableTypes(),
				Collections.emptySet()
		);

		MethodDescriptor descriptor = getMethodDescriptor(
				StockItem.class,
				"setName",
				String.class
		);

		assertNotNull( descriptor );
		assertEquals( descriptor.getName(), "setName" );
		assertEquals( descriptor.getParameterDescriptors().get( 0 ).getConstraintDescriptors().size(), 1 );
	}

	@Test
	@SpecAssertion(section = "6.7", id = "h")
	public void testConstructorDescriptorCanBeRetrievedAlsoIfValidateExecutableIsSetToNONEInXml() {
		assertEquals(
				Validation.byDefaultProvider().configure().getBootstrapConfiguration().getValidatedExecutableTypes(),
				Collections.emptySet()
		);

		ConstructorDescriptor descriptor = getConstructorDescriptor(
				StockItem.class,
				String.class
		);

		assertNotNull( descriptor );
		assertEquals( descriptor.getName(), "StockItem" );
		assertEquals( descriptor.getParameterDescriptors().get( 0 ).getConstraintDescriptors().size(), 1 );
	}
}
