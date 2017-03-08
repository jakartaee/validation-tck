/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.integration.cdi.executable.types;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.executable.ExecutableType;
import javax.validation.executable.ValidateOnExecution;

/**
 * @author Gunnar Morling
 */
public class CalendarService {

	//NONE

	@ValidateOnExecution(type = ExecutableType.NONE)
	public Event createEvent(@NotNull String title) {
		return new Event();
	}

	@ValidateOnExecution(type = { })
	public Event createEvent(@Min(0) int duration) {
		return new Event();
	}

	@ValidateOnExecution(type = { ExecutableType.NONE, ExecutableType.NON_GETTER_METHODS })
	public void createEvent(@Min(0) long duration) {
	}

	//NON_GETTER_METHODS

	@ValidateOnExecution(type = ExecutableType.NON_GETTER_METHODS)
	public Event createEvent(@Min(0) short duration) {
		return new Event();
	}

	@ValidateOnExecution(type = ExecutableType.NON_GETTER_METHODS)
	@ValidObject
	public Event createEvent(byte duration) {
		return new Event();
	}

	@ValidateOnExecution(type = ExecutableType.NON_GETTER_METHODS)
	@ValidObject
	public Event getEvent() {
		return new Event();
	}

	//GETTER_METHODS

	@ValidateOnExecution(type = ExecutableType.GETTER_METHODS)
	@ValidObject
	public Event getSpecialEvent() {
		return new Event();
	}

	@ValidateOnExecution(type = ExecutableType.GETTER_METHODS)
	@ValidObject
	public Event getSpecialEvent(int duration) {
		return new Event();
	}

	//ALL

	@ValidateOnExecution(type = ExecutableType.ALL)
	public Event createEvent(@Min(0) double duration) {
		return new Event();
	}

	@ValidateOnExecution(type = ExecutableType.ALL)
	@ValidObject
	public Event getVerySpecialEvent() {
		return new Event();
	}

	@ValidateOnExecution(type = { ExecutableType.ALL, ExecutableType.NONE })
	public Event createEvent(@Min(0) float duration) {
		return new Event();
	}
}
