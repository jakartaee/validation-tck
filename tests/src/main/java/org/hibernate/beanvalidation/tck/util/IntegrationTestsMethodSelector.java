/*
* JBoss, Home of Professional Open Source
* Copyright 2012, Red Hat, Inc. and/or its affiliates, and individual contributors
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
package org.hibernate.beanvalidation.tck.util;

import java.util.List;

import org.testng.IMethodSelector;
import org.testng.IMethodSelectorContext;
import org.testng.ITestNGMethod;

/**
 * TestNG test selector which will depending on the system property <i>excludeIntegrationTests</i> and
 * the existence of the {@code @IntegrationTest} annotation on a test class, in- or exclude the test.
 *
 * @author Hardy Ferentschik
 */
public class IntegrationTestsMethodSelector implements IMethodSelector {

	/**
	 * Name of the system property for excluding integration tests.
	 */
	private static final String EXCLUDE_INTEGRATION_TESTS = "excludeIntegrationTests";

	private static boolean excludeIntegrationTests = false;

	static {
		String envSetting = System.getProperty( EXCLUDE_INTEGRATION_TESTS );
		excludeIntegrationTests = Boolean.valueOf( envSetting );
	}

	@Override
	public boolean includeMethod(IMethodSelectorContext context, ITestNGMethod method, boolean isTestMethod) {
		if ( excludeIntegrationTests && method.getConstructorOrMethod().getDeclaringClass().isAnnotationPresent(
				IntegrationTest.class
		) ) {
			context.setStopped( true );
			return false;
		}
		else {
			return true;
		}
	}

	@Override
	public void setTestMethods(List<ITestNGMethod> testMethods) {
	}
}
