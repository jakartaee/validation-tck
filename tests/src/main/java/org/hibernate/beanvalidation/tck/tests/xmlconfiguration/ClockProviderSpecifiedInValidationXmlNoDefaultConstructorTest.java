/*
* JBoss, Home of Professional Open Source
* Copyright 2016, Red Hat, Inc. and/or its affiliates, and individual contributors
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

import static org.testng.Assert.fail;

import javax.validation.ValidationException;

import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Guillaume Smet
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class ClockProviderSpecifiedInValidationXmlNoDefaultConstructorTest extends Arquillian {

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClass( ClockProviderSpecifiedInValidationXmlNoDefaultConstructorTest.class )
				.withValidationXml( "validation-ClockProviderSpecifiedInValidationXmlNoDefaultConstructorTest.xml" )
				.build();
	}

	@Test
	// XXX BVAL-496 update specification references
	@SpecAssertions({
			@SpecAssertion(section = "", id = "")
	})
	public void testClockProviderSpecifiedInValidationXmlHasNoDefaultConstructor() {
		try {
			TestUtil.getValidatorUnderTest();
			fail( "Bootstrapping should have failed due to missing no-arg constructor in ClockProvider" );
		}
		catch ( ValidationException e ) {
			// success
		}
	}
}
