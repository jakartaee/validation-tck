/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

/**
 * @author Gunnar Morling
 */
public class Shipment {

	//valid getter
	@NotNull
	public String getId() {
		return null;
	}

	//valid boolean getter
	@AssertTrue
	public boolean isShipped() {
		return false;
	}
}
