/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation.graphnavigation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author Hardy Ferentschik
 */
public class Order {
	@NotNull
	Integer orderId;

	@Valid
	private List<OrderLine> orderLines = new ArrayList<OrderLine>();

	@Valid
	private User customer;

	@Valid
	private Address shippingAddress;

	@Valid
	private Address billingAddress;

	public Order(Integer id) {
		orderId = id;
	}

	public void addOrderLine(OrderLine orderLine) {
		orderLines.add( orderLine );
	}

	public List<OrderLine> getOrderLines() {
		return Collections.unmodifiableList( orderLines );
	}

	public User getCustomer() {
		return customer;
	}

	public void setCustomer(User customer) {
		this.customer = customer;
	}

	public Address getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(Address shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public Address getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(Address billingAddress) {
		this.billingAddress = billingAddress;
	}

	@Override
	public String toString() {
		return "Order{" +
				"orderId=" + orderId +
				", orderLines=" + orderLines +
				", customer=" + customer +
				", shippingAddress=" + shippingAddress +
				", billingAddress=" + billingAddress +
				'}';
	}
}
