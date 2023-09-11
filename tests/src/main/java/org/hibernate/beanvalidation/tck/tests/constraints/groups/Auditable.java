/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.groups;

import jakarta.validation.constraints.NotNull;

/**
 * @author Hardy Ferentschik
 */
public interface Auditable {
	@NotNull
	String getCreationDate();

	@NotNull
	String getLastUpdate();

	@NotNull
	String getLastModifier();

	@NotNull
	String getLastReader();
}
