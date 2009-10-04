// $Id$
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
package org.hibernate.jsr303.tck.tests.xmlconfiguration.constraintdeclaration.propertylevel;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author Hardy Ferentschik
 */
public class User {
	private String firstname;

	private String lastname;

	private CreditCard firstCreditCard;

	private CreditCard secondCreditCard;

	@NotNull
	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	@NotNull
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

	@Valid
	public CreditCard getSecondCreditCard() {
		return secondCreditCard;
	}

	public void setSecondCreditCard(CreditCard secondCreditCard) {
		this.secondCreditCard = secondCreditCard;
	}
}
