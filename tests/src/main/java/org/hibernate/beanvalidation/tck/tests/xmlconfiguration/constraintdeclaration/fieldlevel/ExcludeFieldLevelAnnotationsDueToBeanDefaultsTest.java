/*
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.constraintdeclaration.fieldlevel;

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
public class ExcludeFieldLevelAnnotationsDueToBeanDefaultsTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( ExcludeFieldLevelAnnotationsDueToBeanDefaultsTest.class )
				.withClasses( User.class, CreditCard.class )
				.withValidationXml( "validation-ExcludeFieldLevelAnnotationsDueToBeanDefaultsTest.xml" )
				.withResource( "user-constraints-ExcludeFieldLevelAnnotationsDueToBeanDefaultsTest.xml" )
				.build();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_FIELDLEVELOVERRIDING, id = "b"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_FIELDLEVELOVERRIDING, id = "c")
	})
	public void testIgnoreAnnotations() {
		Validator validator = TestUtil.getValidatorUnderTest();
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( User.class );
		assertThat( beanDescriptor  ).isNotNull();

		PropertyDescriptor propDescriptor = beanDescriptor.getConstraintsForProperty( "firstname" );
		assertThat( propDescriptor ).as( "The annotation defined constraints should be ignored." ).isNull();
	}
}
