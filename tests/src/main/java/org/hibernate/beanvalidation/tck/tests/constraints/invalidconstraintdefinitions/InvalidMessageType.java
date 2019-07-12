/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.invalidconstraintdefinitions;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Hardy Ferentschik
 */
@Documented
@Constraint(validatedBy = InvalidMessageType.InvalidDefaultGroupValidator.class)
@Target({ TYPE })
@Retention(RUNTIME)
public @interface InvalidMessageType {
	public abstract Class<?> message() default void.class;

	public abstract Class<?>[] groups() default { };

	public abstract Class<?>[] payload() default { };

	public class InvalidDefaultGroupValidator implements ConstraintValidator<InvalidMessageType, Object> {

		@Override
		public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
			return false;
		}
	}
}
