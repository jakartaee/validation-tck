/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.versioning;

import java.time.Clock;

import javax.validation.ClockProvider;

/**
 * @author Guillaume Smet
 */
public class DummyClockProvider implements ClockProvider {

	@Override
	public Clock getClock() {
		return Clock.systemDefaultZone();
	}
}
