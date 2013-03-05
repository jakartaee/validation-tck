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
import javax.validation.ValidationException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

import static org.testng.Assert.fail;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "1.1.0")
public class ReservedElementNameTest extends Arquillian {

	public final static String packageName = "/org/hibernate/beanvalidation/tck/tests/xmlconfiguration/constraintdeclaration/";
	public final static String mappingFile1 = "constraints-GroupIsNotAllowedAsElementNameTest.xml";
	public final static String mappingFile2 = "constraints-MessageIsNotAllowedAsElementNameTest.xml";

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClass( ReservedElementNameTest.class )
				.withClasses( User.class )
				.withResource( ReservedElementNameTest.mappingFile1 )
				.withResource( ReservedElementNameTest.mappingFile2 )
				.build();
	}

	@Test
	@SpecAssertion(section = "8.1.1.6", id = "c")
	public void testGroupIsNotAllowedAsElementName() {
		try {
			Configuration<?> config = TestUtil.getConfigurationUnderTest();
			config.addMapping( TestUtil.getInputStreamForPath( packageName + mappingFile1 ) );
			config.buildValidatorFactory().getValidator();
			fail( "Validator creation should have failed since <element name=\"groups\"> was used." );
		}
		catch ( ValidationException e ) {
			// success
		}
	}

	@Test
	@SpecAssertion(section = "8.1.1.6", id = "c")
	public void testMessageIsNotAllowedAsElementName() {
		try {
			Configuration<?> config = TestUtil.getConfigurationUnderTest();
			config.addMapping( TestUtil.getInputStreamForPath( packageName + mappingFile2 ) );
			config.buildValidatorFactory().getValidator();
			fail( "Validator creation should have failed since <element name=\"message\"> was used." );
		}
		catch ( ValidationException e ) {
			// success
			System.err.println( e.getMessage() );
		}
	}
}
