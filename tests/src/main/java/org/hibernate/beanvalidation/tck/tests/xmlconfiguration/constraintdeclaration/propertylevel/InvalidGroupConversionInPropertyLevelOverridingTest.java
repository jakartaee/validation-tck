/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.constraintdeclaration.propertylevel;

import jakarta.validation.ConstraintDeclarationException;
import jakarta.validation.Validator;
import jakarta.validation.metadata.BeanDescriptor;
import jakarta.validation.metadata.PropertyDescriptor;

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
@SpecVersion(spec = "beanvalidation", version = "3.0.0")
public class InvalidGroupConversionInPropertyLevelOverridingTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( InvalidGroupConversionInPropertyLevelOverridingTest.class )
				.withClasses( User.class, CreditCard.class )
				.withValidationXml( "validation-InvalidGroupConversionInPropertyLevelOverridingTest.xml" )
				.withResource( "user-constraints-InvalidGroupConversionInPropertyLevelOverridingTest.xml" )
				.build();
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertions({
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_PROPERTYLEVELOVERRIDING, id = "f")
	})
	public void testGroupConversionsAreAdditiveAndExceptionIsThrownForMultipleConversionWithSameSource() {
		Validator validator = TestUtil.getValidatorUnderTest();
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( User.class );
		PropertyDescriptor propDescriptor = beanDescriptor.getConstraintsForProperty( "firstCreditCard" );
		propDescriptor.getGroupConversions();
	}
}
