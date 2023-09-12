/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation.graphnavigation.containerelement.model;

import jakarta.validation.constraints.NotBlank;

public class AddressType {

	private static final AddressType INVALID = new AddressType( "" );

	private static final AddressType VALID = new AddressType( "Personal" );

	@NotBlank
	private final String type;

	private AddressType(String type) {
		this.type = type;
	}

	public static AddressType invalid() {
		return INVALID;
	}

	public static AddressType valid() {
		return VALID;
	}
}
