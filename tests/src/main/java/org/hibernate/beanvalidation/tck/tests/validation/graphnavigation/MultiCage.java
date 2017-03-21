/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation.graphnavigation;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

/**
 * @author Hardy Ferentschik
 */
public class MultiCage {
	@Valid
	private Set<Animal> animalsInCage = new HashSet<Animal>();

	public void addAnimal(Animal animal) {
		animalsInCage.add( animal );
	}
}
