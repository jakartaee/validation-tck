/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.common;

import java.io.InputStream;

import jakarta.validation.BootstrapConfiguration;
import jakarta.validation.ClockProvider;
import jakarta.validation.Configuration;
import jakarta.validation.ConstraintValidatorFactory;
import jakarta.validation.MessageInterpolator;
import jakarta.validation.ParameterNameProvider;
import jakarta.validation.TraversableResolver;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.spi.ValidationProvider;
import jakarta.validation.valueextraction.ValueExtractor;

/**
 * @author Hardy Ferentschik
 */
public class TCKValidatorConfiguration implements Configuration<TCKValidatorConfiguration> {
	private final ValidationProvider<?> provider;

	public TCKValidatorConfiguration() {
		provider = null;
	}

	public TCKValidatorConfiguration(ValidationProvider<?> provider) {
		this.provider = provider;
	}

	@Override
	public TCKValidatorConfiguration ignoreXmlConfiguration() {
		throw new UnsupportedOperationException();
	}

	@Override
	public TCKValidatorConfiguration messageInterpolator(MessageInterpolator interpolator) {
		throw new UnsupportedOperationException();
	}

	@Override
	public TCKValidatorConfiguration traversableResolver(TraversableResolver resolver) {
		throw new UnsupportedOperationException();
	}

	@Override
	public TCKValidatorConfiguration constraintValidatorFactory(ConstraintValidatorFactory constraintValidatorFactory) {
		throw new UnsupportedOperationException();
	}

	@Override
	public TCKValidatorConfiguration parameterNameProvider(ParameterNameProvider parameterNameProvider) {
		throw new UnsupportedOperationException();
	}

	@Override
	public TCKValidatorConfiguration clockProvider(ClockProvider clockProvider) {
		throw new UnsupportedOperationException();
	}

	@Override
	public TCKValidatorConfiguration addMapping(InputStream stream) {
		throw new UnsupportedOperationException();
	}

	@Override
	public TCKValidatorConfiguration addProperty(String name, String value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public TCKValidatorConfiguration addValueExtractor(ValueExtractor<?> extractor) {
		throw new UnsupportedOperationException();
	}

	@Override
	public MessageInterpolator getDefaultMessageInterpolator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public TraversableResolver getDefaultTraversableResolver() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ConstraintValidatorFactory getDefaultConstraintValidatorFactory() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ParameterNameProvider getDefaultParameterNameProvider() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ClockProvider getDefaultClockProvider() {
		throw new UnsupportedOperationException();
	}

	@Override
	public BootstrapConfiguration getBootstrapConfiguration() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ValidatorFactory buildValidatorFactory() {
		return provider.buildValidatorFactory( null );
	}

}
