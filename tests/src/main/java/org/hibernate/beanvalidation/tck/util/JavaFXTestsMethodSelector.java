/*
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.util;

import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * JUnit execution condition which will, depending on the system property <i>includeJavaFXTests</i> and
 * the existence of the {@code @JavaFXTest} annotation on a test class, in- or exclude the test.
 *
 * @author Hardy Ferentschik
 * @author Guillaume Smet
 */
public class JavaFXTestsMethodSelector implements ExecutionCondition {

	private static final String INCLUDE_JAVAFX_TESTS = "includeJavaFXTests";

	@Override
	public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
		boolean includeJavaFXTests = Boolean.getBoolean( INCLUDE_JAVAFX_TESTS );

		if ( !includeJavaFXTests && context.getRequiredTestClass().isAnnotationPresent( JavaFXTest.class ) ) {
			return ConditionEvaluationResult.disabled( "JavaFX tests are excluded" );
		}

		return ConditionEvaluationResult.enabled( "JavaFX tests are included" );
	}
}
