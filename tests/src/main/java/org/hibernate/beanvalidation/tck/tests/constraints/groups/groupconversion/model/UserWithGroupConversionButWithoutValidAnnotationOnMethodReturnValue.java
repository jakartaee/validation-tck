/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.model;

import java.util.List;

import jakarta.validation.groups.ConvertGroup;
import jakarta.validation.groups.Default;

/**
 * @author Gunnar Morling
 */
public class UserWithGroupConversionButWithoutValidAnnotationOnMethodReturnValue {

	@ConvertGroup(from = Default.class, to = BasicPostal.class)
	public List<Address> retrieveAddresses() {
		return null;
	}
}
