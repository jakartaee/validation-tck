/*
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.constructorvalidation;

import jakarta.validation.metadata.ConstructorDescriptor;
import jakarta.validation.metadata.CrossParameterDescriptor;
import jakarta.validation.metadata.ParameterDescriptor;
import jakarta.validation.metadata.ReturnValueDescriptor;

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
public class IgnoreAnnotationsOnConstructorTest extends AbstractTCKTest {
	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( IgnoreAnnotationsOnConstructorTest.class )
				.withClass( IgnoreAnnotations.class )
				.withValidationXml( "validation-IgnoreAnnotationsOnConstructorTest.xml" )
				.withResource( "ignore-annotations-IgnoreAnnotationsOnConstructorTest.xml" )
				.build();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONSTRUCTORLEVELOVERRIDING, id = "k"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONSTRUCTORLEVELOVERRIDING, id = "l"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONSTRUCTORLEVELOVERRIDING, id = "m"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONSTRUCTORLEVELOVERRIDING, id = "o")
	})
	public void testIgnoreAnnotationsOnConstructorLevel() {
		ConstructorDescriptor descriptor = TestUtil.getConstructorDescriptor(
				IgnoreAnnotations.class,
				String.class,
				String.class
		);
		CrossParameterDescriptor crossParameterDescriptor = descriptor.getCrossParameterDescriptor();
		assertThat( crossParameterDescriptor.hasConstraints() ).as( "Cross parameter constraints should be ignored." ).isFalse();

		ReturnValueDescriptor returnValueDescriptor = descriptor.getReturnValueDescriptor();
		assertThat( returnValueDescriptor.hasConstraints() ).as( "Return value constraints should be ignored." ).isFalse();
		assertThat( returnValueDescriptor.getGroupConversions().isEmpty() ).as( "Group conversions should be ignored" ).isTrue();

		ParameterDescriptor parameterDescriptor = descriptor.getParameterDescriptors().get( 0 );
		assertThat( parameterDescriptor.hasConstraints() ).as( "First parameter constraints should be ignored." ).isFalse();
		assertThat( parameterDescriptor.getGroupConversions().isEmpty() ).as( "Group conversions should be ignored" ).isTrue();

		parameterDescriptor = descriptor.getParameterDescriptors().get( 1 );
		assertThat( parameterDescriptor.hasConstraints() ).as( "Second parameter constraints should be applied." ).isTrue();
		assertThat( parameterDescriptor.getGroupConversions().size() ).as( "All group conversions should be combined" ).isEqualTo( 2 );
	}
}
