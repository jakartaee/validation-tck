/*
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.invalidconstraintdefinitions;

import java.lang.reflect.Method;
import java.util.Date;

import jakarta.validation.ConstraintDefinitionException;

import org.assertj.core.api.Assertions;
import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.junit.jupiter.api.Test;

/**
 * @author Hardy Ferentschik
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
public class InvalidConstraintDefinitionsTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( InvalidConstraintDefinitionsTest.class )
				.build();
	}

	@Test
    @SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION, id = "g")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES, id = "b")
	@SpecAssertion(section = Sections.EXCEPTION_CONSTRAINTDEFINITION, id = "a")
	public void testConstraintDefinitionWithParameterStartingWithValid() {
		Assertions.assertThatThrownBy( () -> {

			getValidator().validate( new DummyEntityValidProperty() );
	
		} ).isInstanceOf( ConstraintDefinitionException.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION, id = "g")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES_MESSAGE, id = "a")
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_DEFAULTMESSAGEINTERPOLATION, id = "b")
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_DEFAULTMESSAGEINTERPOLATION, id = "c")
	@SpecAssertion(section = Sections.EXCEPTION_CONSTRAINTDEFINITION, id = "a")
	public void testConstraintDefinitionWithoutMessageParameter() {
		Assertions.assertThatThrownBy( () -> {

			getValidator().validate( new DummyEntityNoMessage() );
	
		} ).isInstanceOf( ConstraintDefinitionException.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION, id = "g")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES_GROUPS, id = "a")
	@SpecAssertion(section = Sections.EXCEPTION_CONSTRAINTDEFINITION, id = "a")
	public void testConstraintDefinitionWithoutGroupParameter() {
		Assertions.assertThatThrownBy( () -> {

			getValidator().validate( new DummyEntityNoGroups() );
	
		} ).isInstanceOf( ConstraintDefinitionException.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION, id = "g")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES_PAYLOAD, id = "a")
	@SpecAssertion(section = Sections.EXCEPTION_CONSTRAINTDEFINITION, id = "a")
	public void testConstraintDefinitionWithoutPayloadParameter() {
		Assertions.assertThatThrownBy( () -> {

			getValidator().validate( new DummyEntityNoPayload() );
	
		} ).isInstanceOf( ConstraintDefinitionException.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION, id = "g")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES_GROUPS, id = "c")
	@SpecAssertion(section = Sections.EXCEPTION_CONSTRAINTDEFINITION, id = "a")
	public void testConstraintDefinitionWithWrongDefaultGroupValue() {
		Assertions.assertThatThrownBy( () -> {

			getValidator().validate( new DummyEntityInvalidDefaultGroup() );
	
		} ).isInstanceOf( ConstraintDefinitionException.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION, id = "g")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES_PAYLOAD, id = "b")
	@SpecAssertion(section = Sections.EXCEPTION_CONSTRAINTDEFINITION, id = "a")
	public void testConstraintDefinitionWithWrongDefaultPayloadValue() {
		Assertions.assertThatThrownBy( () -> {

			getValidator().validate( new DummyEntityInvalidDefaultPayload() );
	
		} ).isInstanceOf( ConstraintDefinitionException.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION, id = "g")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES_PAYLOAD, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES_PAYLOAD, id = "c")
	@SpecAssertion(section = Sections.EXCEPTION_CONSTRAINTDEFINITION, id = "a")
	public void testConstraintDefinitionWithWrongPayloadClass() {
		Assertions.assertThatThrownBy( () -> {

			getValidator().validate( new DummyEntityInvalidPayloadClass() );
	
		} ).isInstanceOf( ConstraintDefinitionException.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION, id = "g")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES_MESSAGE, id = "a")
	@SpecAssertion(section = Sections.EXCEPTION_CONSTRAINTDEFINITION, id = "a")
	public void testConstraintDefinitionWithWrongMessageType() {
		Assertions.assertThatThrownBy( () -> {

			getValidator().validate( new DummyEntityInvalidMessageType() );
	
		} ).isInstanceOf( ConstraintDefinitionException.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION, id = "g")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES_GROUPS, id = "b")
	@SpecAssertion(section = Sections.EXCEPTION_CONSTRAINTDEFINITION, id = "a")
	public void testConstraintDefinitionWithWrongGroupType() {
		Assertions.assertThatThrownBy( () -> {

			getValidator().validate( new DummyEntityInvalidGroupsType() );
	
		} ).isInstanceOf( ConstraintDefinitionException.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "g")
	public void testValidatorForCrossParameterConstraintMustValidateObjectOrObjectArray() throws Exception {
		Assertions.assertThatThrownBy( () -> {

			Object object = new CalendarService();
			Method method = CalendarService.class.getMethod( "createEvent", Date.class, Date.class );
			Object[] parameterValues = new Object[2];

			getExecutableValidator().validateParameters( object, method, parameterValues );
	
		} ).isInstanceOf( ConstraintDefinitionException.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION, id = "f")
	public void testCrossParameterConstraintWithSeveralValidatorsCausesException()
			throws Exception {
		Assertions.assertThatThrownBy( () -> {

			Object object = new OnlineCalendarService();
			Method method = OnlineCalendarService.class.getMethod(
					"createEvent",
					Date.class,
					Date.class
			);
			Object[] parameterValues = new Object[2];

			getExecutableValidator().validateParameters( object, method, parameterValues );
	
		} ).isInstanceOf( ConstraintDefinitionException.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION, id = "f")
	public void testCrossParameterConstraintWithValidatorForObjectAndObjectArrayCausesException()
			throws Exception {
		Assertions.assertThatThrownBy( () -> {

			Object object = new AdvancedCalendarService();
			Method method = AdvancedCalendarService.class.getMethod(
					"createEvent",
					Date.class,
					Date.class
			);
			Object[] parameterValues = new Object[2];

			getExecutableValidator().validateParameters( object, method, parameterValues );
	
		} ).isInstanceOf( ConstraintDefinitionException.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION, id = "g")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES_VALIDATIONAPPLIESTO, id = "a")
	@SpecAssertion(section = Sections.EXCEPTION_CONSTRAINTDEFINITION, id = "a")
	public void testGenericAndCrossParameterConstraintWithoutValidationAppliesToCausesException() {
		Assertions.assertThatThrownBy( () -> {

			getValidator().validate( new DummyEntityNoValidationAppliesTo() );
	
		} ).isInstanceOf( ConstraintDefinitionException.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION, id = "g")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES_VALIDATIONAPPLIESTO, id = "a")
	@SpecAssertion(section = Sections.EXCEPTION_CONSTRAINTDEFINITION, id = "a")
	public void testGenericConstraintWithValidationAppliesToCausesException() {
		Assertions.assertThatThrownBy( () -> {

			getValidator().validate( new DummyEntityWithUnexpectedValidationAppliesTo() );
	
		} ).isInstanceOf( ConstraintDefinitionException.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION, id = "g")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES_VALIDATIONAPPLIESTO, id = "a")
	@SpecAssertion(section = Sections.EXCEPTION_CONSTRAINTDEFINITION, id = "a")
	public void testCrossParameterConstraintWithValidationAppliesToCausesException()
			throws Exception {
		Assertions.assertThatThrownBy( () -> {

			Object object = new PaperCalendarService();
			Method method = PaperCalendarService.class.getMethod(
					"createEvent",
					Date.class,
					Date.class
			);
			Object[] parameterValues = new Object[2];

			getExecutableValidator().validateParameters( object, method, parameterValues );
	
		} ).isInstanceOf( ConstraintDefinitionException.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION, id = "g")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES_VALIDATIONAPPLIESTO, id = "b")
	@SpecAssertion(section = Sections.EXCEPTION_CONSTRAINTDEFINITION, id = "a")
	public void testConstraintDefinitionWithWrongValidationAppliesToType() {
		Assertions.assertThatThrownBy( () -> {

			getValidator().validate( new DummyEntityWithValidationAppliesToOfWrongType() );
	
		} ).isInstanceOf( ConstraintDefinitionException.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION, id = "g")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES_VALIDATIONAPPLIESTO, id = "b")
	@SpecAssertion(section = Sections.EXCEPTION_CONSTRAINTDEFINITION, id = "a")
	public void testConstraintDefinitionWithWrongDefaultValidationAppliesTo() {
		Assertions.assertThatThrownBy( () -> {

			getValidator().validate( new DummyEntityWithValidationAppliesToWithWrongDefaultValue() );
	
		} ).isInstanceOf( ConstraintDefinitionException.class );
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
