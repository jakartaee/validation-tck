/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.constraintdefinition;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

/**
 * @author Hardy Ferentschik
 */
@Documented
@Constraint(validatedBy = AlwaysValid.StaticConstraintValidator.class)
@Target({ TYPE, METHOD, FIELD })
@Retention(RUNTIME)
public @interface AlwaysValid {
	public abstract String message() default "default message";

	public abstract Class<?>[] groups() default { };

	public abstract boolean alwaysValid();

	public abstract Class<? extends Payload>[] payload() default {};

	public class StaticConstraintValidator implements ConstraintValidator<AlwaysValid, Object> {

		boolean valid;

		@Override
		public void initialize(AlwaysValid parameters) {
			valid = parameters.alwaysValid();
		}

		@Override
		public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
			return valid;
		}
	}
}
