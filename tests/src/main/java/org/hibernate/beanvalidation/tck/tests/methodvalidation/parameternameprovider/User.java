/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.methodvalidation.parameternameprovider;

import java.util.Date;

import jakarta.validation.constraints.NotNull;

/**
 * @author Gunnar Morling
 */
public class User {

	public User() {
	}

	public User(@NotNull String firstName, @NotNull String lastName, @NotNull Date dateOfBirth) {
	}

	public void setNames(@NotNull String firstName, @NotNull String lastName) {
	}
}
