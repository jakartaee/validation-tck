/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation.graphnavigation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * @author Hardy Ferentschik
 */
public class Address {

	@NotNull
	@Size(max = 30)
	private String addressline1;

	private String zipCode;

	@Size(max = 30)
	@NotNull
	private String city;

	@Valid
	private User inhabitant;

	public Address() {
	}

	public Address(String addressline1, String zipCode, String city) {
		this.addressline1 = addressline1;
		this.zipCode = zipCode;
		this.city = city;
	}

	public String getAddressline1() {
		return addressline1;
	}

	public void setAddressline1(String addressline1) {
		this.addressline1 = addressline1;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public User getInhabitant() {
		return inhabitant;
	}

	public void setInhabitant(User inhabitant) {
		this.inhabitant = inhabitant;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append( "Address" );
		sb.append( "{addressline1='" ).append( addressline1 ).append( '\'' );
		sb.append( ", zipCode='" ).append( zipCode ).append( '\'' );
		sb.append( ", city='" ).append( city ).append( '\'' );
		sb.append( '}' );
		return sb.toString();
	}
}
