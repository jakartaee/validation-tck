/*
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.bootstrap.customprovider;

import java.io.InputStream;

import jakarta.validation.BootstrapConfiguration;
import jakarta.validation.ClockProvider;
import jakarta.validation.Configuration;
import jakarta.validation.ConstraintValidatorFactory;
import jakarta.validation.MessageInterpolator;
import jakarta.validation.ParameterNameProvider;
import jakarta.validation.TraversableResolver;
import jakarta.validation.Validation;
import jakarta.validation.ValidationException;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.spi.BootstrapState;
import jakarta.validation.spi.ConfigurationState;
import jakarta.validation.spi.ValidationProvider;
import jakarta.validation.valueextraction.ValueExtractor;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.ExpectBootstrapFailure;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.junit.jupiter.api.Test;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
@ExpectBootstrapFailure(ValidationException.class)
public class BootstrapNonLoadableValidationProviderTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( BootstrapNonLoadableValidationProviderTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_VALIDATION, id = "f")
	@SpecAssertion(section = Sections.EXCEPTION, id = "a")
	public void testConfiguredValidationProviderIsNotLoadable() {
		Validation.byProvider( DummyValidationProvider.class ).configure();
	}

	/**
	 * A valid validation provider implementing all required interfaces, but instantiation will fail
	 */
	public static class DummyValidationProvider implements ValidationProvider<DummyValidatorConfiguration> {

		public DummyValidationProvider() {
			throw new RuntimeException( "ups" );
		}

		@Override
		public DummyValidatorConfiguration createSpecializedConfiguration(BootstrapState state) {
			return null;
		}

		@Override
		public Configuration<?> createGenericConfiguration(BootstrapState state) {
			return null;
		}

		@Override
		public ValidatorFactory buildValidatorFactory(ConfigurationState configurationState) {
			return null;
		}
	}

	public static class DummyValidatorConfiguration implements Configuration<DummyValidatorConfiguration> {

		@Override
		public DummyValidatorConfiguration ignoreXmlConfiguration() {
			return null;
		}

		@Override
		public DummyValidatorConfiguration messageInterpolator(MessageInterpolator interpolator) {
			return null;
		}

		@Override
		public DummyValidatorConfiguration traversableResolver(TraversableResolver resolver) {
			return null;
		}

		@Override
		public DummyValidatorConfiguration constraintValidatorFactory(ConstraintValidatorFactory constraintValidatorFactory) {
			return null;
		}

		@Override
		public DummyValidatorConfiguration parameterNameProvider(ParameterNameProvider parameterNameProvider) {
			return null;
		}

		@Override
		public DummyValidatorConfiguration clockProvider(ClockProvider clockProvider) {
			return null;
		}

		@Override
		public DummyValidatorConfiguration addMapping(InputStream stream) {
			return null;
		}

		@Override
		public DummyValidatorConfiguration addProperty(String name, String value) {
			return null;
		}

		@Override
		public DummyValidatorConfiguration addValueExtractor(ValueExtractor<?> extractor) {
			return null;
		}

		@Override
		public MessageInterpolator getDefaultMessageInterpolator() {
			return null;
		}

		@Override
		public TraversableResolver getDefaultTraversableResolver() {
			return null;
		}

		@Override
		public ConstraintValidatorFactory getDefaultConstraintValidatorFactory() {
			return null;
		}

		@Override
		public ParameterNameProvider getDefaultParameterNameProvider() {
			return null;
		}

		@Override
		public ClockProvider getDefaultClockProvider() {
			return null;
		}

		@Override
		public BootstrapConfiguration getBootstrapConfiguration() {
			return null;
		}

		@Override
		public ValidatorFactory buildValidatorFactory() {
			return null;
		}
	}
}
