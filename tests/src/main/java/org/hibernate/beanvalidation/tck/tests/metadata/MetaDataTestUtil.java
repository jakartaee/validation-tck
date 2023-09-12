/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.metadata;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jakarta.validation.metadata.ConstraintDescriptor;
import jakarta.validation.metadata.ContainerElementTypeDescriptor;

/**
 * @author Guillaume Smet
 */
class MetaDataTestUtil {

	static ContainerElementTypeDescriptor getContainerElementDescriptor(Set<ContainerElementTypeDescriptor> containerElementTypeDescriptors,
			Class<?> containerClass, int typeArgumentIndex) {
		for ( ContainerElementTypeDescriptor containerElementTypeDescriptor : containerElementTypeDescriptors ) {
			if ( containerClass.equals( containerElementTypeDescriptor.getContainerClass() ) &&
					typeArgumentIndex == containerElementTypeDescriptor.getTypeArgumentIndex() ) {
				return containerElementTypeDescriptor;
			}
		}

		throw new IllegalStateException(
				String.format( "Container element descriptor not found for container class %1$s and type parameter %2$s", containerClass, typeArgumentIndex ) );
	}

	@SafeVarargs
	static void assertConstraintDescriptors(Set<ConstraintDescriptor<?>> constraintDescriptors, Class<? extends Annotation>... expectedConstraints) {
		List<Class<? extends Annotation>> actualConstraints = new ArrayList<>();
		for ( ConstraintDescriptor<?> constraintDescriptor : constraintDescriptors ) {
			actualConstraints.add( constraintDescriptor.getAnnotation().annotationType() );
		}
		assertThat( actualConstraints ).containsExactlyInAnyOrder( expectedConstraints );
	}
}
