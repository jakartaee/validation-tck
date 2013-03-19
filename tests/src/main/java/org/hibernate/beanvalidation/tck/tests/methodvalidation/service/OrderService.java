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
package org.hibernate.beanvalidation.tck.tests.methodvalidation.service;

import javax.validation.GroupSequence;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.groups.ConvertGroup;
import javax.validation.groups.Default;

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
