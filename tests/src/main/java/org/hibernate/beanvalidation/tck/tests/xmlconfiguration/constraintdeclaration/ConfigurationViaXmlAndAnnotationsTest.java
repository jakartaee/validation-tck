/*
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.constraintdeclaration;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;

import java.util.Set;

import jakarta.validation.Configuration;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.groups.Default;
import jakarta.validation.metadata.BeanDescriptor;

import org.assertj.core.api.Assertions;
import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.junit.jupiter.api.Test;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
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
		Assertions.assertThat( beanDescriptor.isBeanConstrained() ).as( "The bean should be constrained" ).isTrue();

		Assertions.assertThat(  beanDescriptor.getConstraintsForProperty( "maxWeight" ).getConstraintDescriptors().size() ).as( "With xml configuration there should be two constraints." ).isEqualTo( 2 );

		Configuration<?> config = TestUtil.getConfigurationUnderTest();
		config.ignoreXmlConfiguration();
		validator = config.buildValidatorFactory().getValidator();
		beanDescriptor = validator.getConstraintsForClass( Package.class );

		Assertions.assertThat(  beanDescriptor.isBeanConstrained() ).as( "Without xml there should be only one constraint." ).isTrue();

		Assertions.assertThat(  beanDescriptor.getConstraintsForProperty( "maxWeight" ).getConstraintDescriptors().size() ).as( "Without xml there should be only one constraint." ).isEqualTo( 1 );
	}

	@Test
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CLASSLEVELOVERRIDING, id = "e")
	public void testDefaultGroupDefinitionDefinedInEntityApplies() {
		Validator validator = TestUtil.getValidatorUnderTest();
		Package p = new Package();
		p.setMaxWeight( 30 );
		Set<ConstraintViolation<Package>> violations = validator.validate( p, Default.class );

		assertThat( violations ).containsOnlyViolations(
				violationOf( ValidPackage.class ).withMessage( "ValidPackage defined as annotation" )
		);
	}
}
