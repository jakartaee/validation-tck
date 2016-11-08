package org.hibernate.beanvalidation.tck.tests.time;

import java.time.LocalTime;
import java.time.MonthDay;
import java.time.OffsetTime;
import java.time.ZonedDateTime;

import javax.validation.constraints.Past;

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
