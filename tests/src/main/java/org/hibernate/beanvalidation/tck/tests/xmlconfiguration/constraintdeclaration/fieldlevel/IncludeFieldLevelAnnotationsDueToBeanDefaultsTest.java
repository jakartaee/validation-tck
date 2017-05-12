/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.constraintdeclaration.fieldlevel;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Set;

import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.PropertyDescriptor;

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
public class IncludeFieldLevelAnnotationsDueToBeanDefaultsTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( IncludeFieldLevelAnnotationsDueToBeanDefaultsTest.class )
				.withClasses( User.class, CreditCard.class )
				.withValidationXml( "validation-IncludeFieldLevelAnnotationsDueToBeanDefaultsTest.xml" )
				.withResource( "user-constraints-IncludeFieldLevelAnnotationsDueToBeanDefaultsTest.xml" )
				.build();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_FIELDLEVELOVERRIDING, id = "b"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_FIELDLEVELOVERRIDING, id = "d")
	})
	public void testAnnotationsIncluded() {
		Validator validator = TestUtil.getValidatorUnderTest();
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( User.class );
		assertNotNull( beanDescriptor );

		PropertyDescriptor propDescriptor = beanDescriptor.getConstraintsForProperty( "firstname" );
		assertNotNull( propDescriptor );

		Set<ConstraintDescriptor<?>> constraintDescriptors = propDescriptor.getConstraintDescriptors();
		assertEquals( constraintDescriptors.size(), 1, "There should be two constraints" );
		ConstraintDescriptor<?> descriptor = constraintDescriptors.iterator().next();
		assertTrue( descriptor.getAnnotation() instanceof NotNull, "Wrong constraint annotation." );
	}
}
