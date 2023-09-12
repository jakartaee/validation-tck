/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.constraintdeclaration.fieldlevel;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.ConvertGroup;
import jakarta.validation.groups.Default;

/**
 * @author Hardy Ferentschik
 */
public class User {
	@NotNull
	private String firstname;

	@NotNull
	private String lastname;

	@ConvertGroup(from = Default.class, to = CreditRatingA.class)
	private CreditCard firstCreditCard;

	@Valid
	private CreditCard secondCreditCard;

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

	public CreditCard getFirstCreditCard() {
		return firstCreditCard;
	}

	public void setFirstCreditCard(CreditCard firstCreditCard) {
		this.firstCreditCard = firstCreditCard;
	}

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
