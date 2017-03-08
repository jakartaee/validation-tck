/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.methodvalidation.service;

import javax.validation.GroupSequence;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.beanvalidation.tck.tests.methodvalidation.constraint.MyCrossParameterConstraint;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.constraint.ValidOrder;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.constraint.ValidOrderService;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.constraint.ValidRetailOrder;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.constraint.ValidRetailOrderService;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.model.Item;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.model.Order;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.service.OrderServiceWithRedefinedDefaultGroupSequence.Basic;

/**
 * @author Gunnar Morling
 */
@GroupSequence({ Basic.class, OrderServiceWithRedefinedDefaultGroupSequence.class })
public class OrderServiceWithRedefinedDefaultGroupSequence {

	public interface Basic {
	}

	@Size(min = 5, groups = Basic.class)
	@Pattern(regexp = "aaa")
	private String name;

	public OrderServiceWithRedefinedDefaultGroupSequence() {
	}

	public OrderServiceWithRedefinedDefaultGroupSequence(String name) {
		this.name = name;
	}

	@MyCrossParameterConstraint
	@ValidOrder(groups = Basic.class)
	@ValidRetailOrder
	@Valid
	public Order placeOrder(
			@NotNull(groups = Basic.class) String customer,
			@Valid Item item,
			@Min(value = 1) byte quantity) {
		return null;
	}

	@MyCrossParameterConstraint
	@ValidOrderService(groups = Basic.class)
	@ValidRetailOrderService
	@Valid
	public OrderServiceWithRedefinedDefaultGroupSequence(@NotNull(groups = Basic.class) String customer, @Valid Item item, @Min(
			value = 1) byte quantity) {
	}

	public String getName() {
		return name;
	}
}
