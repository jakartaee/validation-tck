/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.service.impl;

import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.model.User;
import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.service.UserReadServiceBase;
import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.service.UserReadServiceWithGroupConversionOnReturnValue;

/**
 * @author Gunnar Morling
 */
public class ImplementationOfInterfaceWithGroupConversionOnReturnValueAndSuperClass
		extends UserReadServiceBase implements UserReadServiceWithGroupConversionOnReturnValue {

	@Override
	public User getUser() {
		return null;
	}
}
