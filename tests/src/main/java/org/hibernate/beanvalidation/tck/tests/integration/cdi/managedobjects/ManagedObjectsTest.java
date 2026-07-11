/*
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.integration.cdi.managedobjects;

import java.lang.reflect.Constructor;
import java.util.Arrays;

import jakarta.inject.Inject;
import jakarta.validation.ClockProvider;
import jakarta.validation.ConstraintValidatorFactory;
import jakarta.validation.MessageInterpolator;
import jakarta.validation.ParameterNameProvider;
import jakarta.validation.TraversableResolver;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.IntegrationTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for dependency injection into message interpolators, traversable
 * resolvers etc. All test objects rely on a {@link Greeter} object to be
 * injected which is then used to perform message interpolation etc.
 *
 * @author Gunnar Morling
 */
@IntegrationTest
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
public class ManagedObjectsTest extends AbstractTCKTest {

	@Inject
	private ValidatorFactory defaultValidatorFactory;

	@Inject
	private Validator defaultValidator;

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( ManagedObjectsTest.class )
				.withValidationXml( "validation-ManagedObjectsTest.xml" )
				.withBeansXml()
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_OBJECTSLIFECYCLE, id = "d")
	@SpecAssertion(section = Sections.INTEGRATION_DEPENDENCYINJECTION_CUSTOMCONFIGURATION, id = "a")
	@SpecAssertion(section = Sections.INTEGRATION_DEPENDENCYINJECTION, id = "a")
	public void testMessageInterpolatorIsSubjectToDependencyInjection() {
		assertThat( defaultValidatorFactory  ).isNotNull();
		MessageInterpolator messageInterpolator = defaultValidatorFactory.getMessageInterpolator();

		assertThat( messageInterpolator.interpolate( null, null ) ).isEqualTo( Greeter.MESSAGE );
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_OBJECTSLIFECYCLE, id = "d")
	@SpecAssertion(section = Sections.INTEGRATION_DEPENDENCYINJECTION_CUSTOMCONFIGURATION, id = "a")
	@SpecAssertion(section = Sections.INTEGRATION_DEPENDENCYINJECTION, id = "a")
	public void testTraversableResolverIsSubjectToDependencyInjection() {
		assertThat( defaultValidatorFactory  ).isNotNull();

		TraversableResolver traversableResolver = defaultValidatorFactory.getTraversableResolver();
		MessageHolder message = new MessageHolder();
		traversableResolver.isCascadable( message, null, null, null, null );

		assertThat( message.getValue() ).isEqualTo( Greeter.MESSAGE  );
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_OBJECTSLIFECYCLE, id = "d")
	@SpecAssertion(section = Sections.INTEGRATION_DEPENDENCYINJECTION_CUSTOMCONFIGURATION, id = "a")
	@SpecAssertion(section = Sections.INTEGRATION_DEPENDENCYINJECTION, id = "a")
	public void testConstraintValidatorFactoryIsSubjectToDependencyInjection() {
		assertThat( defaultValidatorFactory  ).isNotNull();

		ConstraintValidatorFactory constraintValidatorFactory = defaultValidatorFactory.getConstraintValidatorFactory();
		GreetingConstraintValidator validator = constraintValidatorFactory.getInstance(
				GreetingConstraintValidator.class
		);

		assertThat( validator.getMessage() ).isEqualTo( Greeter.MESSAGE  );
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_OBJECTSLIFECYCLE, id = "d")
	@SpecAssertion(section = Sections.INTEGRATION_DEPENDENCYINJECTION_CUSTOMCONFIGURATION, id = "a")
	@SpecAssertion(section = Sections.INTEGRATION_DEPENDENCYINJECTION, id = "a")
	public void testParameterNameProviderIsSubjectToDependencyInjection() {
		assertThat( defaultValidatorFactory  ).isNotNull();
		ParameterNameProvider parameterNameProvider = defaultValidatorFactory.getParameterNameProvider();

		assertThat( parameterNameProvider.getParameterNames( (Constructor<?>) null ) ).isEqualTo( Arrays.asList( Greeter.MESSAGE  ) );
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_OBJECTSLIFECYCLE, id = "d")
	@SpecAssertion(section = Sections.INTEGRATION_DEPENDENCYINJECTION_CUSTOMCONFIGURATION, id = "a")
	@SpecAssertion(section = Sections.INTEGRATION_DEPENDENCYINJECTION, id = "a")
	public void testClockProviderIsSubjectToDependencyInjection() {
		assertThat( defaultValidatorFactory ).isNotNull();
		ClockProvider clockProvider = defaultValidatorFactory.getClockProvider();

		assertThat( clockProvider.getClock().getZone() ).isEqualTo( Greeter.ZONE_ID );
	}
}
