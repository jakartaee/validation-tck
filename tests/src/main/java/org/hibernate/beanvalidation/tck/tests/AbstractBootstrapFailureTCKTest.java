/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests;

import java.lang.reflect.Constructor;
//CHECKSTYLE:OFF
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.testng.IHookCallBack;
import org.testng.ITestResult;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
//CHECKSTYLE:ON

/**
 * This base class may only be used to test bootstrapping failures.
 * <p>
 * In some cases, the failure can either happen at deployment or at bootstrap. Thus, with this class, when the
 * deployment is failing, we catch the deployment error if it matches a certain type of exception, and we consider it at
 * the exception that will be thrown by the tests.
 * <p>
 * In this case, the content of the tests is not executed so you need to be very careful to use this class only when
 * this behavior is required.
 *
 * @author Guillaume Smet
 */
public abstract class AbstractBootstrapFailureTCKTest extends AbstractTCKTest {

	private Exception acceptedDeploymentException;

	protected abstract Class<? extends Exception> acceptedDeploymentExceptionType();

	@Override
	@BeforeClass(groups = "arquillian", inheritGroups = true)
	public void arquillianBeforeClass() throws Exception {
		try {
			super.arquillianBeforeClass();
		}
		catch (Exception e) {
			// if the deployment throws an exception and if it's of the right type, we don't throw the exception here
			// and we will "virtually" throw it instead of executing the tests
			acceptedDeploymentException = extractAcceptedDeploymentException( acceptedDeploymentExceptionType(), e );
			if ( acceptedDeploymentException == null ) {
				throw e;
			}
		}
	}

	@Override
	@BeforeMethod(groups = "arquillian", inheritGroups = true)
	public void arquillianBeforeTest(Method testMethod) throws Exception {
		// Don't do anything if the deployment failed
		if ( acceptedDeploymentException != null ) {
			return;
		}

		super.arquillianBeforeTest( testMethod );
	}

	@Override
	public void run(IHookCallBack callback, ITestResult testResult) {
		if ( acceptedDeploymentException != null ) {
			// We consider that the test failed with the exception caught during the deployment
			testResult.setStatus( ITestResult.FAILURE );
			testResult.setThrowable( acceptedDeploymentException );

			return;
		}

		super.run( callback, testResult );
	}

	private static Exception extractAcceptedDeploymentException(Class<? extends Exception> acceptedDeploymentExceptionType, Throwable exception) {
		if ( acceptedDeploymentExceptionType.isAssignableFrom( exception.getClass() ) ) {
			return (Exception) exception;
		}

		if ( exception instanceof InvocationTargetException ) {
			Exception acceptedDeploymentException = extractAcceptedDeploymentException( acceptedDeploymentExceptionType,
					( (InvocationTargetException) exception ).getTargetException() );
			if ( acceptedDeploymentException != null ) {
				return acceptedDeploymentException;
			}
		}

		if ( exception.getCause() != null ) {
			Exception acceptedDeploymentException = extractAcceptedDeploymentException( acceptedDeploymentExceptionType, exception.getCause() );
			if ( acceptedDeploymentException != null ) {
				return acceptedDeploymentException;
			}
		}

		// in the case of Weld, the wrapped exceptions are simply stored in the message, so we check for the exception
		// class name in the message and we try to forge an exception with a constructor taking a String parameter
		if ( exception.getMessage().contains( acceptedDeploymentExceptionType.getName() ) ) {
			try {
				Constructor<? extends Exception> exceptionConstructor = acceptedDeploymentExceptionType.getConstructor( String.class );
				return exceptionConstructor.newInstance( "Forged exception" );
			}
			catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				// we can't really do anything useful here so we just let the deployment exception be thrown
			}
		}

		return null;
	}
}
