/*
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.integration.cdi.executable.priority;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@Interceptor
@Early
@Priority(4799)
public class EarlierInterceptor {

	@Inject
	private InvocationTracker invocationTracker;

	@AroundInvoke
	public Object invoke(InvocationContext ctx) throws Exception {
		assertThat( invocationTracker.isEarlierInterceptorInvoked() ).isFalse();
		assertThat( invocationTracker.isValidatorInvoked() ).isFalse();
		assertThat( invocationTracker.isLaterInterceptorInvoked() ).isFalse();

		invocationTracker.setEarlierInterceptorInvoked( true );
		return ctx.proceed();
	}
}
