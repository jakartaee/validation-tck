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
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.versioning;

import javax.validation.Configuration;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

import static org.testng.Assert.assertEquals;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "1.1.0")
public class Version11InValidationXmlTest extends Arquillian {

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClass( Version11InValidationXmlTest.class )
				.withValidationXml( "validation-Version11InValidationXmlTest.xml" )
				.build();
	}

	@Test
	@SpecAssertion(section = "8.1.4", id = "a")
	public void testValidationXmlVersion11() {
		Configuration<?> config = TestUtil.getConfigurationUnderTest();
		assertEquals(
				config.getBootstrapConfiguration().getMessageInterpolatorClassName(),
				"org.hibernate.beanvalidation.tck.tests.xmlconfiguration.versioning.DummyMessageInterpolator",
				"Wrong message interpolator class name."
		);
	}
}
