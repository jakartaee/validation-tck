/*
* JBoss, Home of Professional Open Source
* Copyright 2009, Red Hat, Inc. and/or its affiliates, and individual contributors
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
package org.hibernate.beanvalidation.tck.tests.metadata;

import javax.validation.ConstraintTarget;
import javax.validation.constraints.Size;

/**
 * @author Hardy Ferentschik
 */
public interface Person {
	@NotEmpty(groups = PersonValidation.class, payload = Severity.Info.class)
	String getFirstName();

	@Size(min = 3, message = "must at least be {min} characters long")
	String getMiddleName();

	@NotEmpty
	String getLastName();

	@CustomConstraint(validationAppliesTo = ConstraintTarget.RETURN_VALUE)
	int getAge();

	public interface PersonValidation {
	}
}
