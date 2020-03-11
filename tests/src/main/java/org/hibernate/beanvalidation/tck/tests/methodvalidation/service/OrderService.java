/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.methodvalidation.service;

import jakarta.validation.GroupSequence;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.ConvertGroup;
import jakarta.validation.groups.Default;

import org.hibernate.beanvalidation.tck.tests.methodvalidation.constraint.MyCrossParameterConstraint;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.constraint.ValidOrder;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.constraint.ValidOrderService;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.constraint.ValidRetailOrder;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.constraint.ValidRetailOrderService;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.model.Item;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.model.Order;

/**
 * Provides test methods used in {@link org.hibernate.beanvalidation.tck.tests.methodvalidation.MethodValidationTest}.
 *
 * @author Gunnar Morling
 */
public class OrderService {

	public interface Basic {
	}

	public interface Complex {
	}

	@GroupSequence({ Basic.class, Complex.class })
	public interface OrderServiceSequence {
	}

	@Size(min = 6)
	private String name;

	public OrderService() {
	}

	public OrderService(String name) {
		this.name = name;
	}

	//(method|constructor)ParameterValidationTargetsParameterCrossParameterAndCascadedConstraints
	//(method|constructor)ReturnValueValidationTargetsReturnValueAndCascadedConstraints
	@MyCrossParameterConstraint
	@ValidOrder
	@Valid
	public Order placeOrder(@NotNull String customer, @Valid Item item, @Min(1) int quantity) {
		return null;
	}

	@MyCrossParameterConstraint
	@ValidOrderService
	@Valid
	public OrderService(@NotNull String customer, @Valid Item item, @Min(1) int quantity) {
	}

	//(method|constructor)(Parameter|ReturnValue)ValidationIsAppliedGroupWise
	@MyCrossParameterConstraint(groups = Basic.class)
	@ValidOrder
	@ValidRetailOrder(groups = Basic.class)
	@Valid
	public Order placeOrder(@NotNull(groups = Basic.class) String customer, @Valid Item item, @Min(1) Integer quantity) {
		return null;
	}

	@MyCrossParameterConstraint(groups = Basic.class)
	@ValidOrderService
	@ValidRetailOrderService(groups = Basic.class)
	@Valid
	public OrderService(@NotNull(groups = Basic.class) String customer, @Valid Item item, @Min(1) Integer quantity) {
	}

	//(method|constructor)(Parameter|ReturnValue)ValidationPerformsGroupConversion
	@Valid
	@ConvertGroup(from = Basic.class, to = Default.class)
	public Order placeOrder(String customer, @Valid @ConvertGroup(from = Basic.class,
			to = Default.class) Item item, long quantity) {
		return null;
	}

	@Valid
	@ConvertGroup(from = Basic.class, to = Default.class)
	public OrderService(String customer, @Valid @ConvertGroup(from = Basic.class,
			to = Default.class) Item item, long quantity) {
	}

	//(method|constructor)(Parameter|ReturnValue)ValidationValidatesEachConstraintOnlyOnce
	@MyCrossParameterConstraint(groups = { Basic.class, Default.class })
	@ValidOrder(groups = { Basic.class, Default.class })
	public Order placeOrder(
			@NotNull(groups = { Basic.class, Default.class }) String customer,
			@Valid @ConvertGroup(from = Basic.class, to = Item.Basic.class) Item item,
			@Min(value = 1, groups = { Basic.class, Default.class }) short quantity) {
		return null;
	}

	@MyCrossParameterConstraint(groups = { Basic.class, Default.class })
	@ValidOrderService(groups = { Basic.class, Default.class })
	public OrderService(
			@NotNull(groups = { Basic.class, Default.class }) String customer,
			@Valid @ConvertGroup(from = Basic.class, to = Item.Basic.class) Item item,
			@Min(value = 1, groups = { Basic.class, Default.class }) short quantity) {
	}

	//(method|constructor)(Parameter|ReturnValue)ValidationUsingSequence
	@MyCrossParameterConstraint(groups = Complex.class)
	@ValidOrder(groups = Basic.class)
	@ValidRetailOrder(groups = Complex.class)
	@Valid
	@ConvertGroup(from = Basic.class, to = Default.class)
	public Order placeOrder(
			@NotNull(groups = Basic.class) String customer,
			@Valid @ConvertGroup(from = Basic.class, to = Default.class) Item item,
			@Min(value = 1, groups = Complex.class) byte quantity) {
		return null;
	}

	@MyCrossParameterConstraint(groups = Complex.class)
	@ValidOrderService(groups = Basic.class)
	@ValidRetailOrderService(groups = Complex.class)
	@Valid
	@ConvertGroup(from = Basic.class, to = Default.class)
	public OrderService(
			@NotNull(groups = Basic.class) String customer,
			@Valid @ConvertGroup(from = Basic.class, to = Default.class) Item item,
			@Min(value = 1, groups = Complex.class) byte quantity) {
	}

	public String getName() {
		return name;
	}
}
