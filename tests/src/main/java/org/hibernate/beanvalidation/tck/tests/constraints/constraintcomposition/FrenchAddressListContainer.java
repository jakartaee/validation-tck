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
import jakarta.validation.constraints.Size;

/**
 * @author Guillaume Smet
 */
public class FrenchAddressListContainer extends Address {

	@Override
	@FrenchZipcodeListContainer
	public String getZipCode() {
		return super.getZipCode();
	}

	@NotEmpty
	@Size
	// first pattern just duplicates the length of 5 characters, the second pattern is just to prove that parameters can be overridden.
	@Pattern.List({ @Pattern(regexp = "....."), @Pattern(regexp = "bar") })
	@Constraint(validatedBy = FrenchZipcodeListContainerConstraintValidator.class)
	@Documented
	@Target({ METHOD, FIELD, TYPE })
	@Retention(RUNTIME)
	public @interface FrenchZipcodeListContainer {
		String message() default "Wrong zipcode";

		Class<?>[] groups() default { };

		Class<? extends Payload>[] payload() default {};

		@OverridesAttribute(constraint = Size.class, name = "min")
		@OverridesAttribute(constraint = Size.class, name = "max")
		int size() default 5;

		@OverridesAttribute(constraint = Size.class, name = "message")
		String sizeMessage() default "A french zip code has a length of 5";

		@OverridesAttribute(constraint = Pattern.class, name = "regexp", constraintIndex = 1)
		String regex() default "\\d*";
	}
}
