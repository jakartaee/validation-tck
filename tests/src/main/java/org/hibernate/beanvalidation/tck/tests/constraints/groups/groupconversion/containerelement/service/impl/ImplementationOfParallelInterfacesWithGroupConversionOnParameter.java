/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.containerelement.service.impl;

import java.util.List;

import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.containerelement.service.UserWriteService;
import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.containerelement.service.UserWriteServiceWithGroupConversionOnParameter;
import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.model.User;

/**
 * @author Guillaume Smet
 */
public class ImplementationOfParallelInterfacesWithGroupConversionOnParameter
		implements UserWriteService, UserWriteServiceWithGroupConversionOnParameter {

	@Override
	public void addUsers(List<User> user) {
	}
}
