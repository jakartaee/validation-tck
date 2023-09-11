/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation;

/**
 * @author Guillaume Smet
 */
@ValidExecutiveProducer
public class ExecutiveProducer extends Employee {

	public ExecutiveProducer(String firstName, String lastName) {
		super( firstName, lastName );
	}
}
