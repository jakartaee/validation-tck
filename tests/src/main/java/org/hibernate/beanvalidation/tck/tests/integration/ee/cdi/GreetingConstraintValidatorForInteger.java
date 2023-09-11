/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.integration.ee.cdi;

import jakarta.inject.Inject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * A validator which makes use of constructor injection.
 *
 * @author Gunnar Morling
 */
public class GreetingConstraintValidatorForInteger implements ConstraintValidator<GreetingConstraint, Integer> {

	private final Greeter greeter;
	private String name;

	@Inject
	public GreetingConstraintValidatorForInteger(Greeter greeter) {
		this.greeter = greeter;
	}

	@Override
	public void initialize(GreetingConstraint constraintAnnotation) {
		this.name = constraintAnnotation.name();
	}

	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate( greeter.greetFormally( name ) ).addConstraintViolation();

		return false;
	}
}
