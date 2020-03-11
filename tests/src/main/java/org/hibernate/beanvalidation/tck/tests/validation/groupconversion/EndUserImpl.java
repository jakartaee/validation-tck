/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation.groupconversion;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.groups.ConvertGroup;
import jakarta.validation.groups.Default;

/**
 * @author Gunnar Morling
 */
public class EndUserImpl extends User implements EndUser {

	public EndUserImpl(Address mainAddress, List<Address> shipmentAddresses, Address preferredShipmentAddress, Address officeAddress, Address weekendAddress) {
		super(
				mainAddress,
				shipmentAddresses,
				preferredShipmentAddress,
				officeAddress,
				weekendAddress
		);
	}

	@Valid
	@ConvertGroup(from = Default.class, to = BasicPostal.class)
	@Override
	public Address retrieveWeekendAddress() {
		return null;
	}

	@Valid
	@ConvertGroup(from = Default.class, to = BasicPostal.class)
	@Override
	public Address retrieveFallbackAddress() {
		return null;
	}
}
