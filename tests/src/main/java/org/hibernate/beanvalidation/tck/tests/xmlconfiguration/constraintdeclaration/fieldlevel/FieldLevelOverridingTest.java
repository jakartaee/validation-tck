/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.constraintdeclaration.fieldlevel;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.Set;

import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.groups.Default;
import jakarta.validation.metadata.BeanDescriptor;
import jakarta.validation.metadata.ConstraintDescriptor;
import jakarta.validation.metadata.GroupConversionDescriptor;
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
public class FieldLevelOverridingTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( FieldLevelOverridingTest.class )
				.withClasses( User.class, CreditCard.class )
				.withValidationXml( "validation-FieldLevelOverridingTest.xml" )
				.withResource( "user-constraints-FieldLevelOverridingTest.xml" )
				.build();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_FIELDLEVELOVERRIDING, id = "a"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_FIELDLEVELOVERRIDING, id = "c")
	})
	public void testIgnoreAnnotations() {
		Validator validator = TestUtil.getValidatorUnderTest();
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( User.class );
		assertNotNull( beanDescriptor );

		PropertyDescriptor propDescriptor = beanDescriptor.getConstraintsForProperty( "firstname" );
		assertNull( propDescriptor, "The annotation defined constraints should be ignored." );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONSTRAINTDESCRIPTOR, id = "a"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_FIELDLEVELOVERRIDING, id = "a"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_FIELDLEVELOVERRIDING, id = "d")
	})
	public void testIncludeAnnotations() {
		Validator validator = TestUtil.getValidatorUnderTest();
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( User.class );
		assertNotNull( beanDescriptor );

		PropertyDescriptor propDescriptor = beanDescriptor.getConstraintsForProperty( "lastname" );
		assertNotNull( propDescriptor );

		Set<ConstraintDescriptor<?>> constraintDescriptors = propDescriptor.getConstraintDescriptors();
		assertEquals( constraintDescriptors.size(), 2, "There should be two constraints" );

		boolean foundNotNullConstraint = false;
		boolean foundPatternConstraint = false;
		for ( ConstraintDescriptor<?> descriptor : constraintDescriptors ) {
			if ( descriptor.getAnnotation() instanceof NotNull ) {
				foundNotNullConstraint = true;
			}
			else if ( descriptor.getAnnotation() instanceof Pattern ) {
				foundPatternConstraint = true;
			}
			else {
				fail( "Invalid constraint for property." );
			}
		}
		if ( !( foundNotNullConstraint && foundPatternConstraint ) ) {
			fail( "Not all configured constraints discovered." );
		}
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_FIELDLEVELOVERRIDING, id = "e")
	})
	public void testCascadedConfiguration() {
		Validator validator = TestUtil.getValidatorUnderTest();
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( User.class );
		assertNotNull( beanDescriptor );

		PropertyDescriptor propDescriptor = beanDescriptor.getConstraintsForProperty( "firstCreditCard" );
		assertNotNull( propDescriptor );
		assertTrue( propDescriptor.isCascaded(), "Cascaded validation is configured via xml." );

		propDescriptor = beanDescriptor.getConstraintsForProperty( "secondCreditCard" );
		assertNull( propDescriptor, "The @Valid annotation should be ignored." );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_FIELDLEVELOVERRIDING, id = "f")
	})
	public void testGroupConversionsAreAdditive() {
		Validator validator = TestUtil.getValidatorUnderTest();
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( User.class );
		assertNotNull( beanDescriptor );

		PropertyDescriptor propDescriptor = beanDescriptor.getConstraintsForProperty( "firstCreditCard" );
		assertNotNull( propDescriptor );
		assertTrue( propDescriptor.isCascaded(), "Cascaded validation is configured via xml." );
		Set<GroupConversionDescriptor> groupConversionDescriptorSet = propDescriptor.getGroupConversions();

		assertTrue(
				groupConversionDescriptorSet.size() == 2,
				"There should be two group conversions. One configured via annotations and one via XML"
		);

		boolean foundDefaultToRatingA = false;
		boolean foundDefaultToRatingAA = false;
		for ( GroupConversionDescriptor groupConversionDescriptor : groupConversionDescriptorSet ) {
			if ( groupConversionDescriptor.getFrom().equals( Default.class )
					&& groupConversionDescriptor.getTo().equals( User.CreditRatingA.class ) ) {
				foundDefaultToRatingA = true;
			}
			else if ( groupConversionDescriptor.getFrom().equals( User.CreditRatingA.class )
					&& groupConversionDescriptor.getTo().equals( User.CreditRatingAA.class ) ) {
				foundDefaultToRatingAA = true;
			}
			else {
				fail( "Unexpected group conversion" );
			}
		}

		assertTrue(
				foundDefaultToRatingA && foundDefaultToRatingAA,
				"Group conversions defined via XML and Annotation are additive"
		);
	}
}
