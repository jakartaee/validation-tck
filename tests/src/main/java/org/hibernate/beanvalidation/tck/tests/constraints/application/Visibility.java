/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.application;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;

/**
 * @author Hardy Ferentschik
 */
public class Visibility {
	@Min(value = 100, message = "publicField")
	public int publicValue;

	@Min(value = 100, message = "protectedField")
	protected int protectedValue;

	@Min(value = 100, message = "privateField")
	private int privateValue;

	@DecimalMin(value = "100.0", message = "publicProperty")
	public int getPublicValue() {
		return publicValue;
	}

	public void setValues(int value) {
		this.publicValue = value;
		this.protectedValue = value;
		this.privateValue = value;
	}

	@DecimalMin(value = "100.0", message = "protectedProperty")
	protected int getProtectedValue() {
		return protectedValue;
	}

	@DecimalMin(value = "100.0", message = "privateProperty")
	private int getPrivateValue() {
		return privateValue;
	}
}
