/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.methodvalidation.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.hibernate.beanvalidation.tck.tests.methodvalidation.model.Customer;

/**
 * @author Gunnar Morling
 */
public class ValidBusinessCustomerValidator
		implements ConstraintValidator<ValidBusinessCustomer, Customer> {

	@Override
	public boolean isValid(Customer value, ConstraintValidatorContext context) {
		if ( value == null ) {
			return false;
		}

		return value.getName() != null;
	}
}
