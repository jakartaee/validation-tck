/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.constraintdefinition.serviceloading;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OrderAnnotationConstraintServiceLoadedConstraintValidator implements ConstraintValidator<OrderAnnotationConstraint, OrderAnnotationBean> {

	@Override
	public boolean isValid(OrderAnnotationBean value, ConstraintValidatorContext context) {
		if ( value.foo == null ) {
			return true;
		}
		return "foo".equals( value.foo );
	}
}
