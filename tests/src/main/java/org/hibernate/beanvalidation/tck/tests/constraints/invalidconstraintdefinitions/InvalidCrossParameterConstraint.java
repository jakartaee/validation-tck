/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.invalidconstraintdefinitions;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
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
import jakarta.validation.constraintvalidation.SupportedValidationTarget;
import jakarta.validation.constraintvalidation.ValidationTarget;

/**
 * @author Gunnar Morling
 */
@Constraint(validatedBy = InvalidCrossParameterConstraint.Validator.class)
@Target({ TYPE, METHOD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Documented
public @interface InvalidCrossParameterConstraint {
	String message() default "{validation.invalidCrossParameterConstraint}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	@SupportedValidationTarget(ValidationTarget.PARAMETERS)
	public static class Validator implements ConstraintValidator<InvalidCrossParameterConstraint, Integer> {

		@Override
		public boolean isValid(Integer value, ConstraintValidatorContext context) {
			return false;
		}
	}
}
