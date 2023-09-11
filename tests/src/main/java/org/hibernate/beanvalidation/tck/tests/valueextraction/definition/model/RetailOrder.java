/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.valueextraction.definition.model;

import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;


@Target(TYPE_USE)
@Constraint(validatedBy = { RetailOrder.Validator.class })
@Documented
@Retention(RUNTIME)
public @interface RetailOrder {

	String message() default "not a valid retail order";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	public class Validator implements ConstraintValidator<RetailOrder, Order> {

		@Override
		public boolean isValid(Order order, ConstraintValidatorContext constraintValidatorContext) {
			if ( order == null || order.getId() == null ) {
				return true;
			}

			return order.getId().startsWith( "RETAIL-" );
		}
	}
}
