/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.integration.cdi.defaultinjection;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Set;

import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotNull;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.IntegrationTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

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
		assertNotNull( defaultValidatorFactory, "Default validator factory should be injectable." );
		assertTrue(
				defaultValidatorFactory.getMessageInterpolator() instanceof ConstantMessageInterpolator,
				"Injected default validator factory should be configured based on META-INF/validation.xml."
		);

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
		assertNotNull(
				qualifiedDefaultValidatorFactory,
				"Qualified default validator factory should be injectable."
		);
		assertTrue(
				qualifiedDefaultValidatorFactory.getMessageInterpolator() instanceof ConstantMessageInterpolator,
				"Injected qualified default validator factory should be configured based on META-INF/validation.xml."
		);

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
		assertNotNull( defaultValidator, "Default validator should be injectable." );

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
		assertNotNull(
				qualifiedDefaultValidator,
				"Qualified default validator should be injectable."
		);

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
