package org.hibernate.beanvalidation.tck.tests.constraints.builtinconstraints;

import static org.hibernate.beanvalidation.tck.util.TestUtil.assertConstraintViolation;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectNumberOfViolations;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests for {@link Null} and {@link NotNull} built-in constraints.
 *
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class NullNotNullConstraintsTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClass( NullNotNullConstraintsTest.class )
				.build();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "7", id = "a"),
			@SpecAssertion(section = "7", id = "c")
	})
	public void testNullConstraint() {
		Validator validator = TestUtil.getValidatorUnderTest();
		NullDummyEntity dummy = new NullDummyEntity();
		Object foo = new Object();
		dummy.setProperty( foo );
		Set<ConstraintViolation<NullDummyEntity>> constraintViolations = validator.validate( dummy );
		assertCorrectNumberOfViolations( constraintViolations, 1 );

		assertConstraintViolation(
				constraintViolations.iterator().next(), NullDummyEntity.class, foo, "property"
		);

		dummy.setProperty( null );
		constraintViolations = validator.validate( dummy );
		assertCorrectNumberOfViolations( constraintViolations, 0 );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "7", id = "a"),
			@SpecAssertion(section = "7", id = "d")
	})
	public void testNotNullConstraint() {
		Validator validator = TestUtil.getValidatorUnderTest();
		NotNullDummyEntity dummy = new NotNullDummyEntity();
		Set<ConstraintViolation<NotNullDummyEntity>> constraintViolations = validator.validate( dummy );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertConstraintViolation(
				constraintViolations.iterator().next(), NotNullDummyEntity.class, null, "property"
		);

		dummy.setProperty( new Object() );
		constraintViolations = validator.validate( dummy );
		assertCorrectNumberOfViolations( constraintViolations, 0 );
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
