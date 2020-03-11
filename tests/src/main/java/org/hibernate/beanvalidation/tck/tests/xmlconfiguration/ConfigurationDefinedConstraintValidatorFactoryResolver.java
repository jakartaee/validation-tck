/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorFactory;
import jakarta.validation.ValidationException;

/**
 * @author Hardy Ferentschik
 */
public class ConfigurationDefinedConstraintValidatorFactoryResolver implements ConstraintValidatorFactory {
	public static int numberOfIsReachableCalls = 0;

	@Override
	public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {
		numberOfIsReachableCalls++;
		throw new ValidationException( "Exception in ConfigurationDefinedConstraintValidatorFactoryResolver" );
	}

	@Override
	public void releaseInstance(ConstraintValidator<?, ?> instance) {
	}
}
