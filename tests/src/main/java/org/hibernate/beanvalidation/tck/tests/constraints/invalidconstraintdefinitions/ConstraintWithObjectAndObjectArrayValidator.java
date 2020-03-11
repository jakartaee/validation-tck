/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.invalidconstraintdefinitions;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.validation.constraintvalidation.SupportedValidationTarget;
import jakarta.validation.constraintvalidation.ValidationTarget;

/**
 * @author Gunnar Morling
 */
@Documented
@Constraint(validatedBy = {
		ConstraintWithObjectAndObjectArrayValidator.Validator.class,
		ConstraintWithObjectAndObjectArrayValidator.AnotherValidator.class
})
@Target({ METHOD })
@Retention(RUNTIME)
public @interface ConstraintWithObjectAndObjectArrayValidator {
	String message() default "default message";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	@SupportedValidationTarget(value = ValidationTarget.PARAMETERS)
	public static class Validator
			implements ConstraintValidator<ConstraintWithObjectAndObjectArrayValidator, Object> {

		@Override
		public boolean isValid(Object parameters, ConstraintValidatorContext constraintValidatorContext) {
			return false;
		}
	}

	@SupportedValidationTarget(value = ValidationTarget.PARAMETERS)
	public static class AnotherValidator
			implements ConstraintValidator<ConstraintWithObjectAndObjectArrayValidator, Object[]> {

		@Override
		public boolean isValid(Object[] parameters, ConstraintValidatorContext constraintValidatorContext) {
			return false;
		}
	}
}
