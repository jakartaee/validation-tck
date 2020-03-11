/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.containerelement.model;

import java.util.List;

import jakarta.validation.groups.ConvertGroup;
import jakarta.validation.groups.Default;

import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.model.Address;
import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.model.BasicPostal;

/**
 * @author Guillaume Smet
 */
public class UserWithContainerElementGroupConversionButWithoutValidAnnotationConstructorParameter {

	public UserWithContainerElementGroupConversionButWithoutValidAnnotationConstructorParameter(
			List<@ConvertGroup(from = Default.class, to = BasicPostal.class) Address> addresses) {
	}
}
