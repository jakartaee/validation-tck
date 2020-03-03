/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.methodvalidation;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

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
import org.testng.annotations.Test;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "3.0.0")
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
		assertFalse( crossParameterDescriptor.hasConstraints(), "Cross parameter constraints should be ignored." );

		ReturnValueDescriptor returnValueDescriptor = descriptor.getReturnValueDescriptor();
		assertFalse( returnValueDescriptor.hasConstraints(), "Return value constraints should be ignored." );

		ParameterDescriptor parameterDescriptor = descriptor.getParameterDescriptors().get( 0 );
		assertFalse( parameterDescriptor.hasConstraints(), "First parameter constraints should be ignored." );

		parameterDescriptor = descriptor.getParameterDescriptors().get( 1 );
		assertTrue( parameterDescriptor.hasConstraints(), "Second parameter constraints should be applied." );
	}
}
