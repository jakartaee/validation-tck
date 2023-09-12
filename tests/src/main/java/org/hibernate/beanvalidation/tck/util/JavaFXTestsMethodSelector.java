/**
 * Jakarta Validation TCK
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
 * TestNG test selector which will, depending on the system property <i>includeJavaFXTests</i> and
 * the existence of the {@code @JavaFXTest} annotation on a test class, in- or exclude the test.
 *
 * @author Hardy Ferentschik
 * @author Guillaume Smet
 */
public class JavaFXTestsMethodSelector implements IMethodSelector {

	/**
	 * Name of the system property for excluding integration tests.
	 */
	private static final String INCLUDE_JAVAFX_TESTS = "includeJavaFXTests";

	private static boolean includeJavaFXTests = false;

	static {
		String envSetting = System.getProperty( INCLUDE_JAVAFX_TESTS );
		includeJavaFXTests = Boolean.valueOf( envSetting );
	}

	@Override
	public boolean includeMethod(IMethodSelectorContext context, ITestNGMethod method, boolean isTestMethod) {
		if ( !includeJavaFXTests && method.getConstructorOrMethod().getDeclaringClass().isAnnotationPresent(
				JavaFXTest.class
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
