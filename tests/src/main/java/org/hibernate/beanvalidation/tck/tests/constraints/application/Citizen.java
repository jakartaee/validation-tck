/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.application;

import javax.validation.constraints.Pattern;
import javax.validation.groups.Default;

/**
 * @author Hardy Ferentschik
 */
@SecurityCheck(groups = { Default.class, TightSecurity.class })
public interface Citizen {
	@Pattern(regexp = "^[0-9][0-9][0-9][0-9][0-9][0-9]-[0-9][0-9][0-9][0-9]$",
			message = "Personal number must be 10 digits with the last 4 separated by a dash.")
	public String getPersonalNumber();
}

interface TightSecurity {
}
