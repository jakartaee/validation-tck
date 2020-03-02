/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Configuration;
import jakarta.validation.Validation;
import jakarta.validation.ValidationProviderResolver;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.bootstrap.GenericBootstrap;
import jakarta.validation.bootstrap.ProviderSpecificBootstrap;
import jakarta.validation.spi.ValidationProvider;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.common.TCKValidationProvider;
import org.hibernate.beanvalidation.tck.common.TCKValidatorConfiguration;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests for the implementation of <code>Validation</code>.
 *
 * @author Hardy Ferentschik
 */

@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class ValidationTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( ValidationTest.class )
				.withClasses( TCKValidationProvider.class, TCKValidatorConfiguration.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_VALIDATION, id = "a")
	public void testBuildDefaultValidatorFactory() {
		ValidatorFactory defaultFactory = Validation.buildDefaultValidatorFactory();
		assertNotNull( defaultFactory, "We should be able to get a factory." );

		ValidatorFactory defaultProviderFactory = Validation.byDefaultProvider().configure().buildValidatorFactory();
		assertNotNull( defaultProviderFactory, "We should be able to get a factory." );

		assertEquals( defaultFactory.getClass(), defaultFactory.getClass(), "The factories have to be identical." );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_VALIDATION, id = "b"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_VALIDATION, id = "e")
	})
	public void testCustomValidationProviderResolution() {
		ValidationProviderResolver resolver = new ValidationProviderResolver() {

			public List<ValidationProvider<?>> getValidationProviders() {
				List<ValidationProvider<?>> list = new ArrayList<ValidationProvider<?>>();
				list.add( new TCKValidationProvider() );
				return list;
			}
		};

		GenericBootstrap bootstrap = Validation.byDefaultProvider();
		Configuration<?> config = bootstrap.providerResolver( resolver ).configure();

		ValidatorFactory factory = config.buildValidatorFactory();
		assertTrue( factory instanceof TCKValidationProvider.DummyValidatorFactory );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_VALIDATION, id = "c"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_VALIDATION, id = "e")
	})
	public void testSpecificValidationProvider() {
		ValidationProviderResolver resolver = new ValidationProviderResolver() {

			public List<ValidationProvider<?>> getValidationProviders() {
				List<ValidationProvider<?>> list = new ArrayList<ValidationProvider<?>>();
				list.add( new TCKValidationProvider() );
				return list;
			}
		};

		// with resolver
		ProviderSpecificBootstrap<TCKValidatorConfiguration> bootstrap = Validation.byProvider( TCKValidationProvider.class );
		Configuration<?> config = bootstrap.providerResolver( resolver ).configure();
		ValidatorFactory factory = config.buildValidatorFactory();
		assertTrue( factory instanceof TCKValidationProvider.DummyValidatorFactory );
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_VALIDATION, id = "d")
	public void testVerifyMethodsOfValidationObjects() {
		Class<?> validatorClass = jakarta.validation.Validation.class;

		List<Method> expectedValidationMethods = new ArrayList<Method>();
		Method buildDefaultValidatorFactoryMethod = null;
		try {
			buildDefaultValidatorFactoryMethod = validatorClass.getMethod( "buildDefaultValidatorFactory" );
		}
		catch ( NoSuchMethodException e ) {
			fail( "Validation class is missing bootstrap method." );
		}
		expectedValidationMethods.add( buildDefaultValidatorFactoryMethod );

		Method byDefaultProviderMethod = null;
		try {
			byDefaultProviderMethod = validatorClass.getMethod( "byDefaultProvider" );
		}
		catch ( NoSuchMethodException e ) {
			fail( "Validation class is missing bootstrap method." );
		}
		expectedValidationMethods.add( byDefaultProviderMethod );

		Method byProviderMethod = null;
		try {
			byProviderMethod = validatorClass.getMethod( "byProvider", Class.class );
		}
		catch ( NoSuchMethodException e ) {
			fail( "Validation class is missing bootstrap method." );
		}
		expectedValidationMethods.add( byProviderMethod );

		Method[] validationMethods = validatorClass.getMethods();
		for ( Method m : validationMethods ) {
			if ( expectedValidationMethods.contains( m ) || m.getDeclaringClass() != validatorClass ) {
				continue;
			}
			if ( Modifier.isPublic( m.getModifiers() ) || Modifier.isProtected( m.getModifiers() ) ) {
				fail( "Validation cannot have a non private method on top of the specified ones. " + m.getName() + " not allowed." );
			}
		}

		Field[] validationFields = validatorClass.getFields();
		for ( Field f : validationFields ) {
			if ( f.getDeclaringClass() != validatorClass ) {
				continue;
			}
			if ( Modifier.isPublic( f.getModifiers() ) || Modifier.isProtected( f.getModifiers() ) ) {
				fail( "Validation cannot have a non private field. " + f.getName() + " not allowed." );
			}
		}
	}
}
