/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.validatorresolution;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertConstraintViolation;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertCorrectConstraintTypes;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertCorrectConstraintViolationMessages;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertNumberOfViolations;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.pathWith;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Set;

import javax.validation.ConstraintDefinitionException;
import javax.validation.ConstraintTarget;
import javax.validation.ConstraintViolation;
import javax.validation.UnexpectedTypeException;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests for constraint validator resolution.
 *
 * @author Hardy Ferentschik
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class ValidatorResolutionTest extends AbstractTCKTest{

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( ValidatorResolutionTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "b")
	public void testTargetTypeIsInterface() {
		assertEquals(
				CustomConstraint.ValidatorForCustomInterface.callCounter,
				0,
				"The validate method of ValidatorForCustomInterface should not have been called yet."
		);

		getValidator().validate( new CustomInterfaceImpl() );

		assertTrue(
				CustomConstraint.ValidatorForCustomInterface.callCounter > 0,
				"The validate method of ValidatorForCustomInterface should have been called."
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "b")
	public void testTargetTypeIsClass() {
		assertEquals(
				CustomConstraint.ValidatorForCustomClass.callCounter,
				0,
				"The validate method of ValidatorForCustomClass should not have been called yet."
		);

		getValidator().validate( new CustomClass() );

		assertTrue(
				CustomConstraint.ValidatorForCustomClass.callCounter > 0,
				"The validate method of ValidatorForCustomClass should have been called."
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "c")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "m")
	public void testTargetedTypeIsField() {
		assertEquals(
				CustomConstraint.ValidatorForSubClassA.callCounter,
				0,
				"The validate method of ValidatorForSubClassA should not have been called yet."
		);

		getValidator().validate( new SubClassAHolder( new SubClassA() ) );

		assertTrue(
				CustomConstraint.ValidatorForSubClassA.callCounter > 0,
				"The validate method of ValidatorForSubClassA should have been called."
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "d")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "m")
	public void testTargetedTypeIsGetter() {
		assertEquals(
				CustomConstraint.ValidatorForSubClassB.callCounter,
				0,
				"The validate method of ValidatorForSubClassB should not have been called yet."
		);

		getValidator().validate( new SubClassBHolder( new SubClassB() ) );

		assertTrue(
				CustomConstraint.ValidatorForSubClassB.callCounter > 0,
				"The validate method of ValidatorForSubClassB should have been called."
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "d")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "m")
	public void testTargetedTypeIsMethod() throws NoSuchMethodException, SecurityException {
		assertEquals(
				CustomConstraint.ValidatorForSubClassD.callCounter,
				0,
				"The validate method of ValidatorForSubClassD should not have been called yet."
		);

		getExecutableValidator().validateReturnValue( new SubClassDService(), SubClassDService.class.getMethod( "retrieveSubClassD" ), new SubClassD() );

		assertTrue(
				CustomConstraint.ValidatorForSubClassD.callCounter > 0,
				"The validate method of ValidatorForSubClassD should have been called."
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "d")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "m")
	public void testTargetedTypeIsConstructor() throws NoSuchMethodException, SecurityException {
		assertEquals(
				CustomConstraint.ValidatorForSubClassC.callCounter,
				0,
				"The validate method of ValidatorForSubClassC should not have been called yet."
		);

		getExecutableValidator().validateConstructorReturnValue( SubClassC.class.getConstructor(), new SubClassC() );

		assertTrue(
				CustomConstraint.ValidatorForSubClassC.callCounter > 0,
				"The validate method of ValidatorForSubClassC should have been called."
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "m")
	public void testClassLevelValidatorForSubTypeHasPredenceOverValidatorForSuperClass() {
		assertEquals(
				CustomConstraint.ValidatorForAnotherSubClass.callCounter,
				0,
				"The validate method of ValidatorForAnotherSubClass should not have been called yet."
		);

		getValidator().validate( new AnotherSubClass() );

		assertTrue(
				CustomConstraint.ValidatorForAnotherSubClass.callCounter > 0,
				"The validate method of ValidatorForAnotherSubClass should have been called."
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "i")
	public void testResolutionOfMultipleSizeValidators() {
		Suburb suburb = new Suburb();

		// all values are null and should pass
		Set<ConstraintViolation<Suburb>> constraintViolations = getValidator().validate( suburb );
		assertNumberOfViolations( constraintViolations, 0 );

		suburb.setName( "" );
		constraintViolations = getValidator().validate( suburb );
		assertNumberOfViolations( constraintViolations, 1 );
		assertConstraintViolation(
				constraintViolations.iterator().next(), Suburb.class, "", pathWith().property( "name" )
		);

		suburb.setName( "Hoegsbo" );
		constraintViolations = getValidator().validate( suburb );
		assertNumberOfViolations( constraintViolations, 0 );

		suburb.addFacility( Suburb.Facility.SHOPPING_MALL, false );
		constraintViolations = getValidator().validate( suburb );
		assertNumberOfViolations( constraintViolations, 1 );
		assertConstraintViolation(
				constraintViolations.iterator().next(),
				Suburb.class,
				suburb.getFacilities(),
				pathWith().property( "facilities" )
		);

		suburb.addFacility( Suburb.Facility.BUS_TERMINAL, true );
		constraintViolations = getValidator().validate( suburb );
		assertNumberOfViolations( constraintViolations, 0 );

		suburb.addStreetName( "Sikelsgatan" );
		constraintViolations = getValidator().validate( suburb );
		assertNumberOfViolations( constraintViolations, 1 );
		assertConstraintViolation(
				constraintViolations.iterator().next(),
				Suburb.class,
				suburb.getStreetNames(),
				pathWith().property( "streetNames" )
		);

		suburb.addStreetName( "Marklandsgatan" );
		constraintViolations = getValidator().validate( suburb );
		assertNumberOfViolations( constraintViolations, 0 );

		Coordinate[] boundingBox = new Coordinate[3];
		boundingBox[0] = new Coordinate( 0l, 0l );
		boundingBox[1] = new Coordinate( 0l, 1l );
		boundingBox[2] = new Coordinate( 1l, 0l );
		suburb.setBoundingBox( boundingBox );
		constraintViolations = getValidator().validate( suburb );
		assertNumberOfViolations( constraintViolations, 1 );
		assertConstraintViolation(
				constraintViolations.iterator().next(),
				Suburb.class,
				suburb.getBoundingBox(),
				pathWith().property( "boundingBox" )
		);

		boundingBox = new Coordinate[4];
		boundingBox[0] = new Coordinate( 0l, 0l );
		boundingBox[1] = new Coordinate( 0l, 1l );
		boundingBox[2] = new Coordinate( 1l, 0l );
		boundingBox[3] = new Coordinate( 1l, 1l );
		suburb.setBoundingBox( boundingBox );
		constraintViolations = getValidator().validate( suburb );
		assertNumberOfViolations( constraintViolations, 0 );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "i")
	public void testResolutionOfMinMaxForDifferentTypes() {
		MinMax minMax = new MinMax( "5", 5 );
		Set<ConstraintViolation<MinMax>> constraintViolations = getValidator().validate( minMax );
		assertNumberOfViolations( constraintViolations, 2 );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "number" ),
				pathWith()
						.property( "numberAsString" )
		);
	}

	@Test(expectedExceptions = UnexpectedTypeException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "l")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION, id = "e")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "m")
	public void testUnexpectedTypeInValidatorResolution() {
		Bar bar = new Bar();
		getValidator().validate( bar );
	}

	@Test(expectedExceptions = UnexpectedTypeException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "n")
	@SpecAssertion(section = Sections.EXCEPTION_CONSTRAINTDECLARATION, id = "b")
	public void testAmbiguousValidatorResolution() {
		Foo foo = new Foo( new SerializableBarSubclass() );
		getValidator().validate( foo );
		fail( "The test should have failed due to ambiguous validator resolution." );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "k")
	public void testValidatorForWrapperTypeIsAppliedForPrimitiveType() {
		PrimitiveHolder primitiveHolder = new PrimitiveHolder();
		Set<ConstraintViolation<PrimitiveHolder>> violations = getValidator().validate( primitiveHolder );

		assertNumberOfViolations( violations, 2 );
		assertCorrectConstraintTypes( violations, ValidInteger.class, ValidLong.class );
	}

	@Test(expectedExceptions = ConstraintDefinitionException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "a")
	public void testSeveralCrossParameterValidatorsCauseConstraintDefinitionException() throws Exception {
		Object object = new CalendarService();
		Method method = CalendarService.class.getMethod( "createEvent", Date.class, Date.class );
		Object[] parameterValues = new Object[2];

		getExecutableValidator().validateParameters( object, method, parameterValues );
	}

	@Test(expectedExceptions = ConstraintDefinitionException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "a")
	public void testCrossParameterConstraintWithoutValidatorCausesConstraintDefinitionException() throws Exception {
		Object object = new OnlineCalendarService();
		Method method = OnlineCalendarService.class.getMethod( "createEvent", Date.class, Date.class );
		Object[] parameterValues = new Object[2];

		getExecutableValidator().validateParameters( object, method, parameterValues );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "a")
	public void testCrossParameterValidatorIsUsedForConstraintImplicitlyTargetingParameters() throws Exception {
		Object object = new OfflineCalendarService();
		Method method = OfflineCalendarService.class.getMethod( "createEvent", Date.class, Date.class );
		Object[] parameterValues = new Object[2];

		Set<ConstraintViolation<Object>> violations = getExecutableValidator()
				.validateParameters( object, method, parameterValues );

		assertCorrectConstraintViolationMessages( violations, "violation created by cross-parameter validator" );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "a")
	public void testCrossParameterValidatorIsUsedForConstraintExplicitlyTargetingParameters() throws Exception {
		Object object = new AdvancedCalendarService();
		Method method = AdvancedCalendarService.class.getMethod( "createEvent", Date.class, Date.class );
		Object[] parameterValues = new Object[2];

		Set<ConstraintViolation<Object>> violations = getExecutableValidator()
				.validateParameters( object, method, parameterValues );
		assertCorrectConstraintViolationMessages( violations, "violation created by cross-parameter validator" );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "g")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "a")
	public void testCrossParameterValidatorValidatingObjectArray() throws Exception {
		Object object = new YetAnotherCalendarService();
		Method method = YetAnotherCalendarService.class.getMethod( "createEvent", Date.class, Date.class );
		Object[] parameterValues = new Object[2];

		Set<ConstraintViolation<Object>> violations = getExecutableValidator()
				.validateParameters( object, method, parameterValues );
		assertCorrectConstraintViolationMessages( violations, "violation created by validator for Object[]" );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "g")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "a")
	public void testCrossParameterValidatorValidatingObject() throws Exception {
		Object object = new EvenYetAnotherCalendarService();
		Method method = EvenYetAnotherCalendarService.class.getMethod( "createEvent", Date.class, Date.class );
		Object[] parameterValues = new Object[2];

		Set<ConstraintViolation<Object>> violations = getExecutableValidator()
				.validateParameters( object, method, parameterValues );
		assertCorrectConstraintViolationMessages( violations, "violation created by validator for Object" );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "j")
	public void testGenericValidatorIsUsedForConstraintTargetingMethodReturnValue() throws Exception {
		Object object = new AnotherCalendarService();
		Method method = AnotherCalendarService.class.getMethod( "createEvent", Date.class, Date.class );
		Object returnValue = null;

		Set<ConstraintViolation<Object>> violations = getExecutableValidator()
				.validateReturnValue( object, method, returnValue );
		assertCorrectConstraintViolationMessages( violations, "violation created by generic validator" );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "j")
	public void testGenericValidatorIsUsedForConstraintTargetingField() {
		Set<ConstraintViolation<TestBean>> violations = getValidator().validate( new TestBean() );
		assertCorrectConstraintViolationMessages( violations, "violation created by generic validator" );
	}

	@Test(expectedExceptions = UnexpectedTypeException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "n")
	public void testTwoValidatorsForSameTypeCauseUnexpectedTypeException() {
		getValidator().validate( new AnotherBean() );
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

	private static class SubClassDService {

		@CustomConstraint
		public SubClassD retrieveSubClassD() {
			return null;
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
