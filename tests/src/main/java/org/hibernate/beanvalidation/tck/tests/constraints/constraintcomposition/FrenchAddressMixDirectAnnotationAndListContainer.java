/**
 * Bean Validation TCK
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

import javax.validation.Constraint;
import javax.validation.OverridesAttribute;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;

/**
 * @author Guillaume Smet
 */
public class FrenchAddressMixDirectAnnotationAndListContainer extends Address {

	@Override
	@FrenchZipcodeMixDirectAnnotationAndListContainer
	public String getZipCode() {
		return super.getZipCode();
	}

	@Pattern(regexp = ".....")
	@Pattern.List({ @Pattern(regexp = "bar") })
	@Constraint(validatedBy = FrenchZipcodeConstraintValidator.class)
	@Documented
	@Target({ METHOD, FIELD, TYPE })
	@Retention(RUNTIME)
	public @interface FrenchZipcodeMixDirectAnnotationAndListContainer {
		String message() default "Wrong zipcode";

		Class<?>[] groups() default { };

		Class<? extends Payload>[] payload() default {};

		@OverridesAttribute(constraint = Pattern.class, name = "regexp", constraintIndex = 1)
		String regex() default "\\d*";
	}
}
