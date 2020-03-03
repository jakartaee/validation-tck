/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.builtinconstraints;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertNoViolations;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotBlank;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests for {@link NotBlank} built-in constraint.
 *
 * @author Guillaume Smet
 */
@SpecVersion(spec = "beanvalidation", version = "3.0.0")
public class NotBlankConstraintTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( NotBlankConstraintTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS_NOTBLANK, id = "a")
	public void testNotBlankConstraint() {
		Validator validator = getValidator();
		NotBlankDummyEntity foo = new NotBlankDummyEntity();

		Set<ConstraintViolation<NotBlankDummyEntity>> constraintViolations = validator.validate( foo );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotBlank.class ).withProperty( "name" )
		);

		foo.name = "";
		constraintViolations = validator.validate( foo );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotBlank.class ).withProperty( "name" )
		);

		foo.name = " ";
		constraintViolations = validator.validate( foo );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotBlank.class ).withProperty( "name" )
		);

		foo.name = "\t";
		constraintViolations = validator.validate( foo );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotBlank.class ).withProperty( "name" )
		);

		foo.name = "\n";
		constraintViolations = validator.validate( foo );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotBlank.class ).withProperty( "name" )
		);

		foo.name = "john doe";
		constraintViolations = validator.validate( foo );
		assertNoViolations( constraintViolations );
	}

	@Test
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS_NOTBLANK, id = "a")
	public void testNotBlankConstraintOnStringBuilder() {
		// @NotBlank has to support CharSequence so let's also try a StringBuilder
		Validator validator = getValidator();
		NotBlankStringBuilderDummyEntity foo = new NotBlankStringBuilderDummyEntity();

		Set<ConstraintViolation<NotBlankStringBuilderDummyEntity>> constraintViolations = validator.validate( foo );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotBlank.class ).withProperty( "name" )
		);

		foo.name = new StringBuilder( " " );
		constraintViolations = validator.validate( foo );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotBlank.class ).withProperty( "name" )
		);

		foo.name = new StringBuilder( "john doe" );
		constraintViolations = validator.validate( foo );
		assertNoViolations( constraintViolations );
	}

	private class NotBlankDummyEntity {

		@NotBlank
		private String name;
	}

	private class NotBlankStringBuilderDummyEntity {

		@NotBlank
		private StringBuilder name;
	}
}
