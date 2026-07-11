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
 * JUnit execution condition which will, depending on the system property <i>excludeIntegrationTests</i> and
 * the existence of the {@code @IntegrationTest} annotation on a test class, in- or exclude the test.
 *
 * @author Hardy Ferentschik
 */
public class IntegrationTestsMethodSelector implements ExecutionCondition {

	private static final String EXCLUDE_INTEGRATION_TESTS = "excludeIntegrationTests";

	@Override
	public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
		boolean excludeIntegrationTests = Boolean.getBoolean( EXCLUDE_INTEGRATION_TESTS );

		if ( excludeIntegrationTests && context.getRequiredTestClass().isAnnotationPresent( IntegrationTest.class ) ) {
			return ConditionEvaluationResult.disabled( "Integration tests are excluded" );
		}

		return ConditionEvaluationResult.enabled( "Integration tests are included" );
	}
}
