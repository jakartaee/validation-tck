/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.invalidconstraintdefinitions;

import static org.testng.Assert.fail;

import java.lang.reflect.Method;
import java.util.Date;

import javax.validation.ConstraintDefinitionException;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Hardy Ferentschik
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class InvalidConstraintDefinitionsTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( InvalidConstraintDefinitionsTest.class )
				.build();
	}

	@Test(expectedExceptions = ConstraintDefinitionException.class)
    @SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION, id = "g")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES, id = "b")
	@SpecAssertion(section = Sections.EXCEPTION_CONSTRAINTDEFINITION, id = "a")
	public void testConstraintDefinitionWithParameterStartingWithValid() {
		getValidator().validate( new DummyEntityValidProperty() );
		fail( "The used constraint does use an invalid property name. The validation should have failed." );
	}

	@Test(expectedExceptions = ConstraintDefinitionException.class)
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION, id = "g")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES_MESSAGE, id = "a")
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_DEFAULTMESSAGEINTERPOLATION, id = "b")
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_DEFAULTMESSAGEINTERPOLATION, id = "c")
	@SpecAssertion(section = Sections.EXCEPTION_CONSTRAINTDEFINITION, id = "a")
	public void testConstraintDefinitionWithoutMessageParameter() {
		getValidator().validate( new DummyEntityNoMessage() );
		fail( "The used constraint does not define a message parameter. The validation should have failed." );
	}

	@Test(expectedExceptions = ConstraintDefinitionException.class)
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION, id = "g")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES_GROUPS, id = "a")
	@SpecAssertion(section = Sections.EXCEPTION_CONSTRAINTDEFINITION, id = "a")
	public void testConstraintDefinitionWithoutGroupParameter() {
		getValidator().validate( new DummyEntityNoGroups() );
		fail( "The used constraint does not define a groups parameter. The validation should have failed." );
	}

	@Test(expectedExceptions = ConstraintDefinitionException.class)
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION, id = "g")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES_PAYLOAD, id = "a")
	@SpecAssertion(section = Sections.EXCEPTION_CONSTRAINTDEFINITION, id = "a")
	public void testConstraintDefinitionWithoutPayloadParameter() {
		getValidator().validate( new DummyEntityNoPayload() );
		fail( "The used constraint does not define a payload parameter. The validation should have failed." );
	}

	@Test(expectedExceptions = ConstraintDefinitionException.class)
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION, id = "g")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES_GROUPS, id = "c")
	@SpecAssertion(section = Sections.EXCEPTION_CONSTRAINTDEFINITION, id = "a")
	public void testConstraintDefinitionWithWrongDefaultGroupValue() {
		getValidator().validate( new DummyEntityInvalidDefaultGroup() );
		fail( "The default groups parameter is not the empty array. The validation should have failed." );
	}

	@Test(expectedExceptions = ConstraintDefinitionException.class)
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION, id = "g")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES_PAYLOAD, id = "b")
	@SpecAssertion(section = Sections.EXCEPTION_CONSTRAINTDEFINITION, id = "a")
	public void testConstraintDefinitionWithWrongDefaultPayloadValue() {
		getValidator().validate( new DummyEntityInvalidDefaultPayload() );
		fail( "The default payload parameter is not the empty array. The validation should have failed." );
	}

	@Test(expectedExceptions = ConstraintDefinitionException.class)
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION, id = "g")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES_PAYLOAD, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES_PAYLOAD, id = "c")
	@SpecAssertion(section = Sections.EXCEPTION_CONSTRAINTDEFINITION, id = "a")
	public void testConstraintDefinitionWithWrongPayloadClass() {
		getValidator().validate( new DummyEntityInvalidPayloadClass() );
		fail( "The default payload parameter has to be of type Class<? extends Payload>[]. The validation should have failed." );
	}

	@Test(expectedExceptions = ConstraintDefinitionException.class)
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION, id = "g")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES_MESSAGE, id = "a")
	@SpecAssertion(section = Sections.EXCEPTION_CONSTRAINTDEFINITION, id = "a")
	public void testConstraintDefinitionWithWrongMessageType() {
		getValidator().validate( new DummyEntityInvalidMessageType() );
		fail( "The message parameter has to be of type String. The validation should have failed." );
	}

	@Test(expectedExceptions = ConstraintDefinitionException.class)
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION, id = "g")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES_GROUPS, id = "b")
	@SpecAssertion(section = Sections.EXCEPTION_CONSTRAINTDEFINITION, id = "a")
	public void testConstraintDefinitionWithWrongGroupType() {
		getValidator().validate( new DummyEntityInvalidGroupsType() );
		fail( "The groups parameter has to be of type Class<?>[]. The validation should have failed." );
	}

	@Test(expectedExceptions = ConstraintDefinitionException.class)
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "g")
	public void testValidatorForCrossParameterConstraintMustValidateObjectOrObjectArray() throws Exception {
		Object object = new CalendarService();
		Method method = CalendarService.class.getMethod( "createEvent", Date.class, Date.class );
		Object[] parameterValues = new Object[2];

		getExecutableValidator().validateParameters( object, method, parameterValues );
		fail( "Validators for cross-parameter constraints must validate the type Object or Object[]. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDefinitionException.class)
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION, id = "f")
	public void testCrossParameterConstraintWithSeveralValidatorsCausesException()
			throws Exception {
		Object object = new OnlineCalendarService();
		Method method = OnlineCalendarService.class.getMethod(
				"createEvent",
				Date.class,
				Date.class
		);
		Object[] parameterValues = new Object[2];

		getExecutableValidator().validateParameters( object, method, parameterValues );
		fail( "There must be only one validator for a cross-parameter constraint. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDefinitionException.class)
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION, id = "f")
	public void testCrossParameterConstraintWithValidatorForObjectAndObjectArrayCausesException()
			throws Exception {
		Object object = new AdvancedCalendarService();
		Method method = AdvancedCalendarService.class.getMethod(
				"createEvent",
				Date.class,
				Date.class
		);
		Object[] parameterValues = new Object[2];

		getExecutableValidator().validateParameters( object, method, parameterValues );
		fail( "There must be only one validator for a cross-parameter constraint. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDefinitionException.class)
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION, id = "g")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES_VALIDATIONAPPLIESTO, id = "a")
	@SpecAssertion(section = Sections.EXCEPTION_CONSTRAINTDEFINITION, id = "a")
	public void testGenericAndCrossParameterConstraintWithoutValidationAppliesToCausesException() {
		getValidator().validate( new DummyEntityNoValidationAppliesTo() );
		fail( "A constraint which is generic and cross-parameter needs to define a member validationAppliesTo. The validation should have failed." );
	}

	@Test(expectedExceptions = ConstraintDefinitionException.class)
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION, id = "g")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES_VALIDATIONAPPLIESTO, id = "a")
	@SpecAssertion(section = Sections.EXCEPTION_CONSTRAINTDEFINITION, id = "a")
	public void testGenericConstraintWithValidationAppliesToCausesException() {
		getValidator().validate( new DummyEntityWithUnexpectedValidationAppliesTo() );
		fail( "A pure generic constraint must not define a member validationAppliesTo. The validation should have failed." );
	}

	@Test(expectedExceptions = ConstraintDefinitionException.class)
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION, id = "g")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES_VALIDATIONAPPLIESTO, id = "a")
	@SpecAssertion(section = Sections.EXCEPTION_CONSTRAINTDEFINITION, id = "a")
	public void testCrossParameterConstraintWithValidationAppliesToCausesException()
			throws Exception {
		Object object = new PaperCalendarService();
		Method method = PaperCalendarService.class.getMethod(
				"createEvent",
				Date.class,
				Date.class
		);
		Object[] parameterValues = new Object[2];

		getExecutableValidator().validateParameters( object, method, parameterValues );
		fail( "A pure cross-parameter constraint must not define a member validationAppliesTo. The validation should have failed." );
	}

	@Test(expectedExceptions = ConstraintDefinitionException.class)
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION, id = "g")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES_VALIDATIONAPPLIESTO, id = "b")
	@SpecAssertion(section = Sections.EXCEPTION_CONSTRAINTDEFINITION, id = "a")
	public void testConstraintDefinitionWithWrongValidationAppliesToType() {
		getValidator().validate( new DummyEntityWithValidationAppliesToOfWrongType() );
		fail( "The validationAppliesTo parameter has to be of type ConstraintTarget. The validation should have failed." );
	}

	@Test(expectedExceptions = ConstraintDefinitionException.class)
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION, id = "g")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES_VALIDATIONAPPLIESTO, id = "b")
	@SpecAssertion(section = Sections.EXCEPTION_CONSTRAINTDEFINITION, id = "a")
	public void testConstraintDefinitionWithWrongDefaultValidationAppliesTo() {
		getValidator().validate( new DummyEntityWithValidationAppliesToWithWrongDefaultValue() );
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
