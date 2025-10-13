/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.constraintdefinition.serviceloading;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ServiceLoadedConstraintValidator implements ConstraintValidator<ServiceLoadedConstraint, Object> {

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		return false;
	}
}
