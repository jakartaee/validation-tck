/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.constraintdefinition;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ValidationException;

/**
 * Check that a char sequence's length is between min and max.
 *
 * @author Hardy Ferentschik
 */
public class DummyLengthValidator implements ConstraintValidator<Length, CharSequence> {
	private int min;
	private int max;

	@Override
	public void initialize(Length parameters) {
		min = parameters.min();
		max = parameters.max();
		validateParameters();
	}

	@Override
	public boolean isValid(CharSequence value, ConstraintValidatorContext constraintValidatorContext) {
		if ( value == null ) {
			return true;
		}
		int length = value.length();
		return length >= min && length <= max;
	}

	private void validateParameters() {
		if ( min < 0 ) {
			throw new ValidationException( "The min parameter cannot be negative." );
		}
		if ( max < 0 ) {
			throw new ValidationException( "The max paramter cannot be negative." );
		}
		if ( max < min ) {
			throw new ValidationException( "The length cannot be negative." );
		}
	}
}
