/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation.graphnavigation;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

/**
 * @author Hardy Ferentschik
 */
public class AnimalCaretaker {
	@Valid
	Map<String, Animal> caresFor = new HashMap<String, Animal>();

	public void addAnimal(String name, Animal animal) {
		caresFor.put( name, animal );
	}
}
