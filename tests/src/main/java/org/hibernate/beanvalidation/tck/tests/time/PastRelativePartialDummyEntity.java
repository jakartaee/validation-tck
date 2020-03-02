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

import jakarta.validation.constraints.Past;

public class PastRelativePartialDummyEntity {

	@Past
	private LocalTime localTime;

	@Past
	private MonthDay monthDay;

	@Past
	private OffsetTime offsetTime;

	public PastRelativePartialDummyEntity() {
	}

	public PastRelativePartialDummyEntity(ZonedDateTime dateTime) {
		localTime = dateTime.toLocalTime();
		monthDay = MonthDay.from( dateTime );
		offsetTime = OffsetTime.from( dateTime );
	}
}
