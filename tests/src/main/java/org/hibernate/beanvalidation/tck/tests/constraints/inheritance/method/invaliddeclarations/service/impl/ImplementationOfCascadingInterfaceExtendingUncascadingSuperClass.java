/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations.service.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations.model.Person;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations.service.AbstractCalendarService;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations.service.YetAnotherCalendarService;

/**
 * @author Gunnar Morling
 */
public class ImplementationOfCascadingInterfaceExtendingUncascadingSuperClass
		extends AbstractCalendarService implements YetAnotherCalendarService {

	@Override
	public void createEvent(Date start, Date end, List<Person> participants) {
	}

	@Override
	public void addParticipants(Date start, Date end, List<Person> participants) {
	}
}
