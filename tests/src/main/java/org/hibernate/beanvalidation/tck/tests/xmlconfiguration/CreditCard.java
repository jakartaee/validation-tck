/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration;

import javax.validation.constraints.Pattern;

/**
 * @author Hardy Ferentschik
 */
public class CreditCard {

	@Pattern(regexp = "[0-9]+", message = "Not a credit card number.")
	private String number;

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
}
