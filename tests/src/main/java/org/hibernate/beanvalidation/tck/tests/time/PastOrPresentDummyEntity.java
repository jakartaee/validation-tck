/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.time;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.chrono.HijrahDate;
import java.time.chrono.JapaneseDate;
import java.time.chrono.MinguoDate;
import java.time.chrono.ThaiBuddhistDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.validation.constraints.PastOrPresent;

public class PastOrPresentDummyEntity {

	@PastOrPresent
	private Calendar calendar;

	@PastOrPresent
	private Date date;

	@PastOrPresent
	private HijrahDate hijrahDate;

	@PastOrPresent
	private Instant instant;

	@PastOrPresent
	private JapaneseDate japaneseDate;

	@PastOrPresent
	private LocalDate localDate;

	@PastOrPresent
	private LocalDateTime localDateTime;

	@PastOrPresent
	private MinguoDate minguoDate;

	@PastOrPresent
	private OffsetDateTime offsetDateTime;

	@PastOrPresent
	private ThaiBuddhistDate thaiBuddhistDate;

	@PastOrPresent
	private Year year;

	@PastOrPresent
	private YearMonth yearMonth;

	@PastOrPresent
	private ZonedDateTime zonedDateTime;

	public PastOrPresentDummyEntity() {
	}

	public PastOrPresentDummyEntity(ZonedDateTime dateTime) {
		calendar = GregorianCalendar.from( dateTime );
		date = calendar.getTime();

		instant = dateTime.toInstant();
		localDateTime = dateTime.toLocalDateTime();

		hijrahDate = HijrahDate.from( dateTime );
		japaneseDate = JapaneseDate.from( dateTime );
		localDate = LocalDate.from( dateTime );
		minguoDate = MinguoDate.from( dateTime );
		offsetDateTime = dateTime.toOffsetDateTime();
		thaiBuddhistDate = ThaiBuddhistDate.from( dateTime );
		year = Year.from( dateTime );
		yearMonth = YearMonth.from( dateTime );
		zonedDateTime = dateTime;
	}
}
