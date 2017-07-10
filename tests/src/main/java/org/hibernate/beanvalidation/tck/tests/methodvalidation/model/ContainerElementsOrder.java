/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.methodvalidation.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Gunnar Morling
 * @author Guillaume Smet
 */
public class ContainerElementsOrder {

	@Size(min = 6)
	private final String name;

	private Map<@NotNull ProductCategory, List<@Valid OrderLine>> lines = new HashMap<>();

	public ContainerElementsOrder(String name) {
		this.name = name;
		this.lines = new HashMap<>();
	}

	public ContainerElementsOrder(String name, Map<@NotNull ProductCategory, List<@Valid OrderLine>> lines) {
		this.name = name;
		this.lines = lines;
	}

	public String getName() {
		return name;
	}

	public void addOrderLine(ProductCategory category, OrderLine orderLine) {
		List<OrderLine> lines = this.lines.computeIfAbsent( category, c -> new ArrayList<>() );
		lines.add( orderLine );
	}

	public void replaceOrderLines(Map<@NotNull ProductCategory, List<@Valid OrderLine>> lines) {
		this.lines = lines;
	}

	@Valid
	public ContainerElementsOrder getOrder() {
		return this;
	}
}
