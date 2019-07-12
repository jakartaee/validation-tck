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
public class CalendarEvent {

	@Valid
	private User user;

	@NotNull
	private String type;

	public CalendarEvent() {
		type = "business";
		user = new User();
	}

	public CalendarEvent(@Valid User user) {
	}

	@Valid
	public CalendarEvent(String userName) {
	}

	public CalendarEvent(User user, String type) {
		this.user = user;
		this.type = type;
	}

	public void setUser(@Valid User user) {
	}

	@Valid
	public User getUser() {
		return user;
	}
}
