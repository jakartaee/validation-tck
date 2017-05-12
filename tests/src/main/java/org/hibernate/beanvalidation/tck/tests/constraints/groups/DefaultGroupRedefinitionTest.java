/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.groups;

import static org.hibernate.beanvalidation.tck.util.TestUtil.assertConstraintViolation;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectConstraintViolationMessages;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectNumberOfViolations;
import static org.testng.Assert.fail;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.GroupDefinitionException;
import javax.validation.GroupSequence;
import javax.validation.Validator;
import javax.validation.groups.Default;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests for redefining the default group sequence.
 *
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class DefaultGroupRedefinitionTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( DefaultGroupRedefinitionTest.class )
				.withClasses(
						Address.class,
						ZipCodeCoherenceChecker.class,
						ZipCodeCoherenceValidator.class,
						Car.class
				)
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_REDEFININGDEFAULTGROUP, id = "a")
	public void testRedefiningDefaultGroup() {
		Address address = new Address();
		address.setStreet( "Guldmyntgatan" );
		address.setCity( "Gothenborg" );

		Validator validator = TestUtil.getValidatorUnderTest();

		Set<ConstraintViolation<Address>> constraintViolations = validator.validate( address );
		assertCorrectNumberOfViolations( constraintViolations, 1 );

		ConstraintViolation<Address> violation = constraintViolations.iterator().next();
		assertConstraintViolation( violation, Address.class, null, "zipcode" );
		assertCorrectConstraintViolationMessages( constraintViolations, "Zipcode may not be null" );

		address.setZipcode( "41841" );

		// now the second group in the re-defined default group causes an error
		constraintViolations = validator.validate( address );
		assertCorrectNumberOfViolations( constraintViolations, 1 );

		violation = constraintViolations.iterator().next();
		assertConstraintViolation( violation, Address.class, address, "" );
		assertCorrectConstraintViolationMessages( constraintViolations, "Zip code is not coherent." );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_REDEFININGDEFAULTGROUP, id = "a")
	public void testValidatingAgainstRedefinedDefaultGroup() {
		Car car = new Car();
		car.setType( "A" );

		Validator validator = TestUtil.getValidatorUnderTest();

		// if the group sequence would not be properly redefined there would be no error when validating default.

		Set<ConstraintViolation<Car>> constraintViolations = validator.validate( car );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintViolationMessages(
				constraintViolations, "Car type has to be between 2 and 20 characters."
		);

		constraintViolations = validator.validateProperty( car, "type" );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintViolationMessages(
				constraintViolations, "Car type has to be between 2 and 20 characters."
		);

		constraintViolations = validator.validateValue( Car.class, "type", "A" );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintViolationMessages(
				constraintViolations, "Car type has to be between 2 and 20 characters."
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_REDEFININGDEFAULTGROUP, id = "c")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_REDEFININGDEFAULTGROUP, id = "d")
	public void testGroupSequenceContainingDefault() {
		Address address = new AddressWithDefaultInGroupSequence();
		Validator validator = TestUtil.getValidatorUnderTest();
		try {
			validator.validate( address );
			fail( "It should not be allowed to have Default.class in the group sequence of a class." );
		}
		catch ( GroupDefinitionException e ) {
			// success
		}
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_REDEFININGDEFAULTGROUP, id = "c")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_REDEFININGDEFAULTGROUP, id = "d")
	public void testGroupSequenceWithNoImplicitDefaultGroup() {
		Address address = new AddressWithNoImplicitDefaultGroupSequence();
		Validator validator = TestUtil.getValidatorUnderTest();
		try {
			validator.validate( address );
			fail( "A valid group sequence definition must contain the class itself as implicit default group." );
		}
		catch ( GroupDefinitionException e ) {
			// success
		}
	}

	@GroupSequence({ Default.class, Address.HighLevelCoherence.class })
	public class AddressWithDefaultInGroupSequence extends Address {

	}

	@GroupSequence({ Address.HighLevelCoherence.class })
	public class AddressWithNoImplicitDefaultGroupSequence extends Address {

	}
}
