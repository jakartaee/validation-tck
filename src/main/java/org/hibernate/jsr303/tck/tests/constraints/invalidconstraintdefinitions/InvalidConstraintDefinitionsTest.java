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
package org.hibernate.jsr303.tck.tests.constraints.invalidconstraintdefinitions;

import javax.validation.ConstraintDefinitionException;
import javax.validation.Validator;

import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.testharness.AbstractTest;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ArtifactType;
import org.jboss.testharness.impl.packaging.Classes;
import static org.testng.Assert.fail;
import org.testng.annotations.Test;

import org.hibernate.jsr303.tck.util.TestUtil;

/**
 * @author Hardy Ferentschik
 */
@Artifact(artifactType = ArtifactType.JSR303)
@Classes({ TestUtil.class, TestUtil.PathImpl.class, TestUtil.NodeImpl.class })
public class InvalidConstraintDefinitionsTest extends AbstractTest {

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "2.1", id = "d"),
			@SpecAssertion(section = "2.1.1", id = "b"),
			@SpecAssertion(section = "8.2", id = "a")
	})
	public void testConstraintDefinitionWithParameterStartingWithValid() {
		try {
			Validator validator = TestUtil.getValidatorUnderTest();
			validator.validate( new DummyEntityValidProperty() );
			fail( "The used constraint does use an invalid property name. The validation should have failed." );
		}
		catch ( ConstraintDefinitionException e ) {
			// success
		}
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "2.1", id = "c"),
			@SpecAssertion(section = "2.1.1", id = "c"),
			@SpecAssertion(section = "4.3.1", id = "b"),
			@SpecAssertion(section = "4.3.1", id = "c"),
			@SpecAssertion(section = "8.2", id = "a")
	})
	public void testConstraintDefinitionWithoutMessageParameter() {
		try {
			Validator validator = TestUtil.getValidatorUnderTest();
			validator.validate( new DummyEntityNoMessage() );
			fail( "The used constraint does not define a message parameter. The validation should have failed." );
		}
		catch ( ConstraintDefinitionException e ) {
			// success
		}
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "2.1", id = "c"),
			@SpecAssertion(section = "2.1.1", id = "d"),
			@SpecAssertion(section = "8.2", id = "a")
	})
	public void testConstraintDefinitionWithoutGroupParameter() {
		try {
			Validator validator = TestUtil.getValidatorUnderTest();
			validator.validate( new DummyEntityNoGroups() );
			fail( "The used constraint does not define a groups parameter. The validation should have failed." );
		}
		catch ( ConstraintDefinitionException e ) {
			// success
		}
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "2.1", id = "c"),
			@SpecAssertion(section = "2.1.1", id = "h"),
			@SpecAssertion(section = "8.2", id = "a")
	})
	public void testConstraintDefinitionWithoutPayloadParameter() {
		try {
			Validator validator = TestUtil.getValidatorUnderTest();
			validator.validate( new DummyEntityNoGroups() );
			fail( "The used constraint does not define a payload parameter. The validation should have failed." );
		}
		catch ( ConstraintDefinitionException e ) {
			// success
		}
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "2.1", id = "c"),
			@SpecAssertion(section = "2.1.1", id = "e"),
			@SpecAssertion(section = "8.2", id = "a")
	})
	public void testConstraintDefinitionWithWrongDefaultGroupValue() {
		try {
			Validator validator = TestUtil.getValidatorUnderTest();
			validator.validate( new DummyEntityInvalidDefaultGroup() );
			fail( "The default groups parameter is not the empty array. The validation should have failed." );
		}
		catch ( ConstraintDefinitionException e ) {
			// success
		}
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "2.1", id = "c"),
			@SpecAssertion(section = "2.1.1", id = "i"),
			@SpecAssertion(section = "8.2", id = "a")
	})
	public void testConstraintDefinitionWithWrongDefaultPayloadValue() {
		try {
			Validator validator = TestUtil.getValidatorUnderTest();
			validator.validate( new DummyEntityInvalidDefaultPayload() );
			fail( "The default payload parameter is not the empty array. The validation should have failed." );
		}
		catch ( ConstraintDefinitionException e ) {
			// success
		}
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "2.1", id = "c"),
			@SpecAssertion(section = "2.1.1", id = "j"),
			@SpecAssertion(section = "8.2", id = "a")
	})
	public void testConstraintDefinitionWithWrongPayloadClass() {
		try {
			Validator validator = TestUtil.getValidatorUnderTest();
			validator.validate( new DummyEntityInvalidPayloadClass() );
			fail( "The default payload parameter has to be of type Class<? extends Payload>[]. The validation should have failed." );
		}
		catch ( ConstraintDefinitionException e ) {
			// success
		}
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "2.1", id = "c"),
			@SpecAssertion(section = "2.1.1", id = "c"),
			@SpecAssertion(section = "8.2", id = "a")
	})
	public void testConstraintDefinitionWithWrongMessageType() {
		try {
			Validator validator = TestUtil.getValidatorUnderTest();
			validator.validate( new DummyEntityInvalidMessageType() );
			fail( "The message parameter has to be of type String. The validation should have failed." );
		}
		catch ( ConstraintDefinitionException e ) {
			// success
		}
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "2.1", id = "c"),
			@SpecAssertion(section = "2.1.1", id = "g"),
			@SpecAssertion(section = "8.2", id = "a")
	})
	public void testConstraintDefinitionWithWrongGroupType() {
		try {
			Validator validator = TestUtil.getValidatorUnderTest();
			validator.validate( new DummyEntityInvalidGroupsType() );
			fail( "The groups parameter has to be of type Class<?>[]. The validation should have failed." );
		}
		catch ( ConstraintDefinitionException e ) {
			// success
		}
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
}
