/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation.graphnavigation;

import jakarta.validation.constraints.NotNull;

/**
 * @author Hardy Ferentschik
 */
public class Zebra extends Animal {
	private String name;

	public Zebra(String name) {
		this.name = name;
	}

	@NotNull(message = "A zebra needs a name")
	public String getName() {
		return name;
	}
}
