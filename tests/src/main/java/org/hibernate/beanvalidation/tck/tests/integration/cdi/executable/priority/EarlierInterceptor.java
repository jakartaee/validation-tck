/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.integration.cdi.executable.priority;

import static org.testng.Assert.assertFalse;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor
@Early
@Priority(4799)
public class EarlierInterceptor {

	@Inject
	private InvocationTracker invocationTracker;

	@AroundInvoke
	public Object invoke(InvocationContext ctx) throws Exception {
		assertFalse( invocationTracker.isEarlierInterceptorInvoked() );
		assertFalse( invocationTracker.isValidatorInvoked() );
		assertFalse( invocationTracker.isLaterInterceptorInvoked() );

		invocationTracker.setEarlierInterceptorInvoked( true );
		return ctx.proceed();
	}
}
