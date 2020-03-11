/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.Default;

/**
 * @author Hardy Ferentschik
 */
public class Address {

	@NotEmpty(groups = Minimal.class)
	private String street;

	@NotNull(message = "You have to specify a city.")
	@Size(max = 30, message = "City name cannot be longer than {max} characters.")
	private String city;

	@NotEmpty(groups = { Minimal.class, Default.class })
	private String zipCode;


	public String getStreet1() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public interface Minimal {
	}
}
