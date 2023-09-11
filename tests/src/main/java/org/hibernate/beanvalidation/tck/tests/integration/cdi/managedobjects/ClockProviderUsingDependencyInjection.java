/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.integration.cdi.managedobjects;

import java.time.Clock;

import jakarta.inject.Inject;
import jakarta.validation.ClockProvider;

/**
 * @author Guillaume Smet
 */
public class ClockProviderUsingDependencyInjection implements ClockProvider {

	@Inject
	private Greeter greeter;

	@Override
	public Clock getClock() {
		return Clock.system( greeter.zoneId() );
	}
}
