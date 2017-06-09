/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation.graphnavigation.containerelement.model;

import javax.validation.constraints.NotNull;

public class VisitorWithGroups {

	@NotNull
	private final String name;

	@NotNull(groups = ExtendedChecks1.class)
	private final String extended1 = null;

	@NotNull(groups = ExtendedChecks2.class)
	private final String extended2 = null;

	public VisitorWithGroups(String name) {
		this.name = name;
	}
}
