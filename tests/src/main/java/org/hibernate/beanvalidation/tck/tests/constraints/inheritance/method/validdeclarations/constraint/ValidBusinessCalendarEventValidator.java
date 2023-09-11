/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.validdeclarations.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.validdeclarations.model.CalendarEvent;

/**
 * @author Gunnar Morling
 */
public class ValidBusinessCalendarEventValidator
		implements ConstraintValidator<ValidBusinessCalendarEvent, CalendarEvent> {

	@Override
	public boolean isValid(CalendarEvent value, ConstraintValidatorContext context) {
		return false;
	}
}
