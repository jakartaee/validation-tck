/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.invalidconstraintdefinitions;

import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Hardy Ferentschik
 */
@Documented
@Constraint(validatedBy = InvalidGroupsType.InvalidDefaultGroupValidator.class)
@Target({ TYPE })
@Retention(RUNTIME)
public @interface InvalidGroupsType {
	public abstract String message() default "";

	public abstract String groups() default "";

	public abstract Class<?>[] payload() default { };

	public class InvalidDefaultGroupValidator implements ConstraintValidator<InvalidGroupsType, Object> {

		@Override
		public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
			return false;
		}
	}
}
