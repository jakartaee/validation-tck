/*
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.integration.ee;

import jakarta.ejb.EJB;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.IntegrationTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Gunnar Morling
 */
@IntegrationTest
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
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
				.withBeansXml()
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_JAKARTAEE, id = "b")
	void testDefaultValidatorFactoryGetsInjectedAtResource() throws Exception {
		assertThat( testEjb  ).isNotNull();
		testEjb.assertDefaultValidatorFactoryGetsInjected();
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_JAKARTAEE, id = "b")
	void testDefaultValidatorGetsInjectedWithAtResource() {
		assertThat( testEjb  ).isNotNull();
		testEjb.assertDefaultValidatorGetsInjected();
	}
}
