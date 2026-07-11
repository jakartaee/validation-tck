/*
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.bootstrap;


import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Configuration;
import jakarta.validation.Validation;
import jakarta.validation.ValidationException;
import jakarta.validation.ValidationProviderResolver;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.spi.ValidationProvider;

import org.assertj.core.api.Assertions;
import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.common.TCKValidationProvider;
import org.hibernate.beanvalidation.tck.common.TCKValidatorConfiguration;
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
public class ValidationProviderTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( ValidationProviderTest.class )
				.withClasses( TCKValidationProvider.class, TCKValidatorConfiguration.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_VALIDATIONPROVIDER_PROVIDER, id = "b")
	public void testFirstMatchingValidationProviderResolverIsReturned() {
		ValidationProviderResolver resolver = new ValidationProviderResolver() {

			@Override
			public List<ValidationProvider<?>> getValidationProviders() {
				List<ValidationProvider<?>> list = new ArrayList<ValidationProvider<?>>();
				list.add( TestUtil.getValidationProviderUnderTest() );
				list.add( new TCKValidationProvider() );
				return list;
			}
		};

		Configuration<?> configuration = Validation
				.byProvider( TCKValidationProvider.class )
				.providerResolver( resolver )
				.configure();

		ValidatorFactory factory = configuration.buildValidatorFactory();
		assertThat( factory instanceof TCKValidationProvider.DummyValidatorFactory ).isTrue();
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_VALIDATIONPROVIDER_PROVIDER, id = "c")
	public void testByDefaultProviderUsesTheFirstProviderReturnedByValidationProviderResolver() {
		ValidationProviderResolver resolver = new ValidationProviderResolver() {

			@Override
			public List<ValidationProvider<?>> getValidationProviders() {
				List<ValidationProvider<?>> list = new ArrayList<ValidationProvider<?>>();
				list.add( new TCKValidationProvider() );
				list.add( TestUtil.getValidationProviderUnderTest() );
				return list;
			}
		};

		Configuration<?> configuration = Validation
				.byDefaultProvider()
				.providerResolver( resolver )
				.configure();
		ValidatorFactory factory = configuration.buildValidatorFactory();
		assertThat( factory instanceof TCKValidationProvider.DummyValidatorFactory ).isTrue();
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_VALIDATIONPROVIDER_PROVIDER, id = "d")
	public void testValidationProviderContainsNoArgConstructor() {
		ValidationProvider<?> validationProviderUnderTest = TestUtil.getValidationProviderUnderTest();
		try {
			Constructor<?> constructor = validationProviderUnderTest.getClass().getConstructor();
			assertThat( Modifier.isPublic( constructor.getModifiers() ) ).isTrue();
		}
		catch ( Exception e ) {
			Assertions.fail( "The validation provider must have a public no arg constructor" );
		}
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_VALIDATION, id = "f")
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_VALIDATIONPROVIDER_PROVIDER, id = "e")
	public void testValidationExceptionIsThrownInCaseValidatorFactoryCreationFails() {
		Assertions.assertThatThrownBy( () -> {

			ValidationProviderResolver resolver = new ValidationProviderResolver() {

				@Override
				public List<ValidationProvider<?>> getValidationProviders() {
					throw new RuntimeException( "ValidationProviderResolver failed!" );
				}
			};

			Validation.byDefaultProvider().providerResolver( resolver ).configure();
	
		} ).isInstanceOf( ValidationException.class );
	}
}
