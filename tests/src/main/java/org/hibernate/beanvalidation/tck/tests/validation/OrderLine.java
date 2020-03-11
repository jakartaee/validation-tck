/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation;

import jakarta.validation.Valid;

/**
 * @author Gunnar Morling
 */
public class OrderLine {

	private Item item;

	@Valid
	public OrderLine(@Valid Item item) {
		this.item = item;
	}

	public void setItem(@Valid Item item) {
		this.item = item;
	}

	@Valid
	public Item getItem() {
		return item;
	}
}
