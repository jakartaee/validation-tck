/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.constraintdeclaration;

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
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class ConfiguredBeanNotInClassPathTest extends Arquillian {

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClass( ConfiguredBeanNotInClassPathTest.class )
				.withValidationXml( "validation-ConfiguredBeanNotInClassPathTest.xml" )
				.withResource( "constraints-ConfiguredBeanNotInClassPathTest.xml" )
				.build();
	}

	@Test
	@SpecAssertion(section = "8.1.1", id = "f")
	public void testExceptionIsThrownForUnknownBeanNameInXml() {
		try {
			TestUtil.getValidatorUnderTest();
			fail( "Test should have thrown an exception due to wrong class name" );
		}
		catch ( ValidationException e ) {
			//success
		}
	}
}
