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
package org.hibernate.beanvalidation.tck.tests.integration.ee.cdi;

import java.util.Set;
import javax.naming.InitialContext;
import javax.validation.ConstraintViolation;
import javax.validation.ValidatorFactory;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.util.IntegrationTest;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectConstraintViolationMessages;
import static org.testng.Assert.assertNotNull;

/**
 * @author Gunnar Morling
 */
@IntegrationTest
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class ConstraintValidatorInjectionTest extends Arquillian {

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClassPackage( ConstraintValidatorInjectionTest.class )
				.withEmptyBeansXml()
				.build();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "10.2", id = "c"),
			@SpecAssertion(section = "10.3", id = "a")
	})
	public void testJndiBoundValidatorFactoryIsCdiEnabled() throws Exception {
		ValidatorFactory validatorFactory = InitialContext.doLookup( "java:comp/ValidatorFactory" );
		assertNotNull(
				validatorFactory,
				"Default validator factory should be bound to JNDI tree."
		);

		Set<ConstraintViolation<Foo>> violations = validatorFactory.getValidator().validate( new Foo() );

		assertCorrectConstraintViolationMessages( violations, "Hello, bar!", "Good morning, qux!" );
	}

	private static class Foo {
		@GreetingConstraint(name = "bar")
		public String bar;

		@GreetingConstraint(name = "qux")
		public Integer qux;
	}
}
