/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.integration.cdi.managedobjects;

import java.lang.annotation.ElementType;

import javax.inject.Inject;
import javax.validation.Path;
import javax.validation.Path.Node;
import javax.validation.TraversableResolver;

/**
 * @author Gunnar Morling
 */
public class TraversableResolverUsingDependencyInjection implements TraversableResolver {

	@Inject
	private Greeter greeter;

	@Override
	public boolean isReachable(Object traversableObject,
							   Node traversableProperty, Class<?> rootBeanType,
							   Path pathToTraversableObject, ElementType elementType) {

		( (MessageHolder) traversableObject ).setValue( greeter.greet() );
		return false;
	}

	@Override
	public boolean isCascadable(Object traversableObject,
								Node traversableProperty, Class<?> rootBeanType,
								Path pathToTraversableObject, ElementType elementType) {

		( (MessageHolder) traversableObject ).setValue( greeter.greet() );
		return false;
	}
}
