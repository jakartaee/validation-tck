/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.util;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.pathWith;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import org.testng.annotations.Test;

/**
 * Tests for {@link org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.ConstraintViolationSetAssert}.
 *
 * @author Marko Bekhta
 * @author Guillaume Smet
 */
public class ConstraintViolationAssertTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( ConstraintViolationAssertTest.class )
				.withClass( Foo.class )
				.build();
	}

	@Test
	public void testConstraintTypeCorrect() {
		Set<ConstraintViolation<Foo>> violations = TestUtil.getValidatorUnderTest().validate( new Foo( null ) );
		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class )
		);
	}

	@Test(expectedExceptions = AssertionError.class)
	public void testConstraintTypeIncorrect() {
		Set<ConstraintViolation<Foo>> violations = TestUtil.getValidatorUnderTest().validate( new Foo( null ) );
		assertThat( violations ).containsOnlyViolations(
				violationOf( Min.class )
		);
	}

	@Test
	public void testMessageCorrect() {
		Set<ConstraintViolation<Foo>> violations = TestUtil.getValidatorUnderTest().validate( new Foo( null ) );
		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class ).withMessage( "message" )
		);
	}

	@Test(expectedExceptions = AssertionError.class)
	public void testMessageIncorrect() {
		Set<ConstraintViolation<Foo>> violations = TestUtil.getValidatorUnderTest().validate( new Foo( null ) );
		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class ).withMessage( "wrong message" )
		);
	}

	@Test
	public void testRootBeanClassCorrect() {
		Set<ConstraintViolation<Foo>> violations = TestUtil.getValidatorUnderTest().validate( new Foo( null ) );
		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class ).withRootBeanClass( Foo.class )
		);
	}

	@Test(expectedExceptions = AssertionError.class)
	public void testRootBeanClassIncorrect() {
		Set<ConstraintViolation<Foo>> violations = TestUtil.getValidatorUnderTest().validate( new Foo( null ) );
		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class ).withRootBeanClass( ConstraintViolationAssertTest.class )
		);
	}

	@Test
	public void testInvalidValueCorrect() {
		Set<ConstraintViolation<Foo>> violations = TestUtil.getValidatorUnderTest().validate( new Foo( null ) );
		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class ).withInvalidValue( null )
		);
	}

	@Test(expectedExceptions = AssertionError.class)
	public void testInvalidValueIncorrect() {
		Set<ConstraintViolation<Foo>> violations = TestUtil.getValidatorUnderTest().validate( new Foo( null ) );
		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class ).withInvalidValue( "not null" )
		);
	}

	@Test
	public void testPropertyCorrect() {
		Set<ConstraintViolation<Foo>> violations = TestUtil.getValidatorUnderTest().validate( new Foo( null ) );
		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class ).withProperty( "string" )
		);
	}

	@Test(expectedExceptions = AssertionError.class)
	public void testPropertyIncorrect() {
		Set<ConstraintViolation<Foo>> violations = TestUtil.getValidatorUnderTest().validate( new Foo( null ) );
		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class ).withProperty( "wrongPropertyName" )
		);
	}

	@Test
	public void testPropertyPathCorrect() throws Exception {
		Set<ConstraintViolation<Foo>> violations = TestUtil.getValidatorUnderTest().forExecutables()
				.validateReturnValue( new Foo( null ), Foo.class.getDeclaredMethod( "bar" ), null );
		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.method( "bar" )
								.returnValue()
						)
		);
	}

	@Test(expectedExceptions = AssertionError.class)
	public void testPropertyPathIncorrect() throws Exception {
		Set<ConstraintViolation<Foo>> violations = TestUtil.getValidatorUnderTest().forExecutables()
				.validateReturnValue( new Foo( null ), Foo.class.getDeclaredMethod( "bar" ), null );
		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.method( "bar" )
								.property( "nonExistingProperty" )
								.returnValue()
						)
		);
	}

	@Test
	public void testLeafBeanCorrect() {
		Foo foo = new Foo( null );
		Set<ConstraintViolation<Foo>> violations = TestUtil.getValidatorUnderTest().validate( foo );
		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class ).withLeafBean( foo )
		);
	}

	@Test(expectedExceptions = AssertionError.class)
	public void testLeafBeanIncorrect() {
		Set<ConstraintViolation<Foo>> violations = TestUtil.getValidatorUnderTest().validate( new Foo( null ) );
		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class ).withLeafBean( "not the leaf bean" )
		);
	}

	private static class Foo {

		@NotNull(message = "message")
		private final String string;

		public Foo(String string) {
			this.string = string;
		}

		@NotNull
		public String bar() {
			return null;
		}
	}
}
