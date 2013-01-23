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
package org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.service;

import javax.validation.ConvertGroup;
import javax.validation.Valid;
import javax.validation.groups.Default;

import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.model.BasicPostal;
import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.model.User;

/**
 * @author Gunnar Morling
 */
public interface UserWriteServiceWithGroupConversionOnParameter {

	void addUser(
			@Valid
			@ConvertGroup(from = Default.class, to = BasicPostal.class)
			User user);
}
