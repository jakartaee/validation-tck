/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.validdeclarations.service.impl;

import java.util.Date;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.validdeclarations.constraint.ValidCalendarEvent;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.validdeclarations.model.CalendarEvent;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.validdeclarations.service.CalendarService;

/**
 * @author Gunnar Morling
 */
public class CalendarServiceImplementation implements CalendarService {

	@Override
	@NotNull
	public CalendarEvent createEvent(Date start, Date end) {
		return null;
	}

	@Override
	@Valid
	public CalendarEvent createEvent(Date start, Date end, int duration) {
		return null;
	}

	@Override
	@ValidCalendarEvent
	public CalendarEvent createEvent(Date start, Date end, List<String> participants) {
		return null;
	}
}
