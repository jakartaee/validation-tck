/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.time;

import java.time.Clock;
import java.time.ZonedDateTime;

import jakarta.validation.ClockProvider;

/**
 * A clock provider referencing a fixed point in time.
 *
 * @author Guillaume Smet
 */
public class FixedClockProvider implements ClockProvider {

	private final Clock clock;

	public FixedClockProvider(ZonedDateTime dateTime) {
		clock = Clock.fixed( dateTime.toInstant(), dateTime.getZone() );
	}

	@Override
	public Clock getClock() {
		return clock;
	}

}
