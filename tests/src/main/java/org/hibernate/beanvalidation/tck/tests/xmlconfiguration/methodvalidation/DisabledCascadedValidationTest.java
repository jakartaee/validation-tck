/*
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.methodvalidation;

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
public class DisabledCascadedValidationTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( DisabledCascadedValidationTest.class )
				.withClass( Cascaded.class )
				.withValidationXml( "validation-DisabledCascadedValidationTest.xml" )
				.withResource( "DisabledCascadedValidationTest.xml" )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_METHODLEVELOVERRIDING, id = "o")
	public void testValidAnnotationIsIgnored() throws Exception {
		MethodDescriptor descriptor = TestUtil.getMethodDescriptor(
				org.hibernate.beanvalidation.tck.tests.xmlconfiguration.methodvalidation.Cascaded.class,
				"cascade",
				String.class
		);
		assertThat( descriptor ).as( "the specified method should be configured in xml" ).isNotNull();

		ReturnValueDescriptor returnValueDescriptor = descriptor.getReturnValueDescriptor();
		assertThat( returnValueDescriptor.isCascaded() ).as( "Cascaded validation should be ignored" ).isFalse();

		ParameterDescriptor parameterDescriptor = descriptor.getParameterDescriptors().get( 0 );
		assertThat( parameterDescriptor.isCascaded() ).as( "Cascaded validation should be ignored" ).isFalse();
	}
}
