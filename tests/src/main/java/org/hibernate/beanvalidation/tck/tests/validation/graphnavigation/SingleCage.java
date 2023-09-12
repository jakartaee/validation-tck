/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation.graphnavigation;

import jakarta.validation.Valid;

/**
 * @author Hardy Ferentschik
 */
public class SingleCage {
	@Valid
	private Animal containAnimal;

	public Animal getContainAnimal() {
		return containAnimal;
	}

	public void setContainAnimal(Animal containAnimal) {
		this.containAnimal = containAnimal;
	}
}
