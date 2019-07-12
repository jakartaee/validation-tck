/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation.graphnavigation;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.Valid;

/**
 * @author Hardy Ferentschik
 */
public class Zoo {
	@Valid
	Collection<Animal> inhabitants = new ArrayList<Animal>();

	public Collection<Animal> getInhabitants() {
		return inhabitants;
	}

	public void addAnimal(Animal animal) {
		inhabitants.add( animal );
	}
}
