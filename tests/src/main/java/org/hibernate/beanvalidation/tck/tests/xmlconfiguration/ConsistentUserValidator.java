/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Hardy Ferentschik
 */
public class ConsistentUserValidator implements ConstraintValidator<ConsistentUserInformation, User> {

	@Override
	public boolean isValid(User user, ConstraintValidatorContext constraintValidatorContext) {
		return user.isConsistent();
	}
}
