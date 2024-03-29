/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations.service;

import jakarta.validation.Valid;

import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations.model.Order;

/**
 * @author Gunnar Morling
 */
public interface ExtendedOrderService extends OrderService {

	@Override
	@Valid
	Order placeOrder(String item, int quantity);
}
