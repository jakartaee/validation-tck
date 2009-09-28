// $Id$
/*
* JBoss, Home of Professional Open Source
* Copyright 2008, Red Hat Middleware LLC, and individual contributors
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
package org.hibernate.jsr303.tck.tests.constraints.application;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;

/**
 * @author Hardy Ferentschik
 */
public class Visibility {
	@Min(value = 100, message = "publicField")
	public int publicValue;

	@Min(value = 100, message = "protectedField")
	protected int protectedValue;

	@Min(value = 100, message = "privateField")
	private int privateValue;

	@DecimalMin(value = "100.0", message = "publicProperty")
	public int getPublicValue() {
		return publicValue;
	}

	public void setValues(int value) {
		this.publicValue = value;
		this.protectedValue = value;
		this.privateValue = value;
	}

	@DecimalMin(value = "100.0", message = "protectedProperty")
	protected int getProtectedValue() {
		return protectedValue;
	}

	@DecimalMin(value = "100.0", message = "privateProperty")
	private int getPrivateValue() {
		return privateValue;
	}
}
