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

import jakarta.validation.metadata.ConstructorDescriptor;
import jakarta.validation.metadata.MethodDescriptor;

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
public class ExecutableDescriptorIgnoresValidatedExecutableAnnotationSettingsTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( ExecutableDescriptorIgnoresValidatedExecutableAnnotationSettingsTest.class )
				.withClasses(
						Item.class,
						OrderLine.class
				)
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "h")
	public void testMethodDescriptorCanBeRetrievedAlsoIfValidateExecutableIsSetToNONEOnTypeLevel() {
		MethodDescriptor descriptor = getMethodDescriptor(
				OrderLine.class,
				"setItem",
				String.class
		);

		assertNotNull( descriptor );
		assertEquals( descriptor.getName(), "setItem" );
		assertEquals( descriptor.getParameterDescriptors().get( 0 ).getConstraintDescriptors().size(), 1 );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "h")
	public void testConstructorDescriptorCanBeRetrievedAlsoIfValidateExecutableIsSetToNONEOnTypeLevel() {
		ConstructorDescriptor descriptor = getConstructorDescriptor(
				OrderLine.class,
				String.class
		);

		assertNotNull( descriptor );
		assertEquals( descriptor.getName(), "OrderLine" );
		assertEquals( descriptor.getParameterDescriptors().get( 0 ).getConstraintDescriptors().size(), 1 );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "h")
	public void testMethodDescriptorCanBeRetrievedAlsoIfValidateExecutableIsSetToNONEOnMethodLevel() {
		MethodDescriptor descriptor = getMethodDescriptor(
				Item.class,
				"setName",
				String.class
		);

		assertNotNull( descriptor );
		assertEquals( descriptor.getName(), "setName" );
		assertEquals( descriptor.getParameterDescriptors().get( 0 ).getConstraintDescriptors().size(), 1 );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "h")
	public void testConstructorDescriptorCanBeRetrievedAlsoIfValidateExecutableIsSetToNONEOnConstructorLevel() {
		ConstructorDescriptor descriptor = getConstructorDescriptor(
				Item.class,
				String.class
		);

		assertNotNull( descriptor );
		assertEquals( descriptor.getName(), "Item" );
		assertEquals( descriptor.getParameterDescriptors().get( 0 ).getConstraintDescriptors().size(), 1 );
	}
}
