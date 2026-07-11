/*
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.constraintdeclaration.propertylevel;

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
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
public class ExcludePropertyLevelAnnotationsDueToBeanDefaultsTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( ExcludePropertyLevelAnnotationsDueToBeanDefaultsTest.class )
				.withClasses( User.class, CreditCard.class )
				.withValidationXml( "validation-ExcludePropertyLevelAnnotationsDueToBeanDefaultsTest.xml" )
				.withResource( "user-constraints-ExcludePropertyLevelAnnotationsDueToBeanDefaultsTest.xml" )
				.build();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_PROPERTYLEVELOVERRIDING, id = "b"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_PROPERTYLEVELOVERRIDING, id = "c")
	})
	public void testIgnoreAnnotations() {
		Validator validator = TestUtil.getValidatorUnderTest();
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( User.class );
		assertThat( beanDescriptor  ).isNotNull();

		PropertyDescriptor propDescriptor = beanDescriptor.getConstraintsForProperty( "firstname" );
		assertThat( propDescriptor ).as( "The annotation defined constraints should be ignored." ).isNull();
	}
}
