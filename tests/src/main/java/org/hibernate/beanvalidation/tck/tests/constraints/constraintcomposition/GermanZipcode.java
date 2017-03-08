/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.constraintcomposition;

import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.ReportAsSingleViolation;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

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
