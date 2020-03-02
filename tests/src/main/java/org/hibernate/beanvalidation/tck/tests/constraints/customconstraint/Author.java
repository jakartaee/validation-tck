/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.customconstraint;

import jakarta.validation.constraints.Size;

/**
 * @author Hardy Ferentschik
 */
public class Author {

	@Size(min = 2)
	private String firstName;

	@Size(min = 3)
	private String lastName;

	@Size(min = 3, message = "The company name must be a minimum {min} characters")
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
