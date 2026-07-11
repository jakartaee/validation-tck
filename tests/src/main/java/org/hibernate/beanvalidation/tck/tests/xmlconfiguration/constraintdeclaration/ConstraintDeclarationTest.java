/*
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.constraintdeclaration;

import jakarta.validation.Configuration;
import jakarta.validation.Validator;
import jakarta.validation.metadata.BeanDescriptor;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
public class ConstraintDeclarationTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( ConstraintDeclarationTest.class )
				.withClasses( Optional.class, Package.class, PrePosting.class, ValidPackage.class )
				.withValidationXml( "validation-ConstraintDeclarationTest.xml" )
				.withResource( "package-constraints-ConstraintDeclarationTest.xml" )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML, id = "d")
	public void testConstraintAnnotationsArePerDefaultIgnoredForXmlConfiguredEntities() {
		Validator validator = TestUtil.getValidatorUnderTest();
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( Package.class );
		assertThat( beanDescriptor.isBeanConstrained() ).as( "With xml configuration there should be no constraint." ).isFalse();

		Configuration<?> config = TestUtil.getConfigurationUnderTest();
		config.ignoreXmlConfiguration();
		validator = config.buildValidatorFactory().getValidator();
		beanDescriptor = validator.getConstraintsForClass( Package.class );
		assertThat( beanDescriptor.isBeanConstrained() ).as( "If xml configuration is ignored Package should have a single constraint." ).isTrue();
	}
}
