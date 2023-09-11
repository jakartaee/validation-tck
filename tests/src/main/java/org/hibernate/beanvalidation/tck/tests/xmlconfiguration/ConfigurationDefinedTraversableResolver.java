/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration;

import java.lang.annotation.ElementType;

import jakarta.validation.Path;
import jakarta.validation.TraversableResolver;

/**
 * @author Hardy Ferentschik
 */
public class ConfigurationDefinedTraversableResolver implements TraversableResolver {
	public static int numberOfIsReachableCalls = 0;

	public boolean isReachable(Object traversableObject, Path.Node traversableProperty, Class<?> rootBeanType, Path pathToTraversableObject, ElementType elementType) {
		numberOfIsReachableCalls++;
		return true;
	}

	public boolean isCascadable(Object traversableObject, Path.Node traversableProperty, Class<?> rootBeanType, Path pathToTraversableObject, ElementType elementType) {
		return true;
	}
}
