/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.constraintdeclaration.propertylevel;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.Set;

import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.groups.Default;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.GroupConversionDescriptor;
import javax.validation.metadata.PropertyDescriptor;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class PropertyLevelOverridingTest extends Arquillian {

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClass( PropertyLevelOverridingTest.class )
				.withClasses( User.class, CreditCard.class )
				.withValidationXml( "validation-PropertyLevelOverridingTest.xml" )
				.withResource( "user-constraints-PropertyLevelOverridingTest.xml" )
				.build();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_PROPERTYLEVELOVERRIDING, id = "a"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_PROPERTYLEVELOVERRIDING, id = "c")
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
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_PROPERTYLEVELOVERRIDING, id = "a"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_PROPERTYLEVELOVERRIDING, id = "d")
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
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_PROPERTYLEVELOVERRIDING, id = "e")
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
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_PROPERTYLEVELOVERRIDING, id = "f")
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
