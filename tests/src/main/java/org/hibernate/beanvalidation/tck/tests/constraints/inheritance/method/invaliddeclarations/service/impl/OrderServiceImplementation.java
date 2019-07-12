/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations.service.impl;

import javax.validation.Valid;

import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations.model.Order;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations.service.OrderService;

/**
 * @author Gunnar Morling
 */
public class OrderServiceImplementation implements OrderService {

	@Override
	@Valid
	public Order placeOrder(String item, int quantity) {
		return null;
	}
}
