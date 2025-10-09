/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.constructorvalidation;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

import jakarta.validation.metadata.ConstructorDescriptor;
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
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONSTRUCTORLEVELOVERRIDING, id = "n")
	public void testValidAnnotationIsIgnored() throws Exception {
		ConstructorDescriptor descriptor = TestUtil.getConstructorDescriptor( Cascaded.class, String.class );
		assertNotNull( descriptor, "the specified constructor should be configured in xml" );

		ReturnValueDescriptor returnValueDescriptor = descriptor.getReturnValueDescriptor();
		assertFalse( returnValueDescriptor.isCascaded(), "Cascaded validation should disabled" );

		ParameterDescriptor parameterDescriptor = descriptor.getParameterDescriptors().get( 0 );
		assertFalse( parameterDescriptor.isCascaded(), "Cascaded validation should disabled" );
	}
}
