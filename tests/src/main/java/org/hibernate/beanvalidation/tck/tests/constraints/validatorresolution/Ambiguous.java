/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.validatorresolution;

import java.io.Serializable;
import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

/**
 * A test constraint which can lead to a error when trying to resolve the validator.
 *
 * @author Hardy Ferentschik
 */
@Constraint(validatedBy = {
		Ambiguous.AmbiguousValidatorForDummy.class, Ambiguous.AmbiguousValidatorForSerializable.class
})
@Documented
@Target({ METHOD, FIELD, TYPE })
@Retention(RUNTIME)
public @interface Ambiguous {
	public abstract String message() default "foobar";

	public abstract Class<?>[] groups() default { };

	public abstract Class<? extends Payload>[] payload() default { };


	public class AmbiguousValidatorForDummy implements ConstraintValidator<Ambiguous, Dummy> {

		@Override
		public boolean isValid(Dummy d, ConstraintValidatorContext constraintValidatorContext) {
			return true;
		}
	}

	public class AmbiguousValidatorForSerializable implements ConstraintValidator<Ambiguous, Serializable> {

		@Override
		public boolean isValid(Serializable o, ConstraintValidatorContext constraintValidatorContext) {
			return true;
		}
	}
}
