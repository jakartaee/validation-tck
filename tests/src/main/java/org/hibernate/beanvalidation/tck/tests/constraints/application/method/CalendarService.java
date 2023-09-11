/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.application.method;

import java.util.Date;

import jakarta.validation.constraints.NotNull;

/**
 * @author Gunnar Morling
 */
public class CalendarService {

	@OnlineCalendarService
	public CalendarService() {
	}

	public CalendarService(@NotNull String type) {
	}

	@ConsistentDateParameters
	public CalendarService(Date start, Date end) {
	}

	@ConsistentDateParameters
	public CalendarService(@NotNull Date start, Date end, Integer numberOfParticipants) {
	}

	public void setType(@NotNull String type) {
	}

	@ConsistentDateParameters
	public void createEvent(Date start, Date end) {
	}

	@ConsistentDateParameters
	public void createEvent(@NotNull Date start, Date end, Integer numberOfParticipants) {
	}

	@NotNull
	public CalendarEvent findEvents(String name) {
		return null;
	}

	public static void createEvent(@NotNull String title, @NotNull Date start, @NotNull Date end) {
	}
}
