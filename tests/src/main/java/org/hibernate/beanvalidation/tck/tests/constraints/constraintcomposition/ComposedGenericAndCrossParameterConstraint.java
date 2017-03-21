/**
 * Bean Validation TCK
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

import javax.validation.Constraint;
import javax.validation.ConstraintTarget;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

/**
 * @author Gunnar Morling
 */
@Documented
@Constraint(validatedBy = {
		ComposedGenericAndCrossParameterConstraint.ObjectValidator.class,
		ComposedGenericAndCrossParameterConstraint.ParametersValidator.class
})
@GenericAndCrossParameterConstraint(validationAppliesTo = ConstraintTarget.IMPLICIT)
@Target({ METHOD, CONSTRUCTOR, TYPE, FIELD })
@Retention(RUNTIME)
public @interface ComposedGenericAndCrossParameterConstraint {
	String message() default "default message";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	ConstraintTarget validationAppliesTo() default ConstraintTarget.IMPLICIT;

	public static class ObjectValidator
			implements ConstraintValidator<ComposedGenericAndCrossParameterConstraint, Object> {

		@Override
		public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
			return true;
		}
	}

	@SupportedValidationTarget(value = ValidationTarget.PARAMETERS)
	public static class ParametersValidator
			implements ConstraintValidator<ComposedGenericAndCrossParameterConstraint, Object[]> {

		@Override
		public boolean isValid(Object[] parameters, ConstraintValidatorContext constraintValidatorContext) {
			return true;
		}
	}
}
