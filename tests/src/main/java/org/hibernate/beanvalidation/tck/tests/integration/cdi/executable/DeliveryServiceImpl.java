/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.integration.cdi.executable;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.executable.ExecutableType;
import jakarta.validation.executable.ValidateOnExecution;

/**
 * @author Gunnar Morling
 */
// The @ValidateOnExecution annotation here applies only to those methods not
// overriding a super-type method
@ValidateOnExecution(type = ExecutableType.NONE)
public class DeliveryServiceImpl implements DeliveryService {

	//should be validated, since @ValidateOnExecution from overridden method applies
	@Override
	public void createDelivery(String name) {
	}

	//should not be validated, since @ValidateOnExecution from this type applies
	public String createExpressDelivery(@NotNull String name) {
		return "Express delivery";
	}
}
