/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
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
