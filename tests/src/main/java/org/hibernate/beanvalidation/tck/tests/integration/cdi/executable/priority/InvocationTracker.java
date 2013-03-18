/*
* JBoss, Home of Professional Open Source
* Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual contributors
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* http://www.apache.org/licenses/LICENSE-2.0
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
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
