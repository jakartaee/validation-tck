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
 * @author Gunnar Morling
 */
public class GreetingConstraintValidatorForString implements ConstraintValidator<GreetingConstraint, String> {

	@Inject
	private Greeter greeter;

	private String name;

	public GreetingConstraintValidatorForString() {
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
	public boolean isValid(String value, ConstraintValidatorContext context) {
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate( greeter.greet( name ) ).addConstraintViolation();

		return false;
	}
}
