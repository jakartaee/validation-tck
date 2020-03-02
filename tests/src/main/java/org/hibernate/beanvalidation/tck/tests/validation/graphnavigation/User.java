/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation.graphnavigation;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;

/**
 * @author Hardy Ferentschik
 */
public class User {

	@NotNull
	private String firstName;

	@NotNull(groups = Default.class)
	private String lastName;

	@Valid
	private List<Address> addresses = new ArrayList<Address>();

	@Valid
	private List<User> knowsUser = new ArrayList<User>();

	public User() {
	}

	public User(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	public void addAddress(Address address) {
		addresses.add( address );
	}

	public void knows(User user) {
		knowsUser.add( user );
	}

	public List<User> getKnowsUser() {
		return knowsUser;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append( "User" );
		sb.append( "{addresses=" ).append( addresses );
		sb.append( ", lastName='" ).append( lastName ).append( '\'' );
		sb.append( ", firstName='" ).append( firstName ).append( '\'' );
		sb.append( '}' );
		return sb.toString();
	}
}
