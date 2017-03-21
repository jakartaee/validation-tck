/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.constructorvalidation;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

import javax.validation.metadata.ConstructorDescriptor;
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
public class DisabledCascadedValidationTest extends Arquillian {

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
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
