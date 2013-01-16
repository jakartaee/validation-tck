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
package org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.validdeclarations;

import java.util.Date;
import javax.validation.Valid;
import javax.validation.constraints.Max;

import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.validdeclarations.constraint.ValidAbstractCalendarService;
import org.hibernate.beanvalidation.tck.tests.constraints.inheritance.method.validdeclarations.model.CalendarEvent;

/**
 * @author Gunnar Morling
 */
public abstract class AbstractCalendarService {

	public AbstractCalendarService(@Max(5) int mode) {
	}

	public AbstractCalendarService(CalendarEvent defaultEvent) {
	}

	@ValidAbstractCalendarService
	public AbstractCalendarService(String type) {
	}

	@Valid
	public AbstractCalendarService(long mode) {
	}

	public abstract CalendarEvent createEvent(Date start, Date end);

	public abstract CalendarEvent createEvent(Date start, Date end, int duration);
}
