/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation.graphnavigation.containerelement.model;

import jakarta.validation.constraints.NotBlank;

public class Address {

	@NotBlank
	private final String street;

	@NotBlank
	private final String zipCode;

	@NotBlank
	private final String city;

	private Address(String street, String zipCode, String city) {
		this.street = street;
		this.zipCode = zipCode;
		this.city = city;
	}

	public static Address invalid() {
		return new Address( "avenue Félix Faure", "", "Lyon" );
	}

	public static Address valid() {
		return new Address( "avenue Félix Faure", "69003", "Lyon" );
	}
}
