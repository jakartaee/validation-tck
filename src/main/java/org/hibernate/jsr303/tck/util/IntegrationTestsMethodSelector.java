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
package org.hibernate.jsr303.tck.util;

import java.util.List;

import org.testng.IMethodSelector;
import org.testng.IMethodSelectorContext;
import org.testng.ITestNGMethod;

/**
 * @author Hardy Ferentschik
 */
public class IntegrationTestsMethodSelector implements IMethodSelector {
	private static String INCLUDE_INTEGRATION_TESTS = "includeIntegrationTests";
	private static boolean includeIntegrationTests;

	static {
		String envSetting = System.getProperty( INCLUDE_INTEGRATION_TESTS );
		includeIntegrationTests = Boolean.valueOf( envSetting );
	}

	public boolean includeMethod(IMethodSelectorContext context, ITestNGMethod method, boolean isTestMethod) {
		if ( !includeIntegrationTests && method.getConstructorOrMethod()
				.getDeclaringClass()
				.isAnnotationPresent( IntegrationTest.class ) ) {
			context.setStopped( true );
			return false;
		}
		else {
			return true;
		}
	}

	public void setTestMethods(List<ITestNGMethod> testMethods) {
	}
}


