/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.constraintdefinition.serviceloading;

@OrderAnnotationConstraint
public class OrderAnnotationBean {

	public String foo;

	public OrderAnnotationBean(String foo) {
		this.foo = foo;
	}
}
