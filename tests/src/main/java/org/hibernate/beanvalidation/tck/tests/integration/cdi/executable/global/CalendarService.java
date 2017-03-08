/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.integration.cdi.executable.global;

import javax.validation.constraints.NotNull;

/**
 * @author Gunnar Morling
 */
public class CalendarService {

	public Event createEvent(@NotNull String title) {
		return new Event();
	}

	@NotNull
	public Event getEvent() {
		return null;
	}
}
