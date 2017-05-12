/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.constructorvalidation;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import javax.validation.metadata.ConstructorDescriptor;
import javax.validation.metadata.CrossParameterDescriptor;
import javax.validation.metadata.ParameterDescriptor;
import javax.validation.metadata.ReturnValueDescriptor;

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
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class IgnoreAnnotationsInConstructorConfigurationTest extends AbstractTCKTest {
	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( IgnoreAnnotationsInConstructorConfigurationTest.class )
				.withClass( IgnoreAnnotations.class )
				.withValidationXml( "validation-IgnoreAnnotationsInConstructorConfigurationTest.xml" )
				.withResource( "ignore-annotations-IgnoreAnnotationsInConstructorConfigurationTest.xml" )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONSTRUCTORLEVELOVERRIDING, id = "k")
	public void testIgnoreAnnotationsOnReturnValueParameterAndCrossParameter() {
		ConstructorDescriptor descriptor = TestUtil.getConstructorDescriptor(
				IgnoreAnnotations.class,
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
