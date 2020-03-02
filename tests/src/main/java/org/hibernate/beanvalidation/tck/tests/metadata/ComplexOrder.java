/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.metadata;

import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.ConvertGroup;
import jakarta.validation.groups.Default;

/**
 * @author Guillaume Smet
 */
public class ComplexOrder {
	@NotNull(message = "Order number must be specified")
	Integer orderNumber;

	Map<
			@Valid @NotNull
			@ConvertGroup(from = Default.class, to = BasicChecks.class) @ConvertGroup(from = ComplexChecks.class, to = ComplexProductTypeChecks.class)
			ProductType,
			@Size(min = 2)
			List<@NotNull ProductOrderLine>> orderLines;

	public Integer getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Map<ProductType, List<ProductOrderLine>> getOrderLines() {
		return orderLines;
	}

	public void setOrderLines(Map<ProductType, List<ProductOrderLine>> orderLines) {
		this.orderLines = orderLines;
	}
}
