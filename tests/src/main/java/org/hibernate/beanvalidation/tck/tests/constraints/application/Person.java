/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.application;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;

/**
 * @author Hardy Ferentschik
 */
@SecurityCheck(groups = Default.class)
public abstract class Person implements Citizen {
	@NotNull
	String firstName;
	String lastName;
	String personalNumber;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@NotNull
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPersonalNumber() {
		return personalNumber;
	}

	public void setPersonalNumber(String personalNumber) {
		this.personalNumber = personalNumber;
	}

	public abstract Gender getGender();

	enum Gender {
		MALE,
		FEMALE
	}
}
