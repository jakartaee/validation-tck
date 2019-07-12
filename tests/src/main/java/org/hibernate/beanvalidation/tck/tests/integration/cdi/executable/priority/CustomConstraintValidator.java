/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.integration.cdi.executable.priority;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CustomConstraintValidator implements ConstraintValidator<CustomConstraint, Object> {

	@Inject
	private InvocationTracker invocationTracker;

	@Override
	public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
		assertTrue( invocationTracker.isEarlierInterceptorInvoked() );
		assertFalse( invocationTracker.isLaterInterceptorInvoked() );

		invocationTracker.setValidatorInvoked( true );
		return true;
	}
}
