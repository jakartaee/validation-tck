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
package org.hibernate.beanvalidation.tck.tests.integration.ee.cdi;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * A validator which makes use of constructor injection.
 *
 * @author Gunnar Morling
 */
public class GreetingConstraintValidatorForInteger implements ConstraintValidator<GreetingConstraint, Integer> {

	private final Greeter greeter;
	private String name;

	@Inject
	public GreetingConstraintValidatorForInteger(Greeter greeter) {
		this.greeter = greeter;
	}

	@Override
	public void initialize(GreetingConstraint constraintAnnotation) {
		this.name = constraintAnnotation.name();
	}

	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate( greeter.greetFormally( name ) ).addConstraintViolation();

		return false;
	}
}
