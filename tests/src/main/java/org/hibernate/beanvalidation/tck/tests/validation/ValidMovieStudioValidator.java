/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Gunnar Morling
 */
public class ValidMovieStudioValidator
		implements ConstraintValidator<ValidMovieStudio, MovieStudio> {

	@Override
	public boolean isValid(MovieStudio studio, ConstraintValidatorContext constraintValidatorContext) {
		return studio.getName() != null;
	}
}
