/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.service;

import jakarta.validation.Valid;
import jakarta.validation.groups.ConvertGroup;
import jakarta.validation.groups.Default;

import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.model.BasicPostal;
import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.model.User;

/**
 * @author Gunnar Morling
 */
public interface UserReadServiceWithGroupConversionOnReturnValue {

	@Valid
	@ConvertGroup(from = Default.class, to = BasicPostal.class)
	public User getUser();
}
