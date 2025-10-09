/**
 * Jakarta Validation TCK
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
import jakarta.validation.constraints.Pattern;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests for {@link Pattern} built-in constraint.
 *
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
public class PatternConstraintTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( PatternConstraintTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS_PATTERN, id = "a")
	public void testPatternConstraint() {
		Validator validator = TestUtil.getValidatorUnderTest();
		PatternDummyEntity dummy = new PatternDummyEntity();

		Set<ConstraintViolation<PatternDummyEntity>> constraintViolations = validator.validate( dummy );
		assertNoViolations( constraintViolations );

		dummy.pattern = "ab cd";
		constraintViolations = validator.validate( dummy );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Pattern.class )
						.withProperty( "pattern" )
						.withInvalidValue( "ab cd" )
						.withRootBeanClass( PatternDummyEntity.class )
		);

		dummy.pattern = "wc 00";
		constraintViolations = validator.validate( dummy );
		assertNoViolations( constraintViolations );
	}

	private static class PatternDummyEntity {
		@Pattern(regexp = "[a-z][a-z] \\d\\d")
		private String pattern;
	}

}
