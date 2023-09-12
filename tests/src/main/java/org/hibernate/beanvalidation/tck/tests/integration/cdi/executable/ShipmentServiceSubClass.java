/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.integration.cdi.executable;

/**
 * @author Gunnar Morling
 */
public class ShipmentServiceSubClass extends ShipmentBase {

	@Override
	public void findShipment(String id) {
	}

	@Override
	public Shipment getShipment() {
		return null;
	}

	@Override
	public Shipment getAnotherShipment() {
		return null;
	}
}
