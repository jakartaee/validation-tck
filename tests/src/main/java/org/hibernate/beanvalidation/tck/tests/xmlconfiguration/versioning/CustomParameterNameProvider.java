/*
* JBoss, Home of Professional Open Source
* Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual contributors
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* http://www.apache.org/licenses/LICENSE-2.0
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.versioning;

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
