/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.integration.cdi.managedobjects;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import jakarta.inject.Inject;
import jakarta.validation.ParameterNameProvider;

/**
 * @author Gunnar Morling
 */
public class ParameterNameProviderUsingDependencyInjection implements ParameterNameProvider {

	@Inject
	private Greeter greeter;

	@Override
	public List<String> getParameterNames(Constructor<?> constructor) {
		return Arrays.asList( greeter.greet() );
	}

	@Override
	public List<String> getParameterNames(Method method) {
		return null;
	}
}
