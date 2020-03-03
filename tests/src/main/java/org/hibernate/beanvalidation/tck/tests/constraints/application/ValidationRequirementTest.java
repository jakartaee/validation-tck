/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.application;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertNoViolations;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "3.0.0")
public class ValidationRequirementTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( ValidationRequirementTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS, id = "c")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS, id = "e")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_OBJECTVALIDATION, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_OBJECTVALIDATION, id = "b")
	public void testClassLevelConstraints() {
		Woman sarah = new Woman();
		sarah.setFirstName( "Sarah" );
		sarah.setLastName( "Jones" );
		sarah.setPersonalNumber( "000000-0000" );

		Validator validator = TestUtil.getValidatorUnderTest();
		Set<ConstraintViolation<Woman>> violations = validator.validate( sarah );

		assertThat( violations ).containsOnlyViolations(
				violationOf( SecurityCheck.class ), violationOf( SecurityCheck.class )
		);

		violations = validator.validate( sarah, TightSecurity.class );
		// SecurityCheck for TightSecurity in Citizen
		assertThat( violations ).containsOnlyViolations(
				violationOf( SecurityCheck.class )
		);

		// just to make sure - validating against a group which does not have any constraints assigned to it
		violations = validator.validate( sarah, DummyGroup.class );
		assertNoViolations( violations );

		sarah.setPersonalNumber( "740523-1234" );
		violations = validator.validate( sarah );
		assertNoViolations( violations );

		violations = validator.validate( sarah, TightSecurity.class );
		assertNoViolations( violations );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS, id = "e")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_PROPERTYVALIDATION, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_PROPERTYVALIDATION, id = "c")
	public void testFieldAccess() {
		SuperWoman superwoman = new SuperWoman();

		Validator validator = TestUtil.getValidatorUnderTest();
		Set<ConstraintViolation<SuperWoman>> violations = validator.validateProperty( superwoman, "firstName" );
		assertNoViolations( violations );

		superwoman.setFirstName( null );
		violations = validator.validateProperty( superwoman, "firstName" );
		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS, id = "e")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_PROPERTYVALIDATION, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_PROPERTYVALIDATION, id = "d")
	public void testPropertyAccess() {
		SuperWoman superwoman = new SuperWoman();

		Validator validator = TestUtil.getValidatorUnderTest();
		Set<ConstraintViolation<SuperWoman>> violations = validator.validateProperty( superwoman, "lastName" );
		assertNoViolations( violations );

		superwoman.setHiddenName( null );
		violations = validator.validateProperty( superwoman, "lastName" );
		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_PROPERTYVALIDATION, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_PROPERTYVALIDATION, id = "b")
	public void testConstraintAppliedOnFieldAndProperty() {
		Building building = new Building( 10000000 );

		Validator validator = TestUtil.getValidatorUnderTest();
		Set<ConstraintViolation<Building>> violations = validator.validate( building );
		String expectedMessage = "Building costs are max {max} dollars.";
		assertThat( violations ).containsOnlyViolations(
				violationOf( Max.class ).withMessage( expectedMessage ),
				violationOf( Max.class ).withMessage( expectedMessage )
		);
	}

	@Test(enabled = false)
//	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS,
//			id = "b",
//			note = "The spec is not clear about whether validation of static fields/properties should just be ignored or an exception should be thrown.")
	public void testIgnoreStaticFieldsAndProperties() {
		StaticFieldsAndProperties entity = new StaticFieldsAndProperties();

		Validator validator = TestUtil.getValidatorUnderTest();
		Set<ConstraintViolation<StaticFieldsAndProperties>> violations = validator.validate( entity );
		assertNoViolations( violations );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_PROPERTYVALIDATION, id = "e")
	public void testFieldAndPropertyVisibilityIsNotConstrained() {
		Visibility entity = new Visibility();

		Validator validator = TestUtil.getValidatorUnderTest();
		Set<ConstraintViolation<Visibility>> violations = validator.validate( entity );
		assertThat( violations ).containsOnlyViolations(
				violationOf( Min.class ).withMessage( "publicField" ),
				violationOf( Min.class ).withMessage( "protectedField" ),
				violationOf( Min.class ).withMessage( "privateField" ),
				violationOf( DecimalMin.class ).withMessage( "publicProperty" ),
				violationOf( DecimalMin.class ).withMessage( "protectedProperty" ),
				violationOf( DecimalMin.class ).withMessage( "privateProperty" )
		);

		entity.setValues( 100 );
		violations = validator.validate( entity );
		assertNoViolations( violations );
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
