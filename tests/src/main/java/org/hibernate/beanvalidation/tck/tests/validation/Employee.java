/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation;

/**
 * @author Gunnar Morling
 */
public class Employee implements Person {

	private final String firstName;
	private final String lastName;

	public Employee(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	@Override
	public String getFirstName() {
		return firstName;
	}

	@Override
	public String getMiddleName() {
		return null;
	}

	@Override
	public String getLastName() {
		return lastName;
	}
}
