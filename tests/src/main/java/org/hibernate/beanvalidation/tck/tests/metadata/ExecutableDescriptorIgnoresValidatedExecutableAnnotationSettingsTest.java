/*
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.metadata;

import static org.hibernate.beanvalidation.tck.util.TestUtil.getConstructorDescriptor;
import static org.hibernate.beanvalidation.tck.util.TestUtil.getMethodDescriptor;

import jakarta.validation.metadata.ConstructorDescriptor;
import jakarta.validation.metadata.MethodDescriptor;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
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

		assertThat( descriptor  ).isNotNull();
		assertThat( descriptor.getName() ).isEqualTo( "setItem"  );
		assertThat( descriptor.getParameterDescriptors().get( 0 ).getConstraintDescriptors().size() ).isEqualTo( 1  );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "h")
	public void testConstructorDescriptorCanBeRetrievedAlsoIfValidateExecutableIsSetToNONEOnTypeLevel() {
		ConstructorDescriptor descriptor = getConstructorDescriptor(
				OrderLine.class,
				String.class
		);

		assertThat( descriptor  ).isNotNull();
		assertThat( descriptor.getName() ).isEqualTo( "OrderLine"  );
		assertThat( descriptor.getParameterDescriptors().get( 0 ).getConstraintDescriptors().size() ).isEqualTo( 1  );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "h")
	public void testMethodDescriptorCanBeRetrievedAlsoIfValidateExecutableIsSetToNONEOnMethodLevel() {
		MethodDescriptor descriptor = getMethodDescriptor(
				Item.class,
				"setName",
				String.class
		);

		assertThat( descriptor  ).isNotNull();
		assertThat( descriptor.getName() ).isEqualTo( "setName"  );
		assertThat( descriptor.getParameterDescriptors().get( 0 ).getConstraintDescriptors().size() ).isEqualTo( 1  );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "h")
	public void testConstructorDescriptorCanBeRetrievedAlsoIfValidateExecutableIsSetToNONEOnConstructorLevel() {
		ConstructorDescriptor descriptor = getConstructorDescriptor(
				Item.class,
				String.class
		);

		assertThat( descriptor  ).isNotNull();
		assertThat( descriptor.getName() ).isEqualTo( "Item"  );
		assertThat( descriptor.getParameterDescriptors().get( 0 ).getConstraintDescriptors().size() ).isEqualTo( 1  );
	}
}
