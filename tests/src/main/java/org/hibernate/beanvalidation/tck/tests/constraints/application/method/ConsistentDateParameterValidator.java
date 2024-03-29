/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.application.method;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraintvalidation.SupportedValidationTarget;
import jakarta.validation.constraintvalidation.ValidationTarget;

/**
 * @author Gunnar Morling
 */
@SupportedValidationTarget(value = ValidationTarget.PARAMETERS)
public class ConsistentDateParameterValidator implements ConstraintValidator<ConsistentDateParameters, Object[]> {

	@Override
	public boolean isValid(Object[] value, ConstraintValidatorContext context) {
		return false;
	}
}
