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
package org.hibernate.beanvalidation.tck.tests.constraints.validatorresolution;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Set;
import javax.validation.ConstraintDefinitionException;
import javax.validation.ConstraintTarget;
import javax.validation.ConstraintViolation;
import javax.validation.UnexpectedTypeException;
import javax.validation.Validator;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

import static org.hibernate.beanvalidation.tck.util.TestUtil.assertConstraintViolation;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectConstraintTypes;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectConstraintViolationMessages;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectNumberOfViolations;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectPropertyPaths;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

/**
 * Tests for constraint validator resolution.
 *
 * @author Hardy Ferentschik
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "1.1.0")
public class ValidatorResolutionTest {

	private Validator validator;

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClassPackage( ValidatorResolutionTest.class )
				.build();
	}

	@BeforeMethod
	public void setupValidator() {
		validator = TestUtil.getValidatorUnderTest();
	}

	@Test
	@SpecAssertion(section = "4.6.4", id = "b")
	public void testTargetTypeIsInterface() {
		assertEquals(
				CustomConstraint.ValidatorForCustomInterface.callCounter,
				0,
				"The validate method of ValidatorForCustomInterface should not have been called yet."
		);

		validator.validate( new CustomInterfaceImpl() );

		assertTrue(
				CustomConstraint.ValidatorForCustomInterface.callCounter > 0,
				"The validate method of ValidatorForCustomInterface should have been called."
		);
	}

	@Test
	@SpecAssertion(section = "4.6.4", id = "b")
	public void testTargetTypeIsClass() {
		assertEquals(
				CustomConstraint.ValidatorForCustomClass.callCounter,
				0,
				"The validate method of ValidatorForCustomClass should not have been called yet."
		);

		validator.validate( new CustomClass() );

		assertTrue(
				CustomConstraint.ValidatorForCustomClass.callCounter > 0,
				"The validate method of ValidatorForCustomClass should have been called."
		);
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "4.6.4", id = "c"),
			@SpecAssertion(section = "4.6.4", id = "j")
	})
	public void testTargetedTypeIsField() {
		assertEquals(
				CustomConstraint.ValidatorForSubClassA.callCounter,
				0,
				"The validate method of ValidatorForSubClassA should not have been called yet."
		);

		validator.validate( new SubClassAHolder( new SubClassA() ) );

		assertTrue(
				CustomConstraint.ValidatorForSubClassA.callCounter > 0,
				"The validate method of ValidatorForSubClassA should have been called."
		);
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "4.6.4", id = "d"),
			@SpecAssertion(section = "4.6.4", id = "j")
	})
	public void testTargetedTypeIsGetter() {
		assertEquals(
				CustomConstraint.ValidatorForSubClassB.callCounter,
				0,
				"The validate method of ValidatorForSubClassB should not have been called yet."
		);

		validator.validate( new SubClassBHolder( new SubClassB() ) );

		assertTrue(
				CustomConstraint.ValidatorForSubClassB.callCounter > 0,
				"The validate method of ValidatorForSubClassB should have been called."
		);
	}

	@Test
	@SpecAssertion(section = "4.6.4", id = "j")
	public void testClassLevelValidatorForSubTypeHasPredenceOverValidatorForSuperClass() {
		assertEquals(
				CustomConstraint.ValidatorForAnotherSubClass.callCounter,
				0,
				"The validate method of ValidatorForAnotherSubClass should not have been called yet."
		);

		validator.validate( new AnotherSubClass() );

		assertTrue(
				CustomConstraint.ValidatorForAnotherSubClass.callCounter > 0,
				"The validate method of ValidatorForAnotherSubClass should have been called."
		);
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "4.6.4", id = "e")
	})
	public void testResolutionOfMultipleSizeValidators() {
		Suburb suburb = new Suburb();

		// all values are null and should pass
		Set<ConstraintViolation<Suburb>> constraintViolations = validator.validate( suburb );
		assertCorrectNumberOfViolations( constraintViolations, 0 );

		suburb.setName( "" );
		constraintViolations = validator.validate( suburb );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertConstraintViolation(
				constraintViolations.iterator().next(), Suburb.class, "", "name"
		);

		suburb.setName( "Hoegsbo" );
		constraintViolations = validator.validate( suburb );
		assertCorrectNumberOfViolations( constraintViolations, 0 );

		suburb.addFacility( Suburb.Facility.SHOPPING_MALL, false );
		constraintViolations = validator.validate( suburb );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertConstraintViolation(
				constraintViolations.iterator().next(),
				Suburb.class,
				suburb.getFacilities(),
				"facilities"
		);

		suburb.addFacility( Suburb.Facility.BUS_TERMINAL, true );
		constraintViolations = validator.validate( suburb );
		assertCorrectNumberOfViolations( constraintViolations, 0 );

		suburb.addStreetName( "Sikelsgatan" );
		constraintViolations = validator.validate( suburb );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertConstraintViolation(
				constraintViolations.iterator().next(),
				Suburb.class,
				suburb.getStreetNames(),
				"streetNames"
		);

		suburb.addStreetName( "Marklandsgatan" );
		constraintViolations = validator.validate( suburb );
		assertCorrectNumberOfViolations( constraintViolations, 0 );

		Coordinate[] boundingBox = new Coordinate[3];
		boundingBox[0] = new Coordinate( 0l, 0l );
		boundingBox[1] = new Coordinate( 0l, 1l );
		boundingBox[2] = new Coordinate( 1l, 0l );
		suburb.setBoundingBox( boundingBox );
		constraintViolations = validator.validate( suburb );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertConstraintViolation(
				constraintViolations.iterator().next(),
				Suburb.class,
				suburb.getBoundingBox(),
				"boundingBox"
		);

		boundingBox = new Coordinate[4];
		boundingBox[0] = new Coordinate( 0l, 0l );
		boundingBox[1] = new Coordinate( 0l, 1l );
		boundingBox[2] = new Coordinate( 1l, 0l );
		boundingBox[3] = new Coordinate( 1l, 1l );
		suburb.setBoundingBox( boundingBox );
		constraintViolations = validator.validate( suburb );
		assertCorrectNumberOfViolations( constraintViolations, 0 );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "4.6.4", id = "e")
	})
	public void testResolutionOfMinMaxForDifferentTypes() {
		MinMax minMax = new MinMax( "5", 5 );
		Set<ConstraintViolation<MinMax>> constraintViolations = validator.validate( minMax );
		assertCorrectNumberOfViolations( constraintViolations, 2 );
		assertCorrectPropertyPaths( constraintViolations, "number", "numberAsString" );
	}

	@Test(expectedExceptions = UnexpectedTypeException.class)
	@SpecAssertions({
			@SpecAssertion(section = "4.6.4", id = "h"),
			@SpecAssertion(section = "3.1", id = "e"),
			@SpecAssertion(section = "3.4", id = "l")
	})
	public void testUnexpectedTypeInValidatorResolution() {
		Bar bar = new Bar();
		validator.validate( bar );
	}

	@Test(expectedExceptions = UnexpectedTypeException.class)
	@SpecAssertions({
			@SpecAssertion(section = "4.6.4", id = "k"),
			@SpecAssertion(section = "9.3", id = "b")
	})
	public void testAmbiguousValidatorResolution() {
		Foo foo = new Foo( new SerializableBarSubclass() );
		validator.validate( foo );
		fail( "The test should have failed due to ambiguous validator resolution." );
	}

	@Test
	@SpecAssertion(section = "4.6.4", id = "g")
	public void testValidatorForWrapperTypeIsAppliedForPrimitiveType() {
		PrimitiveHolder primitiveHolder = new PrimitiveHolder();
		Set<ConstraintViolation<PrimitiveHolder>> violations = validator.validate( primitiveHolder );

		assertCorrectNumberOfViolations( violations, 2 );
		assertCorrectConstraintTypes( violations, ValidInteger.class, ValidLong.class );
	}

	@Test(expectedExceptions = ConstraintDefinitionException.class)
	@SpecAssertion(section = "4.6.4", id = "a")
	public void testSeveralCrossParameterValidatorsCauseConstraintDefinitionException() throws Exception {
		Object object = new CalendarService();
		Method method = CalendarService.class.getMethod( "createEvent", Date.class, Date.class );
		Object[] parameterValues = new Object[2];

		validator.forExecutables().validateParameters( object, method, parameterValues );
	}

	@Test(expectedExceptions = ConstraintDefinitionException.class)
	@SpecAssertion(section = "4.6.4", id = "a")
	public void testCrossParameterConstraintWithoutValidatorCausesConstraintDefinitionException() throws Exception {
		Object object = new OnlineCalendarService();
		Method method = OnlineCalendarService.class.getMethod( "createEvent", Date.class, Date.class );
		Object[] parameterValues = new Object[2];

		validator.forExecutables().validateParameters( object, method, parameterValues );
	}

	@Test
	@SpecAssertion(section = "4.6.4", id = "a")
	public void testCrossParameterValidatorIsUsedForConstraintImplicitlyTargetingParameters() throws Exception {
		Object object = new OfflineCalendarService();
		Method method = OfflineCalendarService.class.getMethod( "createEvent", Date.class, Date.class );
		Object[] parameterValues = new Object[2];

		Set<ConstraintViolation<Object>> violations = validator.forExecutables()
				.validateParameters( object, method, parameterValues );

		assertCorrectConstraintViolationMessages( violations, "violation created by cross-parameter validator" );
	}

	@Test
	@SpecAssertion(section = "4.6.4", id = "a")
	public void testCrossParameterValidatorIsUsedForConstraintExplicitlyTargetingParameters() throws Exception {
		Object object = new AdvancedCalendarService();
		Method method = AdvancedCalendarService.class.getMethod( "createEvent", Date.class, Date.class );
		Object[] parameterValues = new Object[2];

		Set<ConstraintViolation<Object>> violations = validator.forExecutables()
				.validateParameters( object, method, parameterValues );
		assertCorrectConstraintViolationMessages( violations, "violation created by cross-parameter validator" );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "3.4", id = "g"),
			@SpecAssertion(section = "4.6.4", id = "a")
	})
	public void testCrossParameterValidatorValidatingObjectArray() throws Exception {
		Object object = new YetAnotherCalendarService();
		Method method = YetAnotherCalendarService.class.getMethod( "createEvent", Date.class, Date.class );
		Object[] parameterValues = new Object[2];

		Set<ConstraintViolation<Object>> violations = validator.forExecutables()
				.validateParameters( object, method, parameterValues );
		assertCorrectConstraintViolationMessages( violations, "violation created by validator for Object[]" );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "3.4", id = "g"),
			@SpecAssertion(section = "4.6.4", id = "a")
	})
	public void testCrossParameterValidatorValidatingObject() throws Exception {
		Object object = new EvenYetAnotherCalendarService();
		Method method = EvenYetAnotherCalendarService.class.getMethod( "createEvent", Date.class, Date.class );
		Object[] parameterValues = new Object[2];

		Set<ConstraintViolation<Object>> violations = validator.forExecutables()
				.validateParameters( object, method, parameterValues );
		assertCorrectConstraintViolationMessages( violations, "violation created by validator for Object" );
	}

	@Test
	@SpecAssertion(section = "4.6.4", id = "f")
	public void testGenericValidatorIsUsedForConstraintTargetingMethodReturnValue() throws Exception {
		Object object = new AnotherCalendarService();
		Method method = AnotherCalendarService.class.getMethod( "createEvent", Date.class, Date.class );
		Object returnValue = null;

		Set<ConstraintViolation<Object>> violations = validator.forExecutables()
				.validateReturnValue( object, method, returnValue );
		assertCorrectConstraintViolationMessages( violations, "violation created by generic validator" );
	}

	@Test
	@SpecAssertion(section = "4.6.4", id = "f")
	public void testGenericValidatorIsUsedForConstraintTargetingField() {
		Set<ConstraintViolation<TestBean>> violations = validator.validate( new TestBean() );
		assertCorrectConstraintViolationMessages( violations, "violation created by generic validator" );
	}

	@Test(expectedExceptions = UnexpectedTypeException.class)
	@SpecAssertion(section = "4.6.4", id = "k")
	public void testTwoValidatorsForSameTypeCauseUnexpectedTypeException() {
		validator.validate( new AnotherBean() );
	}

	private static class SubClassAHolder {
		@CustomConstraint
		private final SubClassA subClass;

		SubClassAHolder(SubClassA subClass) {
			this.subClass = subClass;
		}
	}

	private static class SubClassBHolder {
		private final BaseClass baseClass;

		public SubClassBHolder(SubClassB subClass) {
			this.baseClass = subClass;
		}

		@CustomConstraint
		public SubClassB getBaseClass() {
			return (SubClassB) baseClass;
		}
	}

	@CustomConstraint
	static class CustomClass {

	}

	@CustomConstraint
	interface CustomInterface {
	}

	private static class CustomInterfaceImpl implements CustomInterface {

	}

	static class AnotherBaseClass {

	}

	@CustomConstraint
	static class AnotherSubClass extends AnotherBaseClass {

	}

	private static class PrimitiveHolder {

		@ValidInteger
		private int intValue;

		@ValidLong
		private long longValue;
	}

	private static class CalendarService {

		@CrossParameterConstraintWithSeveralValidators
		public void createEvent(Date startDate, Date endDate) {
		}
	}

	private static class OnlineCalendarService {

		@CrossParameterConstraintWithoutValidator(validationAppliesTo = ConstraintTarget.PARAMETERS)
		public void createEvent(Date startDate, Date endDate) {
		}
	}

	private static class OfflineCalendarService {

		@GenericAndCrossParameterConstraint
		public void createEvent(Date startDate, Date endDate) {
		}
	}

	private static class AdvancedCalendarService {

		@GenericAndCrossParameterConstraint(validationAppliesTo = ConstraintTarget.PARAMETERS)
		public Object createEvent(Date startDate, Date endDate) {
			return null;
		}
	}

	private static class AnotherCalendarService {

		@GenericAndCrossParameterConstraint(validationAppliesTo = ConstraintTarget.RETURN_VALUE)
		public Object createEvent(Date startDate, Date endDate) {
			return null;
		}
	}

	private static class YetAnotherCalendarService {

		@CrossParameterConstraintWithObjectArrayValidator
		public void createEvent(Date startDate, Date endDate) {
		}
	}

	private static class EvenYetAnotherCalendarService {

		@CrossParameterConstraintWithObjectValidator
		public void createEvent(Date startDate, Date endDate) {
		}
	}

	private static class TestBean {

		@GenericAndCrossParameterConstraint
		public String foo;
	}

	@ConstraintWithTwoValidatorsForTheSameType
	private static class AnotherBean {
	}
}
