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
package org.hibernate.beanvalidation.tck.tests.integration.cdi.executable;

import javax.inject.Inject;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

/**
 * @author Gunnar Morling
 */
public class AnotherBookingService {

	public interface IgnoredValidationGroup {
	}

	private static int invocationCount = 0;

	private static String name;

	@Inject
	@ValidAnotherBookingService
	@Null(groups = IgnoredValidationGroup.class)
	public AnotherBookingService(@LongName @Size(min = 5) @Null(groups = IgnoredValidationGroup.class) String name) {
		AnotherBookingService.name = name;
		invocationCount++;
	}

	public static String getName() {
		return name;
	}

	public static int getInvocationCount() {
		return invocationCount;
	}
}
