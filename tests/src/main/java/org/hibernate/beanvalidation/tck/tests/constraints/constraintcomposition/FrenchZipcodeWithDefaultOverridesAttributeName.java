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
import jakarta.validation.OverridesAttribute;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;


/**
 * @author Guillaume Smet
 */
@NotEmpty
@Pattern(regexp = "bar")
@Constraint(validatedBy = {})
@Documented
@Target({ METHOD, FIELD, TYPE })
@Retention(RUNTIME)
public @interface FrenchZipcodeWithDefaultOverridesAttributeName {

	@OverridesAttribute(constraint = Pattern.class)
	String message() default "Wrong zip code";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default {};

	@OverridesAttribute(constraint = Pattern.class)
	String regexp() default "\\d*";
}
