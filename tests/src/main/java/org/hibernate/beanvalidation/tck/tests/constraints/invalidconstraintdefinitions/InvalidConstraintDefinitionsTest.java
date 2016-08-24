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
package org.hibernate.beanvalidation.tck.tests.constraints.invalidconstraintdefinitions;

import java.lang.reflect.Method;
import java.util.Date;
import javax.validation.ConstraintDefinitionException;
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

import static org.testng.Assert.fail;

/**
 * @author Hardy Ferentschik
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class InvalidConstraintDefinitionsTest extends Arquillian {

	private Validator validator;
	private ExecutableValidator executableValidator;

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClassPackage( InvalidConstraintDefinitionsTest.class )
				.build();
	}

	@BeforeMethod
	public void setupValidators() {
		validator = TestUtil.getValidatorUnderTest();
		executableValidator = validator.forExecutables();
	}

	@Test(expectedExceptions = ConstraintDefinitionException.class)
	@SpecAssertions({
			@SpecAssertion(section = "3.1", id = "g"),
			@SpecAssertion(section = "3.1.1", id = "b"),
			@SpecAssertion(section = "9.2", id = "a")
	})
	public void testConstraintDefinitionWithParameterStartingWithValid() {
		validator.validate( new DummyEntityValidProperty() );
		fail( "The used constraint does use an invalid property name. The validation should have failed." );
	}

	@Test(expectedExceptions = ConstraintDefinitionException.class)
	@SpecAssertions({
			@SpecAssertion(section = "3.1", id = "g"),
			@SpecAssertion(section = "3.1.1.1", id = "a"),
			@SpecAssertion(section = "5.3.1", id = "b"),
			@SpecAssertion(section = "5.3.1", id = "c"),
			@SpecAssertion(section = "9.2", id = "a")
	})
	public void testConstraintDefinitionWithoutMessageParameter() {
		validator.validate( new DummyEntityNoMessage() );
		fail( "The used constraint does not define a message parameter. The validation should have failed." );
	}

	@Test(expectedExceptions = ConstraintDefinitionException.class)
	@SpecAssertions({
			@SpecAssertion(section = "3.1", id = "g"),
			@SpecAssertion(section = "3.1.1.2", id = "a"),
			@SpecAssertion(section = "9.2", id = "a")
	})
	public void testConstraintDefinitionWithoutGroupParameter() {
		validator.validate( new DummyEntityNoGroups() );
		fail( "The used constraint does not define a groups parameter. The validation should have failed." );
	}

	@Test(expectedExceptions = ConstraintDefinitionException.class)
	@SpecAssertions({
			@SpecAssertion(section = "3.1", id = "g"),
			@SpecAssertion(section = "3.1.1.3", id = "a"),
			@SpecAssertion(section = "9.2", id = "a")
	})
	public void testConstraintDefinitionWithoutPayloadParameter() {
		validator.validate( new DummyEntityNoGroups() );
		fail( "The used constraint does not define a payload parameter. The validation should have failed." );
	}

	@Test(expectedExceptions = ConstraintDefinitionException.class)
	@SpecAssertions({
			@SpecAssertion(section = "3.1", id = "g"),
			@SpecAssertion(section = "3.1.1.2", id = "c"),
			@SpecAssertion(section = "9.2", id = "a")
	})
	public void testConstraintDefinitionWithWrongDefaultGroupValue() {
		validator.validate( new DummyEntityInvalidDefaultGroup() );
		fail( "The default groups parameter is not the empty array. The validation should have failed." );
	}

	@Test(expectedExceptions = ConstraintDefinitionException.class)
	@SpecAssertions({
			@SpecAssertion(section = "3.1", id = "g"),
			@SpecAssertion(section = "3.1.1.3", id = "b"),
			@SpecAssertion(section = "9.2", id = "a")
	})
	public void testConstraintDefinitionWithWrongDefaultPayloadValue() {
		validator.validate( new DummyEntityInvalidDefaultPayload() );
		fail( "The default payload parameter is not the empty array. The validation should have failed." );
	}

	@Test(expectedExceptions = ConstraintDefinitionException.class)
	@SpecAssertions({
			@SpecAssertion(section = "3.1", id = "g"),
			@SpecAssertion(section = "3.1.1.3", id = "a"),
			@SpecAssertion(section = "3.1.1.3", id = "c"),
			@SpecAssertion(section = "9.2", id = "a")
	})
	public void testConstraintDefinitionWithWrongPayloadClass() {
		validator.validate( new DummyEntityInvalidPayloadClass() );
		fail( "The default payload parameter has to be of type Class<? extends Payload>[]. The validation should have failed." );
	}

	@Test(expectedExceptions = ConstraintDefinitionException.class)
	@SpecAssertions({
			@SpecAssertion(section = "3.1", id = "g"),
			@SpecAssertion(section = "3.1.1.1", id = "a"),
			@SpecAssertion(section = "9.2", id = "a")
	})
	public void testConstraintDefinitionWithWrongMessageType() {
		validator.validate( new DummyEntityInvalidMessageType() );
		fail( "The message parameter has to be of type String. The validation should have failed." );
	}

	@Test(expectedExceptions = ConstraintDefinitionException.class)
	@SpecAssertions({
			@SpecAssertion(section = "3.1", id = "g"),
			@SpecAssertion(section = "3.1.1.2", id = "b"),
			@SpecAssertion(section = "9.2", id = "a")
	})
	public void testConstraintDefinitionWithWrongGroupType() {
		validator.validate( new DummyEntityInvalidGroupsType() );
		fail( "The groups parameter has to be of type Class<?>[]. The validation should have failed." );
	}

	@Test(expectedExceptions = ConstraintDefinitionException.class)
	@SpecAssertion(section = "3.4", id = "g")
	public void testValidatorForCrossParameterConstraintMustValidateObjectOrObjectArray() throws Exception {
		Object object = new CalendarService();
		Method method = CalendarService.class.getMethod( "createEvent", Date.class, Date.class );
		Object[] parameterValues = new Object[2];

		executableValidator.validateParameters( object, method, parameterValues );
		fail( "Validators for cross-parameter constraints must validate the type Object or Object[]. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDefinitionException.class)
	@SpecAssertion(section = "3.1", id = "f")
	public void testCrossParameterConstraintWithSeveralValidatorsCausesException()
			throws Exception {
		Object object = new OnlineCalendarService();
		Method method = OnlineCalendarService.class.getMethod(
				"createEvent",
				Date.class,
				Date.class
		);
		Object[] parameterValues = new Object[2];

		executableValidator.validateParameters( object, method, parameterValues );
		fail( "There must be only one validator for a cross-parameter constraint. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDefinitionException.class)
	@SpecAssertion(section = "3.1", id = "f")
	public void testCrossParameterConstraintWithValidatorForObjectAndObjectArrayCausesException()
			throws Exception {
		Object object = new AdvancedCalendarService();
		Method method = AdvancedCalendarService.class.getMethod(
				"createEvent",
				Date.class,
				Date.class
		);
		Object[] parameterValues = new Object[2];

		executableValidator.validateParameters( object, method, parameterValues );
		fail( "There must be only one validator for a cross-parameter constraint. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDefinitionException.class)
	@SpecAssertions({
			@SpecAssertion(section = "3.1", id = "g"),
			@SpecAssertion(section = "3.1.1.4", id = "a"),
			@SpecAssertion(section = "9.2", id = "a")
	})
	public void testGenericAndCrossParameterConstraintWithoutValidationAppliesToCausesException() {
		validator.validate( new DummyEntityNoValidationAppliesTo() );
		fail( "A constraint which is generic and cross-parameter needs to define a member validationAppliesTo. The validation should have failed." );
	}

	@Test(expectedExceptions = ConstraintDefinitionException.class)
	@SpecAssertions({
			@SpecAssertion(section = "3.1", id = "g"),
			@SpecAssertion(section = "3.1.1.4", id = "a"),
			@SpecAssertion(section = "9.2", id = "a")
	})
	public void testGenericConstraintWithValidationAppliesToCausesException() {
		validator.validate( new DummyEntityWithUnexpectedValidationAppliesTo() );
		fail( "A pure generic constraint must not define a member validationAppliesTo. The validation should have failed." );
	}

	@Test(expectedExceptions = ConstraintDefinitionException.class)
	@SpecAssertions({
			@SpecAssertion(section = "3.1", id = "g"),
			@SpecAssertion(section = "3.1.1.4", id = "a"),
			@SpecAssertion(section = "9.2", id = "a")
	})
	public void testCrossParameterConstraintWithValidationAppliesToCausesException()
			throws Exception {
		Object object = new PaperCalendarService();
		Method method = PaperCalendarService.class.getMethod(
				"createEvent",
				Date.class,
				Date.class
		);
		Object[] parameterValues = new Object[2];

		executableValidator.validateParameters( object, method, parameterValues );
		fail( "A pure cross-parameter constraint must not define a member validationAppliesTo. The validation should have failed." );
	}

	@Test(expectedExceptions = ConstraintDefinitionException.class)
	@SpecAssertions({
			@SpecAssertion(section = "3.1", id = "g"),
			@SpecAssertion(section = "3.1.1.4", id = "b"),
			@SpecAssertion(section = "9.2", id = "a")
	})
	public void testConstraintDefinitionWithWrongValidationAppliesToType() {
		validator.validate( new DummyEntityWithValidationAppliesToOfWrongType() );
		fail( "The validationAppliesTo parameter has to be of type ConstraintTarget. The validation should have failed." );
	}

	@Test(expectedExceptions = ConstraintDefinitionException.class)
	@SpecAssertions({
			@SpecAssertion(section = "3.1", id = "g"),
			@SpecAssertion(section = "3.1.1.4", id = "b"),
			@SpecAssertion(section = "9.2", id = "a")
	})
	public void testConstraintDefinitionWithWrongDefaultValidationAppliesTo() {
		validator.validate( new DummyEntityWithValidationAppliesToWithWrongDefaultValue() );
		fail( "The validationAppliesTo parameter must have a default value of ConstraintTarget.IMPLICIT. The validation should have failed." );
	}

	@InvalidDefaultGroup
	public class DummyEntityInvalidDefaultGroup {
	}

	@NoGroups
	public class DummyEntityNoGroups {
	}

	@NoMessage
	public class DummyEntityNoMessage {
	}

	@ValidInPropertyName
	public class DummyEntityValidProperty {
	}

	@NoPayload
	public class DummyEntityNoPayload {
	}

	@InvalidDefaultPayload
	public class DummyEntityInvalidDefaultPayload {
	}

	@InvalidPayloadClass
	public class DummyEntityInvalidPayloadClass {
	}

	@InvalidMessageType
	public class DummyEntityInvalidMessageType {
	}

	@InvalidGroupsType
	public class DummyEntityInvalidGroupsType {
	}

	@InvalidGenericAndCrossParameterConstraint
	private static class DummyEntityNoValidationAppliesTo {
	}

	@GenericConstraintWithValidationAppliesTo
	private static class DummyEntityWithUnexpectedValidationAppliesTo {
	}

	@GenericAndCrossParameterConstraintWithValidationAppliesToOfWrongType
	private static class DummyEntityWithValidationAppliesToOfWrongType {
	}

	@GenericAndCrossParameterConstraintWithValidationAppliesToWithWrongDefaultValue
	private static class DummyEntityWithValidationAppliesToWithWrongDefaultValue {
	}

	private static class CalendarService {
		@InvalidCrossParameterConstraint
		public void createEvent(Date start, Date end) {
		}
	}

	private static class OnlineCalendarService {
		@ConstraintWithTwoCrossParameterValidators
		public void createEvent(Date start, Date end) {
		}
	}

	private static class AdvancedCalendarService {
		@ConstraintWithObjectAndObjectArrayValidator
		public void createEvent(Date start, Date end) {
		}
	}

	private static class PaperCalendarService {
		@CrossParameterConstraintWithValidationAppliesTo
		public void createEvent(Date start, Date end) {
		}
	}
}
