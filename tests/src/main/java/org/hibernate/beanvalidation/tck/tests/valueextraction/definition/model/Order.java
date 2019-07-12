/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.valueextraction.definition.model;

import javax.validation.constraints.NotNull;

public class Order {

	@NotNull
	private final String id;

	public Order(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
}
