/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.integration.cdi.executable;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
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

/**
 * @author Gunnar Morling
 */
@Documented
@Constraint(validatedBy = ValidPersonService.Validator.class)
@Target({ METHOD, CONSTRUCTOR, TYPE, FIELD })
@Retention(RUNTIME)
public @interface ValidPersonService {
	String message() default "default message";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	public static class Validator implements ConstraintValidator<ValidPersonService, PersonService> {

		@Override
		public boolean isValid(PersonService object, ConstraintValidatorContext constraintValidatorContext) {
			return false;
		}
	}
}
