/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.metadata;

import static org.hibernate.beanvalidation.tck.util.TestUtil.getConstructorDescriptor;
import static org.hibernate.beanvalidation.tck.util.TestUtil.getMethodDescriptor;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.Collections;

import javax.validation.Validation;
import javax.validation.metadata.ConstructorDescriptor;
import javax.validation.metadata.MethodDescriptor;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class ExecutableDescriptorIgnoresValidatedExecutableXmlSettingsTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( ExecutableDescriptorIgnoresValidatedExecutableXmlSettingsTest.class )
				.withClasses(
						StockItem.class
				)
				.withValidationXml( "validation-ExecutableDescriptorIgnoresValidatedExecutableXmlSettingsTest.xml" )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "h")
	public void testMethodDescriptorCanBeRetrievedAlsoIfValidateExecutableIsSetToNONEInXml() {
		assertEquals(
				Validation.byDefaultProvider()
						.configure()
						.getBootstrapConfiguration()
						.getDefaultValidatedExecutableTypes(),
				Collections.emptySet()
		);

		MethodDescriptor descriptor = getMethodDescriptor(
				StockItem.class,
				"setName",
				String.class
		);

		assertNotNull( descriptor );
		assertEquals( descriptor.getName(), "setName" );
		assertEquals( descriptor.getParameterDescriptors().get( 0 ).getConstraintDescriptors().size(), 1 );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "h")
	public void testConstructorDescriptorCanBeRetrievedAlsoIfValidateExecutableIsSetToNONEInXml() {
		assertEquals(
				Validation.byDefaultProvider()
						.configure()
						.getBootstrapConfiguration()
						.getDefaultValidatedExecutableTypes(),
				Collections.emptySet()
		);

		ConstructorDescriptor descriptor = getConstructorDescriptor(
				StockItem.class,
				String.class
		);

		assertNotNull( descriptor );
		assertEquals( descriptor.getName(), "StockItem" );
		assertEquals( descriptor.getParameterDescriptors().get( 0 ).getConstraintDescriptors().size(), 1 );
	}
}
