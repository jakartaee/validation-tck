/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.methodvalidation.model;

import jakarta.validation.constraints.Size;
import jakarta.validation.groups.Default;

/**
 * @author Gunnar Morling
 */
public class Item {

	public interface Basic {
	}

	@Size(min = 5, groups = { Basic.class, Default.class })
	private final String name;

	public Item(String name) {
		this.name = name;
	}
}
