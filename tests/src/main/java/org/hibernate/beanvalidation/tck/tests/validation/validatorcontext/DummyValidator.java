/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation.validatorcontext;

import java.util.Map;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Hardy Ferentschik
 */
public class DummyValidator implements ConstraintValidator<Dummy, String> {

	private static boolean disableDefaultError;

	private static Map<String, String> errorMessages;

	@Override
	@SuppressWarnings("deprecation")
	public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
		if ( disableDefaultError ) {
			constraintValidatorContext.disableDefaultConstraintViolation();
		}

		if ( errorMessages != null ) {
			for ( Map.Entry<String, String> entry : errorMessages.entrySet() ) {
				if ( entry.getKey() == null ) {
					constraintValidatorContext.buildConstraintViolationWithTemplate( entry.getValue() )
							.addConstraintViolation();
				}
				else {
					constraintValidatorContext.buildConstraintViolationWithTemplate( entry.getValue() )
							.addNode( entry.getKey() )
							.addConstraintViolation();
				}
			}
		}

		return false;
	}

	public static void disableDefaultError(boolean b) {
		disableDefaultError = b;
	}

	public static void setCustomErrorMessages(Map<String, String> errorMessages) {
		DummyValidator.errorMessages = errorMessages;
	}
}
