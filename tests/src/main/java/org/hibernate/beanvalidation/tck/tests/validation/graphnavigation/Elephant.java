/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation.graphnavigation;

import jakarta.validation.constraints.Min;

/**
 * @author Hardy Ferentschik
 */
public class Elephant extends Animal {
	@Min(value = 1000, message = "An elephant weighs at least 1000 kg")
	private int weight;

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}
}
