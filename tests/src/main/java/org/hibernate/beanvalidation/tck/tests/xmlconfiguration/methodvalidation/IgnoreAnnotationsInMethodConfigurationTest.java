/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.methodvalidation;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import javax.validation.metadata.CrossParameterDescriptor;
import javax.validation.metadata.MethodDescriptor;
import javax.validation.metadata.ParameterDescriptor;
import javax.validation.metadata.ReturnValueDescriptor;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class IgnoreAnnotationsInMethodConfigurationTest extends Arquillian {
	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
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
