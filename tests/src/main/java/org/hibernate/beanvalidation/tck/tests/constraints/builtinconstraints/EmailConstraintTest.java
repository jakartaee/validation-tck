/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.builtinconstraints;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertCorrectConstraintTypes;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertNumberOfViolations;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.pathWith;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.Email;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests for {@link Email} built-in constraint.
 * <p>
 * Exact semantics of what makes up a valid email address are left to Bean Validation providers so we only test very
 * basic example to be sure we have an email validator declared.
 *
 * @author Guillaume Smet
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class EmailConstraintTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( EmailConstraintTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "v")
	public void testEmailConstraint() {
		Validator validator = TestUtil.getValidatorUnderTest();
		EmailDummyEntity foo = new EmailDummyEntity();

		Set<ConstraintViolation<EmailDummyEntity>> constraintViolations = validator.validate( foo );
		assertNumberOfViolations( constraintViolations, 0 );

		foo.email = "4 8";
		constraintViolations = validator.validate( foo );
		assertCorrectConstraintTypes( constraintViolations, Email.class );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "email" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "v")
	public void testEmailConstraintOnStringBuilder() {
		// @Email has to support CharSequence so let's also try a StringBuilder
		Validator validator = TestUtil.getValidatorUnderTest();
		EmailStringBuilderDummyEntity foo = new EmailStringBuilderDummyEntity();

		Set<ConstraintViolation<EmailStringBuilderDummyEntity>> constraintViolations = validator.validate( foo );
		assertNumberOfViolations( constraintViolations, 0 );

		foo.email = new StringBuilder( "4 8" );
		constraintViolations = validator.validate( foo );
		assertCorrectConstraintTypes( constraintViolations, Email.class );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "email" )
		);
	}

	private class EmailDummyEntity {

		@Email
		private String email;
	}

	private class EmailStringBuilderDummyEntity {

		@Email
		private StringBuilder email;
	}
}
