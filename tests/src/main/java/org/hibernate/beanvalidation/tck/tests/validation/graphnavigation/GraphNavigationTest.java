/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation.graphnavigation;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertConstraintViolation;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertCorrectConstraintViolationMessages;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertNumberOfViolations;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.pathWith;
import static org.testng.Assert.assertEquals;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

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
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class GraphNavigationTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( GraphNavigationTest.class )
				.build();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "a"),
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "l"),
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_GRAPHVALIDATION, id = "a"),
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_GRAPHVALIDATION, id = "b")
	})
	public void testGraphNavigationDeterminism() {
		// build the test object graph
		User user = new User( "John", "Doe" );

		Address address1 = new Address( null, "11122", "Stockholm" );
		address1.setInhabitant( user );

		Address address2 = new Address( "Kungsgatan 5", "11122", "Stockholm" );
		address2.setInhabitant( user );

		user.addAddress( address1 );
		user.addAddress( address2 );

		Order order = new Order( 1 );
		order.setShippingAddress( address1 );
		order.setBillingAddress( address2 );
		order.setCustomer( user );

		OrderLine line1 = new OrderLine( order, 42 );
		OrderLine line2 = new OrderLine( order, 101 );
		order.addOrderLine( line1 );
		order.addOrderLine( line2 );

		Validator validator = TestUtil.getValidatorUnderTest();

		Set<ConstraintViolation<Order>> constraintViolations = validator.validate( order );
		assertNumberOfViolations( constraintViolations, 3 );
		assertThat(constraintViolations).containsOnlyPaths(
				pathWith()
						.property( "shippingAddress" )
						.property( "addressline1" ),
				pathWith()
						.property( "customer" )
						.property( "addresses" )
						.property( "addressline1", true, null, 0, List.class, 0 ),
				pathWith()
						.property( "billingAddress" )
						.property( "inhabitant" )
						.property( "addresses" )
						.property( "addressline1", true, null, 0, List.class, 0 )
		);
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "g"),
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_GRAPHVALIDATION, id = "d")
	})
	public void testNoEndlessLoop() {
		User john = new User( "John", null );
		john.knows( john );

		Validator validator = TestUtil.getValidatorUnderTest();

		Set<ConstraintViolation<User>> constraintViolations = validator.validate( john );
		assertEquals( constraintViolations.size(), 1, "Wrong number of constraints" );
		assertConstraintViolation(
				constraintViolations.iterator().next(), User.class, null, pathWith().property( "lastName" )
		);


		User jane = new User( "Jane", "Doe" );
		jane.knows( john );
		john.knows( jane );

		constraintViolations = validator.validate( john );
		assertNumberOfViolations( constraintViolations, 1 );
		assertConstraintViolation(
				constraintViolations.iterator().next(), User.class, null, pathWith().property( "lastName" )
		);

		constraintViolations = validator.validate( jane );
		assertNumberOfViolations( constraintViolations, 1 );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "knowsUser" )
						.property( "lastName", true, null, 0, List.class, 0 )
		);

		john.setLastName( "Doe" );
		constraintViolations = validator.validate( john );
		assertNumberOfViolations( constraintViolations, 0 );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "b")
	public void testTypeOfContainedValueIsDeterminedAtRuntime() {
		SingleCage cage = new SingleCage();
		Elephant elephant = new Elephant();
		elephant.setWeight( 500 );
		cage.setContainAnimal( elephant );

		Validator validator = TestUtil.getValidatorUnderTest();
		Set<ConstraintViolation<SingleCage>> constraintViolations = validator.validate( cage );
		assertNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintViolationMessages( constraintViolations, "An elephant weighs at least 1000 kg" );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "f"),
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "i")
	})
	public void testContainedSet() {
		MultiCage cage = new MultiCage();
		cage.addAnimal( new Zebra( null ) );
		cage.addAnimal( new Zebra( null ) );

		Validator validator = TestUtil.getValidatorUnderTest();
		Set<ConstraintViolation<MultiCage>> constraintViolations = validator.validate( cage );
		assertNumberOfViolations( constraintViolations, 2 );
		assertCorrectConstraintViolationMessages(
				constraintViolations,
				"A zebra needs a name",
				"A zebra needs a name"
		);
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "c"),
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "i")
	})
	public void testContainedIterable() {
		GameReserve<Zebra> reserve = new GameReserve<Zebra>();
		Herd<Zebra> zebraHerd = new Herd<Zebra>();
		zebraHerd.addAnimal( new Zebra( null ) );
		zebraHerd.addAnimal( new Zebra( null ) );
		reserve.setHerd( zebraHerd );

		Validator validator = TestUtil.getValidatorUnderTest();
		Set<ConstraintViolation<GameReserve<Zebra>>> constraintViolations = validator.validate( reserve );
		assertNumberOfViolations( constraintViolations, 2 );
		assertCorrectConstraintViolationMessages(
				constraintViolations,
				"A zebra needs a name",
				"A zebra needs a name"
		);
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "e"),
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "k")
	})
	public void testTypeOfContainedValuesIsDeterminedAtRuntime() {
		Zoo zoo = new Zoo();
		Elephant elephant = new Elephant();
		elephant.setWeight( 500 );
		zoo.addAnimal( elephant );

		Condor condor = new Condor();
		condor.setWingspan( 200 );
		zoo.addAnimal( condor );

		Validator validator = TestUtil.getValidatorUnderTest();
		Set<ConstraintViolation<Zoo>> constraintViolations = validator.validate( zoo );
		assertNumberOfViolations( constraintViolations, 2 );
		assertCorrectConstraintViolationMessages(
				constraintViolations,
				"The wingspan of a condor is at least 250 cm",
				"An elephant weighs at least 1000 kg"
		);

	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "h"),
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "i"),
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "j")
	})
	public void testContainedMap() {
		AnimalCaretaker caretaker = new AnimalCaretaker();
		Elephant elephant = new Elephant();
		elephant.setWeight( 500 );
		caretaker.addAnimal( "Jumbo", elephant );

		Condor condor = new Condor();
		condor.setWingspan( 200 );
		caretaker.addAnimal( "Andes", condor );

		Validator validator = TestUtil.getValidatorUnderTest();
		Set<ConstraintViolation<AnimalCaretaker>> constraintViolations = validator.validate( caretaker );
		assertNumberOfViolations( constraintViolations, 2 );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "caresFor" )
						.property( "weight", true, "Jumbo", null, Map.class, 1 ),
				pathWith()
						.property( "caresFor" )
						.property( "wingspan", true, "Andes", null, Map.class, 1 )
		);
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPSEQUENCE, id = "b"),
			@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPSEQUENCE, id = "d")
	})
	public void testFullGraphValidationBeforeNextGroupInSequence() {
		Parent p = new Parent();
		p.setChild( new Child() );
		Validator validator = TestUtil.getValidatorUnderTest();
		Set<ConstraintViolation<Parent>> errors = validator.validate( p, Parent.ProperOrder.class );
		assertNumberOfViolations( errors, 1 );
		assertThat( errors ).containsOnlyPaths(
				pathWith()
						.property( "child" )
						.property( "name" )
		);

		p.getChild().setName( "Emmanuel" );
		errors = validator.validate( p, Parent.ProperOrder.class );
		assertNumberOfViolations( errors, 1 );
		assertThat( errors ).containsOnlyPaths(
				pathWith()
						.property( "name" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_GRAPHVALIDATION, id = "c")
	public void testNullReferencesGetIgnored() {
		Parent p = new Parent();
		Validator validator = TestUtil.getValidatorUnderTest();
		Set<ConstraintViolation<Parent>> errors = validator.validateProperty( p, "child" );
		assertNumberOfViolations( errors, 0 );
	}
}
