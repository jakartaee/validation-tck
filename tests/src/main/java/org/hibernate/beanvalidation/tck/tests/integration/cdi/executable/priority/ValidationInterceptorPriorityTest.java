/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.integration.cdi.executable.priority;

import static org.testng.Assert.assertTrue;

import javax.inject.Inject;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.IntegrationTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

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
public class ValidationInterceptorPriorityTest extends AbstractTCKTest {

	@Inject
	private CalendarService calendar;

	@Inject
	private InvocationTracker invocationTracker;

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( ValidationInterceptorPriorityTest.class )
				.withEmptyBeansXml()
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_CDI_METHODCONSTRUCTORVALIDATION, id = "a")
	public void testValidationInterceptorHasPriority4800() {
		calendar.createEvent( null );

		assertTrue( invocationTracker.isEarlierInterceptorInvoked() );
		assertTrue( invocationTracker.isValidatorInvoked() );
		assertTrue( invocationTracker.isLaterInterceptorInvoked() );
	}
}
