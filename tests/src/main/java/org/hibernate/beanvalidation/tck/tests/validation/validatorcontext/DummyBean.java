/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation.validatorcontext;

/**
 * @author Hardy Ferentschik
 */
public class DummyBean {

	@Dummy
	String value;


	public DummyBean(String value) {
		this.value = value;
	}
}
