/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation.graphnavigation.containerelement.model;

import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;
import javax.validation.groups.ConvertGroup;
import javax.validation.groups.Default;

public class ValidOnListAndOnTypeArgumentWithGroupConversions {

	@Valid
	@ConvertGroup(from = Default.class, to = ExtendedChecks1.class)
	private final List<@Valid @ConvertGroup(from = ExtendedChecks1.class, to = ExtendedChecks2.class) VisitorWithGroups> visitors;

	private ValidOnListAndOnTypeArgumentWithGroupConversions(List<VisitorWithGroups> visitors) {
		this.visitors = visitors;
	}

	public static ValidOnListAndOnTypeArgumentWithGroupConversions invalid() {
		return new ValidOnListAndOnTypeArgumentWithGroupConversions( Arrays.asList( new VisitorWithGroups( null ) ) );
	}
}
