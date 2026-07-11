/*
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertNoViolations;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.pathWith;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Payload;
import jakarta.validation.UnexpectedTypeException;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.Default;
import jakarta.validation.metadata.BeanDescriptor;
import jakarta.validation.metadata.ConstraintDescriptor;
import jakarta.validation.metadata.PropertyDescriptor;

import org.assertj.core.api.Assertions;
import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.junit.jupiter.api.Test;

/**
 * Tests for the implementation of <code>Validator</code>.
 *
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
public class ValidateTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( ValidateTest.class )
				.withClasses(
						Engine.class,
						Boy.class,
						Actor.class,
						ActorArrayBased.class,
						ActorListBased.class,
						PlayedWith.class,
						Customer.class,
						Person.class,
						Order.class,
						Address.class,
						BadlyBehavedEntity.class,
						Last.class,
						NotEmpty.class,
						ContainerElementsOrder.class,
						OrderLine.class,
						ProductCategory.class,
						Item.class
				)
				.build();
	}

	@Test
	// UnexpectedTypeException is a subclass of ValidationException
	@SpecAssertions({
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS, id = "a"),
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "l"),
			@SpecAssertion(section = Sections.CONSTRAINTMETADATA_VALIDATOR, id = "c")
	})
	public void testUnexpectedTypeException() {
		Assertions.assertThatThrownBy( () -> {
			Boy boy = new Boy();
			TestUtil.getValidatorUnderTest().validate( boy );
		} ).isInstanceOf( UnexpectedTypeException.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_VALIDATOR, id = "a")
	public void testConstraintDescriptorWithoutExplicitGroup() {
		Validator validator = TestUtil.getValidatorUnderTest();

		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( Order.class );
		PropertyDescriptor propertyDescriptor = beanDescriptor.getConstraintsForProperty( "orderNumber" );
		Set<ConstraintDescriptor<?>> descriptors = propertyDescriptor.getConstraintDescriptors();

		Assertions.assertThat( descriptors.size() ).as( "There should be only one constraint descriptor" ).isEqualTo( 1 );
		ConstraintDescriptor<?> descriptor = descriptors.iterator().next();
		Set<Class<?>> groups = descriptor.getGroups();
		Assertions.assertThat( groups.size() == 1 ).as( "There should be only one group" ).isTrue();
		Assertions.assertThat( groups.iterator().next() ).as( "The declared constraint does not explicitly define a group, hence Default is expected" ).isEqualTo( Default.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_VALIDATOR, id = "b")
	public void testNullParameterToGetConstraintsForClass() {
		Assertions.assertThatThrownBy( () -> {

			TestUtil.getValidatorUnderTest().getConstraintsForClass( null );
	
		} ).isInstanceOf( IllegalArgumentException.class );
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_VALIDATIONMETHODS, id = "b")
	public void testValidateWithNullValue() {
		Assertions.assertThatThrownBy( () -> {

			Validator validator = TestUtil.getValidatorUnderTest();
			validator.validate( null );
	
		} ).isInstanceOf( IllegalArgumentException.class );
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_VALIDATIONMETHODS, id = "b")
	public void testValidateWithNullGroup() {
		Assertions.assertThatThrownBy( () -> {

			Validator validator = TestUtil.getValidatorUnderTest();
			validator.validate( new Boy(), (Class<?>) null );
	
		} ).isInstanceOf( IllegalArgumentException.class );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_VALIDATIONMETHODS, id = "a"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_VALIDATIONMETHODS, id = "c")
	})

	public void testMultipleViolationOfTheSameType() {
		Validator validator = TestUtil.getValidatorUnderTest();

		Engine engine = new Engine();
		engine.setSerialNumber( "mail@foobar.com" );
		Set<ConstraintViolation<Engine>> constraintViolations = validator.validate( engine );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Pattern.class ),
				violationOf( Pattern.class )
		);

		engine.setSerialNumber( "ABCDEFGH1234" );
		constraintViolations = validator.validate( engine );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Pattern.class )
		);

		engine.setSerialNumber( "ABCD-EFGH-1234" );
		constraintViolations = validator.validate( engine );
		assertNoViolations( constraintViolations );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE, id = "c"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_VALIDATIONMETHODS, id = "c")
	})
	public void testMultipleConstraintViolationOfDifferentTypes() {
		Validator validator = TestUtil.getValidatorUnderTest();

		Address address = new Address();
		address.setStreet( null );
		address.setZipCode( null );
		address.setCity( "Llanfairpwllgwyngyllgogerychwyrndrobwyll-llantysiliogogogoch" ); //town in North Wales

		Set<ConstraintViolation<Address>> constraintViolations = validator.validate( address );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Size.class ),
				violationOf( NotEmpty.class )
		);
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS, id = "a"),
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE, id = "a"),
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE, id = "c"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "a"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "b"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "c"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "e"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "f"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "i"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "g"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "h"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "l"),
			@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONSTRAINTDESCRIPTOR, id = "a"),
	})
	public void testConstraintViolation() {
		Validator validator = TestUtil.getValidatorUnderTest();

		Engine engine = new Engine();
		engine.setSerialNumber( "ABCDEFGH1234" );
		Set<ConstraintViolation<Engine>> constraintViolations = validator.validate( engine );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Pattern.class )
		);

		ConstraintViolation<Engine> violation = constraintViolations.iterator().next();

		Assertions.assertThat( violation.getMessage() ).as( "Wrong message" ).isEqualTo( "must match ^....-....-....$" );
		Assertions.assertThat( violation.getMessageTemplate() ).as( "Wrong message template" ).isEqualTo( "must match {regexp}" );
		Assertions.assertThat( violation.getRootBean() ).as( "Wrong root entity." ).isEqualTo( engine );
		Assertions.assertThat( violation.getRootBeanClass() ).as( "Wrong root bean class." ).isEqualTo( Engine.class );
		Assertions.assertThat( violation.getLeafBean() ).isEqualTo( engine );
		Assertions.assertThat( violation.getInvalidValue() ).as( "Wrong validated value" ).isEqualTo( "ABCDEFGH1234" );
		Assertions.assertThat( violation.getExecutableParameters() ).isNull();
		Assertions.assertThat( violation.getExecutableReturnValue() ).isNull();
		Assertions.assertThat( violation.getConstraintDescriptor() ).as( "Constraint descriptor should not be null" ).isNotNull();
		Annotation ann = violation.getConstraintDescriptor().getAnnotation();
		Assertions.assertThat( ann.annotationType() ).as( "Wrong annotation type" ).isEqualTo( Pattern.class );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "serialNumber" )
		);

		engine.setSerialNumber( "ABCD-EFGH-1234" );
		constraintViolations = validator.validate( engine );
		assertNoViolations( constraintViolations );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE, id = "a"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "c"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "e"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "f"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "i")
	})
	public void testClassLevelConstraintViolation() {
		Validator validator = TestUtil.getValidatorUnderTest();

		DirtBike bike = new DirtBike();
		Set<ConstraintViolation<DirtBike>> constraintViolations = validator.validate( bike );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( ValidDirtBike.class )
		);

		ConstraintViolation<DirtBike> violation = constraintViolations.iterator().next();

		Assertions.assertThat( violation.getRootBean() ).as( "Wrong root entity." ).isEqualTo( bike );
		Assertions.assertThat( violation.getRootBeanClass() ).as( "Wrong root bean class." ).isEqualTo( DirtBike.class );
		Assertions.assertThat( violation.getLeafBean() ).as( "Wrong leaf bean." ).isEqualTo( bike );
		Assertions.assertThat( violation.getInvalidValue() ).as( "Wrong validated value" ).isEqualTo( bike );
		Assertions.assertThat( violation.getConstraintDescriptor() ).as( "Constraint descriptor should not be null" ).isNotNull();

		Annotation ann = violation.getConstraintDescriptor().getAnnotation();
		Assertions.assertThat( ann.annotationType() ).as( "Wrong annotation type" ).isEqualTo( ValidDirtBike.class );

		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith().bean()
		);
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "s"),
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE, id = "a"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "f")
	})
	public void testGraphValidationWithList() {
		Validator validator = TestUtil.getValidatorUnderTest();

		Actor clint = new ActorListBased( "Clint", "Eastwood" );
		Actor morgan = new ActorListBased( "Morgan", null );
		Actor charlie = new ActorListBased( "Charlie", "Sheen" );

		clint.addPlayedWith( charlie );
		charlie.addPlayedWith( clint );
		charlie.addPlayedWith( morgan );
		morgan.addPlayedWith( charlie );
		morgan.addPlayedWith( clint );
		clint.addPlayedWith( morgan );

		Set<ConstraintViolation<Actor>> constraintViolations = validator.validate( clint );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withMessage( "Everyone has a last name." )
						.withPropertyPath( pathWith()
								.property( "playedWith" )
								.property( "playedWith", true, null, 0, List.class, 0 )
								.property( "lastName", true, null, 1, List.class, 0 )
						),
				violationOf( NotNull.class )
						.withMessage( "Everyone has a last name." )
						.withPropertyPath( pathWith()
								.property( "playedWith" )
								.property( "lastName", true, null, 1, List.class, 0 )
						)
		);

		ConstraintViolation<Actor> constraintViolation = constraintViolations.iterator().next();
		Assertions.assertThat( constraintViolation.getRootBean() ).as( "Wrong root entity" ).isEqualTo( clint );
		Assertions.assertThat( constraintViolation.getLeafBean() ).isEqualTo( morgan );
		Assertions.assertThat( constraintViolation.getInvalidValue() ).as( "Wrong value" ).isEqualTo( morgan.getLastName( ) );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "s"),
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "i"),
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "d")
	})
	public void testGraphValidationWithArray() {
		Validator validator = TestUtil.getValidatorUnderTest();

		Actor clint = new ActorArrayBased( "Clint", "Eastwood" );
		Actor morgan = new ActorArrayBased( "Morgan", null );
		Actor charlie = new ActorArrayBased( "Charlie", "Sheen" );

		clint.addPlayedWith( charlie );
		charlie.addPlayedWith( clint );
		charlie.addPlayedWith( morgan );
		morgan.addPlayedWith( charlie );
		morgan.addPlayedWith( clint );
		clint.addPlayedWith( morgan );

		Set<ConstraintViolation<Actor>> constraintViolations = validator.validate( clint );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withMessage( "Everyone has a last name." )
						.withPropertyPath( pathWith()
								.property( "playedWith" )
								.property( "playedWith", true, null, 0, Object[].class, null )
								.property( "lastName", true, null, 1, Object[].class, null )
						),
				violationOf( NotNull.class )
						.withMessage( "Everyone has a last name." )
						.withPropertyPath( pathWith()
								.property( "playedWith" )
								.property( "lastName", true, null, 1, Object[].class, null )
						)
		);
		ConstraintViolation<Actor> constraintViolation = constraintViolations.iterator().next();
		Assertions.assertThat(  constraintViolation.getRootBean() ).as( "Wrong root entity" ).isEqualTo( clint );
		Assertions.assertThat(  constraintViolation.getInvalidValue() ).as( "Wrong value" ).isEqualTo( morgan.getLastName( ) );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPSEQUENCE, id = "b")
	public void testOnlyFirstGroupInSequenceGetEvaluated() {
		Validator validator = TestUtil.getValidatorUnderTest();
		Car car = new Car( "USd-298" );

		Set<ConstraintViolation<Car>> violations = validator.validateProperty(
				car, "licensePlateNumber", First.class, Second.class
		);
		assertThat( violations ).containsOnlyViolations(
				violationOf( Pattern.class )
		);

		car.setLicensePlateNumber( "USD-298" );
		violations = validator.validateProperty(
				car, "licensePlateNumber", First.class, Second.class
		);
		assertNoViolations( violations );
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_VALIDATIONMETHODS, id = "k")
	public void testUnexpectedExceptionsInValidateGetWrappedInValidationExceptions() {
		Assertions.assertThatThrownBy( () -> {

			Validator validator = TestUtil.getValidatorUnderTest();
			validator.validate( new BadlyBehavedEntity() );
	
		} ).isInstanceOf( ValidationException.class );
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "f")
	public void testContainerElementLeafBean() throws NoSuchMethodException, SecurityException {
		Item invalidItem = new Item( "s" );

		Map<ProductCategory, List<OrderLine>> invalidOrderLines = new HashMap<>();
		invalidOrderLines.put( null, Arrays.asList( new OrderLine( new Item( "item name" ) ) ) );
		invalidOrderLines.put( ProductCategory.MUSIC, Arrays.asList( new OrderLine( invalidItem ) ) );

		ContainerElementsOrder invalidOrder = new ContainerElementsOrder( "order name", invalidOrderLines );

		Set<ConstraintViolation<ContainerElementsOrder>> violations = getValidator().validate( invalidOrder );

		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class ).withLeafBean( invalidOrder ),
				violationOf( Size.class ).withLeafBean( invalidItem )
		);
	}

	private static class Car {
		@Pattern(regexp = "[A-Z][A-Z][A-Z]-[0-9][0-9][0-9]", groups = { First.class, Second.class })
		private String licensePlateNumber;

		Car(String licensePlateNumber) {
			this.licensePlateNumber = licensePlateNumber;
		}

		@SuppressWarnings("unused")
		public String getLicensePlateNumber() {
			return licensePlateNumber;
		}

		public void setLicensePlateNumber(String licensePlateNumber) {
			this.licensePlateNumber = licensePlateNumber;
		}
	}

	@ValidDirtBike
	private static class DirtBike {

	}

	private interface First {
	}

	private interface Second {
	}

	@Constraint(validatedBy = ValidDirtBike.Validator.class)
	@Documented
	@Target({ TYPE })
	@Retention(RUNTIME)
	public @interface ValidDirtBike {
		String message() default "{ValidDirtBike.message}";

		Class<?>[] groups() default { };

		Class<? extends Payload>[] payload() default { };

		public static class Validator implements ConstraintValidator<ValidDirtBike, DirtBike> {

			@Override
			public boolean isValid(DirtBike bike, ConstraintValidatorContext constraintValidatorContext) {
				return false;
			}
		}
	}
}
