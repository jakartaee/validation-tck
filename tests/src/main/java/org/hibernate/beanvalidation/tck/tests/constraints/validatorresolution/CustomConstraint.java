/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.validatorresolution;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
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
		CustomConstraint.ValidatorBaseClass.class,
		CustomConstraint.ValidatorForSubClassA.class,
		CustomConstraint.ValidatorForSubClassB.class,
		CustomConstraint.ValidatorForSubClassC.class,
		CustomConstraint.ValidatorForSubClassD.class,
		CustomConstraint.ValidatorForSubClassE.class,
		CustomConstraint.ValidatorForSubClassF.class,
		CustomConstraint.ValidatorForSubClassG.class,
		CustomConstraint.ValidatorForSubClassH.class,
		CustomConstraint.ValidatorForSubClassI.class,
		CustomConstraint.ValidatorForCustomClass.class,
		CustomConstraint.ValidatorForCustomInterface.class,
		CustomConstraint.ValidatorForAnotherBaseClass.class,
		CustomConstraint.ValidatorForAnotherSubClass.class
})
@Documented
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
public @interface CustomConstraint {
	String message() default "my custom constraint";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	public class ValidatorBaseClass implements ConstraintValidator<CustomConstraint, BaseClass> {

		@Override
		public boolean isValid(BaseClass baseClass, ConstraintValidatorContext constraintValidatorContext) {
			return false;
		}
	}

	public class ValidatorForSubClassA implements ConstraintValidator<CustomConstraint, SubClassA> {
		static int callCounter = 0;

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
		public boolean isValid(SubClassB subClass, ConstraintValidatorContext constraintValidatorContext) {
			callCounter++;
			if ( callCounter > 1 ) {
				throw new IllegalStateException( "This method should have been only called once during the tests." );
			}
			return true;
		}
	}

	public class ValidatorForSubClassC implements ConstraintValidator<CustomConstraint, SubClassC> {
		static int callCounter = 0;

		@Override
		public boolean isValid(SubClassC subClass, ConstraintValidatorContext constraintValidatorContext) {
			callCounter++;
			if ( callCounter > 1 ) {
				throw new IllegalStateException( "This method should have been only called once during the tests." );
			}
			return true;
		}
	}

	public class ValidatorForSubClassD implements ConstraintValidator<CustomConstraint, SubClassD> {
		static int callCounter = 0;

		@Override
		public boolean isValid(SubClassD subClass, ConstraintValidatorContext constraintValidatorContext) {
			callCounter++;
			if ( callCounter > 1 ) {
				throw new IllegalStateException( "This method should have been only called once during the tests." );
			}
			return true;
		}
	}

	public class ValidatorForSubClassE implements ConstraintValidator<CustomConstraint, SubClassE> {
		static int callCounter = 0;

		@Override
		public boolean isValid(SubClassE subClass, ConstraintValidatorContext constraintValidatorContext) {
			callCounter++;
			if ( callCounter > 1 ) {
				throw new IllegalStateException( "This method should have been only called once during the tests." );
			}
			return true;
		}
	}

	public class ValidatorForSubClassF implements ConstraintValidator<CustomConstraint, SubClassF> {
		static int callCounter = 0;

		@Override
		public boolean isValid(SubClassF subClass, ConstraintValidatorContext constraintValidatorContext) {
			callCounter++;
			if ( callCounter > 1 ) {
				throw new IllegalStateException( "This method should have been only called once during the tests." );
			}
			return true;
		}
	}

	public class ValidatorForSubClassG implements ConstraintValidator<CustomConstraint, SubClassG> {
		static int callCounter = 0;

		@Override
		public boolean isValid(SubClassG subClass, ConstraintValidatorContext constraintValidatorContext) {
			callCounter++;
			if ( callCounter > 1 ) {
				throw new IllegalStateException( "This method should have been only called once during the tests." );
			}
			return true;
		}
	}

	public class ValidatorForSubClassH implements ConstraintValidator<CustomConstraint, SubClassH> {
		static int callCounter = 0;

		@Override
		public boolean isValid(SubClassH subClass, ConstraintValidatorContext constraintValidatorContext) {
			callCounter++;
			if ( callCounter > 1 ) {
				throw new IllegalStateException( "This method should have been only called once during the tests." );
			}
			return true;
		}
	}

	public class ValidatorForSubClassI implements ConstraintValidator<CustomConstraint, SubClassI> {
		static int callCounter = 0;

		@Override
		public boolean isValid(SubClassI subClass, ConstraintValidatorContext constraintValidatorContext) {
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
		public boolean isValid(ValidatorResolutionTest.AnotherSubClass anotherSubClass, ConstraintValidatorContext constraintValidatorContext) {
			callCounter++;
			if ( callCounter > 1 ) {
				throw new IllegalStateException( "This method should have been only called once during the tests." );
			}
			return true;
		}
	}
}
