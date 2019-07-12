/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.common;

import javax.validation.ClockProvider;
import javax.validation.Configuration;
import javax.validation.ConstraintValidatorFactory;
import javax.validation.MessageInterpolator;
import javax.validation.ParameterNameProvider;
import javax.validation.TraversableResolver;
import javax.validation.Validator;
import javax.validation.ValidatorContext;
import javax.validation.ValidatorFactory;
import javax.validation.spi.BootstrapState;
import javax.validation.spi.ConfigurationState;
import javax.validation.spi.ValidationProvider;

/**
 * @author Hardy Ferentschik
 */
public class TCKValidationProvider implements ValidationProvider<TCKValidatorConfiguration> {

	@Override
	public TCKValidatorConfiguration createSpecializedConfiguration(BootstrapState state) {
		return TCKValidatorConfiguration.class.cast( new TCKValidatorConfiguration( this ) );
	}

	@Override
	public Configuration<?> createGenericConfiguration(BootstrapState state) {
		return new TCKValidatorConfiguration( this );
	}

	@Override
	public ValidatorFactory buildValidatorFactory(ConfigurationState configurationState) {
		return new DummyValidatorFactory();
	}

	public static class DummyValidatorFactory implements ValidatorFactory {
		@Override
		public Validator getValidator() {
			throw new UnsupportedOperationException();
		}

		@Override
		public ValidatorContext usingContext() {
			throw new UnsupportedOperationException();
		}

		@Override
		public MessageInterpolator getMessageInterpolator() {
			throw new UnsupportedOperationException();
		}

		@Override
		public TraversableResolver getTraversableResolver() {
			throw new UnsupportedOperationException();
		}

		@Override
		public ConstraintValidatorFactory getConstraintValidatorFactory() {
			throw new UnsupportedOperationException();
		}

		@Override
		public ParameterNameProvider getParameterNameProvider() {
			throw new UnsupportedOperationException();
		}

		@Override
		public ClockProvider getClockProvider() {
			throw new UnsupportedOperationException();
		}

		@Override
		public <T> T unwrap(Class<T> type) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void close() {
			throw new UnsupportedOperationException();
		}
	}
}
