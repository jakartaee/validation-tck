/*
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.integration.cdi.defaultinjection;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;

import java.util.Set;

import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotNull;

import org.assertj.core.api.Assertions;
import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.IntegrationTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.junit.jupiter.api.Test;

/**
 * @author Gunnar Morling
 */
@IntegrationTest
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
public class DefaultInjectionTest extends AbstractTCKTest {

	@Inject
	private ValidatorFactory defaultValidatorFactory;

	@Inject
	@Default
	private ValidatorFactory qualifiedDefaultValidatorFactory;

	@Inject
	private Validator defaultValidator;

	@Inject
	@Default
	private Validator qualifiedDefaultValidator;

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( DefaultInjectionTest.class )
				.withValidationXml( "validation-DefaultInjectionTest.xml" )
				.withBeansXml()
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_OBJECTSLIFECYCLE, id = "a")
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_OBJECTSLIFECYCLE, id = "b")
	@SpecAssertion(section = Sections.INTEGRATION_DEPENDENCYINJECTION_VALIDATORFACTORY, id = "a")
	@SpecAssertion(section = Sections.INTEGRATION_DEPENDENCYINJECTION, id = "a")
	public void testDefaultValidatorFactoryGetsInjected() {
		Assertions.assertThat(  defaultValidatorFactory ).as( "Default validator factory should be injectable." ).isNotNull();
		Assertions.assertThat(  defaultValidatorFactory.getMessageInterpolator() instanceof ConstantMessageInterpolator ).as( "Injected default validator factory should be configured based on META-INF/validation.xml." ).isTrue();

		Set<ConstraintViolation<Foo>> violations = defaultValidatorFactory.getValidator()
				.validate( new Foo() );

		//expecting message from interpolator configured in META-INF/validation.xml
		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class ).withMessage( "Invalid constraint" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_OBJECTSLIFECYCLE, id = "a")
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_OBJECTSLIFECYCLE, id = "b")
	@SpecAssertion(section = Sections.INTEGRATION_DEPENDENCYINJECTION_VALIDATORFACTORY, id = "a")
	public void testQualifiedDefaultValidatorFactoryGetsInjected() {
		Assertions.assertThat(  qualifiedDefaultValidatorFactory ).as( "Qualified default validator factory should be injectable." ).isNotNull();
		Assertions.assertThat(  qualifiedDefaultValidatorFactory.getMessageInterpolator() instanceof ConstantMessageInterpolator ).as( "Injected qualified default validator factory should be configured based on META-INF/validation.xml." ).isTrue();

		Set<ConstraintViolation<Foo>> violations = qualifiedDefaultValidatorFactory.getValidator()
				.validate( new Foo() );

		//expecting message from interpolator configured in META-INF/validation.xml
		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class ).withMessage( "Invalid constraint" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_OBJECTSLIFECYCLE, id = "a")
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_OBJECTSLIFECYCLE, id = "b")
	@SpecAssertion(section = Sections.INTEGRATION_DEPENDENCYINJECTION_VALIDATORFACTORY, id = "a")
	@SpecAssertion(section = Sections.INTEGRATION_DEPENDENCYINJECTION, id = "a")
	public void testDefaultValidatorGetsInjected() {
		Assertions.assertThat(  defaultValidator ).as( "Default validator should be injectable." ).isNotNull();

		Set<ConstraintViolation<Foo>> violations = defaultValidator.validate( new Foo() );

		//expecting message from interpolator configured in META-INF/validation.xml
		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class ).withMessage( "Invalid constraint" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_OBJECTSLIFECYCLE, id = "a")
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_OBJECTSLIFECYCLE, id = "b")
	@SpecAssertion(section = Sections.INTEGRATION_DEPENDENCYINJECTION_VALIDATORFACTORY, id = "a")
	public void testQualifiedDefaultValidatorGetsInjected() {
		Assertions.assertThat(  qualifiedDefaultValidator ).as( "Qualified default validator should be injectable." ).isNotNull();

		Set<ConstraintViolation<Foo>> violations = qualifiedDefaultValidator.validate( new Foo() );

		//expecting message from interpolator configured in META-INF/validation.xml
		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class ).withMessage( "Invalid constraint" )
		);
	}

	private static class Foo {
		@NotNull
		public String bar;
	}
}
