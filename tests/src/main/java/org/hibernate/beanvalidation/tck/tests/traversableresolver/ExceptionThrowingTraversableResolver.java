/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.traversableresolver;

import java.lang.annotation.ElementType;

import jakarta.validation.Path;
import jakarta.validation.TraversableResolver;

/**
 * @author Emmanuel Bernard
 */
public class ExceptionThrowingTraversableResolver implements TraversableResolver {

	public boolean isReachable(Object o, Path.Node node, Class<?> aClass, Path path, ElementType elementType) {
		throw new RuntimeException( "isReachable failed" );
	}

	public boolean isCascadable(Object o, Path.Node node, Class<?> aClass, Path path, ElementType elementType) {
		throw new RuntimeException( "isCascadable failed" );
	}
}
