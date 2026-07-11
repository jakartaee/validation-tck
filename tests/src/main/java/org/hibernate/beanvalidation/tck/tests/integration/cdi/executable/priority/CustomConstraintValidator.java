/*
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.integration.cdi.executable.priority;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.inject.Inject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CustomConstraintValidator implements ConstraintValidator<CustomConstraint, Object> {

	@Inject
	private InvocationTracker invocationTracker;

	@Override
	public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
		assertThat( invocationTracker.isEarlierInterceptorInvoked() ).isTrue();
		assertThat( invocationTracker.isLaterInterceptorInvoked() ).isFalse();

		invocationTracker.setValidatorInvoked( true );
		return true;
	}
}
