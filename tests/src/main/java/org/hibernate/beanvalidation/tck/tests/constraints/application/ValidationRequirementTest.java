/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.application;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectConstraintTypes;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectConstraintViolationMessages;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectNumberOfViolations;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class ValidationRequirementTest extends Arquillian {

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClassPackage( ValidationRequirementTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = "4.1", id = "c")
	@SpecAssertion(section = "4.1", id = "e")
	@SpecAssertion(section = "4.1.1", id = "a")
	@SpecAssertion(section = "4.1.1", id = "b")
	public void testClassLevelConstraints() {
		Woman sarah = new Woman();
		sarah.setFirstName( "Sarah" );
		sarah.setLastName( "Jones" );
		sarah.setPersonalNumber( "000000-0000" );

		Validator validator = TestUtil.getValidatorUnderTest();
		Set<ConstraintViolation<Woman>> violations = validator.validate( sarah );

		assertCorrectNumberOfViolations(
				violations, 2
		); // SecurityCheck for Default in Person and Citizen
		assertCorrectConstraintTypes( violations, SecurityCheck.class, SecurityCheck.class );

		violations = validator.validate( sarah, TightSecurity.class );
		assertCorrectNumberOfViolations(
				violations, 1
		); // SecurityCheck for TightSecurity in Citizen
		assertCorrectConstraintTypes( violations, SecurityCheck.class );

		// just to make sure - validating against a group which does not have any constraints assigned to it
		violations = validator.validate( sarah, DummyGroup.class );
		assertCorrectNumberOfViolations( violations, 0 );

		sarah.setPersonalNumber( "740523-1234" );
		violations = validator.validate( sarah );
		assertCorrectNumberOfViolations( violations, 0 );

		violations = validator.validate( sarah, TightSecurity.class );
		assertCorrectNumberOfViolations( violations, 0 );
	}

	@Test
	@SpecAssertion(section = "4.1", id = "e")
	@SpecAssertion(section = "4.1.2", id = "a")
	@SpecAssertion(section = "4.1.2", id = "c")
	public void testFieldAccess() {
		SuperWoman superwoman = new SuperWoman();

		Validator validator = TestUtil.getValidatorUnderTest();
		Set<ConstraintViolation<SuperWoman>> violations = validator.validateProperty( superwoman, "firstName" );
		assertCorrectNumberOfViolations( violations, 0 );

		superwoman.setFirstName( null );
		violations = validator.validateProperty( superwoman, "firstName" );
		assertCorrectNumberOfViolations( violations, 1 );
		assertCorrectConstraintTypes( violations, NotNull.class );
	}

	@Test
	@SpecAssertion(section = "4.1", id = "e")
	@SpecAssertion(section = "4.1.2", id = "a")
	@SpecAssertion(section = "4.1.2", id = "d")
	public void testPropertyAccess() {
		SuperWoman superwoman = new SuperWoman();

		Validator validator = TestUtil.getValidatorUnderTest();
		Set<ConstraintViolation<SuperWoman>> violations = validator.validateProperty( superwoman, "lastName" );
		assertCorrectNumberOfViolations( violations, 0 );

		superwoman.setHiddenName( null );
		violations = validator.validateProperty( superwoman, "lastName" );
		assertCorrectNumberOfViolations( violations, 1 );
		assertCorrectConstraintTypes( violations, NotNull.class );
	}

	@Test
	@SpecAssertion(section = "4.1.2", id = "a")
	@SpecAssertion(section = "4.1.2", id = "b")
	public void testConstraintAppliedOnFieldAndProperty() {
		Building building = new Building( 10000000 );

		Validator validator = TestUtil.getValidatorUnderTest();
		Set<ConstraintViolation<Building>> violations = validator.validate( building );
		assertCorrectNumberOfViolations( violations, 2 );
		String expectedMessage = "Building costs are max {max} dollars.";
		assertCorrectConstraintViolationMessages( violations, expectedMessage, expectedMessage );
	}

	@Test(enabled = false)
//	@SpecAssertion(section = "4.1",
//			id = "b",
//			note = "The spec is not clear about whether validation of static fields/properties should just be ignored or an exception should be thrown.")
	public void testIgnoreStaticFieldsAndProperties() {
		StaticFieldsAndProperties entity = new StaticFieldsAndProperties();

		Validator validator = TestUtil.getValidatorUnderTest();
		Set<ConstraintViolation<StaticFieldsAndProperties>> violations = validator.validate( entity );
		assertCorrectNumberOfViolations( violations, 0 );
	}

	@Test
	@SpecAssertion(section = "4.1.2", id = "e")
	public void testFieldAndPropertyVisibilityIsNotConstrained() {
		Visibility entity = new Visibility();

		Validator validator = TestUtil.getValidatorUnderTest();
		Set<ConstraintViolation<Visibility>> violations = validator.validate( entity );
		assertCorrectNumberOfViolations( violations, 6 );
		assertCorrectConstraintTypes(
				violations, Min.class, Min.class, Min.class, DecimalMin.class, DecimalMin.class, DecimalMin.class
		);
		assertCorrectConstraintViolationMessages(
				violations,
				"publicField",
				"protectedField",
				"privateField",
				"publicProperty",
				"protectedProperty",
				"privateProperty"
		);

		entity.setValues( 100 );
		violations = validator.validate( entity );
		assertCorrectNumberOfViolations( violations, 0 );
	}

	static class StaticFieldsAndProperties {
		@NotNull
		static Object staticField = null;

		@NotNull
		static Object getStaticProperty() {
			return null;
		}
	}
}

interface DummyGroup {
}
