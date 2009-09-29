// $Id$
/*
* JBoss, Home of Professional Open Source
* Copyright 2008, Red Hat, Inc. and/or its affiliates, and individual contributors
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
package org.hibernate.jsr303.tck.tests.constraints.validatorresolution;

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

		public void initialize(Ambiguous parameters) {
		}

		public boolean isValid(Dummy d, ConstraintValidatorContext constraintValidatorContext) {
			return true;
		}
	}

	public class AmbiguousValidatorForSerializable implements ConstraintValidator<Ambiguous, Serializable> {

		public void initialize(Ambiguous parameters) {
		}

		public boolean isValid(Serializable o, ConstraintValidatorContext constraintValidatorContext) {
			return true;
		}
	}
}