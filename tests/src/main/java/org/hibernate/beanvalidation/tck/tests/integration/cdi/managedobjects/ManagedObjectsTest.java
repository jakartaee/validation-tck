/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.integration.cdi.managedobjects;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.lang.reflect.Constructor;
import java.util.Arrays;

import javax.inject.Inject;
import javax.validation.ClockProvider;
import javax.validation.ConstraintValidatorFactory;
import javax.validation.MessageInterpolator;
import javax.validation.ParameterNameProvider;
import javax.validation.TraversableResolver;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.IntegrationTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests for dependency injection into message interpolators, traversable
 * resolvers etc. All test objects rely on a {@link Greeter} object to be
 * injected which is then used to perform message interpolation etc.
 *
 * @author Gunnar Morling
 */
@IntegrationTest
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
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
				.withEmptyBeansXml()
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_OBJECTSLIFECYCLE, id = "d")
	@SpecAssertion(section = Sections.INTEGRATION_CDI_CUSTOMCONFIGURATION, id = "a")
	@SpecAssertion(section = Sections.INTEGRATION_CDI, id = "a")
	public void testMessageInterpolatorIsSubjectToDependencyInjection() {
		assertNotNull( defaultValidatorFactory );
		MessageInterpolator messageInterpolator = defaultValidatorFactory.getMessageInterpolator();

		assertEquals( messageInterpolator.interpolate( null, null ), Greeter.MESSAGE );
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_OBJECTSLIFECYCLE, id = "d")
	@SpecAssertion(section = Sections.INTEGRATION_CDI_CUSTOMCONFIGURATION, id = "a")
	@SpecAssertion(section = Sections.INTEGRATION_CDI, id = "a")
	public void testTraversableResolverIsSubjectToDependencyInjection() {
		assertNotNull( defaultValidatorFactory );

		TraversableResolver traversableResolver = defaultValidatorFactory.getTraversableResolver();
		MessageHolder message = new MessageHolder();
		traversableResolver.isCascadable( message, null, null, null, null );

		assertEquals( message.getValue(), Greeter.MESSAGE );
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_OBJECTSLIFECYCLE, id = "d")
	@SpecAssertion(section = Sections.INTEGRATION_CDI_CUSTOMCONFIGURATION, id = "a")
	@SpecAssertion(section = Sections.INTEGRATION_CDI, id = "a")
	public void testConstraintValidatorFactoryIsSubjectToDependencyInjection() {
		assertNotNull( defaultValidatorFactory );

		ConstraintValidatorFactory constraintValidatorFactory = defaultValidatorFactory.getConstraintValidatorFactory();
		GreetingConstraintValidator validator = constraintValidatorFactory.getInstance(
				GreetingConstraintValidator.class
		);

		assertEquals( validator.getMessage(), Greeter.MESSAGE );
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_OBJECTSLIFECYCLE, id = "d")
	@SpecAssertion(section = Sections.INTEGRATION_CDI_CUSTOMCONFIGURATION, id = "a")
	@SpecAssertion(section = Sections.INTEGRATION_CDI, id = "a")
	public void testParameterNameProviderIsSubjectToDependencyInjection() {
		assertNotNull( defaultValidatorFactory );
		ParameterNameProvider parameterNameProvider = defaultValidatorFactory.getParameterNameProvider();

		assertEquals(
				parameterNameProvider.getParameterNames( (Constructor<?>) null ),
				Arrays.asList( Greeter.MESSAGE )
		);
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_OBJECTSLIFECYCLE, id = "d")
	@SpecAssertion(section = Sections.INTEGRATION_CDI_CUSTOMCONFIGURATION, id = "a")
	@SpecAssertion(section = Sections.INTEGRATION_CDI, id = "a")
	public void testClockProviderIsSubjectToDependencyInjection() {
		assertNotNull( defaultValidatorFactory );
		ClockProvider clockProvider = defaultValidatorFactory.getClockProvider();

		assertEquals(
				clockProvider.getClock().getZone(),
				Greeter.ZONE_ID
		);
	}
}
