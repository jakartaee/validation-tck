/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertNoViolations;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;

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
 * Tests for the implementation of {@code Validator}.
 *
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "3.0.0")
public class ValidateWithGroupsTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( ValidateWithGroupsTest.class )
				.withClasses( Address.class, NotEmpty.class )
				.build();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_GROUPS, id = "a"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_GROUPS, id = "b")
	})
	public void testCorrectGroupsAreAppliedForValidate() {
		Validator validator = TestUtil.getValidatorUnderTest();

		Set<ConstraintViolation<Address>> constraintViolations = validator.validate( new Address() );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class ).withProperty( "city" ),
				violationOf( NotEmpty.class ).withProperty( "zipCode" )
		);

		constraintViolations = validator.validate( new Address(), Default.class );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class ).withProperty( "city" ),
				violationOf( NotEmpty.class ).withProperty( "zipCode" )
		);

		constraintViolations = validator.validate( new Address(), Address.Minimal.class );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotEmpty.class ).withProperty( "street" ),
				violationOf( NotEmpty.class ).withProperty( "zipCode" )
		);

		constraintViolations = validator.validate( new Address(), Default.class, Address.Minimal.class );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class ).withProperty( "city" ),
				violationOf( NotEmpty.class ).withProperty( "street" ),
				violationOf( NotEmpty.class ).withProperty( "zipCode" )
		);
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_GROUPS, id = "a"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_GROUPS, id = "b")
	})
	public void testCorrectGroupsAreAppliedForValidateProperty() {
		Validator validator = TestUtil.getValidatorUnderTest();

		Set<ConstraintViolation<Address>> constraintViolations = validator.validateProperty( new Address(), "city" );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class ).withProperty( "city" )
		);

		constraintViolations = validator.validateProperty( new Address(), "city", Default.class );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class ).withProperty( "city" )
		);

		constraintViolations = validator.validateProperty( new Address(), "city", Address.Minimal.class );
		assertNoViolations( constraintViolations );

		constraintViolations = validator.validateProperty( new Address(), "street", Address.Minimal.class );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotEmpty.class ).withProperty( "street" )
		);
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_GROUPS, id = "a"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_GROUPS, id = "b")
	})
	public void testCorrectGroupsAreAppliedForValidateValue() {
		Validator validator = TestUtil.getValidatorUnderTest();

		Set<ConstraintViolation<Address>> constraintViolations = validator.validateValue( Address.class, "city", null );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class ).withProperty( "city" )
		);

		constraintViolations = validator.validateValue( Address.class, "city", null, Default.class );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class ).withProperty( "city" )
		);

		constraintViolations = validator.validateValue( Address.class, "city", null, Address.Minimal.class );
		assertNoViolations( constraintViolations );

		constraintViolations = validator.validateValue( Address.class, "street", null, Address.Minimal.class );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotEmpty.class ).withProperty( "street" )
		);
	}
}
