/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validatorfactory;

import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import javax.validation.Configuration;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorFactory;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class CustomConstraintValidatorTest extends Arquillian {

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClass( CustomConstraintValidatorTest.class )
				.withClass( MyConstraint.class )
				.withClass( MyConstraintValidator.class )
				.withClass( MySecondConstraint.class )
				.withClass( MySecondConstraintValidator.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTFACTORY, id = "b")
	public void testDefaultConstructorInValidatorCalled() {
		Validator validator = TestUtil.getValidatorUnderTest();
		validator.validate( new Dummy() );
		assertTrue(
				MyConstraintValidator.defaultConstructorCalled,
				"The no-arg default constructor should have been called."
		);
	}

	@Test(expectedExceptions = ValidationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTFACTORY, id = "c")
	public void testRuntimeExceptionInValidatorCreationIsWrapped() {
		Validator validator = TestUtil.getValidatorUnderTest();
		validator.validate( new SecondDummy() );
	}

	@Test(expectedExceptions = ValidationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTFACTORY, id = "d")
	public void testValidationExceptionIsThrownInCaseFactoryReturnsNull() {
		Configuration<?> config = TestUtil.getConfigurationUnderTest().constraintValidatorFactory(
				new CustomConstraintValidatorFactory()
		);
		Validator validator = config.buildValidatorFactory().getValidator();
		validator.validate( new SecondDummy() );
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_VALIDATORFACTORY, id = "d")
	public void testGetConstraintValidatorFactoryFromValidatorFactory() {
		CustomConstraintValidatorFactory constraintValidatorFactory = new CustomConstraintValidatorFactory();

		ValidatorFactory validatorFactory = TestUtil.getConfigurationUnderTest()
				.constraintValidatorFactory( constraintValidatorFactory )
				.buildValidatorFactory();

		assertSame(
				validatorFactory.getConstraintValidatorFactory(),
				constraintValidatorFactory,
				"getConstraintValidatorFactory() should return the parameter name provider set via configuration"
		);
	}

	private class CustomConstraintValidatorFactory implements ConstraintValidatorFactory {

		@Override
		public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {
			return null;
		}

		@Override
		public void releaseInstance(ConstraintValidator<?, ?> instance) {
		}
	}

	private static class Dummy {
		@MyConstraint
		public int value;
	}

	private static class SecondDummy {
		@MySecondConstraint
		public int value;
	}
}
