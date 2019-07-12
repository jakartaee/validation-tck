/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.methodvalidation.model;

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

	//testOneViolation
	public void setFirstName(@Size(min = 3) String firstName) {
	}

	public User(@Size(min = 3) String firstName) {
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
	//testGetInvalidValueForCrossParameterConstraint
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

	public User() {
	}
}
