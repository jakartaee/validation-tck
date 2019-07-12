/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.validdeclarations.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.validdeclarations.service.AbstractCalendarService;

/**
 * @author Gunnar Morling
 */
public class ValidAbstractCalendarServiceValidator
		implements ConstraintValidator<ValidAbstractCalendarService, AbstractCalendarService> {

	@Override
	public boolean isValid(AbstractCalendarService value, ConstraintValidatorContext context) {
		return false;
	}
}
