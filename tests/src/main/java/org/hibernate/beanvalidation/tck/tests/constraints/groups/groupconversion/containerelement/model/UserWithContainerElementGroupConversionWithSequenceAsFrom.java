/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.containerelement.model;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.groups.ConvertGroup;

import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.model.Address;
import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.model.BasicPostal;
import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.model.PostalSequence;

/**
 * @author Guillaume Smet
 */
public class UserWithContainerElementGroupConversionWithSequenceAsFrom {

	@SuppressWarnings("unused")
	private final List<@Valid @ConvertGroup(from = PostalSequence.class, to = BasicPostal.class) Address> addresses = null;
}
