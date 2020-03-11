/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations.service.impl;

import java.util.Date;
import java.util.List;

import jakarta.validation.Valid;

import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations.model.Person;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations.service.CalendarService;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations.service.YetAnotherCalendarService;

/**
 * @author Gunnar Morling
 */
public class ImplementationOfCascadingAndNonCascadingInterfaces
		implements CalendarService, YetAnotherCalendarService {

	@Override
	public void createEvent(Date start, Date end, List<Person> participants) {
	}

	@Override
	public void addParticipants(Date start, Date end, @Valid List<@Valid Person> participants) {
	}
}
