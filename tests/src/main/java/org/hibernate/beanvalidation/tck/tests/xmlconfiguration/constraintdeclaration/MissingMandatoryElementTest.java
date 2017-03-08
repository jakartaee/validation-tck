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
public class MissingMandatoryElementTest extends Arquillian {

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClass( MissingMandatoryElementTest.class )
				.withClasses( Optional.class, Package.class, PrePosting.class, ValidPackage.class )
				.withValidationXml( "validation-MissingMandatoryElementTest.xml" )
				.withResource( "package-constraints-MissingMandatoryElementTest.xml" )
				.build();
	}

	@Test
	@SpecAssertion(section = "8.1.1.6", id = "i")
	public void testMissingMandatoryElementInConstraintDeclaration() {
		try {
			TestUtil.getValidatorUnderTest();
			fail( "Creating the validator should have failed since the constraint declaration in xml is incomplete." );
		}
		catch ( ValidationException e ) {
			// success
		}
	}
}
