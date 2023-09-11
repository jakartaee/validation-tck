/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.methodvalidation.model;

import java.util.Date;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import org.hibernate.beanvalidation.tck.tests.methodvalidation.constraint.ValidBusinessCustomer;
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
	@Size(min = 3)
	public String getAddress() {
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

	@ValidCustomer
	@ValidBusinessCustomer
	public Customer(String name) {
		this.name = name;
	}

	//testValidationWithGroup
	@Size(min = 3, groups = Extended.class)
	public String getLastName(long l) {
		return null;
	}

	@ValidCustomer(groups = Extended.class)
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

	@ValidCustomer(groups = Extended.class)
	@ValidBusinessCustomer(groups = Basic.class)
	public Customer(Date dateOfBirth) {
	}

	public String getName() {
		return name;
	}
}
