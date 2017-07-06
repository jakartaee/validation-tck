/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.builtinconstraints;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertNoViolations;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests for {@link Null} and {@link NotNull} built-in constraints.
 *
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class NullNotNullConstraintsTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( NullNotNullConstraintsTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS_NULL, id = "a")
	public void testNullConstraint() {
		Validator validator = TestUtil.getValidatorUnderTest();
		NullDummyEntity dummy = new NullDummyEntity();
		Object foo = new Object();
		dummy.setProperty( foo );
		Set<ConstraintViolation<NullDummyEntity>> constraintViolations = validator.validate( dummy );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Null.class )
						.withProperty( "property" )
						.withInvalidValue( foo )
						.withRootBeanClass( NullDummyEntity.class )
		);

		dummy.setProperty( null );
		constraintViolations = validator.validate( dummy );
		assertNoViolations( constraintViolations );
	}

	@Test
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS_NOTNULL, id = "a")
	public void testNotNullConstraint() {
		Validator validator = TestUtil.getValidatorUnderTest();
		NotNullDummyEntity dummy = new NotNullDummyEntity();
		Set<ConstraintViolation<NotNullDummyEntity>> constraintViolations = validator.validate( dummy );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withProperty( "property" )
						.withInvalidValue( null )
						.withRootBeanClass( NotNullDummyEntity.class )
		);

		dummy.setProperty( new Object() );
		constraintViolations = validator.validate( dummy );
		assertNoViolations( constraintViolations );
	}

	private static class NullDummyEntity {
		@Null
		private Object property;

		public void setProperty(Object property) {
			this.property = property;
		}
	}

	private static class NotNullDummyEntity {
		@NotNull
		private Object property;

		public void setProperty(Object property) {
			this.property = property;
		}
	}

}
