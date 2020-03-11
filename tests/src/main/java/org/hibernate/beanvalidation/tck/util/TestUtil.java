/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.util;

import static org.testng.Assert.assertTrue;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import jakarta.validation.Configuration;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.MessageInterpolator;
import jakarta.validation.Path;
import jakarta.validation.Path.Node;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.bootstrap.ProviderSpecificBootstrap;
import jakarta.validation.metadata.ConstraintDescriptor;
import jakarta.validation.metadata.ConstructorDescriptor;
import jakarta.validation.metadata.ElementDescriptor;
import jakarta.validation.metadata.MethodDescriptor;
import jakarta.validation.metadata.PropertyDescriptor;
import jakarta.validation.spi.ValidationProvider;

/**
 * @author Hardy Ferentschik
 * @author Gunnar Morling
 */
public final class TestUtil {

	private static final String VALIDATION_PROVIDER_TEST_CLASS = "validation.provider";

	private static ValidationProvider<?> validationProviderUnderTest;

	private TestUtil() {
	}

	public static Validator getValidatorUnderTest() {
		return getValidatorFactoryUnderTest().getValidator();
	}

	public static ValidationProvider<?> getValidationProviderUnderTest() {
		if ( validationProviderUnderTest == null ) {
			instantiateValidationProviderUnderTest();
		}
		return validationProviderUnderTest;
	}

	public static ValidatorFactory getValidatorFactoryUnderTest() {
		Configuration<?> config = getConfigurationUnderTest();
		return config.buildValidatorFactory();
	}

	public static Configuration<?> getConfigurationUnderTest() {
		if ( validationProviderUnderTest == null ) {
			instantiateValidationProviderUnderTest();
		}

		@SuppressWarnings("unchecked")
		ProviderSpecificBootstrap<?> bootstrap = Validation.byProvider( validationProviderUnderTest.getClass() );
		return bootstrap.configure();
	}

	public static MessageInterpolator getDefaultMessageInterpolator() {
		Configuration<?> config = getConfigurationUnderTest();
		return config.getDefaultMessageInterpolator();
	}

	/**
	 * Retrieves the parameter names from the given set of constraint
	 * violations, which must represent method or constructor constraint
	 * violations.
	 *
	 * @param constraintViolations The violations to retrieve the names from.
	 *
	 * @return The parameter names.
	 */
	public static Set<String> getParameterNames(Set<? extends ConstraintViolation<?>> constraintViolations) {
		Set<String> parameterNames = new HashSet<String>();

		for ( ConstraintViolation<?> constraintViolation : constraintViolations ) {
			parameterNames.add( getParameterName( constraintViolation.getPropertyPath() ) );
		}

		return parameterNames;
	}

	public static <T> ConstraintViolation<T> getConstraintViolationForParameter(Set<ConstraintViolation<T>> constraintViolations, String parameterName) {
		for ( ConstraintViolation<T> constraintViolation : constraintViolations ) {
			if ( parameterName.equals( getParameterName( constraintViolation.getPropertyPath() ) ) ) {
				return constraintViolation;
			}
		}

		throw new IllegalArgumentException( "Found no constraint violation for parameter " + parameterName );
	}

	public static <T> ConstraintViolation<T> getConstraintViolationForConstraintType(Set<ConstraintViolation<T>> constraintViolations,
																					 Class<? extends Annotation> constraint) {
		for ( ConstraintViolation<T> constraintViolation : constraintViolations ) {
			if ( constraintViolation.getConstraintDescriptor().getAnnotation().annotationType().equals( constraint ) ) {
				return constraintViolation;
			}
		}

		throw new IllegalArgumentException( "Found no constraint violation for constraint " + constraint.getSimpleName() );
	}

	public static String getParameterName(Path path) {
		Iterator<Node> nodes = path.iterator();

		assertTrue( nodes.hasNext() );
		nodes.next();

		assertTrue( nodes.hasNext() );
		return nodes.next().getName();
	}

	@SafeVarargs
	public static <T> Set<T> asSet(T... ts) {
		return new HashSet<T>( Arrays.asList( ts ) );
	}

	public static PropertyDescriptor getPropertyDescriptor(Class<?> clazz, String property) {
		Validator validator = getValidatorUnderTest();
		return validator.getConstraintsForClass( clazz ).getConstraintsForProperty( property );
	}

	public static MethodDescriptor getMethodDescriptor(Class<?> clazz, String name, Class<?>... parameterTypes) {
		Validator validator = getValidatorUnderTest();
		return validator.getConstraintsForClass( clazz )
				.getConstraintsForMethod( name, parameterTypes );
	}

	public static ConstructorDescriptor getConstructorDescriptor(Class<?> clazz, Class<?>... parameterTypes) {
		Validator validator = getValidatorUnderTest();
		return validator.getConstraintsForClass( clazz ).getConstraintsForConstructor(
				parameterTypes
		);
	}

	public static Set<ConstraintDescriptor<?>> getConstraintDescriptorsFor(Class<?> clazz, String property) {
		ElementDescriptor elementDescriptor = getPropertyDescriptor( clazz, property );
		return elementDescriptor.getConstraintDescriptors();
	}

	public static InputStream getInputStreamForPath(String path) {
		// try the context class loader first
		InputStream inputStream = Thread.currentThread()
				.getContextClassLoader()
				.getResourceAsStream( path );

		// try the current class loader
		if ( inputStream == null ) {
			inputStream = TestUtil.class.getResourceAsStream( path );
		}
		return inputStream;
	}

	@SuppressWarnings("unchecked")
	public static <T, I extends T> T getValidatingProxy(I implementor, Class<?>[] interfaces, Validator executableValidator, Class<?>... validationGroups) {
		InvocationHandler handler = new ValidationInvocationHandler(
				implementor, executableValidator, validationGroups );

		return (T) Proxy.newProxyInstance(
				implementor.getClass().getClassLoader(),
				interfaces,
				handler );
	}

	public static <T, I extends T> T getValidatingProxy(I implementor, Validator executableValidator, Class<?>... validationGroups) {
		return getValidatingProxy(
				implementor,
				implementor.getClass().getInterfaces(),
				executableValidator,
				validationGroups );
	}

	private static <U extends ValidationProvider<?>> void instantiateValidationProviderUnderTest() {
		String validatorProviderClassName = System.getProperty( VALIDATION_PROVIDER_TEST_CLASS );
		if ( validatorProviderClassName == null ) {
			throw new RuntimeException(
					"The test harness must specify the class name of the validation provider under test. Set system property '" + VALIDATION_PROVIDER_TEST_CLASS + "'"
			);
		}

		Class<U> providerClass;
		try {
			@SuppressWarnings("unchecked")
			Class<U> tmpClazz = (Class<U>) TestUtil.class.getClassLoader()
					.loadClass( validatorProviderClassName );
			providerClass = tmpClazz;
		}
		catch ( ClassNotFoundException e ) {
			throw new RuntimeException( "Unable to load " + validatorProviderClassName );
		}

		try {
			validationProviderUnderTest = providerClass.newInstance();
		}
		catch ( Exception e ) {
			throw new RuntimeException( "Unable to instantiate " + validatorProviderClassName );
		}
	}
}
