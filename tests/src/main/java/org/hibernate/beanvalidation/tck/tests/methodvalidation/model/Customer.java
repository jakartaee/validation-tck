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
package org.hibernate.beanvalidation.tck.tests.methodvalidation.model;

import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.beanvalidation.tck.tests.methodvalidation.constraint.ValidCustomer;

/**
 * @author Gunnar Morling
 */
public class Customer {

	private String name;

	public interface Basic {
	}

	public interface Extended {
	}

	//testOneViolation
	@NotNull
	public Address getAddress() {
		return null;
	}

	@ValidCustomer
	public Customer() {
	}

	//testTwoViolations
	//testNoViolations
	@Size(min = 3)
	@Pattern(regexp = "aaa")
	public String getFirstName(String s) {
		return null;
	}

	@NotNull
	@ValidCustomer
	public Customer(String name) {
		this.name = name;
	}

	//testValidationWithGroup
	@Size(min = 3, groups = Extended.class)
	public String getLastName(long l) {
		return null;
	}

	@NotNull(groups = Extended.class)
	public Customer(long l) {
	}

	//testTwoConstraintsOfSameType
	@Size.List({
			@Size(min = 3),
			@Size(min = 6)
	})
	public String getLastName(CharSequence lastName) {
		return null;
	}

	@ValidCustomer.List({
			@ValidCustomer(message = "1"),
			@ValidCustomer(message = "2")
	})
	public Customer(CharSequence lastName) {
	}

	//testValidationWithSeveralGroups
	@Size(min = 3, groups = Extended.class)
	@Pattern(regexp = "aaa", groups = Basic.class)
	public String getAllData(Date dateOfBirth) {
		return null;
	}

	@NotNull(groups = Extended.class)
	@ValidCustomer(groups = Basic.class)
	public Customer(Date dateOfBirth) {
	}

	public String getName() {
		return name;
	}
}
