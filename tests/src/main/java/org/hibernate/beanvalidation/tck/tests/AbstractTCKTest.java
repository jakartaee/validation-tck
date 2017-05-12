/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests;

import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;

import org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert;
import org.hibernate.beanvalidation.tck.util.PathNodeKinds;
import org.hibernate.beanvalidation.tck.util.PathNodeNames;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.ArchiveBuilder;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;
import org.jboss.arquillian.testng.Arquillian;

/**
 * @author Marko Bekhta
 * @author Guillaume Smet
 */
public abstract class AbstractTCKTest extends Arquillian {

	private Validator validator;

	private ExecutableValidator executableValidator;

	protected static WebArchiveBuilder webArchiveBuilder() {
		return new WebArchiveBuilder().withClasses(
				ArchiveBuilder.class,
				WebArchiveBuilder.class,
				AbstractTCKTest.class,
				TestUtil.class,
				PathNodeKinds.class,
				PathNodeNames.class,
				ConstraintViolationAssert.class
		);
	}

	protected Validator getValidator() {
		if ( validator == null ) {
			validator = TestUtil.getValidatorUnderTest();
		}
		return validator;
	}

	protected ExecutableValidator getExecutableValidator() {
		if ( executableValidator == null ) {
			executableValidator = getValidator().forExecutables();
		}
		return executableValidator;
	}
}
