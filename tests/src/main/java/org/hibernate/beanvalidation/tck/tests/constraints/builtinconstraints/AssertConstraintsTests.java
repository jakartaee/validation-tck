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
import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.AssertTrue;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests for {@link AssertTrue} and {@link AssertFalse} built-in constraints.
 *
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "3.0.0")
public class AssertConstraintsTests extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( AssertConstraintsTests.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS_ASSERTTRUE, id = "a")
	public void testAssertTrueConstraint() {
		Validator validator = TestUtil.getValidatorUnderTest();
		AssertTrueDummyEntity dummy = new AssertTrueDummyEntity();

		Set<ConstraintViolation<AssertTrueDummyEntity>> constraintViolations = validator.validate( dummy );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( AssertTrue.class )
						.withRootBeanClass( AssertTrueDummyEntity.class )
						.withInvalidValue( false )
						.withProperty( "primitiveBoolean" )
		);

		dummy.setPrimitiveBoolean( true );
		dummy.setObjectBoolean( Boolean.FALSE );

		constraintViolations = validator.validate( dummy );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( AssertTrue.class )
						.withRootBeanClass( AssertTrueDummyEntity.class )
						.withInvalidValue( Boolean.FALSE )
						.withProperty( "objectBoolean" )
		);

		dummy.setObjectBoolean( Boolean.TRUE );
		constraintViolations = validator.validate( dummy );
		assertNoViolations( constraintViolations );
	}

	@Test
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS_ASSERTFALSE, id = "a")
	public void testAssertFalseConstraint() {
		Validator validator = TestUtil.getValidatorUnderTest();
		AssertFalseDummyEntity dummy = new AssertFalseDummyEntity();
		dummy.setPrimitiveBoolean( true );

		Set<ConstraintViolation<AssertFalseDummyEntity>> constraintViolations = validator.validate( dummy );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( AssertFalse.class )
						.withRootBeanClass( AssertFalseDummyEntity.class )
						.withInvalidValue( true )
						.withProperty( "primitiveBoolean" )
		);

		dummy.setPrimitiveBoolean( false );
		dummy.setObjectBoolean( Boolean.TRUE );

		constraintViolations = validator.validate( dummy );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( AssertFalse.class )
						.withRootBeanClass( AssertFalseDummyEntity.class )
						.withInvalidValue( Boolean.TRUE )
						.withProperty( "objectBoolean" )
		);

		dummy.setObjectBoolean( Boolean.FALSE );
		constraintViolations = validator.validate( dummy );
		assertNoViolations( constraintViolations );
	}

	private static class AssertTrueDummyEntity {
		@AssertTrue
		private boolean primitiveBoolean;

		@AssertTrue
		private Boolean objectBoolean;

		public void setPrimitiveBoolean(boolean primitiveBoolean) {
			this.primitiveBoolean = primitiveBoolean;
		}

		public void setObjectBoolean(Boolean objectBoolean) {
			this.objectBoolean = objectBoolean;
		}
	}

	private static class AssertFalseDummyEntity {
		@AssertFalse
		private boolean primitiveBoolean;

		@AssertFalse
		private Boolean objectBoolean;

		public void setPrimitiveBoolean(boolean primitiveBoolean) {
			this.primitiveBoolean = primitiveBoolean;
		}

		public void setObjectBoolean(Boolean objectBoolean) {
			this.objectBoolean = objectBoolean;
		}
	}

}
