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
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.constructorvalidation;

import javax.validation.metadata.ConstructorDescriptor;
import javax.validation.metadata.CrossParameterDescriptor;
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
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "1.1.0")
public class IgnoreAnnotationsOnConstructorTest extends Arquillian {
	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClass( IgnoreAnnotationsOnConstructorTest.class )
				.withClass( IgnoreAnnotations.class )
				.withValidationXml( "validation-IgnoreAnnotationsOnConstructorTest.xml" )
				.withResource( "ignore-annotations-IgnoreAnnotationsOnConstructorTest.xml" )
				.build();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "8.1.1.4", id = "k"),
			@SpecAssertion(section = "8.1.1.4", id = "l"),
			@SpecAssertion(section = "8.1.1.4", id = "m"),
			@SpecAssertion(section = "8.1.1.4", id = "p")
	})
	public void testIgnoreAnnotationsOnConstructorLevel() {
		ConstructorDescriptor descriptor = TestUtil.getConstructorDescriptor(
				IgnoreAnnotations.class,
				String.class,
				String.class
		);
		CrossParameterDescriptor crossParameterDescriptor = descriptor.getCrossParameterDescriptor();
		assertFalse( crossParameterDescriptor.hasConstraints(), "Cross parameter constraints should be ignored." );

		ReturnValueDescriptor returnValueDescriptor = descriptor.getReturnValueDescriptor();
		assertFalse( returnValueDescriptor.hasConstraints(), "Return value constraints should be ignored." );
		assertTrue( returnValueDescriptor.getGroupConversions().isEmpty(), "Group conversions should be ignored" );

		ParameterDescriptor parameterDescriptor = descriptor.getParameterDescriptors().get( 0 );
		assertFalse( parameterDescriptor.hasConstraints(), "First parameter constraints should be ignored." );
		assertTrue( parameterDescriptor.getGroupConversions().isEmpty(), "Group conversions should be ignored" );

		parameterDescriptor = descriptor.getParameterDescriptors().get( 1 );
		assertTrue( parameterDescriptor.hasConstraints(), "Second parameter constraints should be applied." );
		assertEquals( parameterDescriptor.getGroupConversions().size(), 2, "All group conversions should be combined" );
	}
}
