/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.builtinconstraints;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Email;

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
 * Exact semantics of what makes up a valid email address are left to Jakarta Bean Validation providers so we use the regexp
 * mechanism to test an invalid email.
 *
 * @author Guillaume Smet
 */
@SpecVersion(spec = "beanvalidation", version = "3.0.0")
public class EmailConstraintTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( EmailConstraintTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS_EMAIL, id = "a")
	public void testEmailConstraint() {
		Validator validator = TestUtil.getValidatorUnderTest();
		EmailDummyEntity foo = new EmailDummyEntity( "test@example.com" );

		Set<ConstraintViolation<EmailDummyEntity>> constraintViolations = validator.validate( foo );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Email.class ).withProperty( "email" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS_EMAIL, id = "a")
	public void testEmailConstraintOnStringBuilder() {
		// @Email has to support CharSequence so let's also try a StringBuilder
		Validator validator = TestUtil.getValidatorUnderTest();
		EmailStringBuilderDummyEntity foo = new EmailStringBuilderDummyEntity( new StringBuilder( "test@example.com" ) );

		Set<ConstraintViolation<EmailStringBuilderDummyEntity>> constraintViolations = validator.validate( foo );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Email.class ).withProperty( "email" )
		);
	}

	private class EmailDummyEntity {

		@Email(regexp = "^invalid$")
		private String email;

		private EmailDummyEntity(String email) {
			this.email = email;
		}
	}

	private class EmailStringBuilderDummyEntity {

		@Email(regexp = "^invalid$")
		private StringBuilder email;

		private EmailStringBuilderDummyEntity(StringBuilder email) {
			this.email = email;
		}
	}
}
