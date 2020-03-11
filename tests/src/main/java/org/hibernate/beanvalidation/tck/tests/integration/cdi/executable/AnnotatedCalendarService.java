/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.integration.cdi.executable;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.executable.ExecutableType;
import jakarta.validation.executable.ValidateOnExecution;

/**
 * @author Gunnar Morling
 */
@ValidateOnExecution(type = ExecutableType.NONE)
public class AnnotatedCalendarService {

	@ValidateOnExecution(type = ExecutableType.NON_GETTER_METHODS)
	public void createEvent(@NotNull String name) {
	}

	@ValidateOnExecution(type = ExecutableType.GETTER_METHODS)
	public Event createEvent(@Min(0) int duration) {
		return new Event();
	}
}
