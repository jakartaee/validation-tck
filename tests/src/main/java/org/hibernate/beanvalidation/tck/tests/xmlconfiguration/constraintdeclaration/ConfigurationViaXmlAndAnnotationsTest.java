/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.constraintdeclaration;

import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectConstraintViolationMessages;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectNumberOfViolations;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Set;

import javax.validation.Configuration;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import javax.validation.metadata.BeanDescriptor;

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
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class ConfigurationViaXmlAndAnnotationsTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( ConfigurationViaXmlAndAnnotationsTest.class )
				.withClasses( Optional.class, Package.class, PrePosting.class, ValidPackage.class )
				.withValidationXml( "validation-ConfigurationViaXmlAndAnnotationsTest.xml" )
				.withResource( "package-constraints-ConfigurationViaXmlAndAnnotationsTest.xml" )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML, id = "e")
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML, id = "a")
	public void testEntityConfiguredViaAnnotationsAndXml() {
		Validator validator = TestUtil.getValidatorUnderTest();
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( Package.class );
		assertTrue( beanDescriptor.isBeanConstrained(), "The bean should be constrained" );

		assertEquals(
				beanDescriptor.getConstraintsForProperty( "maxWeight" ).getConstraintDescriptors().size(),
				2,
				"With xml configuration there should be two constraints."
		);

		Configuration<?> config = TestUtil.getConfigurationUnderTest();
		config.ignoreXmlConfiguration();
		validator = config.buildValidatorFactory().getValidator();
		beanDescriptor = validator.getConstraintsForClass( Package.class );

		assertTrue(
				beanDescriptor.isBeanConstrained(),
				"Without xml there should be only one constraint."
		);

		assertEquals(
				beanDescriptor.getConstraintsForProperty( "maxWeight" ).getConstraintDescriptors().size(),
				1,
				"Without xml there should be only one constraint."
		);
	}

	@Test
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CLASSLEVELOVERRIDING, id = "e")
	public void testDefaultGroupDefinitionDefinedInEntityApplies() {
		Validator validator = TestUtil.getValidatorUnderTest();
		Package p = new Package();
		p.setMaxWeight( 30 );
		Set<ConstraintViolation<Package>> violations = validator.validate( p, Default.class );

		assertCorrectNumberOfViolations( violations, 1 );
		assertCorrectConstraintViolationMessages( violations, "ValidPackage defined as annotation" );
	}
}
