/*
* JBoss, Home of Professional Open Source
* Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual contributors
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
package org.hibernate.beanvalidation.tck.tests.integration.cdi.executable.priority;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

@SupportedValidationTarget(value = ValidationTarget.PARAMETERS)
public class CustomConstraintValidator implements ConstraintValidator<CustomConstraint, Object> {

	@Inject
	private InvocationTracker invocationTracker;

	@Override
	public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
		assertTrue( invocationTracker.isEarlierInterceptorInvoked() );
		assertFalse( invocationTracker.isLaterInterceptorInvoked() );

		invocationTracker.setValidatorInvoked( true );
		return true;
	}
}
