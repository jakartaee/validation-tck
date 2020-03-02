/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.valueextraction.definition.model;

import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;


@Target(TYPE_USE)
@Constraint(validatedBy = { CustomConstraint.Validator.class })
@Documented
@Retention(RUNTIME)
public @interface CustomConstraint {

	public static final ContainerElement INSTANCE = new ContainerElement();

	String message() default "my custom constraint";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	public class Validator implements ConstraintValidator<CustomConstraint, ContainerElement> {

		@Override
		public boolean isValid(ContainerElement element, ConstraintValidatorContext constraintValidatorContext) {
			if ( element != INSTANCE ) {
				throw new IllegalArgumentException( "The passed element must be INSTANCE." );
			}

			return false;
		}
	}
}
