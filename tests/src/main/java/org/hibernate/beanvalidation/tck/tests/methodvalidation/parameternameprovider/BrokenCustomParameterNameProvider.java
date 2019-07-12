/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.methodvalidation.parameternameprovider;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

import javax.validation.ParameterNameProvider;

/**
 * @author Gunnar Morling
 */
public class BrokenCustomParameterNameProvider implements ParameterNameProvider {

	@Override
	public List<String> getParameterNames(Constructor<?> constructor) {
		throw new UnsupportedOperationException( "Exception in ParameterNameProvider" );
	}

	@Override
	public List<String> getParameterNames(Method method) {
		throw new UnsupportedOperationException( "Exception in ParameterNameProvider" );
	}
}
