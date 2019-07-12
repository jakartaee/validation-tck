/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.integration.cdi.executable;

import java.util.Date;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;

/**
 * @author Gunnar Morling
 */
public class CalendarService {

	@CrossParameterConstraint
	public void createEvent(@NotNull Date start, @Future Date end) {
	}

	@NotNull
	public Event createEvent() {
		return null;
	}

	@NotNull
	public Event getEvent() {
		return null;
	}
}
