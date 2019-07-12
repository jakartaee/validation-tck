/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations.service.impl;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations.model.Person;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations.service.AnotherCalendarService;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations.service.CalendarService;

/**
 * @author Gunnar Morling
 */
public class ImplementationOfConstrainedAndUnconstrainedInterfaces
		implements CalendarService, AnotherCalendarService {

	@Override
	public void createEvent(Date start, Date end, List<Person> participants) {
	}

	@Override
	public void addParticipants(Date start, Date end, List<@NotNull Person> participants) {
	}
}
