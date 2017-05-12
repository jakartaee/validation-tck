/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.model;

import java.util.List;

import javax.validation.Valid;
import javax.validation.groups.ConvertGroup;

/**
 * @author Gunnar Morling
 */
public class UserWithGroupConversionWithSequenceAsFrom {

	@ConvertGroup(from = PostalSequence.class, to = BasicPostal.class)
	@Valid
	private final List<Address> addresses = null;
}
