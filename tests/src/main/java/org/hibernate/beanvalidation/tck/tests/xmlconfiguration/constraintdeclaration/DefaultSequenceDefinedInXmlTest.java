/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.constraintdeclaration;


import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Max;
import jakarta.validation.groups.Default;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
public class DefaultSequenceDefinedInXmlTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( DefaultSequenceDefinedInXmlTest.class )
				.withClasses( Optional.class, Package.class, PrePosting.class, ValidPackage.class )
				.withValidationXml( "validation-DefaultSequenceDefinedInXmlTest.xml" )
				.withResource( "package-constraints-DefaultSequenceDefinedInXmlTest.xml" )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CLASSLEVELOVERRIDING, id = "e")
	public void testDefaultGroupDefinitionDefinedInEntityApplies() {
		Validator validator = TestUtil.getValidatorUnderTest();
		Package p = new Package();
		p.setMaxWeight( 30 );
		Set<ConstraintViolation<Package>> violations = validator.validate( p, Default.class );

		assertThat( violations ).containsOnlyViolations(
				violationOf( Max.class ).withMessage( "The package is too heavy" )
		);
	}
}
