/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.valueextraction.unwrapping.model;

public class IntegerWrapper {

	private final Integer value;

	public IntegerWrapper(Integer value) {
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}
}
