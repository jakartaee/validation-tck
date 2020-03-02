/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.time;

import java.time.LocalTime;
import java.time.MonthDay;
import java.time.OffsetTime;
import java.time.ZonedDateTime;

import jakarta.validation.constraints.FutureOrPresent;

public class FutureOrPresentRelativePartialDummyEntity {

	@FutureOrPresent
	private LocalTime localTime;

	@FutureOrPresent
	private MonthDay monthDay;

	@FutureOrPresent
	private OffsetTime offsetTime;

	public FutureOrPresentRelativePartialDummyEntity() {
	}

	public FutureOrPresentRelativePartialDummyEntity(ZonedDateTime dateTime) {
		localTime = dateTime.toLocalTime();
		monthDay = MonthDay.from( dateTime );
		offsetTime = OffsetTime.from( dateTime );
	}
}
