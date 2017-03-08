/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.metadata;

import javax.validation.constraints.NotNull;


/**
 * @author Hardy Ferentschik
 */
@SuperConstraint
public class SuperClass {
	@NotNull(groups = BasicGroup.class)
	private String myField = "12345678901234567890";

	public String getMyField() {
		return myField;
	}

	interface UnusedGroup {
	}

	interface BasicGroup {
	}

	interface InheritedGroup extends BasicGroup {
	}
}
