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

import jakarta.validation.constraints.PastOrPresent;

public class PastOrPresentRelativePartialDummyEntity {

	@PastOrPresent
	private LocalTime localTime;

	@PastOrPresent
	private MonthDay monthDay;

	@PastOrPresent
	private OffsetTime offsetTime;

	public PastOrPresentRelativePartialDummyEntity() {
	}

	public PastOrPresentRelativePartialDummyEntity(ZonedDateTime dateTime) {
		localTime = dateTime.toLocalTime();
		monthDay = MonthDay.from( dateTime );
		offsetTime = OffsetTime.from( dateTime );
	}
}
