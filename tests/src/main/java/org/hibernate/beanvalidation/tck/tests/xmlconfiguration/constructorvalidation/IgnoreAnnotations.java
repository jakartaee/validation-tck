/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.constructorvalidation;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;
import javax.validation.groups.ConvertGroup;

/**
 * @author Hardy Ferentschik
 */
public class IgnoreAnnotations {
	// constructor used for validation
	@NotNull
	@CrossParameterConstraint
	@Valid
	@ConvertGroup(from = A.class, to = B.class)
	IgnoreAnnotations(@Valid @NotNull @ConvertGroup(from = A.class, to = B.class) String foo,
					  @Valid @NotNull @ConvertGroup(from = A.class, to = B.class) String bar) {
	}

	@Target({ CONSTRUCTOR, METHOD })
	@Retention(RetentionPolicy.RUNTIME)
	@Constraint(validatedBy = { CrossParameterConstraintValidator.class })
	@Documented
	public @interface CrossParameterConstraint {
		String message() default "snafu";

		Class<?>[] groups() default { };

		Class<? extends Payload>[] payload() default { };
	}

	@SupportedValidationTarget(value = ValidationTarget.PARAMETERS)
	public class CrossParameterConstraintValidator implements ConstraintValidator<CrossParameterConstraint, Object[]> {

		@Override
		public boolean isValid(Object[] value, ConstraintValidatorContext context) {
			return false;
		}
	}

	public interface A {
	}

	public interface B {
	}
}


