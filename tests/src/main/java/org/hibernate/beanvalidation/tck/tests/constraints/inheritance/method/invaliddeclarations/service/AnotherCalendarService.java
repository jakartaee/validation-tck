/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations.service;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.invaliddeclarations.model.Person;

/**
 * @author Gunnar Morling
 */
public interface AnotherCalendarService {

	void createEvent(@NotNull Date start, @NotNull Date end, @NotNull List<Person> participants);

	void addParticipants(Date start, Date end, List<@NotNull Person> participants);
}
