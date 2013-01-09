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
package org.hibernate.beanvalidation.tck.tests.metadata;

import javax.validation.metadata.ConstructorDescriptor;
import javax.validation.metadata.MethodDescriptor;

import static org.hibernate.beanvalidation.tck.util.TestUtil.getConstructorDescriptor;
import static org.hibernate.beanvalidation.tck.util.TestUtil.getMethodDescriptor;

/**
 * @author Gunnar Morling
 */
public class Executables {

	public static MethodDescriptor parameterConstrainedMethod() {
		return getMethodDescriptor(
				CustomerService.class,
				"createCustomer",
				String.class,
				String.class
		);
	}

	public static MethodDescriptor cascadedParameterMethod() {
		return getMethodDescriptor(
				CustomerService.class,
				"updateAccount",
				Account.class
		);
	}

	public static MethodDescriptor returnValueConstrainedMethod() {
		return getMethodDescriptor(
				CustomerService.class,
				"reset"

		);
	}

	public static MethodDescriptor cascadedReturnValueMethod() {
		return getMethodDescriptor(
				CustomerService.class,
				"findCustomer",
				long.class
		);
	}

	public static MethodDescriptor crossParameterConstrainedMethod() {
		return getMethodDescriptor(
				CustomerService.class,
				"removeCustomer",
				Customer.class
		);
	}

	public static MethodDescriptor unconstrainedMethod() {
		return getMethodDescriptor(
				CustomerService.class,
				"shutDown",
				String.class
		);
	}

	public static ConstructorDescriptor parameterConstrainedConstructor() {
		return getConstructorDescriptor(
				CustomerService.class,
				String.class,
				String.class

		);
	}

	public static ConstructorDescriptor cascadedParameterConstructor() {
		return getConstructorDescriptor(
				CustomerService.class,
				Account.class
		);
	}

	public static ConstructorDescriptor returnValueConstrainedConstructor() {
		return getConstructorDescriptor(
				CustomerService.class
		);
	}

	public static ConstructorDescriptor cascadedReturnValueConstructor() {
		return getConstructorDescriptor(
				CustomerService.class,
				long.class
		);
	}

	public static ConstructorDescriptor crossParameterConstrainedConstructor() {
		return getConstructorDescriptor(
				CustomerService.class,
				Customer.class
		);
	}

	public static ConstructorDescriptor unconstrainedConstructor() {
		return getConstructorDescriptor(
				CustomerService.class,
				String.class
		);
	}
}
