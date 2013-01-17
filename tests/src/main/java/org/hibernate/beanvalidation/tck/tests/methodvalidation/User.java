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
package org.hibernate.beanvalidation.tck.tests.methodvalidation;

import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.beanvalidation.tck.tests.methodvalidation.constraint.MyCrossParameterConstraint;

/**
 * @author Gunnar Morling
 */
public class User {

	public interface Basic {
	}

	public interface Extended {
	}

	public User() {
	}

	//testOneViolation
	public void setFirstName(@NotNull String firstName) {
	}

	public User(@NotNull String firstName) {
	}

	//testTwoViolationsOnSameParameter
	public void setFirstNameStrict(@Size(min = 3) @Pattern(regexp = "aaa") String firstName, int i) {
	}

	public User(@Size(min = 3) @Pattern(regexp = "aaa") String firstName, int i) {
	}

	//testValidationWithGroup
	public void setLastName(@Size(min = 3, groups = Extended.class) String lastName, long l) {
	}

	public User(@Size(min = 3, groups = Extended.class) String lastName, long l) {
	}

	//testTwoConstraintsOfSameType
	public void setLastNameStrict(@Size.List({
			@Size(min = 3),
			@Size(min = 6)
	}) CharSequence lastName) {
	}

	public User(@Size.List({ @Size(min = 3), @Size(min = 6) }) CharSequence lastName) {
	}

	//testTwoViolations
	//testNoViolations
	public void setNames(@NotNull String firstName, @Size(min = 3) CharSequence lastName) {
	}

	public User(@NotNull String firstName, @Size(min = 3) CharSequence lastName) {
	}

	//testValidationWithSeveralGroups
	public void setAllData(@NotNull(groups = Basic.class) String firstName, @Size(min = 3,
			groups = Extended.class) String lastName, @NotNull(groups = Extended.class) Date dateOfBirth) {
	}

	public User(@NotNull(groups = Basic.class) String firstName, @Size(min = 3,
			groups = Extended.class) String lastName, @NotNull(groups = Extended.class) Date dateOfBirth) {
	}

	//testOneViolationFromCrossParameterConstraint
	@MyCrossParameterConstraint
	public void setAddress(String street, String houseNo) {
	}

	@MyCrossParameterConstraint
	public User(String street, String houseNo) {
	}

	//testCrossParameterConstraintValidationWithGroup
	@MyCrossParameterConstraint(groups = Extended.class)
	public void setAddressExtended(CharSequence street, String houseNo) {
	}

	@MyCrossParameterConstraint(groups = Extended.class)
	public User(CharSequence street, String houseNo) {
	}

	//testCrossParameterConstraintGivenSeveralTimes
	@MyCrossParameterConstraint.List({
			@MyCrossParameterConstraint(message = "1"),
			@MyCrossParameterConstraint(message = "2")
	})
	public void setAddress(String street, String houseNo, String city) {
	}

	@MyCrossParameterConstraint.List({
			@MyCrossParameterConstraint(message = "1"),
			@MyCrossParameterConstraint(message = "2")
	})
	public User(String street, String houseNo, String city) {
	}
}
