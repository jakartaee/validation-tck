/*
* JBoss, Home of Professional Open Source
* Copyright 2016, Red Hat, Inc. and/or its affiliates, and individual contributors
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* http://www.apache.org/licenses/LICENSE-2.0
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
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
