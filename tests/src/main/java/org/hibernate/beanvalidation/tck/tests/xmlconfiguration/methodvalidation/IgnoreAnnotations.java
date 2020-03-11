/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.methodvalidation;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraintvalidation.SupportedValidationTarget;
import jakarta.validation.constraintvalidation.ValidationTarget;
import jakarta.validation.groups.ConvertGroup;

/**
 * @author Hardy Ferentschik
 */
public class IgnoreAnnotations {
	// constructor used for validation
	@NotNull
	@CrossParameterConstraint
	@Valid
	@ConvertGroup(from = A.class, to = B.class)
	private String foobar(@Valid @NotNull @ConvertGroup(from = A.class, to = B.class) String foo,
						  @Valid @NotNull @ConvertGroup(from = A.class, to = B.class) String bar) {
		return "snafu";
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


