/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertCorrectConstraintTypes;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertNumberOfViolations;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.pathWith;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import javax.validation.Payload;
import javax.validation.UnexpectedTypeException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.PropertyDescriptor;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests for the implementation of <code>Validator</code>.
 *
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
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

	@Test(expectedExceptions = UnexpectedTypeException.class)
	// UnexpectedTypeException is a subclass of ValidationException
	@SpecAssertions({
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS, id = "a"),
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_TYPEVALIDATORRESOLUTION, id = "l"),
			@SpecAssertion(section = Sections.CONSTRAINTMETADATA_VALIDATOR, id = "c")
	})
	public void testUnexpectedTypeException() {
		Boy boy = new Boy();
		TestUtil.getValidatorUnderTest().validate( boy );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_VALIDATOR, id = "a")
	public void testConstraintDescriptorWithoutExplicitGroup() {
		Validator validator = TestUtil.getValidatorUnderTest();

		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( Order.class );
		PropertyDescriptor propertyDescriptor = beanDescriptor.getConstraintsForProperty( "orderNumber" );
		Set<ConstraintDescriptor<?>> descriptors = propertyDescriptor.getConstraintDescriptors();

		assertEquals( descriptors.size(), 1, "There should be only one constraint descriptor" );
		ConstraintDescriptor<?> descriptor = descriptors.iterator().next();
		Set<Class<?>> groups = descriptor.getGroups();
		assertTrue( groups.size() == 1, "There should be only one group" );
		assertEquals(
				groups.iterator().next(),
				Default.class,
				"The declared constraint does not explicitly define a group, hence Default is expected"
		);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_VALIDATOR, id = "b")
	public void testNullParameterToGetConstraintsForClass() {
		TestUtil.getValidatorUnderTest().getConstraintsForClass( null );
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_VALIDATIONMETHODS, id = "b")
	public void testValidateWithNullValue() {
		Validator validator = TestUtil.getValidatorUnderTest();
		validator.validate( null );
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_VALIDATIONMETHODS, id = "b")
	public void testValidateWithNullGroup() {
		Validator validator = TestUtil.getValidatorUnderTest();
		validator.validate( new Boy(), (Class<?>) null );
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
		assertNumberOfViolations( constraintViolations, 2 );

		engine.setSerialNumber( "ABCDEFGH1234" );
		constraintViolations = validator.validate( engine );
		assertNumberOfViolations( constraintViolations, 1 );

		engine.setSerialNumber( "ABCD-EFGH-1234" );
		constraintViolations = validator.validate( engine );
		assertNumberOfViolations( constraintViolations, 0 );
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
		assertNumberOfViolations( constraintViolations, 2 );
		assertCorrectConstraintTypes( constraintViolations, Size.class, NotEmpty.class );
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
		assertNumberOfViolations( constraintViolations, 1 );

		ConstraintViolation<Engine> violation = constraintViolations.iterator().next();

		assertEquals( violation.getMessage(), "must match ^....-....-....$", "Wrong message" );
		assertEquals( violation.getMessageTemplate(), "must match {regexp}", "Wrong message template" );
		assertEquals( violation.getRootBean(), engine, "Wrong root entity." );
		assertEquals( violation.getRootBeanClass(), Engine.class, "Wrong root bean class." );
		assertEquals( violation.getLeafBean(), engine );
		assertEquals( violation.getInvalidValue(), "ABCDEFGH1234", "Wrong validated value" );
		assertNull( violation.getExecutableParameters() );
		assertNull( violation.getExecutableReturnValue() );
		assertNotNull( violation.getConstraintDescriptor(), "Constraint descriptor should not be null" );
		Annotation ann = violation.getConstraintDescriptor().getAnnotation();
		assertEquals( ann.annotationType(), Pattern.class, "Wrong annotation type" );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "serialNumber" )
		);

		engine.setSerialNumber( "ABCD-EFGH-1234" );
		constraintViolations = validator.validate( engine );
		assertNumberOfViolations( constraintViolations, 0 );
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
		assertNumberOfViolations( constraintViolations, 1 );

		ConstraintViolation<DirtBike> violation = constraintViolations.iterator().next();

		assertEquals( violation.getRootBean(), bike, "Wrong root entity." );
		assertEquals( violation.getRootBeanClass(), DirtBike.class, "Wrong root bean class." );
		assertEquals( violation.getLeafBean(), bike, "Wrong leaf bean." );
		assertEquals( violation.getInvalidValue(), bike, "Wrong validated value" );
		assertNotNull( violation.getConstraintDescriptor(), "Constraint descriptor should not be null" );

		Annotation ann = violation.getConstraintDescriptor().getAnnotation();
		assertEquals( ann.annotationType(), ValidDirtBike.class, "Wrong annotation type" );

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
		assertNumberOfViolations( constraintViolations, 2 );

		ConstraintViolation<Actor> constraintViolation = constraintViolations.iterator().next();
		assertEquals( constraintViolation.getMessage(), "Everyone has a last name.", "Wrong message" );
		assertEquals( constraintViolation.getRootBean(), clint, "Wrong root entity" );
		assertEquals( constraintViolation.getLeafBean(), morgan );
		assertEquals( constraintViolation.getInvalidValue(), morgan.getLastName(), "Wrong value" );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "playedWith" )
						.property( "playedWith", true, null, 0, List.class, 0 )
						.property( "lastName", true, null, 1, List.class, 0 ),
				pathWith()
						.property( "playedWith" )
						.property( "lastName", true, null, 1, List.class, 0 )
		);
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
		assertNumberOfViolations( constraintViolations, 2 );
		ConstraintViolation<Actor> constraintViolation = constraintViolations.iterator().next();
		assertEquals( constraintViolation.getMessage(), "Everyone has a last name.", "Wrong message" );
		assertEquals( constraintViolation.getRootBean(), clint, "Wrong root entity" );
		assertEquals( constraintViolation.getInvalidValue(), morgan.getLastName(), "Wrong value" );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "playedWith" )
						.property( "playedWith", true, null, 0, Object[].class, null )
						.property( "lastName", true, null, 1, Object[].class, null ),
				pathWith()
						.property( "playedWith" )
						.property( "lastName", true, null, 1, Object[].class, null )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPSEQUENCE, id = "b")
	public void testOnlyFirstGroupInSequenceGetEvaluated() {
		Validator validator = TestUtil.getValidatorUnderTest();
		Car car = new Car( "USd-298" );

		Set<ConstraintViolation<Car>> violations = validator.validateProperty(
				car, "licensePlateNumber", First.class, Second.class
		);
		assertNumberOfViolations( violations, 1 );

		car.setLicensePlateNumber( "USD-298" );
		violations = validator.validateProperty(
				car, "licensePlateNumber", First.class, Second.class
		);
		assertNumberOfViolations( violations, 0 );
	}

	@Test(expectedExceptions = ValidationException.class)
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_VALIDATIONMETHODS, id = "k")
	public void testUnexpectedExceptionsInValidateGetWrappedInValidationExceptions() {
		Validator validator = TestUtil.getValidatorUnderTest();
		validator.validate( new BadlyBehavedEntity() );
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
