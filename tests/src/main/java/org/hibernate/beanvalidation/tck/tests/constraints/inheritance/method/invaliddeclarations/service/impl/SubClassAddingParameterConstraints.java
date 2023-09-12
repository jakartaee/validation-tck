/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations.service.impl;

import java.util.Date;
import java.util.List;

import jakarta.validation.constraints.NotNull;

import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations.model.Person;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations.service.AbstractCalendarService;

/**
 * @author Gunnar Morling
 */
public class SubClassAddingParameterConstraints extends AbstractCalendarService {

	@Override
	public void createEvent(@NotNull Date start, @NotNull Date end, @NotNull List<Person> participants) {
	}

	@Override
	public void addParticipants(Date start, Date end, List<@NotNull Person> participants) {
	}
}
