/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.integration.cdi.factory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

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

	@PostConstruct
	private void initialize() {
		name = "Mr. ";
	}

	@Override
	public void initialize(GreetingConstraint constraintAnnotation) {
		name = name + constraintAnnotation.name();
	}

	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate( greeter.greetFormally( name ) ).addConstraintViolation();

		return false;
	}
}
