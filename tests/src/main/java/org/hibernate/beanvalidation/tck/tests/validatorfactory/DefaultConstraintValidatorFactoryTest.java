/*
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validatorfactory;

import jakarta.validation.ConstraintValidatorFactory;
import jakarta.validation.ValidationException;

import org.assertj.core.api.Assertions;
import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

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
		assertThat( MyConstraintValidator.defaultConstructorCalled ).as( "The no-arg default constructor should have been called." ).isTrue();
	}

	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_CONFIGURATION, id = "c")
	@Test
	public void testRuntimeExceptionInValidatorCreationIsWrapped() {
		Assertions.assertThatThrownBy( () -> {

			ConstraintValidatorFactory factory = TestUtil.getConfigurationUnderTest()
					.getDefaultConstraintValidatorFactory();
			factory.getInstance( MySecondConstraintValidator.class );
	
		} ).isInstanceOf( ValidationException.class );
	}
}
