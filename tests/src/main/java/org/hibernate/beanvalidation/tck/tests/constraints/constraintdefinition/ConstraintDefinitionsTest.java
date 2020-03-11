/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.constraintdefinition;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertNoViolations;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;
import static org.testng.Assert.assertEquals;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.Default;
import jakarta.validation.metadata.ConstraintDescriptor;

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
 * @author Guillaume Smet
 */
@SpecVersion(spec = "beanvalidation", version = "3.0.0")
public class ConstraintDefinitionsTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( ConstraintDefinitionsTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_MULTIPLECONSTRAINTS, id = "a")
	public void testConstraintWithCustomAttributes() {
		Validator validator = TestUtil.getValidatorUnderTest();
		Set<ConstraintDescriptor<?>> descriptors = validator.getConstraintsForClass( Person.class )
				.getConstraintsForProperty( "lastName" )
				.getConstraintDescriptors();

		assertEquals( descriptors.size(), 2, "There should be two constraints on the lastName property." );
		for ( ConstraintDescriptor<?> descriptor : descriptors ) {
			assertEquals(
					descriptor.getAnnotation().annotationType().getName(),
					AlwaysValid.class.getName(),
					"Wrong annotation type."
			);
		}

		Set<ConstraintViolation<Person>> constraintViolations = validator.validate( new Person( "John", "Doe" ) );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( AlwaysValid.class )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_MULTIPLECONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_MULTIPLECONSTRAINTS, id = "b")
	public void testRepeatableConstraint() {
		Validator validator = TestUtil.getValidatorUnderTest();
		Set<ConstraintDescriptor<?>> descriptors = validator.getConstraintsForClass( Movie.class )
				.getConstraintsForProperty( "title" )
				.getConstraintDescriptors();

		assertEquals( descriptors.size(), 2, "There should be two constraints on the title property." );
		for ( ConstraintDescriptor<?> descriptor : descriptors ) {
			assertEquals(
					descriptor.getAnnotation().annotationType().getName(),
					Size.class.getName(),
					"Wrong annotation type."
			);
		}

		Set<ConstraintViolation<Movie>> constraintViolations = validator.validate( new Movie( "Title" ) );
		assertNoViolations( constraintViolations );

		constraintViolations = validator.validate( new Movie( "A" ) );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Size.class )
		);

		constraintViolations = validator.validate( new Movie( "A movie title far too long that does not respect the constraint" ) );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Size.class )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES_GROUPS, id = "d")
	public void testDefaultGroupAssumedWhenNoGroupsSpecified() {
		Validator validator = TestUtil.getValidatorUnderTest();
		ConstraintDescriptor<?> descriptor = validator.getConstraintsForClass( Person.class )
				.getConstraintsForProperty( "firstName" )
				.getConstraintDescriptors()
				.iterator()
				.next();

		Set<Class<?>> groups = descriptor.getGroups();
		assertEquals( groups.size(), 1, "The group set should only contain one entry." );
		assertEquals( groups.iterator().next(), Default.class, "The Default group should be returned." );
	}
}
