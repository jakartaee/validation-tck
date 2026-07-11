/*
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.methodvalidation;

import jakarta.validation.metadata.CrossParameterDescriptor;
import jakarta.validation.metadata.MethodDescriptor;
import jakarta.validation.metadata.ParameterDescriptor;
import jakarta.validation.metadata.ReturnValueDescriptor;

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
public class IgnoreAnnotationsInMethodConfigurationTest extends AbstractTCKTest {
	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( IgnoreAnnotationsInMethodConfigurationTest.class )
				.withClass( IgnoreAnnotations.class )
				.withValidationXml( "validation-IgnoreAnnotationsInMethodConfigurationTest.xml" )
				.withResource( "ignore-annotations-IgnoreAnnotationsInMethodConfigurationTest.xml" )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_METHODLEVELOVERRIDING, id = "l")
	public void testIgnoreAnnotationsOnReturnValueParameterAndCrossParameter() {
		MethodDescriptor descriptor = TestUtil.getMethodDescriptor(
				IgnoreAnnotations.class,
				"foobar",
				String.class,
				String.class
		);
		CrossParameterDescriptor crossParameterDescriptor = descriptor.getCrossParameterDescriptor();
		assertThat( crossParameterDescriptor.hasConstraints() ).as( "Cross parameter constraints should be ignored." ).isFalse();

		ReturnValueDescriptor returnValueDescriptor = descriptor.getReturnValueDescriptor();
		assertThat( returnValueDescriptor.hasConstraints() ).as( "Return value constraints should be ignored." ).isFalse();

		ParameterDescriptor parameterDescriptor = descriptor.getParameterDescriptors().get( 0 );
		assertThat( parameterDescriptor.hasConstraints() ).as( "First parameter constraints should be ignored." ).isFalse();

		parameterDescriptor = descriptor.getParameterDescriptors().get( 1 );
		assertThat( parameterDescriptor.hasConstraints() ).as( "Second parameter constraints should be applied." ).isTrue();
	}
}
