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
package org.hibernate.beanvalidation.tck.tests.constraints.validatorresolution;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * A test constraint which can lead to a error when trying to resolve the validator.
 *
 * @author Hardy Ferentschik
 */
@Constraint(validatedBy = {
		CustomConstraint.ValidatorBaseClass.class,
		CustomConstraint.ValidatorForSubClassA.class,
		CustomConstraint.ValidatorForSubClassB.class,
		CustomConstraint.ValidatorForCustomClass.class,
		CustomConstraint.ValidatorForCustomInterface.class,
		CustomConstraint.ValidatorForAnotherBaseClass.class,
		CustomConstraint.ValidatorForAnotherSubClass.class
})
@Documented
@Target({ METHOD, FIELD, TYPE })
@Retention(RUNTIME)
public @interface CustomConstraint {
	String message() default "my custom constraint";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	public class ValidatorBaseClass implements ConstraintValidator<CustomConstraint, BaseClass> {

		@Override
		public void initialize(CustomConstraint parameters) {
		}

		@Override
		public boolean isValid(BaseClass baseClass, ConstraintValidatorContext constraintValidatorContext) {
			return false;
		}
	}

	public class ValidatorForSubClassA implements ConstraintValidator<CustomConstraint, SubClassA> {
		static int callCounter = 0;

		@Override
		public void initialize(CustomConstraint parameters) {
		}

		@Override
		public boolean isValid(SubClassA subClass, ConstraintValidatorContext constraintValidatorContext) {
			callCounter++;
			if ( callCounter > 1 ) {
				throw new IllegalStateException( "This method should have been only called once during the tests." );
			}
			return true;
		}
	}

	public class ValidatorForSubClassB implements ConstraintValidator<CustomConstraint, SubClassB> {
		static int callCounter = 0;

		@Override
		public void initialize(CustomConstraint parameters) {
		}

		@Override
		public boolean isValid(SubClassB subClass, ConstraintValidatorContext constraintValidatorContext) {
			callCounter++;
			if ( callCounter > 1 ) {
				throw new IllegalStateException( "This method should have been only called once during the tests." );
			}
			return true;
		}
	}

	public class ValidatorForCustomClass
			implements ConstraintValidator<CustomConstraint, ValidatorResolutionTest.CustomClass> {
		static int callCounter = 0;

		@Override
		public void initialize(CustomConstraint parameters) {
		}

		@Override
		public boolean isValid(ValidatorResolutionTest.CustomClass customClass, ConstraintValidatorContext constraintValidatorContext) {
			callCounter++;
			if ( callCounter > 1 ) {
				throw new IllegalStateException( "This method should have been only called once during the tests." );
			}
			return true;
		}
	}

	public class ValidatorForCustomInterface
			implements ConstraintValidator<CustomConstraint, ValidatorResolutionTest.CustomInterface> {
		static int callCounter = 0;

		@Override
		public void initialize(CustomConstraint parameters) {
		}

		@Override
		public boolean isValid(ValidatorResolutionTest.CustomInterface customInterface, ConstraintValidatorContext constraintValidatorContext) {
			callCounter++;
			if ( callCounter > 1 ) {
				throw new IllegalStateException( "This method should have been only called once during the tests." );
			}
			return true;
		}
	}

	public class ValidatorForAnotherBaseClass
			implements ConstraintValidator<CustomConstraint, ValidatorResolutionTest.AnotherBaseClass> {
		static int callCounter = 0;

		@Override
		public void initialize(CustomConstraint parameters) {
		}

		@Override
		public boolean isValid(ValidatorResolutionTest.AnotherBaseClass anotherCustomInterfaceImpl, ConstraintValidatorContext constraintValidatorContext) {
			callCounter++;
			if ( callCounter > 1 ) {
				throw new IllegalStateException( "This method should have been only called once during the tests." );
			}
			return true;
		}
	}

	public class ValidatorForAnotherSubClass
			implements ConstraintValidator<CustomConstraint, ValidatorResolutionTest.AnotherSubClass> {
		static int callCounter = 0;

		@Override
		public void initialize(CustomConstraint parameters) {
		}

		@Override
		public boolean isValid(ValidatorResolutionTest.AnotherSubClass anotherSubClass, ConstraintValidatorContext constraintValidatorContext) {
			callCounter++;
			if ( callCounter > 1 ) {
				throw new IllegalStateException( "This method should have been only called once during the tests." );
			}
			return true;
		}
	}
}
