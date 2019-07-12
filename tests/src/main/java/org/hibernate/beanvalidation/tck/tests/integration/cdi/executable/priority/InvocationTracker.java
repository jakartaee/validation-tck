/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.integration.cdi.executable.priority;

import javax.enterprise.context.ApplicationScoped;

/**
 * @author Gunnar Morling
 */
@ApplicationScoped
public class InvocationTracker {

	private boolean earlierInterceptorInvoked = false;
	private boolean laterInterceptorInvoked = false;
	private boolean validatorInvoked = false;

	public boolean isEarlierInterceptorInvoked() {
		return earlierInterceptorInvoked;
	}

	public void setEarlierInterceptorInvoked(boolean earlierInterceptorInvoked) {
		this.earlierInterceptorInvoked = earlierInterceptorInvoked;
	}

	public boolean isLaterInterceptorInvoked() {
		return laterInterceptorInvoked;
	}

	public void setLaterInterceptorInvoked(boolean laterInterceptorInvoked) {
		this.laterInterceptorInvoked = laterInterceptorInvoked;
	}

	public boolean isValidatorInvoked() {
		return validatorInvoked;
	}

	public void setValidatorInvoked(boolean validatorInvoked) {
		this.validatorInvoked = validatorInvoked;
	}
}
