/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.containerelement.model;

import java.util.List;

import javax.validation.Valid;
import javax.validation.groups.ConvertGroup;
import javax.validation.groups.Default;

import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.model.Address;
import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.model.BasicPostal;
import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.model.FullPostal;

/**
 * @author Guillaume Smet
 */
public class UserWithSeveralContainerElementGroupConversionsForSameFrom {

	@SuppressWarnings("unused")
	private final List<@Valid
			@ConvertGroup(from = Default.class, to = BasicPostal.class)
			@ConvertGroup(from = Default.class, to = FullPostal.class)
			Address> addresses = null;
}
