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

import java.time.LocalTime;
import java.time.MonthDay;
import java.time.OffsetTime;
import java.time.ZonedDateTime;

import javax.validation.constraints.Future;

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
