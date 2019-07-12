/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.validdeclarations.service.impl;

import java.util.Date;

import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.validdeclarations.model.CalendarEvent;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.validdeclarations.service.AnotherInterfaceMarkingReturnValueAsCascaded;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.validdeclarations.service.InterfaceMarkingReturnValueAsCascaded;

/**
 * @author Gunnar Morling
 */
public class ImplementationOfParallelInterfacesMarkingReturnValueAsCascaded
		implements InterfaceMarkingReturnValueAsCascaded, AnotherInterfaceMarkingReturnValueAsCascaded {

	@Override
	public CalendarEvent createEvent(Date start, Date end) {
		return new CalendarEvent();
	}
}
