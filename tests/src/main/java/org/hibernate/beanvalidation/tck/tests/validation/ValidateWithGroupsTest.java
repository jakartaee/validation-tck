/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectConstraintTypes;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectNumberOfViolations;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectPropertyPaths;

/**
 * Tests for the implementation of {@code Validator}.
 *
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class ValidateWithGroupsTest extends Arquillian {

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClass( ValidateWithGroupsTest.class )
				.withClasses( Address.class, NotEmpty.class )
				.build();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.1.3", id = "a"),
			@SpecAssertion(section = "5.1.3", id = "b")
	})
	public void testCorrectGroupsAreAppliedForValidate() {
		Validator validator = TestUtil.getValidatorUnderTest();

		Set<ConstraintViolation<Address>> constraintViolations = validator.validate( new Address() );
		assertCorrectNumberOfViolations( constraintViolations, 2 );
		assertCorrectConstraintTypes( constraintViolations, NotNull.class, NotEmpty.class );
		assertCorrectPropertyPaths( constraintViolations, "city", "zipCode" );

		constraintViolations = validator.validate( new Address(), Default.class );
		assertCorrectNumberOfViolations( constraintViolations, 2 );
		assertCorrectConstraintTypes( constraintViolations, NotNull.class, NotEmpty.class );
		assertCorrectPropertyPaths( constraintViolations, "city", "zipCode" );

		constraintViolations = validator.validate( new Address(), Address.Minimal.class );
		assertCorrectNumberOfViolations( constraintViolations, 2 );
		assertCorrectConstraintTypes( constraintViolations, NotEmpty.class, NotEmpty.class );
		assertCorrectPropertyPaths( constraintViolations, "street", "zipCode" );

		constraintViolations = validator.validate( new Address(), Default.class, Address.Minimal.class );
		assertCorrectNumberOfViolations( constraintViolations, 3 );
		assertCorrectConstraintTypes( constraintViolations, NotNull.class, NotEmpty.class, NotEmpty.class );
		assertCorrectPropertyPaths( constraintViolations, "city", "street", "zipCode" );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.1.3", id = "a"),
			@SpecAssertion(section = "5.1.3", id = "b")
	})
	public void testCorrectGroupsAreAppliedForValidateProperty() {
		Validator validator = TestUtil.getValidatorUnderTest();

		Set<ConstraintViolation<Address>> constraintViolations = validator.validateProperty( new Address(), "city" );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintTypes( constraintViolations, NotNull.class );
		assertCorrectPropertyPaths( constraintViolations, "city" );

		constraintViolations = validator.validateProperty( new Address(), "city", Default.class );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintTypes( constraintViolations, NotNull.class );
		assertCorrectPropertyPaths( constraintViolations, "city" );

		constraintViolations = validator.validateProperty( new Address(), "city", Address.Minimal.class );
		assertCorrectNumberOfViolations( constraintViolations, 0 );

		constraintViolations = validator.validateProperty( new Address(), "street", Address.Minimal.class );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintTypes( constraintViolations, NotEmpty.class );
		assertCorrectPropertyPaths( constraintViolations, "street" );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.1.3", id = "a"),
			@SpecAssertion(section = "5.1.3", id = "b")
	})
	public void testCorrectGroupsAreAppliedForValidateValue() {
		Validator validator = TestUtil.getValidatorUnderTest();

		Set<ConstraintViolation<Address>> constraintViolations = validator.validateValue( Address.class, "city", null );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintTypes( constraintViolations, NotNull.class );
		assertCorrectPropertyPaths( constraintViolations, "city" );

		constraintViolations = validator.validateValue( Address.class, "city", null, Default.class );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintTypes( constraintViolations, NotNull.class );
		assertCorrectPropertyPaths( constraintViolations, "city" );

		constraintViolations = validator.validateValue( Address.class, "city", null, Address.Minimal.class );
		assertCorrectNumberOfViolations( constraintViolations, 0 );

		constraintViolations = validator.validateValue( Address.class, "street", null, Address.Minimal.class );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintTypes( constraintViolations, NotEmpty.class );
		assertCorrectPropertyPaths( constraintViolations, "street" );
	}
}
