/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.groups;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertConstraintViolation;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertCorrectConstraintViolationMessages;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertNumberOfViolations;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.pathWith;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.pathsAreEqual;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.GroupDefinitionException;
import javax.validation.Validator;
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
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests for the group and group sequence feature.
 *
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class GroupTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( GroupTest.class )
				.withClasses(
						User.class,
						CreditCard.class,
						Author.class,
						Book.class,
						Animal.class,
						First.class,
						Second.class,
						Last.class,
						Order.class,
						Auditable.class,
						CyclicGroupSequence.class,
						CyclicGroupSequence1.class,
						CyclicGroupSequence2.class
				)
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE, id = "a")
	public void testConstraintWithNoExplicitlySpecifiedGroupBelongsToDefault() {
		Validator validator = TestUtil.getValidatorUnderTest();
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( User.class );
		assertTrue( beanDescriptor.isBeanConstrained() );

		PropertyDescriptor propDesc = beanDescriptor.getConstraintsForProperty( "firstname" );
		assertTrue( propDesc.getConstraintDescriptors().size() == 1 );

		ConstraintDescriptor<?> descriptor = propDesc.getConstraintDescriptors().iterator().next();
		assertTrue( descriptor.getGroups().size() == 1 );
		assertEquals(
				descriptor.getGroups().iterator().next(),
				Default.class,
				"Constraint should implicitly belong to the Default group."
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE, id = "d")
	public void testValidateAgainstDifferentGroups() {
		User user = new User();

		// all fields per default null. Depending on the validation groups there should be  a different amount
		// of constraint failures.
		Validator validator = TestUtil.getValidatorUnderTest();

		Set<ConstraintViolation<User>> constraintViolations = validator.validate( user );
		assertEquals(
				constraintViolations.size(),
				2,
				"There should be two violations against the implicit default group"
		);

		constraintViolations = validator.validate( user, Default.class );
		assertEquals(
				constraintViolations.size(),
				2,
				"There should be two violations against the explicit default group"
		);

		constraintViolations = validator.validate( user, User.Billable.class );
		assertEquals(
				constraintViolations.size(),
				1,
				"There should be one violation against Billable"
		);

		constraintViolations = validator.validate( user, Default.class, User.Billable.class );
		assertEquals(
				constraintViolations.size(),
				3,
				"There should be 3 violation against Default and  Billable"
		);

		constraintViolations = validator.validate( user, User.BuyInOneClick.class );
		assertEquals(
				constraintViolations.size(),
				3,
				"Three violations expected since BuyInOneClick extends Default and Billable"
		);

		constraintViolations = validator.validate( user, User.BuyInOneClick.class, User.Billable.class );
		assertEquals(
				constraintViolations.size(),
				3,
				"BuyInOneClick already contains all other groups. Adding Billable does not change the number of violations"
		);

		constraintViolations = validator.validate( user, User.BuyInOneClick.class, Default.class );
		assertEquals(
				constraintViolations.size(),
				3,
				"BuyInOneClick already contains all other groups. Adding Default does not change the number of violations"
		);

		constraintViolations = validator.validate( user, User.BuyInOneClick.class, Default.class, User.Billable.class );
		assertEquals(
				constraintViolations.size(),
				3,
				"BuyInOneClick already contains all other groups. Adding Billable and Default does not change the number of violations"
		);

		constraintViolations = validator.validate( user, User.Billable.class, User.Billable.class );
		assertEquals(
				constraintViolations.size(),
				1,
				"Adding the same group twice is still only leads to a single violation"
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE, id = "c")
	public void testConstraintCanBelongToMoreThanOneGroup() {
		Validator validator = TestUtil.getValidatorUnderTest();
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( User.class );
		assertTrue( beanDescriptor.isBeanConstrained() );

		PropertyDescriptor propDesc = beanDescriptor.getConstraintsForProperty( "defaultCreditCard" );
		assertTrue( propDesc.getConstraintDescriptors().size() == 1 );

		ConstraintDescriptor<?> descriptor = propDesc.getConstraintDescriptors().iterator().next();
		assertTrue( descriptor.getGroups().size() == 2 );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE, id = "d")
	public void testGroups() {
		Validator validator = TestUtil.getValidatorUnderTest();

		Author author = new Author();
		author.setLastName( "" );
		author.setFirstName( "" );
		Book book = new Book();
		book.setTitle( "" );
		book.setAuthor( author );

		Set<ConstraintViolation<Book>> constraintViolations = validator.validate(
				book, First.class, Second.class, Last.class
		);
		assertEquals( constraintViolations.size(), 3, "Wrong number of constraints" );

		author.setFirstName( "Gavin" );
		author.setLastName( "King" );

		constraintViolations = validator.validate( book, First.class, Second.class, Last.class );
		ConstraintViolation<Book> constraintViolation = constraintViolations.iterator().next();
		assertEquals( constraintViolations.size(), 1, "Wrong number of constraints" );
		assertEquals( constraintViolation.getRootBean(), book, "Wrong root entity" );
		assertEquals( constraintViolation.getInvalidValue(), book.getTitle(), "Wrong value" );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "title" )
		);

		book.setTitle( "Hibernate Persistence with JPA" );
		book.setSubtitle( "Revised Edition of Hibernate in Action" );

		constraintViolations = validator.validate( book, First.class, Second.class, Last.class );
		assertNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintViolationMessages(
				constraintViolations, "The book's subtitle can only have 30 characters"
		);
		constraintViolation = constraintViolations.iterator().next();
		assertEquals( constraintViolation.getRootBean(), book, "Wrong root entity" );
		assertEquals( constraintViolation.getInvalidValue(), book.getSubtitle(), "Wrong value" );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "subtitle" )
		);

		book.setSubtitle( "Revised Edition" );
		author.setCompany( "JBoss a division of RedHat" );

		constraintViolations = validator.validate( book, First.class, Second.class, Last.class );
		constraintViolation = constraintViolations.iterator().next();
		assertNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintViolationMessages(
				constraintViolations, "The company name can only have 20 characters"
		);
		assertEquals( constraintViolation.getRootBean(), book, "Wrong root entity" );
		assertEquals( constraintViolation.getInvalidValue(), author.getCompany(), "Wrong value" );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "author" )
						.property( "company" )
		);

		author.setCompany( "JBoss" );

		constraintViolations = validator.validate( book, First.class, Second.class, Last.class );
		assertEquals( constraintViolations.size(), 0, "Wrong number of constraints" );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPSEQUENCE, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPSEQUENCE, id = "d")
	public void testGroupSequence() {
		Validator validator = TestUtil.getValidatorUnderTest();

		Author author = new Author();
		author.setLastName( "" );
		author.setFirstName( "" );
		Book book = new Book();
		book.setAuthor( author );

		Set<ConstraintViolation<Book>> constraintViolations = validator.validate( book, Book.All.class );
		assertEquals( constraintViolations.size(), 2, "Wrong number of constraints" );

		author.setFirstName( "Gavin" );
		author.setLastName( "King" );

		constraintViolations = validator.validate( book, Book.All.class );
		assertNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintViolationMessages( constraintViolations, "The book title cannot be null" );
		ConstraintViolation<Book> constraintViolation = constraintViolations.iterator().next();
		assertEquals( constraintViolation.getRootBean(), book, "Wrong root entity" );
		assertEquals( constraintViolation.getInvalidValue(), book.getTitle(), "Wrong value" );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "title" )
		);

		book.setTitle( "Hibernate Persistence with JPA" );
		book.setSubtitle( "Revised Edition of Hibernate in Action" );

		constraintViolations = validator.validate( book, Book.All.class );
		assertEquals( constraintViolations.size(), 1, "Wrong number of constraints" );

		book.setSubtitle( "Revised Edition" );
		author.setCompany( "JBoss a division of RedHat" );

		constraintViolations = validator.validate( book, Book.All.class );
		assertEquals( constraintViolations.size(), 1, "Wrong number of constraints" );

		author.setCompany( "JBoss" );

		constraintViolations = validator.validate( book, Book.All.class );
		assertEquals( constraintViolations.size(), 0, "Wrong number of constraints" );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE, id = "c")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE, id = "a")
	public void testValidationFailureInMultipleGroups() {
		Validator validator = TestUtil.getValidatorUnderTest();
		Animal elephant = new Animal();
		elephant.setName( "" );
		elephant.setDomain( Animal.Domain.EUKARYOTA );

		Set<ConstraintViolation<Animal>> constraintViolations = validator.validate(
				elephant, First.class, Second.class
		);
		assertEquals(
				constraintViolations.size(),
				1,
				"The should be only one invalid constraint even though the constraint belongs to both groups"
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE, id = "d")
	public void testGroupSequenceFollowedByGroup() {
		User user = new User();
		user.setFirstname( "Foo" );
		user.setLastname( "Bar" );
		user.setPhoneNumber( "+46 123-456" );

		Validator validator = TestUtil.getValidatorUnderTest();

		Set<ConstraintViolation<User>> constraintViolations = validator.validate(
				user, User.BuyInOneClick.class, User.Optional.class
		);
		assertEquals(
				constraintViolations.size(),
				2,
				"There should be two violations against the implicit default group"
		);

		for ( ConstraintViolation<User> constraintViolation : constraintViolations ) {
			if ( pathsAreEqual(
					constraintViolation.getPropertyPath(), pathWith().property( "defaultCreditCard" )
			) ) {
				assertConstraintViolation(
						constraintViolation,
						User.class,
						null,
						pathWith().property( "defaultCreditCard" )
				);
			}
			else if ( pathsAreEqual(
					constraintViolation.getPropertyPath(), pathWith().property( "phoneNumber" )
			) ) {
				assertConstraintViolation(
						constraintViolation,
						User.class,
						"+46 123-456",
						pathWith().property( "phoneNumber" )
				);
			}
			else {
				fail( "Unexpected violation" );
			}
		}
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_IMPLICITGROUPING, id = "a")
	public void testImplicitGrouping() {
		Validator validator = TestUtil.getValidatorUnderTest();
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( Order.class );
		assertTrue( beanDescriptor.isBeanConstrained() );

		// validating the Default Group should validate all 5 constraints
		Order order = new Order();
		Set<ConstraintViolation<Order>> violations = validator.validate( order );
		assertTrue( violations.size() == 5, "All 5 NotNull constraints should fail." );

		// use implicit group Auditable  - only the constraints defined on Auditable should be validated
		violations = validator.validate( order, Auditable.class );
		assertTrue( violations.size() == 4, "All 4 NotNull constraints on Auditable should fail." );
	}

	@Test(expectedExceptions = GroupDefinitionException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPSEQUENCE, id = "e")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPSEQUENCE, id = "f")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPSEQUENCE, id = "i")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_FORMALDEFINITION, id = "j")
	@SpecAssertion(section = Sections.EXCEPTION_GROUPDEFINITION, id = "a")
	public void testCyclicGroupSequence() {
		Validator validator = TestUtil.getValidatorUnderTest();
		validator.validate( new Order(), CyclicGroupSequence.class );
	}
}
