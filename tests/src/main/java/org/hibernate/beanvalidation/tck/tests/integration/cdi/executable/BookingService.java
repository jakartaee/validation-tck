/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.integration.cdi.executable;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

/**
 * @author Gunnar Morling
 */
public class BookingService {

	public interface IgnoredValidationGroup {
	}

	private int invocationCount = 0;

	@DecimalMin("10001")
	@Null(groups = IgnoredValidationGroup.class)
	public String placeBooking(@Size(min = 5) @Null(groups = IgnoredValidationGroup.class) String name) {
		invocationCount++;
		return name;
	}

	public int getInvocationCount() {
		return invocationCount;
	}
}
