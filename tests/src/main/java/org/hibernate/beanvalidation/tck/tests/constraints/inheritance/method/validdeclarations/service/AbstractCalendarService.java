/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.validdeclarations.service;

import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.Max;

import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.validdeclarations.constraint.ValidAbstractCalendarService;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.validdeclarations.model.CalendarEvent;

/**
 * @author Gunnar Morling
 */
public abstract class AbstractCalendarService {

	public AbstractCalendarService(@Max(5) int mode) {
	}

	public AbstractCalendarService(CalendarEvent defaultEvent) {
	}

	@ValidAbstractCalendarService
	public AbstractCalendarService(String type) {
	}

	@Valid
	public AbstractCalendarService(long mode) {
	}

	public abstract CalendarEvent createEvent(Date start, Date end);

	public abstract CalendarEvent createEvent(Date start, Date end, int duration);
}
