/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.customconstraint;

import java.lang.annotation.Annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidationException;

/**
 * @author Emmanuel Bernard
 * @author Hardy Ferentschik
 */
public abstract class BoundariesConstraintValidator<T extends Annotation> implements ConstraintValidator<T, Integer> {
	public static boolean initializeCalled = false;
	public static int isValidCalls = 0;
	public static boolean throwRuntimeExceptionFromInitialize = false;
	public static boolean throwRuntimeExceptionFromIsValid = false;

	private int low;
	private int high;

	protected void initialize(int low, int high) {
		initializeCalled = true;
		if ( throwRuntimeExceptionFromInitialize ) {
			throwRuntimeExceptionFromInitialize = false;
			throw new RuntimeException( "Throwing a RuntimeException from BoundariesConstraintValidator.initialize" );
		}
		this.low = low;
		this.high = high;
	}

	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
		if ( !initializeCalled ) {
			throw new ValidationException( "initialize() must be called before the usage of isValid()" );
		}
		if ( throwRuntimeExceptionFromIsValid ) {
			throwRuntimeExceptionFromIsValid = false;
			throw new RuntimeException( "Throwing a RuntimeException from BoundariesConstraintValidator.isValid" );
		}
		isValidCalls++;
		return value >= low && value <= high;
	}
}
