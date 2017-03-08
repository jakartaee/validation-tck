/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration;

import java.time.ZonedDateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.groups.Default;

/**
 * @author Hardy Ferentschik
 */
public class User {

	private boolean isConsistent;

	private String firstname;

	private String lastname;

	private CreditCard creditcard;

	@Pattern(regexp = "[0-9 -]+",
			message = "A phone number can only contain numbers, whitespaces and dashes.",
			groups = Optional.class)
	private String phoneNumber;

	@Past
	private ZonedDateTime birthday;

	@NotNull(groups = Default.class)
	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setConsistent(boolean consistent) {
		isConsistent = consistent;
	}

	public boolean isConsistent() {
		return isConsistent;
	}

	public CreditCard getCreditcard() {
		return creditcard;
	}

	public void setCreditcard(CreditCard creditcard) {
		this.creditcard = creditcard;
	}

	public ZonedDateTime getBirthday() {
		return birthday;
	}

	public void setBirthday(ZonedDateTime birthday) {
		this.birthday = birthday;
	}
}
