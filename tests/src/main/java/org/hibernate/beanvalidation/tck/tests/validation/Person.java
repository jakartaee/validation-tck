/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation;

import javax.validation.constraints.NotNull;

/**
 * @author Hardy Ferentschik
 */
public interface Person {
	@NotNull
	String getFirstName();

	String getMiddleName();

	@NotNull(message = "Everyone has a last name.")
	String getLastName();
}
