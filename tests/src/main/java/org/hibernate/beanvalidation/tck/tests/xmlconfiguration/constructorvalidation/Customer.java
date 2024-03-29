/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.constructorvalidation;

import jakarta.validation.constraints.NotNull;

public class Customer {
	public final String name;

	public Customer(String name) {
		this.name = name;
	}

	@NotNull
	public String getName() {
		return name;
	}

}
