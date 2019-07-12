/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation.graphnavigation;

import javax.validation.constraints.Min;

/**
 * @author Hardy Ferentschik
 */
public class Condor extends Animal {
	@Min(value = 250, message = "The wingspan of a condor is at least 250 cm")
	private int wingspan;

	public int getWingspan() {
		return wingspan;
	}

	public void setWingspan(int wingspan) {
		this.wingspan = wingspan;
	}
}
