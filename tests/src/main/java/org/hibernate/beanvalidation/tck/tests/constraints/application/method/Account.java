/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.application.method;

import jakarta.validation.constraints.NotNull;

/**
 * @author Gunnar Morling
 */
public class Account {

	@NotNull
	private String login;
}
