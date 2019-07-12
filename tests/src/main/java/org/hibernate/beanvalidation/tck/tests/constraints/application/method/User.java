/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.application.method;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author Gunnar Morling
 */
public class User {

	@NotNull
	private String name;

	@Valid
	private final Account account;

	public User() {
		this.account = null;
	}

	public User(Account account) {
		this.name = "Bob";
		this.account = account;
	}
}
