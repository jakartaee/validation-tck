/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations.service;

import java.util.Date;
import java.util.List;

import jakarta.validation.Valid;

import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations.model.Person;

/**
 * @author Gunnar Morling
 */
public interface YetAnotherCalendarService {

	void createEvent(Date start, Date end, @Valid List<Person> participants);

	void addParticipants(Date start, Date end, List<@Valid Person> participants);
}
