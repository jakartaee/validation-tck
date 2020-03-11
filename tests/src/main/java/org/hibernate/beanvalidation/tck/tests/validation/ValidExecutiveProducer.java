/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation;

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

import org.hibernate.beanvalidation.tck.tests.validation.ValidExecutiveProducer.ValidExecutiveProducerValidator;

/**
 * @author Guillaume Smet
 */
@Constraint(validatedBy = ValidExecutiveProducerValidator.class)
@Documented
@Target({ METHOD, CONSTRUCTOR, FIELD, TYPE })
@Retention(RUNTIME)
public @interface ValidExecutiveProducer {
	String message() default "{ValidExecutiveProducer.message}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	public static class ValidExecutiveProducerValidator implements ConstraintValidator<ValidExecutiveProducer, ExecutiveProducer> {

		@Override
		public boolean isValid(ExecutiveProducer executiveProducer, ConstraintValidatorContext constraintValidatorContext) {
			if ( executiveProducer == null ) {
				return true;
			}
			if ( executiveProducer.getFirstName() == null || executiveProducer.getFirstName().trim().length() == 0 ) {
				return false;
			}
			if ( executiveProducer.getLastName() == null || executiveProducer.getLastName().trim().length() == 0 ) {
				return false;
			}
			return true;
		}
	}
}
