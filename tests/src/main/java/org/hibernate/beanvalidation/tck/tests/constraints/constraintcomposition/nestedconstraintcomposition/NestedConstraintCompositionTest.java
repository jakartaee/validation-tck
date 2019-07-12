/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.constraintcomposition.nestedconstraintcomposition;


import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests for error creation for nested composed constraints with different variations of @ReportAsSingleViolation.
 *
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class NestedConstraintCompositionTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( NestedConstraintCompositionTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTCOMPOSITION, id = "l")
	public void testCompositeConstraint1WithNestedConstraintSingleViolation() {
		Validator validator = TestUtil.getValidatorUnderTest();
		DummyEntity1 dummy = new DummyEntity1( "" );
		Set<ConstraintViolation<DummyEntity1>> constraintViolations = validator.validate( dummy );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Pattern.class ).withMessage( "Pattern must match abc" ),
				violationOf( NestedConstraintSingleViolation.class ).withMessage( "NestedConstraintSingleViolation failed." )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTCOMPOSITION, id = "l")
	public void testCompositeConstraint2WithNestedConstraintSingleViolation() {
		Validator validator = TestUtil.getValidatorUnderTest();
		DummyEntity2 dummy = new DummyEntity2( "" );
		Set<ConstraintViolation<DummyEntity2>> constraintViolations = validator.validate( dummy );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( CompositeConstraint2.class ).withMessage( "CompositeConstraint2 failed." )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTCOMPOSITION, id = "l")
	public void testCompositeConstraint3WithNestedConstraint() {
		Validator validator = TestUtil.getValidatorUnderTest();
		DummyEntity3 dummy = new DummyEntity3( "" );
		Set<ConstraintViolation<DummyEntity3>> constraintViolations = validator.validate( dummy );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Pattern.class ).withMessage( "Pattern must match abc" ),
				violationOf( Pattern.class ).withMessage( "Pattern must match ..." ),
				violationOf( Size.class ).withMessage( "Size must be 3" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTCOMPOSITION, id = "l")
	public void testCompositeConstraint4WithNestedConstraintSingleViolation() {
		Validator validator = TestUtil.getValidatorUnderTest();
		DummyEntity4 dummy = new DummyEntity4( "" );
		Set<ConstraintViolation<DummyEntity4>> constraintViolations = validator.validate( dummy );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( CompositeConstraint4.class ).withMessage( "CompositeConstraint4 failed." )
		);
	}

	class DummyEntity1 {
		@CompositeConstraint1
		String string;

		DummyEntity1(String s) {
			this.string = s;
		}
	}

	class DummyEntity2 {
		@CompositeConstraint2
		String string;

		DummyEntity2(String s) {
			this.string = s;
		}
	}

	class DummyEntity3 {
		@CompositeConstraint3
		String string;

		DummyEntity3(String s) {
			this.string = s;
		}
	}

	class DummyEntity4 {
		@CompositeConstraint4
		String string;

		DummyEntity4(String s) {
			this.string = s;
		}
	}
}
