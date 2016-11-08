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

import javax.validation.constraints.Future;

public class FutureDummyEntity {

	@Future
	private Calendar calendar;

	@Future
	private Date date;

	@Future
	private HijrahDate hijrahDate;

	@Future
	private Instant instant;

	@Future
	private JapaneseDate japaneseDate;

	@Future
	private LocalDate localDate;

	@Future
	private LocalDateTime localDateTime;

	@Future
	private MinguoDate minguoDate;

	@Future
	private OffsetDateTime offsetDateTime;

	@Future
	private ThaiBuddhistDate thaiBuddhistDate;

	@Future
	private Year year;

	@Future
	private YearMonth yearMonth;

	@Future
	private ZonedDateTime zonedDateTime;

	public FutureDummyEntity() {
	}

	public FutureDummyEntity(ZonedDateTime dateTime) {
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
