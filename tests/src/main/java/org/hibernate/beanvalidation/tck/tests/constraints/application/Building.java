/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.application;

import jakarta.validation.constraints.Max;

/**
 * @author Hardy Ferentschik
 */
public class Building {
	@Max(value = 5000000, message = "Building costs are max {max} dollars.")
	long buildingCosts;

	@Max(value = 5000000, message = "Building costs are max {max} dollars.")
	public long getBuildingCosts() {
		return buildingCosts;
	}

	public Building(long buildingCosts) {
		this.buildingCosts = buildingCosts;
	}
}
