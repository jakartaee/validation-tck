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
package org.hibernate.beanvalidation.tck.tests.constraints.crossparameter;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Set;
import javax.validation.ConstraintTarget;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectConstraintTypes;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectNumberOfViolations;
import static org.testng.Assert.assertEquals;

/**
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class GenericAndCrossParameterConstraintTest extends Arquillian {

	private Validator validator;
	private ExecutableValidator executableValidator;

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClassPackage( GenericAndCrossParameterConstraintTest.class )
				.build();
	}

	@BeforeMethod
	public void setupValidator() {
		validator = TestUtil.getValidatorUnderTest();
		executableValidator = validator.forExecutables();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "3.4", id = "b"),
			@SpecAssertion(section = "3.4", id = "e")
	})
	public void testAnnotatedElementIsTargetedByDefault() throws Exception {
		Object object = new Calendar();
		Method method = Calendar.class.getMethod( "createEvent", Date.class, Date.class );
		Object[] parameterValues = new Object[2];
		Object returnValue = new Object();

		Set<ConstraintViolation<Object>> violations = executableValidator.validateParameters(
				object,
				method,
				parameterValues
		);
		assertCorrectNumberOfViolations( violations, 0 );

		violations = executableValidator.validateReturnValue( object, method, returnValue );
		assertCorrectNumberOfViolations( violations, 1 );
		assertCorrectConstraintTypes( violations, GenericConstraint.class );
		assertEquals( violations.iterator().next().getInvalidValue(), returnValue );
	}

	@Test
	@SpecAssertion(section = "3.4", id = "e")
	public void testAnnotatedElementIsTargetedUsingSupportedValidationTarget() throws Exception {
		Object object = new WebCalendar();
		Method method = WebCalendar.class.getMethod( "createEvent", Date.class, Date.class );
		Object[] parameterValues = new Object[2];
		Object returnValue = new Object();

		Set<ConstraintViolation<Object>> violations = executableValidator.validateParameters(
				object,
				method,
				parameterValues
		);
		assertCorrectNumberOfViolations( violations, 0 );

		violations = executableValidator.validateReturnValue( object, method, returnValue );
		assertCorrectNumberOfViolations( violations, 1 );
		assertCorrectConstraintTypes( violations, ExplicitGenericConstraint.class );
		assertEquals( violations.iterator().next().getInvalidValue(), returnValue );
	}

	@Test
	@SpecAssertion(section = "3.4", id = "e")
	public void testParametersAreTargetedUsingSupportedValidationTarget() throws Exception {
		Object object = new OnlineCalendar();
		Method method = OnlineCalendar.class.getMethod( "createEvent", Date.class, Date.class );
		Object[] parameterValues = new Object[2];
		Object returnValue = new Object();

		Set<ConstraintViolation<Object>> violations = executableValidator.validateReturnValue(
				object,
				method,
				returnValue
		);
		assertCorrectNumberOfViolations( violations, 0 );

		violations = executableValidator.validateParameters( object, method, parameterValues );
		assertCorrectNumberOfViolations( violations, 1 );
		assertCorrectConstraintTypes( violations, CrossParameterConstraint.class );
		assertEquals( violations.iterator().next().getInvalidValue(), parameterValues );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "3.4", id = "f"),
			@SpecAssertion(section = "4.5.2.1", id = "c"),
			@SpecAssertion(section = "4.5.3", id = "b")
	})
	public void testOneValidatorSupportsBothValidationTargets() throws Exception {
		Object object = new MobileCalendar();
		Method method = MobileCalendar.class.getMethod( "createEvent", Date.class, Date.class );
		Object[] parameterValues = new Object[2];
		Object returnValue = new Object();

		//constraint applies to return value
		Set<ConstraintViolation<Object>> violations = executableValidator.validateParameters(
				object,
				method,
				parameterValues
		);
		assertCorrectNumberOfViolations( violations, 0 );

		violations = executableValidator.validateReturnValue( object, method, returnValue );
		assertCorrectNumberOfViolations( violations, 1 );
		assertCorrectConstraintTypes( violations, GenericAndCrossParameterConstraintWithOneValidator.class );
		assertEquals( violations.iterator().next().getInvalidValue(), returnValue );

		method = MobileCalendar.class.getMethod( "addEvent", Date.class, Date.class );

		//constraint applies to parameters
		violations = executableValidator.validateReturnValue( object, method, returnValue );
		assertCorrectNumberOfViolations( violations, 0 );

		violations = executableValidator.validateParameters( object, method, parameterValues );
		assertCorrectNumberOfViolations( violations, 1 );
		assertCorrectConstraintTypes( violations, GenericAndCrossParameterConstraintWithOneValidator.class );
		assertEquals( violations.iterator().next().getInvalidValue(), parameterValues );
	}

	private static class Calendar {

		@GenericConstraint
		public Object createEvent(Date start, Date end) {
			return null;
		}
	}

	private static class WebCalendar {

		@ExplicitGenericConstraint
		public Object createEvent(Date start, Date end) {
			return null;
		}
	}


	private static class OnlineCalendar {

		@CrossParameterConstraint
		public Object createEvent(Date start, Date end) {
			return null;
		}
	}

	private static class MobileCalendar {

		@GenericAndCrossParameterConstraintWithOneValidator(validationAppliesTo = ConstraintTarget.RETURN_VALUE)
		public Object createEvent(Date start, Date end) {
			return null;
		}

		@GenericAndCrossParameterConstraintWithOneValidator(validationAppliesTo = ConstraintTarget.PARAMETERS)
		public Object addEvent(Date start, Date end) {
			return null;
		}
	}
}
