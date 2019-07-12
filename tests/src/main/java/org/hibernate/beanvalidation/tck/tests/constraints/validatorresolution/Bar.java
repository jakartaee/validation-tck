/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.validatorresolution;

import java.io.Serializable;

import javax.validation.constraints.Size;

/**
 * @author Hardy Ferentschik
 */
public class Bar implements Serializable, Dummy {
	// validating Bar actually raises an UnexpectedTypeException - @Size is not defined for Integer
	@Size
	private Integer value = 0;

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}
}
