/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.validdeclarations.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.validdeclarations.service.impl.CalendarServiceSubClass;

/**
 * @author Gunnar Morling
 */
public class ValidCalendarServiceSubClassValidator
		implements ConstraintValidator<ValidCalendarServiceSubClass, CalendarServiceSubClass> {

	@Override
	public boolean isValid(CalendarServiceSubClass value, ConstraintValidatorContext context) {
		return false;
	}
}
