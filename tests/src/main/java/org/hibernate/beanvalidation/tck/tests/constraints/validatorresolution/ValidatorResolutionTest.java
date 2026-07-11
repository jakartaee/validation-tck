/*
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.validatorresolution;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertNoViolations;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

import jakarta.validation.ConstraintDefinitionException;
import jakarta.validation.ConstraintTarget;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.UnexpectedTypeException;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import jakarta.validation.valueextraction.ExtractedValue;
import jakarta.validation.valueextraction.UnwrapByDefault;
import jakarta.validation.valueextraction.Unwrapping;
import jakarta.validation.valueextraction.ValueExtractor;

import org.assertj.core.api.Assertions;
import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.junit.jupiter.api.Test;

/**
 * Tests for constraint validator resolution.
 *
 * @author Hardy Ferentschik
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
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
		Assertions.assertThat(  CustomConstraint.ValidatorForCustomInterface.callCounter ).as( "The validate method of ValidatorForCustomInterface should not have been called yet." ).isEqualTo( 0 );

		getValidator().validate( new CustomInterfaceImpl() );

		Assertions.assertThat(  CustomConstraint.ValidatorForCustomInterface.callCounter > 0 ).as( "The validate method of ValidatorForCustomInterface should have been called." ).isTrue();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "b")
	public void testTargetTypeIsClass() {
		Assertions.assertThat(  CustomConstraint.ValidatorForCustomClass.callCounter ).as( "The validate method of ValidatorForCustomClass should not have been called yet." ).isEqualTo( 0 );

		getValidator().validate( new CustomClass() );

		Assertions.assertThat(  CustomConstraint.ValidatorForCustomClass.callCounter > 0 ).as( "The validate method of ValidatorForCustomClass should have been called." ).isTrue();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "c")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "m")
	public void testTargetedTypeIsField() {
		Assertions.assertThat(  CustomConstraint.ValidatorForSubClassA.callCounter ).as( "The validate method of ValidatorForSubClassA should not have been called yet." ).isEqualTo( 0 );

		getValidator().validate( new SubClassAHolder( new SubClassA() ) );

		Assertions.assertThat(  CustomConstraint.ValidatorForSubClassA.callCounter > 0 ).as( "The validate method of ValidatorForSubClassA should have been called." ).isTrue();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "d")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "m")
	public void testTargetedTypeIsGetter() {
		Assertions.assertThat(  CustomConstraint.ValidatorForSubClassB.callCounter ).as( "The validate method of ValidatorForSubClassB should not have been called yet." ).isEqualTo( 0 );

		getValidator().validate( new SubClassBHolder( new SubClassB() ) );

		Assertions.assertThat(  CustomConstraint.ValidatorForSubClassB.callCounter > 0 ).as( "The validate method of ValidatorForSubClassB should have been called." ).isTrue();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "d")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "m")
	public void testTargetedTypeIsConstructor() throws NoSuchMethodException, SecurityException {
		Assertions.assertThat(  CustomConstraint.ValidatorForSubClassC.callCounter ).as( "The validate method of ValidatorForSubClassC should not have been called yet." ).isEqualTo( 0 );

		getExecutableValidator().validateConstructorReturnValue( SubClassC.class.getConstructor(), new SubClassC() );

		Assertions.assertThat(  CustomConstraint.ValidatorForSubClassC.callCounter > 0 ).as( "The validate method of ValidatorForSubClassC should have been called." ).isTrue();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "d")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "m")
	public void testTargetedTypeIsMethod() throws NoSuchMethodException, SecurityException {
		Assertions.assertThat(  CustomConstraint.ValidatorForSubClassD.callCounter ).as( "The validate method of ValidatorForSubClassD should not have been called yet." ).isEqualTo( 0 );

		getExecutableValidator().validateReturnValue( new SubClassDService(), SubClassDService.class.getMethod( "retrieveSubClassD" ), new SubClassD() );

		Assertions.assertThat(  CustomConstraint.ValidatorForSubClassD.callCounter > 0 ).as( "The validate method of ValidatorForSubClassD should have been called." ).isTrue();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "e")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "m")
	public void testTargetedTypeIsMethodParameter() throws NoSuchMethodException, SecurityException {
		Assertions.assertThat(  CustomConstraint.ValidatorForSubClassE.callCounter ).as( "The validate method of ValidatorForSubClassE should not have been called yet." ).isEqualTo( 0 );

		getExecutableValidator().validateParameters( new SubClassEService(), SubClassEService.class.getMethod( "retrieveSubClassE", SubClassE.class ),
				new Object[]{ new SubClassE() } );

		Assertions.assertThat(  CustomConstraint.ValidatorForSubClassE.callCounter > 0 ).as( "The validate method of ValidatorForSubClassE should have been called." ).isTrue();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "e")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "m")
	public void testTargetedTypeIsConstructorParameter() throws NoSuchMethodException, SecurityException {
		Assertions.assertThat(  CustomConstraint.ValidatorForSubClassF.callCounter ).as( "The validate method of ValidatorForSubClassF should not have been called yet." ).isEqualTo( 0 );

		getExecutableValidator().validateConstructorParameters( SubClassFService.class.getConstructor( SubClassF.class ),
				new Object[]{ new SubClassF() } );

		Assertions.assertThat(  CustomConstraint.ValidatorForSubClassF.callCounter > 0 ).as( "The validate method of ValidatorForSubClassF should have been called." ).isTrue();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "f")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "m")
	public void testTargetedTypeIsTypeArgument() {
		Assertions.assertThat(  CustomConstraint.ValidatorForSubClassG.callCounter ).as( "The validate method of ValidatorForSubClassG should not have been called yet." ).isEqualTo( 0 );

		getValidator().validate( new SubClassGHolder() );

		Assertions.assertThat(  CustomConstraint.ValidatorForSubClassG.callCounter > 0 ).as( "The validate method of ValidatorForSubClassG should have been called." ).isTrue();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "g")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "m")
	public void testTargetedTypeIsTypeArgumentForNonGenericContainerInheritingFromGenericTypeWithValueExtractor() {
		Assertions.assertThat(  CustomConstraint.ValidatorForSubClassH.callCounter ).as( "The validate method of ValidatorForSubClassH should not have been called yet." ).isEqualTo( 0 );

		getValidator().validate( new SubClassHHolder() );

		Assertions.assertThat(  CustomConstraint.ValidatorForSubClassH.callCounter > 0 ).as( "The validate method of ValidatorForSubClassH should have been called." ).isTrue();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "h")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "m")
	public void testTargetedTypeIsTypeArgumentForNonGenericContainerWithValueExtractorWithExtractedType() {
		Assertions.assertThat(  CustomConstraint.ValidatorForSubClassI.callCounter ).as( "The validate method of ValidatorForSubClassI should not have been called yet." ).isEqualTo( 0 );

		TestUtil.getConfigurationUnderTest()
				.addValueExtractor( new SubClassIContainerValueExtractor() )
				.buildValidatorFactory().getValidator()
				.validate( new SubClassIHolder() );

		Assertions.assertThat(  CustomConstraint.ValidatorForSubClassI.callCounter > 0 ).as( "The validate method of ValidatorForSubClassI should have been called." ).isTrue();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "m")
	public void testClassLevelValidatorForSubTypeHasPredenceOverValidatorForSuperClass() {
		Assertions.assertThat(  CustomConstraint.ValidatorForAnotherSubClass.callCounter ).as( "The validate method of ValidatorForAnotherSubClass should not have been called yet." ).isEqualTo( 0 );

		getValidator().validate( new AnotherSubClass() );

		Assertions.assertThat(  CustomConstraint.ValidatorForAnotherSubClass.callCounter > 0 ).as( "The validate method of ValidatorForAnotherSubClass should have been called." ).isTrue();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "i")
	public void testResolutionOfMultipleSizeValidators() {
		Suburb suburb = new Suburb();

		// all values are null and should pass
		Set<ConstraintViolation<Suburb>> constraintViolations = getValidator().validate( suburb );
		assertNoViolations( constraintViolations );

		suburb.setName( "" );
		constraintViolations = getValidator().validate( suburb );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Size.class )
						.withProperty( "name" )
						.withInvalidValue( "" )
						.withRootBeanClass( Suburb.class )
		);

		suburb.setName( "Hoegsbo" );
		constraintViolations = getValidator().validate( suburb );
		assertNoViolations( constraintViolations );

		suburb.addFacility( Suburb.Facility.SHOPPING_MALL, false );
		constraintViolations = getValidator().validate( suburb );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Size.class )
						.withProperty( "facilities" )
						.withInvalidValue( suburb.getFacilities() )
						.withRootBeanClass( Suburb.class )
		);

		suburb.addFacility( Suburb.Facility.BUS_TERMINAL, true );
		constraintViolations = getValidator().validate( suburb );
		assertNoViolations( constraintViolations );

		suburb.addStreetName( "Sikelsgatan" );
		constraintViolations = getValidator().validate( suburb );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Size.class )
						.withProperty( "streetNames" )
						.withInvalidValue( suburb.getStreetNames() )
						.withRootBeanClass( Suburb.class )
		);

		suburb.addStreetName( "Marklandsgatan" );
		constraintViolations = getValidator().validate( suburb );
		assertNoViolations( constraintViolations );

		Coordinate[] boundingBox = new Coordinate[3];
		boundingBox[0] = new Coordinate( 0l, 0l );
		boundingBox[1] = new Coordinate( 0l, 1l );
		boundingBox[2] = new Coordinate( 1l, 0l );
		suburb.setBoundingBox( boundingBox );
		constraintViolations = getValidator().validate( suburb );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Size.class )
						.withProperty( "boundingBox" )
						.withInvalidValue( suburb.getBoundingBox() )
						.withRootBeanClass( Suburb.class )
		);

		boundingBox = new Coordinate[4];
		boundingBox[0] = new Coordinate( 0l, 0l );
		boundingBox[1] = new Coordinate( 0l, 1l );
		boundingBox[2] = new Coordinate( 1l, 0l );
		boundingBox[3] = new Coordinate( 1l, 1l );
		suburb.setBoundingBox( boundingBox );
		constraintViolations = getValidator().validate( suburb );
		assertNoViolations( constraintViolations );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "i")
	public void testResolutionOfMinMaxForDifferentTypes() {
		MinMax minMax = new MinMax( "5", 5 );
		Set<ConstraintViolation<MinMax>> constraintViolations = getValidator().validate( minMax );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Min.class ).withProperty( "number" ),
				violationOf( Min.class ).withProperty( "numberAsString" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "l")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION, id = "e")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "m")
	public void testUnexpectedTypeInValidatorResolution() {
		Assertions.assertThatThrownBy( () -> {

			Bar bar = new Bar();
			getValidator().validate( bar );
	
		} ).isInstanceOf( UnexpectedTypeException.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "n")
	@SpecAssertion(section = Sections.EXCEPTION_CONSTRAINTDECLARATION, id = "b")
	public void testAmbiguousValidatorResolution() {
		Assertions.assertThatThrownBy( () -> {

			Foo foo = new Foo( new SerializableBarSubclass() );
			getValidator().validate( foo );
	
		} ).isInstanceOf( UnexpectedTypeException.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "k")
	public void testValidatorForWrapperTypeIsAppliedForPrimitiveType() {
		PrimitiveHolder primitiveHolder = new PrimitiveHolder();
		Set<ConstraintViolation<PrimitiveHolder>> constraintViolations = getValidator().validate( primitiveHolder );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( ValidInteger.class ),
				violationOf( ValidLong.class )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "a")
	public void testSeveralCrossParameterValidatorsCauseConstraintDefinitionException() throws Exception {
		Assertions.assertThatThrownBy( () -> {

			Object object = new CalendarService();
			Method method = CalendarService.class.getMethod( "createEvent", Date.class, Date.class );
			Object[] parameterValues = new Object[2];

			getExecutableValidator().validateParameters( object, method, parameterValues );
	
		} ).isInstanceOf( ConstraintDefinitionException.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "a")
	public void testCrossParameterConstraintWithoutValidatorCausesConstraintDefinitionException() throws Exception {
		Assertions.assertThatThrownBy( () -> {

			Object object = new OnlineCalendarService();
			Method method = OnlineCalendarService.class.getMethod( "createEvent", Date.class, Date.class );
			Object[] parameterValues = new Object[2];

			getExecutableValidator().validateParameters( object, method, parameterValues );
	
		} ).isInstanceOf( ConstraintDefinitionException.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "a")
	public void testCrossParameterValidatorIsUsedForConstraintImplicitlyTargetingParameters() throws Exception {
		Object object = new OfflineCalendarService();
		Method method = OfflineCalendarService.class.getMethod( "createEvent", Date.class, Date.class );
		Object[] parameterValues = new Object[2];

		Set<ConstraintViolation<Object>> constraintViolations = getExecutableValidator()
				.validateParameters( object, method, parameterValues );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( GenericAndCrossParameterConstraint.class ).withMessage( "violation created by cross-parameter validator" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "a")
	public void testCrossParameterValidatorIsUsedForConstraintExplicitlyTargetingParameters() throws Exception {
		Object object = new AdvancedCalendarService();
		Method method = AdvancedCalendarService.class.getMethod( "createEvent", Date.class, Date.class );
		Object[] parameterValues = new Object[2];

		Set<ConstraintViolation<Object>> violations = getExecutableValidator()
				.validateParameters( object, method, parameterValues );
		assertThat( violations ).containsOnlyViolations(
				violationOf( GenericAndCrossParameterConstraint.class ).withMessage( "violation created by cross-parameter validator" )
		);
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
		assertThat( violations ).containsOnlyViolations(
				violationOf( CrossParameterConstraintWithObjectArrayValidator.class ).withMessage( "violation created by validator for Object[]" )
		);
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
		assertThat( violations ).containsOnlyViolations(
				violationOf( CrossParameterConstraintWithObjectValidator.class ).withMessage( "violation created by validator for Object" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "j")
	public void testGenericValidatorIsUsedForConstraintTargetingMethodReturnValue() throws Exception {
		Object object = new AnotherCalendarService();
		Method method = AnotherCalendarService.class.getMethod( "createEvent", Date.class, Date.class );
		Object returnValue = null;

		Set<ConstraintViolation<Object>> violations = getExecutableValidator()
				.validateReturnValue( object, method, returnValue );
		assertThat( violations ).containsOnlyViolations(
				violationOf( GenericAndCrossParameterConstraint.class ).withMessage( "violation created by generic validator" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "j")
	public void testGenericValidatorIsUsedForConstraintTargetingField() {
		Set<ConstraintViolation<TestBean>> violations = getValidator().validate( new TestBean() );
		assertThat( violations ).containsOnlyViolations(
				violationOf( GenericAndCrossParameterConstraint.class ).withMessage( "violation created by generic validator" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "n")
	public void testTwoValidatorsForSameTypeCauseUnexpectedTypeException() {
		Assertions.assertThatThrownBy( () -> {

			getValidator().validate( new AnotherBean() );
	
		} ).isInstanceOf( UnexpectedTypeException.class );
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

	private static class SubClassEService {

		@SuppressWarnings("unused")
		public void retrieveSubClassE(@CustomConstraint SubClassE parameter) {
		}
	}

	private static class SubClassFService {

		@SuppressWarnings("unused")
		public SubClassFService(@CustomConstraint SubClassF parameter) {
		}
	}

	private static class SubClassGHolder {

		@SuppressWarnings("unused")
		private Optional<@CustomConstraint SubClassG> property = Optional.of( new SubClassG() );
	}

	private static class SubClassHContainer extends ArrayList<SubClassH> {

		private SubClassHContainer() {
			add( new SubClassH() );
		}
	}

	private static class SubClassHHolder {

		@CustomConstraint(payload = Unwrapping.Unwrap.class)
		private SubClassHContainer property = new SubClassHContainer();
	}

	private static class SubClassIContainer {

		private SubClassI value = new SubClassI();

		private SubClassIContainer() {
		}

		private SubClassI getValue() {
			return value;
		}
	}

	@UnwrapByDefault
	private static class SubClassIContainerValueExtractor implements ValueExtractor<@ExtractedValue(type = SubClassI.class) SubClassIContainer> {

		@Override
		public void extractValues(SubClassIContainer originalValue, ValueExtractor.ValueReceiver receiver) {
			receiver.value( null, originalValue.getValue() );
		}
	}

	private static class SubClassIHolder {

		@CustomConstraint
		private SubClassIContainer property = new SubClassIContainer();
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
