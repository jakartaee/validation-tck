/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.valueextraction.declaration.model;

import jakarta.validation.constraints.NotBlank;

/**
 * @author Gunnar Morling
 */
public class Visitor {

	@NotBlank
	private String name;

	public Visitor() {
	}

	public Visitor(String name) {
		this.name = name;
	}
}
