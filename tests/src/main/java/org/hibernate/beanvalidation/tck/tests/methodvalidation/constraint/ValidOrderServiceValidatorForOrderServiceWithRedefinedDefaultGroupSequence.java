/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.methodvalidation.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.hibernate.beanvalidation.tck.tests.methodvalidation.service.OrderServiceWithRedefinedDefaultGroupSequence;

/**
 * @author Gunnar Morling
 */
public class ValidOrderServiceValidatorForOrderServiceWithRedefinedDefaultGroupSequence
		implements ConstraintValidator<ValidOrderService, OrderServiceWithRedefinedDefaultGroupSequence> {

	@Override
	public boolean isValid(OrderServiceWithRedefinedDefaultGroupSequence value, ConstraintValidatorContext context) {
		return "valid".equals( value.getName() );
	}
}
