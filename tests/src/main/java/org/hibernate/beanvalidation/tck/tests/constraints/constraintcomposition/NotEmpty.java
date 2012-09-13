/*
* JBoss, Home of Professional Open Source
* Copyright 2009, Red Hat, Inc. and/or its affiliates, and individual contributors
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* http://www.apache.org/licenses/LICENSE-2.0
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.hibernate.beanvalidation.tck.tests.constraints.constraintcomposition;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Emmanuel Bernard
 */
@Documented
@NotNull(message = "may not be null", groups = NotEmpty.DummyGroup.class)
@Size(min = 1)
@Constraint(validatedBy = NotEmpty.NotEmptyValidator.class)
@Target({ TYPE, METHOD, FIELD })
@Retention(RUNTIME)
public @interface NotEmpty {
	String message() default "{constraint.notEmpty}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default {};

	public class NotEmptyValidator implements ConstraintValidator<NotEmpty, String> {

		public void initialize(NotEmpty parameters) {
		}

		public boolean isValid(String object, ConstraintValidatorContext constraintValidatorContext) {
			return true;
		}
	}

	public interface DummyGroup {

	}
}
