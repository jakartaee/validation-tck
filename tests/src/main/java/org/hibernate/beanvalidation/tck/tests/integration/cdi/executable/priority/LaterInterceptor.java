package org.hibernate.beanvalidation.tck.tests.integration.cdi.executable.priority;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

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
