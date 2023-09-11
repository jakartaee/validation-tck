/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.groups;

import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.Default;

/**
 * @author Emmanuel Bernard
 * @author Hardy Ferentschik
 */
@GroupSequence({ Car.class, Car.Test.class })
public class Car {
	@Pattern(regexp = ".*", groups = Default.class)
	@Size(min = 2, max = 20, groups = Car.Test.class, message = "Car type has to be between {min} and {max} characters.")
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public interface Test {

	}
}
