/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.integration.cdi.executable;

import jakarta.inject.Inject;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;

/**
 * @author Gunnar Morling
 */
public class AnotherBookingService {

	public interface IgnoredValidationGroup {
	}

	private static int invocationCount = 0;

	private static String name;

	@Inject
	@ValidAnotherBookingService
	@Null(groups = IgnoredValidationGroup.class)
	public AnotherBookingService(@LongName @Size(min = 5) @Null(groups = IgnoredValidationGroup.class) String name) {
		AnotherBookingService.name = name;
		invocationCount++;
	}

	public static String getName() {
		return name;
	}

	public static int getInvocationCount() {
		return invocationCount;
	}
}
