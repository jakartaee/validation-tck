/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.integration.cdi.executable;

import jakarta.validation.constraints.NotNull;

/**
 * @author Gunnar Morling
 */
public class Event {

	@NotNull
	private String name;

	public String getName() {
		return name;
	}
}
