/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.integration.cdi.executable;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.executable.ExecutableType;
import jakarta.validation.executable.ValidateOnExecution;

/**
 * @author Gunnar Morling
 */
@ValidateOnExecution(type = ExecutableType.ALL)
public interface ShipmentService {

	public void findShipment(@NotNull String id);

	@ValidateOnExecution(type = ExecutableType.GETTER_METHODS)
	@NotNull
	public Shipment getShipment();

	@NotNull
	public Shipment getAnotherShipment();
}
