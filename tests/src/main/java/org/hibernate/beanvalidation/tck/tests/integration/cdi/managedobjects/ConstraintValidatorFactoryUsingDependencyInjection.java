/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.integration.cdi.managedobjects;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorFactory;

/**
 * @author Gunnar Morling
 */
public class ConstraintValidatorFactoryUsingDependencyInjection
		implements ConstraintValidatorFactory {

	@Inject
	private Greeter greeter;

	@Override
	public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {
		GreetingConstraintValidator validator = new GreetingConstraintValidator( greeter.greet() );
		@SuppressWarnings("unchecked")
		T instance = (T) validator;
		return instance;
	}

	@Override
	public void releaseInstance(ConstraintValidator<?, ?> instance) {
	}
}
