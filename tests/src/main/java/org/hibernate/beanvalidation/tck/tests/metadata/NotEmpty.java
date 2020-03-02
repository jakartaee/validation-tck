/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.metadata;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.OverridesAttribute;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * @author Emmanuel Bernard
 */
@Documented
@Constraint(validatedBy = { })
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
@ReportAsSingleViolation
@NotNull(payload = NotEmpty.UnusedPayload.class)
@Size
public @interface NotEmpty {
	String message() default "cannot be empty";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	@OverridesAttribute(constraint = Size.class, name = "min") int min() default 5;

	static class UnusedPayload implements Payload {
	}
}
