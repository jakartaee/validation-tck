/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.metadata;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.groups.ConvertGroup;
import jakarta.validation.groups.Default;

/**
 * @author Hardy Ferentschik
 */
public class Customer implements Person {

	public interface StrictCustomerChecks {
	}

	public interface StrictChecks {
	}

	private String firstName;
	private String middleName;
	private String lastName;

	@Valid
	@ConvertGroup.List({
			@ConvertGroup(from = Default.class, to = BasicChecks.class),
			@ConvertGroup(from = StrictCustomerChecks.class, to = StrictChecks.class)
	})
	private final List<Order> orderList = new ArrayList<Order>();

	public void addOrder(Order order) {
		orderList.add( order );
	}

	public List<Order> getOrderList() {
		return orderList;
	}

	@Override
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Override
	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	@Override
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Valid
	@ConvertGroup.List({
			@ConvertGroup(from = Default.class, to = BasicChecks.class),
			@ConvertGroup(from = StrictCustomerChecks.class, to = StrictChecks.class)
	})
	public Account getAccount() {
		return null;
	}

	@Override
	public int getAge() {
		return 0;
	}
}
