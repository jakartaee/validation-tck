package org.hibernate.beanvalidation.tck.tests.integration.cdi.executable.priority;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import static org.testng.Assert.assertFalse;

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
