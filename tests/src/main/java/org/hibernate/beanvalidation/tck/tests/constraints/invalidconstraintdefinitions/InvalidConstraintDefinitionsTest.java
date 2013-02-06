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
import javax.validation.ExecutableValidator;
import javax.validation.Validator;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.util.Groups;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

import static org.testng.Assert.fail;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "1.1.0")
public class InvalidConstraintDefinitionsTest extends Arquillian {

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClassPackage( InvalidConstraintDefinitionsTest.class )
				.build();
	}

	@Test(expectedExceptions = ConstraintDefinitionException.class)
	@SpecAssertions({
			@SpecAssertion(section = "3.1", id = "g"),
			@SpecAssertion(section = "3.1.1", id = "b"),
			@SpecAssertion(section = "9.2", id = "a")
	})
	public void testConstraintDefinitionWithParameterStartingWithValid() {

		Validator validator = TestUtil.getValidatorUnderTest();
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
		Validator validator = TestUtil.getValidatorUnderTest();
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
		Validator validator = TestUtil.getValidatorUnderTest();
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
		Validator validator = TestUtil.getValidatorUnderTest();
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
		Validator validator = TestUtil.getValidatorUnderTest();
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
		Validator validator = TestUtil.getValidatorUnderTest();
		validator.validate( new DummyEntityInvalidDefaultPayload() );
		fail( "The default payload parameter is not the empty array. The validation should have failed." );
	}

	@Test(expectedExceptions = ConstraintDefinitionException.class)
	@SpecAssertions({
			@SpecAssertion(section = "3.1", id = "g"),
			@SpecAssertion(section = "3.1.1.3", id = "c"),
			@SpecAssertion(section = "9.2", id = "a")
	})
	public void testConstraintDefinitionWithWrongPayloadClass() {
		Validator validator = TestUtil.getValidatorUnderTest();
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
		Validator validator = TestUtil.getValidatorUnderTest();
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
		Validator validator = TestUtil.getValidatorUnderTest();
		validator.validate( new DummyEntityInvalidGroupsType() );
		fail( "The groups parameter has to be of type Class<?>[]. The validation should have failed." );
	}

	@Test(expectedExceptions = ConstraintDefinitionException.class, groups = Groups.FAILING_IN_RI)
	//TODO: Adapt to latest spec
	public void testConstraintDefinitionGenericAndCrossParameterConstraint() {
		Validator validator = TestUtil.getValidatorUnderTest();
		validator.validate( new DummyGenericAndCrossParameterConstraint() );
		fail( "A constraint may either be annotated with @Constraint or @CrossParameterConstraint, but not both. The validation should have failed." );
	}

	//TODO: Should we more clearly specify which exception shall be raised? The RI throws a declaration
	//exception as no validator for type Object[] can be found, but one could also specify a definition
	//exception
	@Test(expectedExceptions = Exception.class)
	@SpecAssertion(section = "3.4", id = "e")
	public void testValidatorForCrossParameterConstraintMustValidateObjectArray() throws Exception {
		Object object = new CalendarService();
		Method method = CalendarService.class.getMethod( "createEvent", Date.class, Date.class );
		Object[] parameterValues = new Object[2];

		ExecutableValidator executableValidator = TestUtil.getValidatorUnderTest().forExecutables();

		executableValidator.validateParameters( object, method, parameterValues );
		fail( "Validators for cross-parameter constraints must validate the type Object[]. Expected exception wasn't thrown." );
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

	@GenericAndCrossParameterConstraint
	public class DummyGenericAndCrossParameterConstraint {
	}

	public static class CalendarService {
		@InvalidCrossParameterConstraint
		public void createEvent(Date start, Date end) {
		}
	}
}
