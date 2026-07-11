/*
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.jboss.arquillian.junit5.ArquillianExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;

/**
 * Works around Arquillian JUnit5 silently skipping {@code @BeforeEach}/{@code @AfterEach}
 * methods when running in standalone (non-container) mode. In standalone mode, Arquillian's
 * {@code interceptBeforeEachMethod}/{@code interceptAfterEachMethod} call
 * {@code invocation.skip()} instead of {@code invocation.proceed()}, preventing the lifecycle
 * methods from ever executing.
 * <p>
 * This subclass overrides that behavior: in standalone mode it lets the methods proceed normally,
 * and in container mode it delegates to the parent class.
 * <p>
 * Additionally, handles bootstrap/deployment failures for test classes annotated with
 * {@link ExpectBootstrapFailure}. In container mode, deployment can fail before any test method
 * runs — this extension catches such exceptions during {@code beforeAll} and skips test method
 * execution if the exception matches the declared type. In standalone mode, the test body is
 * executed and the extension asserts that it throws a matching exception.
 */
public class TckArquillianExtension extends ArquillianExtension {

	private static final ExtensionContext.Namespace NAMESPACE =
			ExtensionContext.Namespace.create( TckArquillianExtension.class );
	private static final String DEPLOYMENT_EXCEPTION_KEY = "deploymentException";

	private static boolean isInsideArquillian(ExtensionContext context) {
		return Boolean.parseBoolean(
				context.getConfigurationParameter( RUNNING_INSIDE_ARQUILLIAN ).orElse( "false" )
		);
	}

	private static Exception getStoredDeploymentException(ExtensionContext context) {
		ExtensionContext classContext = context.getTestMethod().isPresent()
				? context.getParent().orElse( context )
				: context;
		return classContext.getStore( NAMESPACE )
				.get( DEPLOYMENT_EXCEPTION_KEY, Exception.class );
	}

	private static Class<? extends Exception> getExpectedBootstrapFailure(ExtensionContext context) {
		ExpectBootstrapFailure annotation = context.getRequiredTestClass()
				.getAnnotation( ExpectBootstrapFailure.class );
		return annotation != null ? annotation.value() : null;
	}

	@Override
	public void beforeAll(ExtensionContext context) throws Exception {
		Class<? extends Exception> expectedType = getExpectedBootstrapFailure( context );

		if ( expectedType != null ) {
			validateSingleTestMethod( context.getRequiredTestClass() );
		}

		try {
			super.beforeAll( context );
		}
		catch (Exception e) {
			if ( expectedType != null ) {
				Exception matchingException = extractAcceptedDeploymentException( expectedType, e );
				if ( matchingException != null ) {
					context.getStore( NAMESPACE ).put( DEPLOYMENT_EXCEPTION_KEY, matchingException );
					return;
				}
			}
			throw e;
		}
	}

	@Override
	public void afterAll(ExtensionContext context) throws Exception {
		if ( getStoredDeploymentException( context ) != null ) {
			return;
		}
		super.afterAll( context );
	}

	@Override
	public void beforeEach(ExtensionContext context) throws Exception {
		if ( getStoredDeploymentException( context ) != null ) {
			return;
		}
		super.beforeEach( context );
	}

	@Override
	public void afterEach(ExtensionContext context) throws Exception {
		if ( getStoredDeploymentException( context ) != null ) {
			return;
		}
		super.afterEach( context );
	}

	@Override
	public void beforeTestExecution(ExtensionContext context) throws Exception {
		if ( getStoredDeploymentException( context ) != null ) {
			return;
		}
		super.beforeTestExecution( context );
	}

	@Override
	public void interceptTestMethod(InvocationInterceptor.Invocation<Void> invocation,
			ReflectiveInvocationContext<Method> invocationContext,
			ExtensionContext extensionContext) throws Throwable {
		Class<? extends Exception> expectedType = getExpectedBootstrapFailure( extensionContext );

		if ( expectedType != null && getStoredDeploymentException( extensionContext ) != null ) {
			invocation.skip();
			return;
		}

		if ( expectedType != null && !isInsideArquillian( extensionContext ) ) {
			try {
				super.interceptTestMethod( invocation, invocationContext, extensionContext );
			}
			catch (Throwable t) {
				if ( expectedType.isInstance( t ) || extractAcceptedDeploymentException( expectedType, t ) != null ) {
					return;
				}
				throw t;
			}
			throw new AssertionError( "Expected " + expectedType.getName() + " to be thrown, but nothing was thrown" );
		}

		super.interceptTestMethod( invocation, invocationContext, extensionContext );
	}

	@Override
	public void interceptBeforeEachMethod(InvocationInterceptor.Invocation<Void> invocation,
			ReflectiveInvocationContext<Method> invocationContext,
			ExtensionContext extensionContext) throws Throwable {
		if ( getStoredDeploymentException( extensionContext ) != null ) {
			invocation.skip();
			return;
		}
		if ( isInsideArquillian( extensionContext ) ) {
			super.interceptBeforeEachMethod( invocation, invocationContext, extensionContext );
		}
		else {
			invocation.proceed();
		}
	}

	@Override
	public void interceptAfterEachMethod(InvocationInterceptor.Invocation<Void> invocation,
			ReflectiveInvocationContext<Method> invocationContext,
			ExtensionContext extensionContext) throws Throwable {
		if ( getStoredDeploymentException( extensionContext ) != null ) {
			invocation.skip();
			return;
		}
		if ( isInsideArquillian( extensionContext ) ) {
			super.interceptAfterEachMethod( invocation, invocationContext, extensionContext );
		}
		else {
			invocation.proceed();
		}
	}

	private static void validateSingleTestMethod(Class<?> testClass) {
		long testMethodCount = Arrays.stream( testClass.getDeclaredMethods() )
				.filter( m -> m.isAnnotationPresent( Test.class ) )
				.count();
		if ( testMethodCount != 1 ) {
			throw new IllegalStateException(
					"@ExpectBootstrapFailure requires exactly one @Test method, but "
							+ testClass.getName() + " has " + testMethodCount
							+ ". Split into separate test classes so container and standalone modes exercise the same scenario." );
		}
	}

	private static Exception extractAcceptedDeploymentException(
			Class<? extends Exception> acceptedDeploymentExceptionType, Throwable exception) {
		if ( acceptedDeploymentExceptionType.isAssignableFrom( exception.getClass() ) ) {
			return (Exception) exception;
		}

		if ( exception instanceof InvocationTargetException ) {
			Exception accepted = extractAcceptedDeploymentException( acceptedDeploymentExceptionType,
					( (InvocationTargetException) exception ).getTargetException() );
			if ( accepted != null ) {
				return accepted;
			}
		}

		if ( exception.getCause() != null ) {
			Exception accepted = extractAcceptedDeploymentException( acceptedDeploymentExceptionType,
					exception.getCause() );
			if ( accepted != null ) {
				return accepted;
			}
		}

		for ( Throwable suppressedException : exception.getSuppressed() ) {
			Exception accepted = extractAcceptedDeploymentException( acceptedDeploymentExceptionType,
					suppressedException );
			if ( accepted != null ) {
				return accepted;
			}
		}

		// Weld compatibility: wrapped exceptions may only appear in the message string
		if ( exception.getMessage() != null
				&& exception.getMessage().contains( acceptedDeploymentExceptionType.getName() ) ) {
			try {
				Constructor<? extends Exception> exceptionConstructor =
						acceptedDeploymentExceptionType.getConstructor( String.class );
				return exceptionConstructor.newInstance( "Forged exception" );
			}
			catch (NoSuchMethodException | SecurityException | InstantiationException
					| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// cannot forge the exception, let the deployment exception propagate
			}
		}

		return null;
	}
}
