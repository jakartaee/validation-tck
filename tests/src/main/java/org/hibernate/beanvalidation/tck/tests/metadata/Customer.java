/*
* JBoss, Home of Professional Open Source
* Copyright 2009, Red Hat, Inc. and/or its affiliates, and individual contributors
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

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.groups.ConvertGroup;
import javax.validation.groups.Default;

/**
 * @author Hardy Ferentschik
 */
public class Customer implements Person {

	public interface BasicChecks {
	}

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
