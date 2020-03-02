/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.constraintdefinition;

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
 * Validate that the string is between min and max included
 *
 * @author Emmanuel Bernard
 * @author Hardy Ferentschik
 */
@Documented
@Constraint(validatedBy = LengthValidator.class)
@Target({ METHOD, FIELD, TYPE })
@Retention(RUNTIME)
public @interface Length {
	public abstract int min() default 0;

	public abstract int max() default Integer.MAX_VALUE;

	public abstract String message() default "min length {min}. max length {max}.";

	public abstract Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
}
