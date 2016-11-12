package org.hibernate.beanvalidation.tck.tests.time;

import java.time.Clock;
import java.time.ZonedDateTime;

import javax.validation.ClockProvider;

/**
 * A clock provider referencing a fixed point in time.
 *
 * @author Guillaume Smet
 */
public class FixedClockProvider implements ClockProvider {

	private Clock clock;

	public FixedClockProvider(ZonedDateTime dateTime) {
		clock = Clock.fixed( dateTime.toInstant(), dateTime.getZone() );
	}

	@Override
	public Clock getClock() {
		return clock;
	}

}
