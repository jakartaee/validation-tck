/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests;

import java.net.URISyntaxException;

import com.beust.jcommander.JCommander;
import jakarta.validation.Validator;
import jakarta.validation.executable.ExecutableValidator;

import org.assertj.core.api.Assert;
import org.hibernate.beanvalidation.tck.util.CollectionHelper;
import org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.ValidationInvocationHandler;
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
		WebArchiveBuilder webArchiveBuilder = new WebArchiveBuilder();

		webArchiveBuilder.withClasses(
				ArchiveBuilder.class,
				WebArchiveBuilder.class,
				AbstractTCKTest.class,
				AbstractBootstrapFailureTCKTest.class,
				TestUtil.class,
				ConstraintViolationAssert.class,
				CollectionHelper.class,
				ValidationInvocationHandler.class
		);

		// We don't use the Maven features of Shrinkwrap as the TCK might not be run with Maven
		// or it could be run in an offline environment.
		// Thus we use the protection domain API to obtain the original jar and include it in the war.
		// According to our security policy, the TCK has the permission to access the API even if
		// the security manager is enabled.
		webArchiveBuilder.withAdditionalJar( jarPath( Assert.class ) );
		// testng 6.14.3 has dependency on jcommander
		webArchiveBuilder.withAdditionalJar( jarPath( JCommander.class ) );

		return webArchiveBuilder;
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

	private static String jarPath(Class<?> libraryClass) {
		try {
			return libraryClass.getProtectionDomain()
					.getCodeSource()
					.getLocation()
					.toURI()
					.getPath();
		}
		catch (URISyntaxException e) {
			throw new IllegalStateException( "Cannot resolve jar path for " + libraryClass.getPackageName(), e );
		}
	}
}
