/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.integration.cdi.managedobjects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Gunnar Morling
 */
public class GreetingConstraintValidator implements ConstraintValidator<GreetingConstraint, Object> {

	private final String message;

	public GreetingConstraintValidator(String message) {
		this.message = message;
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		return false;
	}

	public String getMessage() {
		return message;
	}
}
