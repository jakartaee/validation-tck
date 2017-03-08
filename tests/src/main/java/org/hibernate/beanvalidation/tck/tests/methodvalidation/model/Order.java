/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.methodvalidation.model;

import javax.validation.constraints.Size;

/**
 * @author Gunnar Morling
 */
public class Order {

	@Size(min = 6)
	private final String name;

	public Order(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
