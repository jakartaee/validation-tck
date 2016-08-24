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
package org.hibernate.beanvalidation.tck.tests.integration.cdi.executable.priority;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.util.IntegrationTest;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

import static org.testng.Assert.assertTrue;

/**
 * Test for the priority of the validation interceptor (which should be 4800).
 * Two other interceptors with priorities 4799 and 4801 are registered as well
 * and expected to be invoked before and after the validation interceptor,
 * respectively.
 *
 * @author Gunnar Morling
 */
@IntegrationTest
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class ValidationInterceptorPriorityTest extends Arquillian {

	@Inject
	private CalendarService calendar;

	@Inject
	private InvocationTracker invocationTracker;

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClassPackage( ValidationInterceptorPriorityTest.class )
				.withEmptyBeansXml()
				.build();
	}

	@Test
	@SpecAssertion(section = "10.3.3", id = "a")
	public void testValidationInterceptorHasPriority4800() {
		calendar.createEvent( null );

		assertTrue( invocationTracker.isEarlierInterceptorInvoked() );
		assertTrue( invocationTracker.isValidatorInvoked() );
		assertTrue( invocationTracker.isLaterInterceptorInvoked() );
	}
}
