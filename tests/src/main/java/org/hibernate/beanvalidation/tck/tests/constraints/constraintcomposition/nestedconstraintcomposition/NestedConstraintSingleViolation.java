/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.constraintcomposition.nestedconstraintcomposition;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author Hardy Ferentschik
 */
@Pattern(regexp = "...", message = "Pattern must match {regexp}")
@Size(min = 3, max = 3, message = "Size must be {min}")
@Target({ METHOD, FIELD, TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = { })
@ReportAsSingleViolation
public @interface NestedConstraintSingleViolation {
	public abstract String message() default "NestedConstraintSingleViolation failed.";

	public abstract Class<?>[] groups() default { };

	public abstract Class<? extends Payload>[] payload() default { };
}
