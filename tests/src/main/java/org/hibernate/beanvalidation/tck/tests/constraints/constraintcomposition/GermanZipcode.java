/**
 * Jakarta Validation TCK
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
import jakarta.validation.ReportAsSingleViolation;

/**
 * Constraint used to test nested composing constraints.
 *
 * @author Hardy Ferentschik
 */
@FrenchZipcode
@Constraint(validatedBy = GermanZipcode.GermanZipcodeConstraintValidator.class)
@Documented
@Target({ METHOD, FIELD, TYPE })
@Retention(RUNTIME)
@ReportAsSingleViolation
public @interface GermanZipcode {

	public abstract String message() default "Falsche Postnummer.";

	public abstract Class<?>[] groups() default {};

	public abstract Class<? extends Payload>[] payload() default {};

	public class GermanZipcodeConstraintValidator implements ConstraintValidator<GermanZipcode, String> {

		@Override
		public boolean isValid(String object, ConstraintValidatorContext constraintValidatorContext) {
			return true;
		}
	}
}
