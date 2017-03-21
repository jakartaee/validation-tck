/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.validation.ParameterNameProvider;

/**
 * @author Gunnar Morling
 */
public class CustomParameterNameProvider implements ParameterNameProvider {

	@Override
	public List<String> getParameterNames(Constructor<?> constructor) {
		List<String> names = new ArrayList<String>();

		for ( int i = 0; i < constructor.getParameterTypes().length; i++ ) {
			names.add( "param" + i );
		}

		return names;
	}

	@Override
	public List<String> getParameterNames(Method method) {
		List<String> names = new ArrayList<String>();

		for ( int i = 0; i < method.getParameterTypes().length; i++ ) {
			names.add( "param" + i );
		}

		return names;
	}
}
