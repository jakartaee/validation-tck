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
package org.hibernate.beanvalidation.tck.tests.integration.ee;

import java.util.Set;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectConstraintViolationMessages;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * A test EJB which retrieves validator and validator factory via
 * {@code @Resource} injection.
 *
 * @author Gunnar Morling
 */
@Stateless
public class ValidationTestEjb {

	@Resource
	public ValidatorFactory defaultValidatorFactory;

	@Resource
	public Validator defaultValidator;

	public void assertDefaultValidatorFactoryGetsInjected() {
		assertNotNull( defaultValidatorFactory, "Default validator factory should be injectable." );
		assertTrue(
				defaultValidatorFactory.getMessageInterpolator() instanceof ConstantMessageInterpolator,
				"Injected default validator factory should be configured based on META-INF/validation.xml."
		);

		Set<ConstraintViolation<Foo>> violations = defaultValidatorFactory.getValidator()
				.validate( new Foo() );

		//expecting message from interpolator configured in META-INF/validation.xml
		assertCorrectConstraintViolationMessages( violations, "Invalid constraint" );
	}

	public void assertDefaultValidatorGetsInjected() {
		assertNotNull( defaultValidator, "Default validator should be injectable." );

		Set<ConstraintViolation<Foo>> violations = defaultValidator.validate( new Foo() );

		//expecting message from interpolator configured in META-INF/validation.xml
		assertCorrectConstraintViolationMessages( violations, "Invalid constraint" );
	}
}
