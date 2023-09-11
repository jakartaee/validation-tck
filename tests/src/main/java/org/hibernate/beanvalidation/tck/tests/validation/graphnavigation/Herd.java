/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation.graphnavigation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Hardy Ferentschik
 */
public class Herd<T extends Animal> implements Iterable<T> {
	List<T> animals = new ArrayList<T>();

	public void addAnimal(T animal) {
		animals.add( animal );
	}

	public Iterator<T> iterator() {
		return animals.iterator();
	}
}
