/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation.graphnavigation.containerelement.model;

import javax.validation.constraints.NotBlank;

public class EmailAddress {

	@NotBlank
	private final String email;

	public EmailAddress(String value) {
		this.email = value;
	}

	@Override
	public String toString() {
		return "EmailAddress<email=" + email + ">";
	}
}
