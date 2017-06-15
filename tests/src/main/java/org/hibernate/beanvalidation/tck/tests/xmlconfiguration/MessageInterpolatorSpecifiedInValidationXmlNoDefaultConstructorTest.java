/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration;

import static org.testng.Assert.fail;

import javax.validation.ValidationException;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class MessageInterpolatorSpecifiedInValidationXmlNoDefaultConstructorTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( MessageInterpolatorSpecifiedInValidationXmlNoDefaultConstructorTest.class )
				.withValidationXml( "validation-MessageInterpolatorSpecifiedInValidationXmlNoDefaultConstructorTest.xml" )
				.build();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "f"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "x")
	})
	public void testMessageInterpolatorSpecifiedInValidationXmlHasNoDefaultConstructor() {
		try {
			TestUtil.getValidatorUnderTest();
			fail( "Bootstrapping should have failed due to missing no-arg constructor in MessageInterpolator" );
		}
		catch ( ValidationException e ) {
			// success
		}
	}
}
