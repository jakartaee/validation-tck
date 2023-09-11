/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.integration.ee.cdi;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * @author Gunnar Morling
 */
@Constraint(validatedBy = { GreetingConstraintValidatorForString.class, GreetingConstraintValidatorForInteger.class })
@Documented
@Target({ METHOD, FIELD, TYPE })
@Retention(RUNTIME)
public @interface GreetingConstraint {
	String message() default "my custom constraint";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	String name();
}
