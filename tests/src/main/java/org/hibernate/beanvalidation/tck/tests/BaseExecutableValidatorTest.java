/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests;

import javax.validation.executable.ExecutableValidator;

/**
 * @author Marko Bekhta
 */
public abstract class BaseExecutableValidatorTest extends BaseValidatorTest {

	private ExecutableValidator executableValidator;

	protected ExecutableValidator getExecutableValidator() {
		if ( executableValidator == null ) {
			executableValidator = getValidator().forExecutables();
		}
		return executableValidator;
	}

}
