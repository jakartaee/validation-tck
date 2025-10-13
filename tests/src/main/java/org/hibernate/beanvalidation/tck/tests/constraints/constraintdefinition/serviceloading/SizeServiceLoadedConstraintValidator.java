/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.constraintdefinition.serviceloading;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.Size;

public class SizeServiceLoadedConstraintValidator implements ConstraintValidator<Size, Book> {

	private int max;

	@Override
	public void initialize(Size constraintAnnotation) {
		max = constraintAnnotation.max();
	}

	@Override
	public boolean isValid(Book value, ConstraintValidatorContext context) {
		if ( value == null ) {
			return true;
		}

		if ( value.pages <= max ) {
			return true;
		}
		return false;
	}
}
