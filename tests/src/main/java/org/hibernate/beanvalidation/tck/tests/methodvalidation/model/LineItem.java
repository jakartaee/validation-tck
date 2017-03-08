/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.methodvalidation.model;

import javax.validation.constraints.NotNull;
import javax.validation.executable.ExecutableType;
import javax.validation.executable.ValidateOnExecution;

import org.hibernate.beanvalidation.tck.tests.methodvalidation.constraint.ValidLineItem;

/**
 * @author Gunnar Morling
 */
@ValidateOnExecution(type = ExecutableType.NONE)
public class LineItem {

	@ValidLineItem
	public LineItem(@NotNull String name) {
	}

	@NotNull
	public String setName(@NotNull String name) {
		return null;
	}
}
