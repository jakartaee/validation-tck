/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation.graphnavigation.containerelement.model;

public class SomeReference<T> implements Reference<T> {

	private final T value;

	public SomeReference(T value) {
		this.value = value;
	}

	@Override
	public T getValue() {
		return value;
	}
}
