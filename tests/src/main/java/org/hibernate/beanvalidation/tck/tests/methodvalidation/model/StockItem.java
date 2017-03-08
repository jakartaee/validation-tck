/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.methodvalidation.model;

import javax.validation.constraints.NotNull;

import org.hibernate.beanvalidation.tck.tests.methodvalidation.constraint.ValidStockItem;

/**
 * @author Gunnar Morling
 */
public class StockItem {

	@ValidStockItem
	public StockItem(@NotNull String name) {
	}

	@NotNull
	public String setName(@NotNull String name) {
		return null;
	}
}
