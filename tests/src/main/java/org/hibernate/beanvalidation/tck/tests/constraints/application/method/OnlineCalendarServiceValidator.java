/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.application.method;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Gunnar Morling
 */
public class OnlineCalendarServiceValidator
		implements ConstraintValidator<OnlineCalendarService, CalendarService> {

	@Override
	public boolean isValid(CalendarService service, ConstraintValidatorContext context) {
		return false;
	}
}
