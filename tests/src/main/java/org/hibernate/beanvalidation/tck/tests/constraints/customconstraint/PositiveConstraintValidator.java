/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.customconstraint;

/**
 * @author Emmanuel Bernard
 */
public class PositiveConstraintValidator extends BoundariesConstraintValidator<Positive> {

	@Override
	public void initialize(Positive constraintAnnotation) {
		super.initialize( 0, Integer.MAX_VALUE );
	}
}
