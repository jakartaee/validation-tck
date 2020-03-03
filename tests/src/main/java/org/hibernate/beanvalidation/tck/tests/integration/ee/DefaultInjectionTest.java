/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.integration.ee;

import static org.testng.Assert.assertNotNull;

import jakarta.ejb.EJB;

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
@SpecVersion(spec = "beanvalidation", version = "3.0.0")
public class DefaultInjectionTest extends AbstractTCKTest {

	@EJB
	private ValidationTestEjb testEjb;

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( DefaultInjectionTest.class )
				.withClass( ConstantMessageInterpolator.class )
				.withClass( Foo.class )
				.withClass( ValidationTestEjb.class )
				.withValidationXml( "test-validation.xml" )
				.withEmptyBeansXml()
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_JAKARTAEE, id = "b")
	private void testDefaultValidatorFactoryGetsInjectedAtResource() throws Exception {
		assertNotNull( testEjb );
		testEjb.assertDefaultValidatorFactoryGetsInjected();
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_JAKARTAEE, id = "b")
	private void testDefaultValidatorGetsInjectedWithAtResource() {
		assertNotNull( testEjb );
		testEjb.assertDefaultValidatorGetsInjected();
	}
}
