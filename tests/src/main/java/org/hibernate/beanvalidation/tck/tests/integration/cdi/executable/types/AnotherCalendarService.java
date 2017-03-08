/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.integration.cdi.executable.types;

import javax.inject.Inject;
import javax.validation.constraints.Size;
import javax.validation.executable.ExecutableType;
import javax.validation.executable.ValidateOnExecution;

/**
 * @author Gunnar Morling
 */
public class AnotherCalendarService {

	@Inject
	@ValidateOnExecution(type = ExecutableType.NON_GETTER_METHODS)
	public AnotherCalendarService(@LongName @Size(min = 5) String name) {
	}
}
