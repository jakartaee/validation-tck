/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.metadata;

import javax.validation.ConstraintTarget;
import javax.validation.constraints.Size;

/**
 * @author Hardy Ferentschik
 */
public interface Person {
	@NotEmpty(groups = PersonValidation.class, payload = Severity.Info.class)
	String getFirstName();

	@Size(min = 3, message = "must at least be {min} characters long")
	String getMiddleName();

	@NotEmpty
	String getLastName();

	@CustomConstraint(validationAppliesTo = ConstraintTarget.RETURN_VALUE)
	int getAge();

	public interface PersonValidation {
	}
}
