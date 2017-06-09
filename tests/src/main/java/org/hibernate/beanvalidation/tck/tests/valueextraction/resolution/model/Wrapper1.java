/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.valueextraction.resolution.model;

/**
 * @author Guillaume Smet
 */
public class Wrapper1<T> implements IWrapper11<T> {

	private T property;

	public Wrapper1(T property) {
		this.property = property;
	}

	@Override
	public T getProperty() {
		return property;
	}
}
