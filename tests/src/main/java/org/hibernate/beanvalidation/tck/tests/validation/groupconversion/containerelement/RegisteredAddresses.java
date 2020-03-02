/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation.groupconversion.containerelement;

import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;
import jakarta.validation.groups.ConvertGroup;
import jakarta.validation.groups.Default;

/**
 * @author Guillaume Smet
 */
public class RegisteredAddresses {

	private final Map<Year, List<@Valid @ConvertGroup(from = Default.class, to = BasicPostal.class) Address>> mainAddresses = new HashMap<>();

	private final Map<Year, List<Address>> shipmentAddresses = new HashMap<>();

	private final Map<Year, List<@Valid
			@ConvertGroup(from = Default.class, to = BasicPostal.class)
			@ConvertGroup(from = Complex.class, to = ComplexPostal.class)
	Address>> preferredShipmentAddresses = new HashMap<>();

	private final Map<Year, List<@Valid
			@ConvertGroup(from = Default.class, to = BasicPostal.class)
			@ConvertGroup(from = BasicPostal.class, to = ComplexPostal.class)
	Address>> officeAddresses = new HashMap<>();

	private final Map<Year, List<@Valid @ConvertGroup(from = Default.class, to = PostalSequence.class) Address>> weekendAddresses = new HashMap<>();

	public RegisteredAddresses() {
	}

	@Valid
	@ConvertGroup(from = Default.class, to = BasicPostal.class)
	public RegisteredAddresses(Map<Year, List<@Valid @ConvertGroup(from = Default.class, to = BasicPostal.class) Address>> mainAddresses) {
		this.mainAddresses.putAll( mainAddresses );
	}

	public RegisteredAddresses addMainAddress(Year year, Address address) {
		mainAddresses.computeIfAbsent( year, y -> new ArrayList<>() ).add( address );
		return this;
	}

	public RegisteredAddresses addShipmentAddress(Year year, Address address) {
		shipmentAddresses.computeIfAbsent( year, y -> new ArrayList<>() ).add( address );
		return this;
	}

	public RegisteredAddresses addPreferredShipmentAddress(Year year, Address address) {
		preferredShipmentAddresses.computeIfAbsent( year, y -> new ArrayList<>() ).add( address );
		return this;
	}

	public RegisteredAddresses addOfficeAddress(Year year, Address address) {
		officeAddresses.computeIfAbsent( year, y -> new ArrayList<>() ).add( address );
		return this;
	}

	public RegisteredAddresses addWeekendAddress(Year year, Address address) {
		weekendAddresses.computeIfAbsent( year, y -> new ArrayList<>() ).add( address );
		return this;
	}

	public Map<Year, List<Address>> getMainAddresses() {
		return mainAddresses;
	}

	public void setMainAddresses(Map<Year, List<@Valid @ConvertGroup(from = Default.class, to = BasicPostal.class) Address>> mainAddresses) {
		this.mainAddresses.clear();
		this.mainAddresses.putAll( mainAddresses );
	}

	public Map<Year, List<Address>> getPreferredShipmentAddresses() {
		return preferredShipmentAddresses;
	}

	public Map<Year, List<@Valid @ConvertGroup(from = Default.class, to = BasicPostal.class) Address>> getShipmentAddresses() {
		return shipmentAddresses;
	}

	public Map<Year, List<Address>> getOfficeAddresses() {
		return officeAddresses;
	}

	public Map<Year, List<Address>> getWeekendAddresses() {
		return weekendAddresses;
	}

	public Map<Year, List<@Valid @ConvertGroup(from = Default.class, to = BasicPostal.class) Address>> retrieveMainAddresses() {
		return mainAddresses;
	}

	public Map<Year, List<Address>> retrieveWeekendAddresses() {
		return weekendAddresses;
	}
}
