/*
* JBoss, Home of Professional Open Source
* Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual contributors
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
package org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.validdeclarations.service.impl;

import java.util.Date;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.validdeclarations.constraint.ValidCalendarServiceSubClass;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.validdeclarations.model.CalendarEvent;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.validdeclarations.service.AbstractCalendarService;

/**
 * @author Gunnar Morling
 */
public class CalendarServiceSubClass extends AbstractCalendarService {

	@Min(1)
	private long mode;

	public CalendarServiceSubClass() {
		super( 0 );
	}

	public CalendarServiceSubClass(@Min(5) int mode) {
		super( mode );
	}

	@ValidCalendarServiceSubClass
	public CalendarServiceSubClass(String type) {
		super( type );
	}

	@Valid
	public CalendarServiceSubClass(long mode) {
		super( mode );
	}

	public CalendarServiceSubClass(@Valid CalendarEvent defaultEvent) {
		super( defaultEvent );
	}

	@Override
	@NotNull
	public CalendarEvent createEvent(Date start, Date end) {
		return null;
	}

	@Override
	@Valid
	public CalendarEvent createEvent(Date start, Date end, int duration) {
		return null;
	}
}
