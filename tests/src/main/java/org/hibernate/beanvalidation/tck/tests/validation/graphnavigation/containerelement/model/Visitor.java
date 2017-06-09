/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation.graphnavigation.containerelement.model;

import javax.validation.constraints.NotNull;

/**
 * @author Gunnar Morling
 */
public class Visitor {

	@NotNull
	private String name;

	public Visitor() {
	}

	public Visitor(String name) {
		this.name = name;
	}
}
