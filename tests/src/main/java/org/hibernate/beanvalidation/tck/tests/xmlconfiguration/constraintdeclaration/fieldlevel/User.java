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
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.constraintdeclaration.fieldlevel;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.groups.ConvertGroup;
import javax.validation.groups.Default;

/**
 * @author Hardy Ferentschik
 */
public class User {
	@NotNull
	private String firstname;

	@NotNull
	private String lastname;

	@ConvertGroup(from = Default.class, to = CreditRatingA.class)
	private CreditCard firstCreditCard;

	@Valid
	private CreditCard secondCreditCard;

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public CreditCard getFirstCreditCard() {
		return firstCreditCard;
	}

	public void setFirstCreditCard(CreditCard firstCreditCard) {
		this.firstCreditCard = firstCreditCard;
	}

	public CreditCard getSecondCreditCard() {
		return secondCreditCard;
	}

	public void setSecondCreditCard(CreditCard secondCreditCard) {
		this.secondCreditCard = secondCreditCard;
	}

	public interface CreditRatingA {
	}

	public interface CreditRatingAA {
	}
}
