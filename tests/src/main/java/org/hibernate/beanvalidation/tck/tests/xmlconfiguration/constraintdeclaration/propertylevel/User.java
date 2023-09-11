/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.constraintdeclaration.propertylevel;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.ConvertGroup;
import jakarta.validation.groups.Default;

/**
 * @author Hardy Ferentschik
 */
public class User {
	private String firstname;

	private String lastname;

	private CreditCard firstCreditCard;

	private CreditCard secondCreditCard;

	@NotNull
	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	@NotNull
	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	@ConvertGroup(from = Default.class, to = CreditRatingA.class)
	public CreditCard getFirstCreditCard() {
		return firstCreditCard;
	}

	public void setFirstCreditCard(CreditCard firstCreditCard) {
		this.firstCreditCard = firstCreditCard;
	}

	@Valid
	public CreditCard getSecondCreditCard() {
		return secondCreditCard;
	}

	public void setSecondCreditCard(CreditCard secondCreditCard) {
		this.secondCreditCard = secondCreditCard;
	}

	public interface CreditRatingA {
	}

	public interface CreditRatingAA {
	}
}
