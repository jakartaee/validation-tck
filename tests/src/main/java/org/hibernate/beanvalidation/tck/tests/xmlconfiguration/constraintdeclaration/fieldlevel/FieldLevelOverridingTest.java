/*
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.constraintdeclaration.fieldlevel;

import java.util.Set;

import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.groups.Default;
import jakarta.validation.metadata.BeanDescriptor;
import jakarta.validation.metadata.ConstraintDescriptor;
import jakarta.validation.metadata.GroupConversionDescriptor;
import jakarta.validation.metadata.PropertyDescriptor;

import org.assertj.core.api.Assertions;
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
		assertThat( beanDescriptor  ).isNotNull();

		PropertyDescriptor propDescriptor = beanDescriptor.getConstraintsForProperty( "firstname" );
		assertThat( propDescriptor ).as( "The annotation defined constraints should be ignored." ).isNull();
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
		assertThat( beanDescriptor  ).isNotNull();

		PropertyDescriptor propDescriptor = beanDescriptor.getConstraintsForProperty( "lastname" );
		assertThat( propDescriptor  ).isNotNull();

		Set<ConstraintDescriptor<?>> constraintDescriptors = propDescriptor.getConstraintDescriptors();
		assertThat( constraintDescriptors.size() ).as( "There should be two constraints" ).isEqualTo( 2 );

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
				Assertions.fail( "Invalid constraint for property." );
			}
		}
		if ( !( foundNotNullConstraint && foundPatternConstraint ) ) {
			Assertions.fail( "Not all configured constraints discovered." );
		}
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_FIELDLEVELOVERRIDING, id = "e")
	})
	public void testCascadedConfiguration() {
		Validator validator = TestUtil.getValidatorUnderTest();
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( User.class );
		assertThat( beanDescriptor  ).isNotNull();

		PropertyDescriptor propDescriptor = beanDescriptor.getConstraintsForProperty( "firstCreditCard" );
		assertThat( propDescriptor  ).isNotNull();
		assertThat( propDescriptor.isCascaded() ).as( "Cascaded validation is configured via xml." ).isTrue();

		propDescriptor = beanDescriptor.getConstraintsForProperty( "secondCreditCard" );
		assertThat( propDescriptor ).as( "The @Valid annotation should be ignored." ).isNull();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_FIELDLEVELOVERRIDING, id = "f")
	})
	public void testGroupConversionsAreAdditive() {
		Validator validator = TestUtil.getValidatorUnderTest();
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( User.class );
		assertThat( beanDescriptor  ).isNotNull();

		PropertyDescriptor propDescriptor = beanDescriptor.getConstraintsForProperty( "firstCreditCard" );
		assertThat( propDescriptor  ).isNotNull();
		assertThat( propDescriptor.isCascaded() ).as( "Cascaded validation is configured via xml." ).isTrue();
		Set<GroupConversionDescriptor> groupConversionDescriptorSet = propDescriptor.getGroupConversions();

		assertThat( groupConversionDescriptorSet.size() == 2 ).as( "There should be two group conversions. One configured via annotations and one via XML" ).isTrue();

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
				Assertions.fail( "Unexpected group conversion" );
			}
		}

		assertThat( foundDefaultToRatingA && foundDefaultToRatingAA ).as( "Group conversions defined via XML and Annotation are additive" ).isTrue();
	}
}
