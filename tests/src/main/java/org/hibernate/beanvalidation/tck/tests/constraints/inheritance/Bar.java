/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.inheritance;

import javax.validation.constraints.DecimalMin;

/**
 * @author Hardy Ferentschik
 */
@ValidBar
public class Bar extends Foo implements Fubar {

	@Override
	public String getFubar() {
		return null;
	}

	@Override
	@DecimalMin("10")
	public String getName() {
		return "3";
	}

	@Override
	@DecimalMin("10")
	public String getLastName() {
		return "3";
	}
}
