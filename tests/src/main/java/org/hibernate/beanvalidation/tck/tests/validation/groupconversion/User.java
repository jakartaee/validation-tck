/*
* JBoss, Home of Professional Open Source
* Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual contributors
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* http://www.apache.org/licenses/LICENSE-2.0
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.hibernate.beanvalidation.tck.tests.validation.groupconversion;

import java.util.Collections;
import java.util.List;
import javax.validation.Valid;
import javax.validation.groups.ConvertGroup;
import javax.validation.groups.Default;

/**
 * @author Gunnar Morling
 */
public class User {

	@Valid
	@ConvertGroup(from = Default.class, to = BasicPostal.class)
	private Address mainAddress;

	private List<Address> shipmentAddresses;

	@Valid
	@ConvertGroup.List({
			@ConvertGroup(from = Default.class, to = BasicPostal.class),
			@ConvertGroup(from = Complex.class, to = ComplexPostal.class)
	})
	private Address preferredShipmentAddress;

	@Valid
	@ConvertGroup.List({
			@ConvertGroup(from = Default.class, to = BasicPostal.class),
			@ConvertGroup(from = BasicPostal.class, to = ComplexPostal.class)
	})
	private Address officeAddress;

	@Valid
	@ConvertGroup(from = Default.class, to = PostalSequence.class)
	private Address weekendAddress;

	@Valid
	@ConvertGroup(from = Default.class, to = BasicPostal.class)
	public User(
			@Valid
			@ConvertGroup(from = Default.class, to = BasicPostal.class)
			Address mainAddress) {
		this.mainAddress = mainAddress;
		this.shipmentAddresses = Collections.emptyList();
	}

	public User(Address mainAddress, List<Address> shipmentAddresses, Address preferredShipmentAddress, Address officeAddress, Address weekendAddress) {
		this.mainAddress = mainAddress;
		this.shipmentAddresses = shipmentAddresses;
		this.preferredShipmentAddress = preferredShipmentAddress;
		this.officeAddress = officeAddress;
		this.weekendAddress = weekendAddress;
	}

	public void setMainAddress(
			@Valid
			@ConvertGroup(from = Default.class, to = BasicPostal.class)
			Address mainAddress) {
		this.mainAddress = mainAddress;
	}

	public void setShipmentAddresses(List<Address> shipmentAddresses) {
		this.shipmentAddresses = shipmentAddresses;
	}

	@Valid
	@ConvertGroup(from = Default.class, to = BasicPostal.class)
	public List<Address> getShipmentAddresses() {
		return shipmentAddresses;
	}

	public void setPreferredShipmentAddress(Address preferredShipmentAddress) {
		this.preferredShipmentAddress = preferredShipmentAddress;
	}

	public void setOfficeAddress(Address officeAddress) {
		this.officeAddress = officeAddress;
	}

	public void setWeekendAddress(Address weekendAddress) {
		this.weekendAddress = weekendAddress;
	}

	public Address getWeekendAddress() {
		return weekendAddress;
	}

	@Valid
	@ConvertGroup(from = Default.class, to = BasicPostal.class)
	public Address retrieveMainAddress() {
		return mainAddress;
	}

	public Address retrieveWeekendAddress() {
		return weekendAddress;
	}
}
