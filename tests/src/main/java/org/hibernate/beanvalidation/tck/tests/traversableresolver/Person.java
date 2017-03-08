/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.traversableresolver;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

/**
 * @author Hardy Ferentschik
 */
public class Person {
	@NotNull
	private String firstName;

	@NotNull
	private String lastName;

	@Digits(integer = 10, fraction = 0)
	private long personalNumber;


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

	public long getPersonalNumber() {
		return personalNumber;
	}

	public void setPersonalNumber(long personalNumber) {
		this.personalNumber = personalNumber;
	}
}
