/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.time;

import java.time.LocalTime;
import java.time.MonthDay;
import java.time.OffsetTime;
import java.time.ZonedDateTime;

import jakarta.validation.constraints.Future;

public class FutureRelativePartialDummyEntity {

	@Future
	private LocalTime localTime;

	@Future
	private MonthDay monthDay;

	@Future
	private OffsetTime offsetTime;

	public FutureRelativePartialDummyEntity() {
	}

	public FutureRelativePartialDummyEntity(ZonedDateTime dateTime) {
		localTime = dateTime.toLocalTime();
		monthDay = MonthDay.from( dateTime );
		offsetTime = OffsetTime.from( dateTime );
	}
}
