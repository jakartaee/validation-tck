/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validatorfactory;

import static org.testng.Assert.assertTrue;

import jakarta.validation.ConstraintValidatorFactory;
import jakarta.validation.ValidationException;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Hardy Ferentschik
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
public class DefaultConstraintValidatorFactoryTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( DefaultConstraintValidatorFactoryTest.class )
				.withClass( MyConstraint.class )
				.withClass( MyConstraintValidator.class )
				.withClass( MySecondConstraint.class )
				.withClass( MySecondConstraintValidator.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_CONFIGURATION, id = "c")
	public void testDefaultConstructorInValidatorCalled() {
		ConstraintValidatorFactory factory = TestUtil.getConfigurationUnderTest()
				.getDefaultConstraintValidatorFactory();
		factory.getInstance( MyConstraintValidator.class );
		assertTrue(
				MyConstraintValidator.defaultConstructorCalled,
				"The no-arg default constructor should have been called."
		);
	}

	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_CONFIGURATION, id = "c")
	@Test(expectedExceptions = ValidationException.class)
	public void testRuntimeExceptionInValidatorCreationIsWrapped() {
		ConstraintValidatorFactory factory = TestUtil.getConfigurationUnderTest()
				.getDefaultConstraintValidatorFactory();
		factory.getInstance( MySecondConstraintValidator.class );
	}
}
