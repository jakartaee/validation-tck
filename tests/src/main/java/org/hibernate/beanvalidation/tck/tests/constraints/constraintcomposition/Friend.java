/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.constraintcomposition;

/**
 * @author Hardy Ferentschik
 */
public class Friend {
	@Name(payload = Severity.Error.class)
	private String firstName;

	@Name(payload = Severity.Error.class)
	private String lastName;

	@Name(payload = Severity.Warn.class)
	private String nickName;

	public Friend(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

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
}
