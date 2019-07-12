/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.validdeclarations.service.impl;

import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.validdeclarations.constraint.ValidCalendarServiceSubClass;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.validdeclarations.model.CalendarEvent;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.validdeclarations.service.AbstractCalendarService;

/**
 * @author Gunnar Morling
 */
public class CalendarServiceSubClass extends AbstractCalendarService {

	@Min(1)
	private long mode;

	public CalendarServiceSubClass() {
		super( 0 );
	}

	public CalendarServiceSubClass(@Min(5) int mode) {
		super( mode );
	}

	@ValidCalendarServiceSubClass
	public CalendarServiceSubClass(String type) {
		super( type );
	}

	@Valid
	public CalendarServiceSubClass(long mode) {
		super( mode );
	}

	public CalendarServiceSubClass(@Valid CalendarEvent defaultEvent) {
		super( defaultEvent );
	}

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
}
