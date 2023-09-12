/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.groups;

import jakarta.validation.constraints.Size;

/**
 * @author Hardy Ferentschik
 */
public class Author {

	@Size(min = 1, groups = Last.class)
	private String firstName;

	@Size(min = 1, groups = First.class)
	private String lastName;

	@Size(max = 20, groups = Last.class, message = "The company name can only have {max} characters")
	private String company;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}
}
