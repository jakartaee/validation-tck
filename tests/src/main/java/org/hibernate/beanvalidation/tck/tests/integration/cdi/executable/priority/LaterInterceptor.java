/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.integration.cdi.executable.priority;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@Interceptor
@Late
@Priority(4801)
public class LaterInterceptor {

	@Inject
	private InvocationTracker invocationTracker;

	@AroundInvoke
	public Object invoke(InvocationContext ctx) throws Exception {
		assertTrue( invocationTracker.isEarlierInterceptorInvoked() );
		assertTrue( invocationTracker.isValidatorInvoked() );
		assertFalse( invocationTracker.isLaterInterceptorInvoked() );

		invocationTracker.setLaterInterceptorInvoked( true );
		return ctx.proceed();
	}
}
