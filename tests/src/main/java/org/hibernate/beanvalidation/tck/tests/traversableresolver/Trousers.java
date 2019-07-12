/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.traversableresolver;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @author Emmanuel Bernard
 */
public class Trousers {
	@Min(value = 70)
	@Max(value = 220)
	private Integer length;

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}
}
