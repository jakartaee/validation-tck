/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.constraintcomposition;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintTarget;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.validation.constraintvalidation.SupportedValidationTarget;
import jakarta.validation.constraintvalidation.ValidationTarget;

/**
 * @author Gunnar Morling
 */
@Documented
@Constraint(validatedBy = { ComposedConstraint.Validator.class })
@ComposingConstraintSupportingParameters
@ComposingConstraintSupportingAnnotatedElement
@Target({ METHOD, CONSTRUCTOR, TYPE, FIELD })
@Retention(RUNTIME)
public @interface ComposedConstraint {
	String message() default "default message";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	ConstraintTarget validationAppliesTo() default ConstraintTarget.IMPLICIT;

	@SupportedValidationTarget({ ValidationTarget.PARAMETERS, ValidationTarget.ANNOTATED_ELEMENT })
	public static class Validator
			implements ConstraintValidator<ComposedConstraint, Object[]> {

		@Override
		public boolean isValid(Object[] parameters, ConstraintValidatorContext constraintValidatorContext) {
			return true;
		}
	}
}
