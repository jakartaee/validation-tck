/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.metadata;

import static org.hibernate.beanvalidation.tck.util.TestUtil.getConstructorDescriptor;
import static org.hibernate.beanvalidation.tck.util.TestUtil.getMethodDescriptor;

import java.util.Map;

import jakarta.validation.metadata.ConstructorDescriptor;
import jakarta.validation.metadata.MethodDescriptor;

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

	public static MethodDescriptor methodWithGroupConversionOnParameter() {
		return getMethodDescriptor(
				CustomerService.class,
				"updateAccountStrictly",
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

	public static MethodDescriptor methodWithGroupConversionOnReturnValue() {
		return getMethodDescriptor(
				CustomerService.class,
				"findCustomer",
				long.class,
				int.class
		);
	}

	public static MethodDescriptor crossParameterConstrainedMethod() {
		return getMethodDescriptor(
				CustomerService.class,
				"removeCustomer",
				Customer.class
		);
	}

	public static MethodDescriptor methodOverridingCrossParameterConstrainedMethod() {
		return getMethodDescriptor(
				CustomerServiceExtension.class,
				"removeCustomer",
				Customer.class
		);
	}

	public static MethodDescriptor crossParameterConstrainedMethodFromSuperType() {
		return getMethodDescriptor(
				CustomerServiceExtension.class,
				"updateCustomer",
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

	public static ConstructorDescriptor parameterConstrainedConstructorOfInnerClass() {
		return getConstructorDescriptor(
				CustomerService.InnerClass.class,
				CustomerService.class,
				String.class
		);
	}

	public static ConstructorDescriptor cascadedParameterConstructor() {
		return getConstructorDescriptor(
				CustomerService.class,
				Account.class
		);
	}

	public static ConstructorDescriptor constructorWithGroupConversionOnParameter() {
		return getConstructorDescriptor(
				CustomerService.class,
				int.class,
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

	public static ConstructorDescriptor constructorWithGroupConversionOnReturnValue() {
		return getConstructorDescriptor(
				CustomerService.class,
				long.class,
				int.class
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

	public static ConstructorDescriptor constructorWithCascadedContainerElementsOnParameter() {
		return getConstructorDescriptor(
				CustomerService.class,
				Map.class
		);
	}

	public static MethodDescriptor parameterWithCascadedContainerElements() {
		return getMethodDescriptor(
				CustomerService.class,
				"createOrder",
				long.class,
				Map.class
		);
	}

	public static MethodDescriptor returnValueWithCascadedContainerElements() {
		return getMethodDescriptor(
				CustomerService.class,
				"getOrderContent",
				long.class
		);
	}
}
