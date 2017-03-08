/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration;

import javax.validation.constraints.NotNull;

/**
 * @author Gunnar Morling
 */
public class CreditCardValidationService {

	public boolean validateCreditCard(@NotNull CreditCard creditCard) {
		return false;
	}
}
