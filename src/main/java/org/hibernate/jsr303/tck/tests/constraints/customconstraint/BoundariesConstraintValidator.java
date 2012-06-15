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
package org.hibernate.jsr303.tck.tests.constraints.customconstraint;

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
