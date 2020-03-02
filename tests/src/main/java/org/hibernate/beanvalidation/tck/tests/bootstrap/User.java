/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.bootstrap;

import jakarta.validation.constraints.NotNull;

/**
 * @author Gunnar Morling
 */
@SuppressWarnings("unused")
public class User {

	@NotNull
	private String firstName;

	private String lastName;

	private final Address address = new Address();
}
