/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.service;

import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.model.User;

/**
 * @author Gunnar Morling
 */
public interface UserWriteService {

	void addUser(User user);
}
