/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.application;

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
 * @author Hardy Ferentschik
 */
@Documented
@Constraint(validatedBy = SecurityCheck.SecurityCheckValidator.class)
@Target({ TYPE, METHOD, FIELD })
@Retention(RUNTIME)
public @interface SecurityCheck {
	public abstract String message() default "Security check failed.";

	public abstract Class<?>[] groups() default { };

	public abstract Class<? extends Payload>[] payload() default {};

	public class SecurityCheckValidator implements ConstraintValidator<SecurityCheck, Object> {

		@Override
		public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
			if ( object == null ) {
				return true;
			}

			if ( !( object instanceof Person ) && !( object instanceof Citizen ) ) {
				return false;
			}

			Citizen citizen = ( Citizen ) object;
			return !"000000-0000".equals( citizen.getPersonalNumber() );
		}
	}
}
