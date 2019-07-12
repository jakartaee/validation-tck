/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.inheritance;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Hardy Ferentschik
 */
@ValidFubar
public interface Fubar {
	@NotNull
	String getFubar();

	@Size(min = 5)
	String getName();
}
