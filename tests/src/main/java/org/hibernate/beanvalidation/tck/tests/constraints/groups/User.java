/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.groups;

import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.groups.Default;


/**
 * @author Emmanuel Bernard
 * @author Hardy Ferentschik
 */
@GroupSequence({ User.class })
public class User {
	@NotNull
	private String firstname;

	@NotNull(groups = Default.class)
	private String lastname;

	@Pattern(regexp = "[0-9 -]?", groups = Optional.class)
	private String phoneNumber;

	@NotNull(groups = { Billable.class, BuyInOneClick.class })
	private CreditCard defaultCreditCard;

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

	public CreditCard getDefaultCreditCard() {
		return defaultCreditCard;
	}

	public void setDefaultCreditCard(CreditCard defaultCreditCard) {
		this.defaultCreditCard = defaultCreditCard;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public interface BuyInOneClick extends Default, Billable {
	}

	public interface Billable {
	}

	public interface Optional {
	}
}
