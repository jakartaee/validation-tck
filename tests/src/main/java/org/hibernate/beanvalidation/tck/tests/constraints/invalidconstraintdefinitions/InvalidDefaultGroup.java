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
import javax.validation.Payload;
import javax.validation.groups.Default;

/**
 * @author Hardy Ferentschik
 */
@Documented
@Constraint(validatedBy = InvalidDefaultGroup.InvalidDefaultGroupValidator.class)
@Target({ TYPE })
@Retention(RUNTIME)
public @interface InvalidDefaultGroup {
	public abstract String message() default "default message";

	public abstract Class<?>[] groups() default Default.class;

	public abstract Class<? extends Payload>[] payload() default {};

	public class InvalidDefaultGroupValidator implements ConstraintValidator<InvalidDefaultGroup, Object> {

		@Override
		public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
			return false;
		}
	}
}
