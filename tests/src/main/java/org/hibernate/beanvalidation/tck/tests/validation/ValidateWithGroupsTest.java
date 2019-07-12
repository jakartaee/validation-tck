/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertCorrectConstraintTypes;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertNumberOfViolations;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.pathWith;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

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
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
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
		assertNumberOfViolations( constraintViolations, 2 );
		assertCorrectConstraintTypes( constraintViolations, NotNull.class, NotEmpty.class );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "city" ),
				pathWith()
						.property( "zipCode" )
		);

		constraintViolations = validator.validate( new Address(), Default.class );
		assertNumberOfViolations( constraintViolations, 2 );
		assertCorrectConstraintTypes( constraintViolations, NotNull.class, NotEmpty.class );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "city" ),
				pathWith()
						.property( "zipCode" )
		);

		constraintViolations = validator.validate( new Address(), Address.Minimal.class );
		assertNumberOfViolations( constraintViolations, 2 );
		assertCorrectConstraintTypes( constraintViolations, NotEmpty.class, NotEmpty.class );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "street" ),
				pathWith()
						.property( "zipCode" )
		);

		constraintViolations = validator.validate( new Address(), Default.class, Address.Minimal.class );
		assertNumberOfViolations( constraintViolations, 3 );
		assertCorrectConstraintTypes( constraintViolations, NotNull.class, NotEmpty.class, NotEmpty.class );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "street" ),
				pathWith()
						.property( "city" ),
				pathWith()
						.property( "zipCode" )
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
		assertNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintTypes( constraintViolations, NotNull.class );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "city" )
		);

		constraintViolations = validator.validateProperty( new Address(), "city", Default.class );
		assertNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintTypes( constraintViolations, NotNull.class );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "city" )
		);

		constraintViolations = validator.validateProperty( new Address(), "city", Address.Minimal.class );
		assertNumberOfViolations( constraintViolations, 0 );

		constraintViolations = validator.validateProperty( new Address(), "street", Address.Minimal.class );
		assertNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintTypes( constraintViolations, NotEmpty.class );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "street" )
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
		assertNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintTypes( constraintViolations, NotNull.class );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "city" )
		);

		constraintViolations = validator.validateValue( Address.class, "city", null, Default.class );
		assertNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintTypes( constraintViolations, NotNull.class );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "city" )
		);

		constraintViolations = validator.validateValue( Address.class, "city", null, Address.Minimal.class );
		assertNumberOfViolations( constraintViolations, 0 );

		constraintViolations = validator.validateValue( Address.class, "street", null, Address.Minimal.class );
		assertNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintTypes( constraintViolations, NotEmpty.class );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "street" )
		);
	}
}
