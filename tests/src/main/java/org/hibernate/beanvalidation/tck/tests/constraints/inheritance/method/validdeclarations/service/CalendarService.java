/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.validdeclarations.service;

import java.util.Date;
import java.util.List;

import jakarta.validation.constraints.NotNull;

import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.validdeclarations.model.CalendarEvent;

/**
 * @author Gunnar Morling
 */
public interface CalendarService {

	CalendarEvent createEvent(Date start, Date end);

	CalendarEvent createEvent(Date start, Date end, int duration);

	@NotNull
	CalendarEvent createEvent(Date start, Date end, List<String> participants);
}
