/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.integration.cdi.executable.types;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.executable.ValidateOnExecution;

/**
 * @author Gunnar Morling
 */
@ValidateOnExecution
public class DeliveryService {

	@ValidateOnExecution
	public void findDelivery(@NotNull String id) {
	}

	@ValidateOnExecution
	@NotNull
	public Delivery getDelivery() {
		return null;
	}

	@NotNull
	public Delivery getAnotherDelivery() {
		return null;
	}
}
