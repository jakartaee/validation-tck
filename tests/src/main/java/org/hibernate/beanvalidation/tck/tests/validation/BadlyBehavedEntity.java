/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation;

import jakarta.validation.constraints.NotNull;

/**
 * @author Hardy Ferentschik
 */
public class BadlyBehavedEntity {

	@NotNull
	public Object getValue() {
		throw new RuntimeException( "BadlyBehavedEntity.getValue() always throws an exception." );
	}

}
