/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation.graphnavigation;

import jakarta.validation.constraints.NotNull;

/**
 * @author Emmanuel Bernard
 */
public class Child {
	private String name;

	@NotNull(groups = Parent.ChildFirst.class)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
