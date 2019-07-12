/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.integration.cdi.executable.global;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertCorrectConstraintTypes;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotNull;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.IntegrationTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Gunnar Morling
 */
@IntegrationTest
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class ExecutableValidationBasedOnGlobalConfigurationTest extends AbstractTCKTest {

	@Inject
	private CalendarService calendar;

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( ExecutableValidationBasedOnGlobalConfigurationTest.class )
				.withValidationXml( "validation-ExecutableValidationBasedOnGlobalConfigurationTest.xml" )
				.withEmptyBeansXml()
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "p")
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "g")
	public void testValidationOfConstrainedMethodOnTypeAnnotatedWithValidateOnExecutionContainingExecutableType() {
		try {
			calendar.getEvent();
			fail( "Method invocation should have caused a ConstraintViolationException" );
		}
		catch ( ConstraintViolationException e ) {
			assertCorrectConstraintTypes( e.getConstraintViolations(), NotNull.class );
		}
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "g")
	public void testValidationOfConstrainedMethodOnTypeAnnotatedWithValidateOnExecutionNotContainingExecutableType() {
		Event event = calendar.createEvent( null );
		assertNotNull( event );

		// success; the constraint is invalid, but no violation exception is
		// expected since the executable type is not given in META-INF/validation.xml
	}
}
