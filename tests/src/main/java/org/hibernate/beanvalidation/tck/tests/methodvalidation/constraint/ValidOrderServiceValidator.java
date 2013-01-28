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
package org.hibernate.beanvalidation.tck.tests.methodvalidation.constraint;

import java.util.concurrent.atomic.AtomicInteger;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.hibernate.beanvalidation.tck.tests.methodvalidation.service.OrderService;

import static org.testng.Assert.fail;

/**
 * @author Gunnar Morling
 */
public class ValidOrderServiceValidator
		implements ConstraintValidator<ValidOrderService, OrderService> {

	private int expectedMaxInvocationCount;
	private AtomicInteger actualInvocationCount;

	@Override
	public void initialize(ValidOrderService constraintAnnotation) {
		expectedMaxInvocationCount = constraintAnnotation.expectedMaxInvocationCount();
		actualInvocationCount = new AtomicInteger();
	}

	@Override
	public boolean isValid(OrderService orderService, ConstraintValidatorContext context) {
		int invocationCount = actualInvocationCount.incrementAndGet();
		if ( invocationCount > expectedMaxInvocationCount ) {
			fail(
					String.format(
							"Constraint validator was expected to be invoked only %s times but was invoked %s times.",
							expectedMaxInvocationCount,
							actualInvocationCount
					)
			);
		}

		return orderService == null ? true : orderService.getName().length() >= 5;
	}
}
