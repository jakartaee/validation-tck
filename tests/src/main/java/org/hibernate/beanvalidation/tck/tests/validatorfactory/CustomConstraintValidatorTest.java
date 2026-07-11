/*
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validatorfactory;

import jakarta.validation.Configuration;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorFactory;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

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
 */
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
public class CustomConstraintValidatorTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
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
		assertThat( MyConstraintValidator.defaultConstructorCalled ).as( "The no-arg default constructor should have been called." ).isTrue();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTFACTORY, id = "c")
	public void testRuntimeExceptionInValidatorCreationIsWrapped() {
		Assertions.assertThatThrownBy( () -> {

			Validator validator = TestUtil.getValidatorUnderTest();
			validator.validate( new SecondDummy() );
	
		} ).isInstanceOf( ValidationException.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTFACTORY, id = "d")
	public void testValidationExceptionIsThrownInCaseFactoryReturnsNull() {
		Assertions.assertThatThrownBy( () -> {

			Configuration<?> config = TestUtil.getConfigurationUnderTest().constraintValidatorFactory(
					new CustomConstraintValidatorFactory()
			);
			Validator validator = config.buildValidatorFactory().getValidator();
			validator.validate( new SecondDummy() );
	
		} ).isInstanceOf( ValidationException.class );
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_VALIDATORFACTORY, id = "d")
	public void testGetConstraintValidatorFactoryFromValidatorFactory() {
		CustomConstraintValidatorFactory constraintValidatorFactory = new CustomConstraintValidatorFactory();

		ValidatorFactory validatorFactory = TestUtil.getConfigurationUnderTest()
				.constraintValidatorFactory( constraintValidatorFactory )
				.buildValidatorFactory();

		assertThat( validatorFactory.getConstraintValidatorFactory() ).as( "getConstraintValidatorFactory() should return the parameter name provider set via configuration" ).isSameAs( constraintValidatorFactory );
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
