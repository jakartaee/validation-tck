/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.application;

/**
 * @author Hardy Ferentschik
 */
public class SuperWoman extends Woman {
	private String hiddenName = "Lane";

	public SuperWoman() {
		firstName = "Lois";
		lastName = null;
	}

	public String getFirstName() {
		return null;
	}

	public String getLastName() {
		return hiddenName;
	}

	public void setHiddenName(String hiddenName) {
		this.hiddenName = hiddenName;
	}
}
