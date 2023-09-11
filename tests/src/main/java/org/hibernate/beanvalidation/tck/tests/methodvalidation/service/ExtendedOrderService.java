/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.methodvalidation.service;

import org.hibernate.beanvalidation.tck.tests.methodvalidation.constraint.ValidRetailOrder;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.constraint.ValidRetailOrderService;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.model.Item;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.model.Order;

/**
 * @author Gunnar Morling
 */
public class ExtendedOrderService extends OrderService {

	public ExtendedOrderService() {
	}

	@ValidRetailOrderService
	public ExtendedOrderService(String customer, Item item, int quantity) {
		super( customer, item, quantity );
	}

	@Override
	@ValidRetailOrder
	public Order placeOrder(String customer, Item item, int quantity) {
		return null;
	}
}
