/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation.groupconversion;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * @author Gunnar Morling
 */
public class Address {
	@NotNull(groups = BasicPostal.class)
	private String street1;

	@NotNull
	private final String street2;

	@Size(groups = BasicPostal.class, min = 3)
	private final String zipCode;

	@Size(groups = ComplexPostal.class, max = 2)
	private String doorCode;

	public Address(String street1, String street2, String zipCode, String doorCode) {
		this.street1 = street1;
		this.street2 = street2;
		this.zipCode = zipCode;
		this.doorCode = doorCode;
	}

	public void setStreet1(String street1) {
		this.street1 = street1;
	}

	public void setDoorCode(String doorCode) {
		this.doorCode = doorCode;
	}
}
