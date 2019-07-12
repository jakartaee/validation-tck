/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.constraintcomposition;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.hibernate.beanvalidation.tck.tests.constraints.constraintcomposition.FrenchAddressMixDirectAnnotationAndListContainer.FrenchZipcodeMixDirectAnnotationAndListContainer;

/**
 * @author Guillaume Smet
 */
public class FrenchZipcodeMixDirectAnnotationAndListContainerConstraintValidator
		implements ConstraintValidator<FrenchZipcodeMixDirectAnnotationAndListContainer, String> {

	@Override
	public boolean isValid(String zip, ConstraintValidatorContext constraintValidatorContext) {
		if ( zip == null ) {
			return true;
		}
		if ( "00000".equals( zip ) ) {
			constraintValidatorContext.disableDefaultConstraintViolation();
			constraintValidatorContext.buildConstraintViolationWithTemplate( "00000 is a reserved code"  ).addConstraintViolation();
			return false;
		}
		else {
			return true;
		}
	}
}
