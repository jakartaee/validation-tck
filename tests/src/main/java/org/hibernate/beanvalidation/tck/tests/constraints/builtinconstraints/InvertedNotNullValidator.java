/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.builtinconstraints;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.NotNull;

/**
 * @author Hardy Ferentschik
 */
public class InvertedNotNullValidator implements ConstraintValidator<NotNull, Object> {

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
		return value == null;
	}
}
