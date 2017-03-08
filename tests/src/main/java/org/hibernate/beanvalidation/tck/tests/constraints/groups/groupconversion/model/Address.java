/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Gunnar Morling
 */
public class Address {
	@NotNull(groups = BasicPostal.class)
	String street1;

	@NotNull
	String street2;

	@Size(groups = BasicPostal.class, min = 3)
	String zipCode = "12";

	@Size(groups = FullPostal.class, max = 2)
	String doorCode = "ABC";
}
