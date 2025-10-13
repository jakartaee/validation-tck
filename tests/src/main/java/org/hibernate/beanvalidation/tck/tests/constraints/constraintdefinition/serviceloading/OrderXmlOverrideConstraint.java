/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.constraintdefinition.serviceloading;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = {})
@Target({ TYPE, METHOD, FIELD })
@Retention(RUNTIME)
public @interface OrderXmlOverrideConstraint {

	String message() default "message";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	class OrderXmlOverrideConstraintConstraintValidator implements ConstraintValidator<OrderXmlOverrideConstraint, OrderXmlOverrideBean> {

		@Override
		public boolean isValid(OrderXmlOverrideBean value, ConstraintValidatorContext context) {
			if ( value.foo == null ) {
				return true;
			}
			return "bar".equals( value.foo );
		}
	}
}
