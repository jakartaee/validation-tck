/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests;

import javax.validation.Validator;

import org.hibernate.beanvalidation.tck.util.TestUtil;

import org.jboss.arquillian.testng.Arquillian;

/**
 * @author Marko Bekhta
 */
public abstract class BaseValidatorTest extends Arquillian {

	private Validator validator;

	protected Validator getValidator() {
		if ( validator == null ) {
			validator = TestUtil.getValidatorUnderTest();
		}
		return validator;
	}

}
