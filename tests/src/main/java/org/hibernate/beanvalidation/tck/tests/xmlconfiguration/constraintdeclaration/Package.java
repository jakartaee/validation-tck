/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.constraintdeclaration;

import javax.validation.GroupSequence;
import javax.validation.constraints.Max;

/**
 * @author Hardy Ferentschik
 */
@GroupSequence(value = { Package.class, PrePosting.class })
@ValidPackage(message = "ValidPackage defined as annotation", groups = PrePosting.class)
public class Package {
	@Max(value = 20, groups = Optional.class, message = "The package is too heavy")
	private int maxWeight;

	public int getMaxWeight() {
		return maxWeight;
	}

	public void setMaxWeight(int maxWeight) {
		this.maxWeight = maxWeight;
	}
}
