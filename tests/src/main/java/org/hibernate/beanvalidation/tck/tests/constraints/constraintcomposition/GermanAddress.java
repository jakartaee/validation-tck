/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.constraintcomposition;

/**
 * @author Hardy Ferentschik
 */
public class GermanAddress extends Address {

	@GermanZipcode
	public String getZipCode() {
		return super.getZipCode();
	}
}
