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
package org.hibernate.beanvalidation.tck.tests.integration.cdi.executable.types;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.executable.ExecutableType;
import javax.validation.executable.ValidateOnExecution;

/**
 * @author Gunnar Morling
 */
public class CalendarService {

	//NONE

	@ValidateOnExecution(type = ExecutableType.NONE)
	public Event createEvent(@NotNull String title) {
		return new Event();
	}

	@ValidateOnExecution(type = { })
	public Event createEvent(@Min(0) int duration) {
		return new Event();
	}

	@ValidateOnExecution(type = { ExecutableType.NONE, ExecutableType.NON_GETTER_METHODS })
	public void createEvent(@Min(0) long duration) {
	}

	//NON_GETTER_METHODS

	@ValidateOnExecution(type = ExecutableType.NON_GETTER_METHODS)
	public Event createEvent(@Min(0) short duration) {
		return new Event();
	}

	@ValidateOnExecution(type = ExecutableType.NON_GETTER_METHODS)
	@ValidOjbect
	public Event createEvent(byte duration) {
		return new Event();
	}

	@ValidateOnExecution(type = ExecutableType.NON_GETTER_METHODS)
	@ValidOjbect
	public Event getEvent() {
		return new Event();
	}

	//GETTER_METHODS

	@ValidateOnExecution(type = ExecutableType.GETTER_METHODS)
	@ValidOjbect
	public Event getSpecialEvent() {
		return new Event();
	}

	@ValidateOnExecution(type = ExecutableType.GETTER_METHODS)
	@ValidOjbect
	public Event getSpecialEvent(int duration) {
		return new Event();
	}

	//ALL

	@ValidateOnExecution(type = ExecutableType.ALL)
	public Event createEvent(@Min(0) double duration) {
		return new Event();
	}

	@ValidateOnExecution(type = ExecutableType.ALL)
	@ValidOjbect
	public Event getVerySpecialEvent() {
		return new Event();
	}

	@ValidateOnExecution(type = { ExecutableType.ALL, ExecutableType.NONE })
	public Event createEvent(@Min(0) float duration) {
		return new Event();
	}
}
