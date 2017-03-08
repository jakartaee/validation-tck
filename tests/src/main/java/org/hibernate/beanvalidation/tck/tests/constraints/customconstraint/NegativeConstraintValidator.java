/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.customconstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Hardy Ferentschik
 */
public class NegativeConstraintValidator implements ConstraintValidator<Negative, Integer> {

	@Override
	public void initialize(Negative constraintAnnotation) {
		throw new RuntimeException( "Throwing a RuntimeException from NegativeConstraintValidator.initialize" );
	}

	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
		return true;
	}
}
