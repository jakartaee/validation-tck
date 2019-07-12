/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation.graphnavigation;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author Hardy Ferentschik
 */
public class OrderLine {
	@NotNull
	Integer articleNumber;

	@Valid
	Order order;

	public OrderLine(Order order, Integer articleNumber) {
		this.articleNumber = articleNumber;
		this.order = order;
	}

	public Integer getArticleNumber() {
		return articleNumber;
	}

	public void setArticleNumber(Integer articleNumber) {
		this.articleNumber = articleNumber;
	}

	@Override
	public String toString() {
		return "OrderLine{" +
				"articleNumber=" + articleNumber +
				'}';
	}
}
