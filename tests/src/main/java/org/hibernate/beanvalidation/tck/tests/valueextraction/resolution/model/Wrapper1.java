/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.valueextraction.resolution.model;

/**
 * @author Guillaume Smet
 */
public class Wrapper1<T, V> implements IWrapper11<T, V> {

	private T property1;

	private V property2;

	public Wrapper1(T property1, V property2) {
		this.property1 = property1;
		this.property2 = property2;
	}

	@Override
	public T getProperty1() {
		return property1;
	}

	@Override
	public V getProperty2() {
		return property2;
	}
}
