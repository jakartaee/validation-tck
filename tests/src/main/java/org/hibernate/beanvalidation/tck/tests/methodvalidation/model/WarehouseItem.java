/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.methodvalidation.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.executable.ExecutableType;
import jakarta.validation.executable.ValidateOnExecution;

import org.hibernate.beanvalidation.tck.tests.methodvalidation.constraint.ValidWarehouseItem;

/**
 * @author Gunnar Morling
 */
public class WarehouseItem {

	@ValidateOnExecution(type = ExecutableType.NONE)
	@ValidWarehouseItem
	public WarehouseItem(@NotNull String name) {
	}

	@ValidateOnExecution(type = ExecutableType.NONE)
	@NotNull
	public String setName(@NotNull String name) {
		return null;
	}
}
