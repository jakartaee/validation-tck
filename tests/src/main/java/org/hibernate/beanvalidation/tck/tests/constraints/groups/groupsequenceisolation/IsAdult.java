/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.groups.groupsequenceisolation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

/**
 * @author Hardy Ferentschik
 */
@Constraint(validatedBy = { IsAdult.IsAdultValidator.class })
@Target({ METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Documented
public @interface IsAdult {
	public abstract String message() default "You are too young!";

	public abstract Class<?>[] groups() default { };

	public abstract Class<? extends Payload>[] payload() default {};


	public class IsAdultValidator implements ConstraintValidator<IsAdult, Integer> {

		@Override
		public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
			if ( value == null ) {
				return true;
			}
			return value >= 18;
		}
	}
}
