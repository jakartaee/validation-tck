/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.methodvalidation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import javax.validation.metadata.CrossParameterDescriptor;
import javax.validation.metadata.MethodDescriptor;
import javax.validation.metadata.ParameterDescriptor;
import javax.validation.metadata.ReturnValueDescriptor;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class IgnoreAnnotationsOnMethodTest extends AbstractTCKTest {
	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( IgnoreAnnotationsOnMethodTest.class )
				.withClass( IgnoreAnnotations.class )
				.withValidationXml( "validation-IgnoreAnnotationsOnMethodTest.xml" )
				.withResource( "ignore-annotations-IgnoreAnnotationsOnMethodTest.xml" )
				.build();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_METHODLEVELOVERRIDING, id = "l"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_METHODLEVELOVERRIDING, id = "m"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_METHODLEVELOVERRIDING, id = "n"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_METHODLEVELOVERRIDING, id = "p")
	})
	public void testIgnoreAnnotationsOnMethodLevel() {
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
		assertTrue( returnValueDescriptor.getGroupConversions().isEmpty(), "Group conversions should be ignored" );

		ParameterDescriptor parameterDescriptor = descriptor.getParameterDescriptors().get( 0 );
		assertFalse( parameterDescriptor.hasConstraints(), "First parameter constraints should be ignored." );
		assertTrue( parameterDescriptor.getGroupConversions().isEmpty(), "Group conversions should be ignored" );

		parameterDescriptor = descriptor.getParameterDescriptors().get( 1 );
		assertTrue( parameterDescriptor.hasConstraints(), "Second parameter constraints should be applied." );
		assertEquals( parameterDescriptor.getGroupConversions().size(), 2, "All group conversions should be combined" );
	}
}
