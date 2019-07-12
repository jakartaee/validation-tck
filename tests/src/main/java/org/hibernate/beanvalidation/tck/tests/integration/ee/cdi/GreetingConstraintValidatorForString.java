/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.integration.ee.cdi;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Gunnar Morling
 */
public class GreetingConstraintValidatorForString implements ConstraintValidator<GreetingConstraint, String> {

	@Inject
	private Greeter greeter;

	private String name;

	public GreetingConstraintValidatorForString() {
	}

	@Override
	public void initialize(GreetingConstraint constraintAnnotation) {
		this.name = constraintAnnotation.name();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate( greeter.greet( name ) ).addConstraintViolation();

		return false;
	}
}
