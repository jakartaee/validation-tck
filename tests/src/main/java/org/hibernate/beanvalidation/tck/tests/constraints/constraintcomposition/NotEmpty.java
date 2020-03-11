/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.constraintcomposition;

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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * @author Emmanuel Bernard
 */
@Documented
@NotNull(message = "may not be null", groups = NotEmpty.DummyGroup.class)
@Size(min = 1)
@Constraint(validatedBy = NotEmpty.NotEmptyValidator.class)
@Target({ TYPE, METHOD, FIELD })
@Retention(RUNTIME)
public @interface NotEmpty {
	String message() default "{constraint.notEmpty}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default {};

	public class NotEmptyValidator implements ConstraintValidator<NotEmpty, String> {

		@Override
		public boolean isValid(String object, ConstraintValidatorContext constraintValidatorContext) {
			return true;
		}
	}

	public interface DummyGroup {

	}
}
