/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.constraintcomposition;

import javax.validation.groups.Default;

/**
 * @author Hardy Ferentschik
 */
public class FrenchAddress extends Address {

	@Override
	@FrenchZipcode(groups = { Default.class, FullAddressCheck.class })
	public String getZipCode() {
		return super.getZipCode();
	}

	public interface FullAddressCheck {
	}
}
