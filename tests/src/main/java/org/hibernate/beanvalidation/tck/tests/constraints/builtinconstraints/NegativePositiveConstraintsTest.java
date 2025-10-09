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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Negative;
import jakarta.validation.constraints.NegativeOrZero;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests for {@link Negative}, {@link NegativeOrZero}, {@link Positive} and {@link PositiveOrZero} built-in constraints.
 *
 * @author Guillaume Smet
 */
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
public class NegativePositiveConstraintsTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( NegativePositiveConstraintsTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS_NEGATIVE, id = "a")
	public void testNegativeConstraint() {
		Validator validator = TestUtil.getValidatorUnderTest();
		NegativeEntity dummy = new NegativeEntity();

		Set<ConstraintViolation<NegativeEntity>> constraintViolations = validator.validate( dummy );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Negative.class ).withProperty( "bytePrimitive" ),
				violationOf( Negative.class ).withProperty( "intPrimitive" ),
				violationOf( Negative.class ).withProperty( "longPrimitive" ),
				violationOf( Negative.class ).withProperty( "shortPrimitive" ),
				violationOf( Negative.class ).withProperty( "doublePrimitive" ),
				violationOf( Negative.class ).withProperty( "floatPrimitive" )
		);

		dummy.intPrimitive = 101;
		dummy.longPrimitive = 1001;
		dummy.bytePrimitive = 111;
		dummy.shortPrimitive = 142;
		dummy.doublePrimitive = 123.34d;
		dummy.floatPrimitive = 456.34f;
		dummy.intObject = Integer.valueOf( 100 );
		dummy.longObject = Long.valueOf( 15678l );
		dummy.byteObject = Byte.valueOf( (byte) 50 );
		dummy.shortObject = Short.valueOf( (short) 3 );
		dummy.doubleObject = Double.valueOf( 123.34d );
		dummy.floatObject = Float.valueOf( 5678.56f );
		dummy.bigDecimal = BigDecimal.valueOf( 100.9 );
		dummy.bigInteger = BigInteger.valueOf( 100 );

		constraintViolations = validator.validate( dummy );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Negative.class ).withProperty( "bytePrimitive" ),
				violationOf( Negative.class ).withProperty( "intPrimitive" ),
				violationOf( Negative.class ).withProperty( "longPrimitive" ),
				violationOf( Negative.class ).withProperty( "shortPrimitive" ),
				violationOf( Negative.class ).withProperty( "doublePrimitive" ),
				violationOf( Negative.class ).withProperty( "floatPrimitive" ),
				violationOf( Negative.class ).withProperty( "byteObject" ),
				violationOf( Negative.class ).withProperty( "intObject" ),
				violationOf( Negative.class ).withProperty( "longObject" ),
				violationOf( Negative.class ).withProperty( "shortObject" ),
				violationOf( Negative.class ).withProperty( "doubleObject" ),
				violationOf( Negative.class ).withProperty( "floatObject" ),
				violationOf( Negative.class ).withProperty( "bigDecimal" ),
				violationOf( Negative.class ).withProperty( "bigInteger" )
		);

		dummy.intPrimitive = 0;
		dummy.longPrimitive = 0;
		dummy.bytePrimitive = 0;
		dummy.shortPrimitive = 0;
		dummy.doublePrimitive = 0;
		dummy.floatPrimitive = 0;
		dummy.intObject = Integer.valueOf( 0 );
		dummy.longObject = Long.valueOf( 0 );
		dummy.byteObject = Byte.valueOf( (byte) 0 );
		dummy.shortObject = Short.valueOf( (short) 0 );
		dummy.doubleObject = Double.valueOf( 0 );
		dummy.floatObject = Float.valueOf( 0 );
		dummy.bigDecimal = BigDecimal.valueOf( 0 );
		dummy.bigInteger = BigInteger.valueOf( 0 );

		constraintViolations = validator.validate( dummy );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Negative.class ).withProperty( "bytePrimitive" ),
				violationOf( Negative.class ).withProperty( "intPrimitive" ),
				violationOf( Negative.class ).withProperty( "longPrimitive" ),
				violationOf( Negative.class ).withProperty( "shortPrimitive" ),
				violationOf( Negative.class ).withProperty( "doublePrimitive" ),
				violationOf( Negative.class ).withProperty( "floatPrimitive" ),
				violationOf( Negative.class ).withProperty( "byteObject" ),
				violationOf( Negative.class ).withProperty( "intObject" ),
				violationOf( Negative.class ).withProperty( "longObject" ),
				violationOf( Negative.class ).withProperty( "shortObject" ),
				violationOf( Negative.class ).withProperty( "doubleObject" ),
				violationOf( Negative.class ).withProperty( "floatObject" ),
				violationOf( Negative.class ).withProperty( "bigDecimal" ),
				violationOf( Negative.class ).withProperty( "bigInteger" )
		);

		dummy.intPrimitive = -101;
		dummy.longPrimitive = -1001;
		dummy.bytePrimitive = -111;
		dummy.shortPrimitive = -142;
		dummy.doublePrimitive = -123.34d;
		dummy.floatPrimitive = -456.34f;
		dummy.intObject = Integer.valueOf( -100 );
		dummy.longObject = Long.valueOf( -15678l );
		dummy.byteObject = Byte.valueOf( (byte) -50 );
		dummy.shortObject = Short.valueOf( (short) -3 );
		dummy.doubleObject = Double.valueOf( -123.34d );
		dummy.floatObject = Float.valueOf( -5678.56f );
		dummy.bigDecimal = BigDecimal.valueOf( -100.9 );
		dummy.bigInteger = BigInteger.valueOf( -100 );

		constraintViolations = validator.validate( dummy );
		assertNoViolations( constraintViolations );
	}

	@Test
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS_NEGATIVE, id = "a")
	public void testNegativeConstraintInfinityAndNaN() {
		Validator validator = TestUtil.getValidatorUnderTest();
		NegativeEntity dummy = new NegativeEntity();

		dummy.intPrimitive = -1;
		dummy.longPrimitive = -1;
		dummy.bytePrimitive = -1;
		dummy.shortPrimitive = -1;
		dummy.doublePrimitive = -1;
		dummy.floatPrimitive = -1;
		dummy.floatObject = Float.NEGATIVE_INFINITY;
		dummy.doubleObject = Double.NEGATIVE_INFINITY;

		Set<ConstraintViolation<NegativeEntity>> constraintViolations = validator.validate( dummy );
		assertNoViolations( constraintViolations );

		dummy.floatObject = Float.POSITIVE_INFINITY;
		dummy.doubleObject = Double.POSITIVE_INFINITY;

		constraintViolations = validator.validate( dummy );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Negative.class ).withProperty( "doubleObject" ),
				violationOf( Negative.class ).withProperty( "floatObject" )
		);

		dummy.floatObject = Float.NaN;
		dummy.doubleObject = Double.NaN;

		constraintViolations = validator.validate( dummy );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Negative.class ).withProperty( "doubleObject" ),
				violationOf( Negative.class ).withProperty( "floatObject" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS_NEGATIVEORZERO, id = "a")
	public void testNegativeOrZeroConstraint() {
		Validator validator = TestUtil.getValidatorUnderTest();
		NegativeOrZeroEntity dummy = new NegativeOrZeroEntity();

		Set<ConstraintViolation<NegativeOrZeroEntity>> constraintViolations = validator.validate( dummy );
		assertNoViolations( constraintViolations );

		dummy.intPrimitive = 101;
		dummy.longPrimitive = 1001;
		dummy.bytePrimitive = 111;
		dummy.shortPrimitive = 142;
		dummy.doublePrimitive = 123.34d;
		dummy.floatPrimitive = 456.34f;
		dummy.intObject = Integer.valueOf( 100 );
		dummy.longObject = Long.valueOf( 15678l );
		dummy.byteObject = Byte.valueOf( (byte) 50 );
		dummy.shortObject = Short.valueOf( (short) 3 );
		dummy.doubleObject = Double.valueOf( 123.34d );
		dummy.floatObject = Float.valueOf( 5678.56f );
		dummy.bigDecimal = BigDecimal.valueOf( 100.9 );
		dummy.bigInteger = BigInteger.valueOf( 100 );

		constraintViolations = validator.validate( dummy );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NegativeOrZero.class ).withProperty( "bytePrimitive" ),
				violationOf( NegativeOrZero.class ).withProperty( "intPrimitive" ),
				violationOf( NegativeOrZero.class ).withProperty( "longPrimitive" ),
				violationOf( NegativeOrZero.class ).withProperty( "shortPrimitive" ),
				violationOf( NegativeOrZero.class ).withProperty( "doublePrimitive" ),
				violationOf( NegativeOrZero.class ).withProperty( "floatPrimitive" ),
				violationOf( NegativeOrZero.class ).withProperty( "byteObject" ),
				violationOf( NegativeOrZero.class ).withProperty( "intObject" ),
				violationOf( NegativeOrZero.class ).withProperty( "longObject" ),
				violationOf( NegativeOrZero.class ).withProperty( "shortObject" ),
				violationOf( NegativeOrZero.class ).withProperty( "doubleObject" ),
				violationOf( NegativeOrZero.class ).withProperty( "floatObject" ),
				violationOf( NegativeOrZero.class ).withProperty( "bigDecimal" ),
				violationOf( NegativeOrZero.class ).withProperty( "bigInteger" )
		);

		dummy.intPrimitive = 0;
		dummy.longPrimitive = 0;
		dummy.bytePrimitive = 0;
		dummy.shortPrimitive = 0;
		dummy.doublePrimitive = 0;
		dummy.floatPrimitive = 0;
		dummy.intObject = Integer.valueOf( 0 );
		dummy.longObject = Long.valueOf( 0 );
		dummy.byteObject = Byte.valueOf( (byte) 0 );
		dummy.shortObject = Short.valueOf( (short) 0 );
		dummy.doubleObject = Double.valueOf( 0 );
		dummy.floatObject = Float.valueOf( 0 );
		dummy.bigDecimal = BigDecimal.valueOf( 0 );
		dummy.bigInteger = BigInteger.valueOf( 0 );

		constraintViolations = validator.validate( dummy );
		assertNoViolations( constraintViolations );

		dummy.intPrimitive = -101;
		dummy.longPrimitive = -1001;
		dummy.bytePrimitive = -111;
		dummy.shortPrimitive = -142;
		dummy.doublePrimitive = -123.34d;
		dummy.floatPrimitive = -456.34f;
		dummy.intObject = Integer.valueOf( -100 );
		dummy.longObject = Long.valueOf( -15678l );
		dummy.byteObject = Byte.valueOf( (byte) -50 );
		dummy.shortObject = Short.valueOf( (short) -3 );
		dummy.doubleObject = Double.valueOf( -123.34d );
		dummy.floatObject = Float.valueOf( -5678.56f );
		dummy.bigDecimal = BigDecimal.valueOf( -100.9 );
		dummy.bigInteger = BigInteger.valueOf( -100 );

		constraintViolations = validator.validate( dummy );
		assertNoViolations( constraintViolations );
	}

	@Test
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS_NEGATIVEORZERO, id = "a")
	public void testNegativeOrZeroConstraintInfinityAndNaN() {
		Validator validator = TestUtil.getValidatorUnderTest();
		NegativeOrZeroEntity dummy = new NegativeOrZeroEntity();

		dummy.floatObject = Float.NEGATIVE_INFINITY;
		dummy.doubleObject = Double.NEGATIVE_INFINITY;

		Set<ConstraintViolation<NegativeOrZeroEntity>> constraintViolations = validator.validate( dummy );
		assertNoViolations( constraintViolations );

		dummy.floatObject = Float.POSITIVE_INFINITY;
		dummy.doubleObject = Double.POSITIVE_INFINITY;

		constraintViolations = validator.validate( dummy );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NegativeOrZero.class ).withProperty( "doubleObject" ),
				violationOf( NegativeOrZero.class ).withProperty( "floatObject" )
		);

		dummy.floatObject = Float.NaN;
		dummy.doubleObject = Double.NaN;

		constraintViolations = validator.validate( dummy );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NegativeOrZero.class ).withProperty( "doubleObject" ),
				violationOf( NegativeOrZero.class ).withProperty( "floatObject" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS_POSITIVE, id = "a")
	public void testPositiveConstraint() {
		Validator validator = TestUtil.getValidatorUnderTest();
		PositiveEntity dummy = new PositiveEntity();

		Set<ConstraintViolation<PositiveEntity>> constraintViolations = validator.validate( dummy );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Positive.class ).withProperty( "bytePrimitive" ),
				violationOf( Positive.class ).withProperty( "intPrimitive" ),
				violationOf( Positive.class ).withProperty( "longPrimitive" ),
				violationOf( Positive.class ).withProperty( "shortPrimitive" ),
				violationOf( Positive.class ).withProperty( "doublePrimitive" ),
				violationOf( Positive.class ).withProperty( "floatPrimitive" )
		);

		dummy.intPrimitive = 101;
		dummy.longPrimitive = 1001;
		dummy.bytePrimitive = 111;
		dummy.shortPrimitive = 142;
		dummy.doublePrimitive = 123.34d;
		dummy.floatPrimitive = 456.34f;
		dummy.intObject = Integer.valueOf( 100 );
		dummy.longObject = Long.valueOf( 15678l );
		dummy.byteObject = Byte.valueOf( (byte) 50 );
		dummy.shortObject = Short.valueOf( (short) 3 );
		dummy.doubleObject = Double.valueOf( 123.34d );
		dummy.floatObject = Float.valueOf( 5678.56f );
		dummy.bigDecimal = BigDecimal.valueOf( 100.9 );
		dummy.bigInteger = BigInteger.valueOf( 100 );

		constraintViolations = validator.validate( dummy );
		assertNoViolations( constraintViolations );

		dummy.intPrimitive = 0;
		dummy.longPrimitive = 0;
		dummy.bytePrimitive = 0;
		dummy.shortPrimitive = 0;
		dummy.doublePrimitive = 0;
		dummy.floatPrimitive = 0;
		dummy.intObject = Integer.valueOf( 0 );
		dummy.longObject = Long.valueOf( 0 );
		dummy.byteObject = Byte.valueOf( (byte) 0 );
		dummy.shortObject = Short.valueOf( (short) 0 );
		dummy.doubleObject = Double.valueOf( 0 );
		dummy.floatObject = Float.valueOf( 0 );
		dummy.bigDecimal = BigDecimal.valueOf( 0 );
		dummy.bigInteger = BigInteger.valueOf( 0 );

		constraintViolations = validator.validate( dummy );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Positive.class ).withProperty( "bytePrimitive" ),
				violationOf( Positive.class ).withProperty( "intPrimitive" ),
				violationOf( Positive.class ).withProperty( "longPrimitive" ),
				violationOf( Positive.class ).withProperty( "shortPrimitive" ),
				violationOf( Positive.class ).withProperty( "doublePrimitive" ),
				violationOf( Positive.class ).withProperty( "floatPrimitive" ),
				violationOf( Positive.class ).withProperty( "byteObject" ),
				violationOf( Positive.class ).withProperty( "intObject" ),
				violationOf( Positive.class ).withProperty( "longObject" ),
				violationOf( Positive.class ).withProperty( "shortObject" ),
				violationOf( Positive.class ).withProperty( "doubleObject" ),
				violationOf( Positive.class ).withProperty( "floatObject" ),
				violationOf( Positive.class ).withProperty( "bigDecimal" ),
				violationOf( Positive.class ).withProperty( "bigInteger" )
		);

		dummy.intPrimitive = -101;
		dummy.longPrimitive = -1001;
		dummy.bytePrimitive = -111;
		dummy.shortPrimitive = -142;
		dummy.doublePrimitive = -123.34d;
		dummy.floatPrimitive = -456.34f;
		dummy.intObject = Integer.valueOf( -100 );
		dummy.longObject = Long.valueOf( -15678l );
		dummy.byteObject = Byte.valueOf( (byte) -50 );
		dummy.shortObject = Short.valueOf( (short) -3 );
		dummy.doubleObject = Double.valueOf( -123.34d );
		dummy.floatObject = Float.valueOf( -5678.56f );
		dummy.bigDecimal = BigDecimal.valueOf( -100.9 );
		dummy.bigInteger = BigInteger.valueOf( -100 );

		constraintViolations = validator.validate( dummy );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Positive.class ).withProperty( "bytePrimitive" ),
				violationOf( Positive.class ).withProperty( "intPrimitive" ),
				violationOf( Positive.class ).withProperty( "longPrimitive" ),
				violationOf( Positive.class ).withProperty( "shortPrimitive" ),
				violationOf( Positive.class ).withProperty( "doublePrimitive" ),
				violationOf( Positive.class ).withProperty( "floatPrimitive" ),
				violationOf( Positive.class ).withProperty( "byteObject" ),
				violationOf( Positive.class ).withProperty( "intObject" ),
				violationOf( Positive.class ).withProperty( "longObject" ),
				violationOf( Positive.class ).withProperty( "shortObject" ),
				violationOf( Positive.class ).withProperty( "doubleObject" ),
				violationOf( Positive.class ).withProperty( "floatObject" ),
				violationOf( Positive.class ).withProperty( "bigDecimal" ),
				violationOf( Positive.class ).withProperty( "bigInteger" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS_POSITIVEORZERO, id = "a")
	public void testPositiveConstraintInfinityAndNaN() {
		Validator validator = TestUtil.getValidatorUnderTest();
		PositiveEntity dummy = new PositiveEntity();

		dummy.intPrimitive = 1;
		dummy.longPrimitive = 1;
		dummy.bytePrimitive = 1;
		dummy.shortPrimitive = 1;
		dummy.doublePrimitive = 1;
		dummy.floatPrimitive = 1;
		dummy.floatObject = Float.POSITIVE_INFINITY;
		dummy.doubleObject = Double.POSITIVE_INFINITY;

		Set<ConstraintViolation<PositiveEntity>> constraintViolations = validator.validate( dummy );
		assertNoViolations( constraintViolations );

		dummy.floatObject = Float.NEGATIVE_INFINITY;
		dummy.doubleObject = Double.NEGATIVE_INFINITY;

		constraintViolations = validator.validate( dummy );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Positive.class ).withProperty( "doubleObject" ),
				violationOf( Positive.class ).withProperty( "floatObject" )
		);

		dummy.floatObject = Float.NaN;
		dummy.doubleObject = Double.NaN;

		constraintViolations = validator.validate( dummy );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Positive.class ).withProperty( "doubleObject" ),
				violationOf( Positive.class ).withProperty( "floatObject" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS_POSITIVEORZERO, id = "a")
	public void testPositiveOrZeroConstraint() {
		Validator validator = TestUtil.getValidatorUnderTest();
		PositiveOrZeroEntity dummy = new PositiveOrZeroEntity();

		Set<ConstraintViolation<PositiveOrZeroEntity>> constraintViolations = validator.validate( dummy );
		assertNoViolations( constraintViolations );

		dummy.intPrimitive = 101;
		dummy.longPrimitive = 1001;
		dummy.bytePrimitive = 111;
		dummy.shortPrimitive = 142;
		dummy.doublePrimitive = 123.34d;
		dummy.floatPrimitive = 456.34f;
		dummy.intObject = Integer.valueOf( 100 );
		dummy.longObject = Long.valueOf( 15678l );
		dummy.byteObject = Byte.valueOf( (byte) 50 );
		dummy.shortObject = Short.valueOf( (short) 3 );
		dummy.doubleObject = Double.valueOf( 123.34d );
		dummy.floatObject = Float.valueOf( 5678.56f );
		dummy.bigDecimal = BigDecimal.valueOf( 100.9 );
		dummy.bigInteger = BigInteger.valueOf( 100 );

		constraintViolations = validator.validate( dummy );
		assertNoViolations( constraintViolations );

		dummy.intPrimitive = 0;
		dummy.longPrimitive = 0;
		dummy.bytePrimitive = 0;
		dummy.shortPrimitive = 0;
		dummy.doublePrimitive = 0;
		dummy.floatPrimitive = 0;
		dummy.intObject = Integer.valueOf( 0 );
		dummy.longObject = Long.valueOf( 0 );
		dummy.byteObject = Byte.valueOf( (byte) 0 );
		dummy.shortObject = Short.valueOf( (short) 0 );
		dummy.doubleObject = Double.valueOf( 0 );
		dummy.floatObject = Float.valueOf( 0 );
		dummy.bigDecimal = BigDecimal.valueOf( 0 );
		dummy.bigInteger = BigInteger.valueOf( 0 );

		constraintViolations = validator.validate( dummy );
		assertNoViolations( constraintViolations );

		dummy.intPrimitive = -101;
		dummy.longPrimitive = -1001;
		dummy.bytePrimitive = -111;
		dummy.shortPrimitive = -142;
		dummy.doublePrimitive = -123.34d;
		dummy.floatPrimitive = -456.34f;
		dummy.intObject = Integer.valueOf( -100 );
		dummy.longObject = Long.valueOf( -15678l );
		dummy.byteObject = Byte.valueOf( (byte) -50 );
		dummy.shortObject = Short.valueOf( (short) -3 );
		dummy.doubleObject = Double.valueOf( -123.34d );
		dummy.floatObject = Float.valueOf( -5678.56f );
		dummy.bigDecimal = BigDecimal.valueOf( -100.9 );
		dummy.bigInteger = BigInteger.valueOf( -100 );

		constraintViolations = validator.validate( dummy );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( PositiveOrZero.class ).withProperty( "bytePrimitive" ),
				violationOf( PositiveOrZero.class ).withProperty( "intPrimitive" ),
				violationOf( PositiveOrZero.class ).withProperty( "longPrimitive" ),
				violationOf( PositiveOrZero.class ).withProperty( "shortPrimitive" ),
				violationOf( PositiveOrZero.class ).withProperty( "doublePrimitive" ),
				violationOf( PositiveOrZero.class ).withProperty( "floatPrimitive" ),
				violationOf( PositiveOrZero.class ).withProperty( "byteObject" ),
				violationOf( PositiveOrZero.class ).withProperty( "intObject" ),
				violationOf( PositiveOrZero.class ).withProperty( "longObject" ),
				violationOf( PositiveOrZero.class ).withProperty( "shortObject" ),
				violationOf( PositiveOrZero.class ).withProperty( "doubleObject" ),
				violationOf( PositiveOrZero.class ).withProperty( "floatObject" ),
				violationOf( PositiveOrZero.class ).withProperty( "bigDecimal" ),
				violationOf( PositiveOrZero.class ).withProperty( "bigInteger" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS_POSITIVE, id = "a")
	public void testPositiveOrZeroConstraintInfinityAndNaN() {
		Validator validator = TestUtil.getValidatorUnderTest();
		PositiveOrZeroEntity dummy = new PositiveOrZeroEntity();

		dummy.floatObject = Float.POSITIVE_INFINITY;
		dummy.doubleObject = Double.POSITIVE_INFINITY;

		Set<ConstraintViolation<PositiveOrZeroEntity>> constraintViolations = validator.validate( dummy );
		assertNoViolations( constraintViolations );

		dummy.floatObject = Float.NEGATIVE_INFINITY;
		dummy.doubleObject = Double.NEGATIVE_INFINITY;

		constraintViolations = validator.validate( dummy );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( PositiveOrZero.class ).withProperty( "doubleObject" ),
				violationOf( PositiveOrZero.class ).withProperty( "floatObject" )
		);

		dummy.floatObject = Float.NaN;
		dummy.doubleObject = Double.NaN;

		constraintViolations = validator.validate( dummy );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( PositiveOrZero.class ).withProperty( "doubleObject" ),
				violationOf( PositiveOrZero.class ).withProperty( "floatObject" )
		);
	}

	private static class NegativeEntity {

		@Negative
		private BigDecimal bigDecimal;

		@Negative
		private BigInteger bigInteger;

		@Negative
		private byte bytePrimitive;

		@Negative
		private short shortPrimitive;

		@Negative
		private int intPrimitive;

		@Negative
		private long longPrimitive;

		@Negative
		private double doublePrimitive;

		@Negative
		private float floatPrimitive;

		@Negative
		private Byte byteObject;

		@Negative
		private Short shortObject;

		@Negative
		private Integer intObject;

		@Negative
		private Long longObject;

		@Negative
		private Double doubleObject;

		@Negative
		private Float floatObject;
	}

	private static class NegativeOrZeroEntity {

		@NegativeOrZero
		private BigDecimal bigDecimal;

		@NegativeOrZero
		private BigInteger bigInteger;

		@NegativeOrZero
		private byte bytePrimitive;

		@NegativeOrZero
		private short shortPrimitive;

		@NegativeOrZero
		private int intPrimitive;

		@NegativeOrZero
		private long longPrimitive;

		@NegativeOrZero
		private double doublePrimitive;

		@NegativeOrZero
		private float floatPrimitive;

		@NegativeOrZero
		private Byte byteObject;

		@NegativeOrZero
		private Short shortObject;

		@NegativeOrZero
		private Integer intObject;

		@NegativeOrZero
		private Long longObject;

		@NegativeOrZero
		private Double doubleObject;

		@NegativeOrZero
		private Float floatObject;
	}

	private static class PositiveEntity {

		@Positive
		private BigDecimal bigDecimal;

		@Positive
		private BigInteger bigInteger;

		@Positive
		private byte bytePrimitive;

		@Positive
		private short shortPrimitive;

		@Positive
		private int intPrimitive;

		@Positive
		private long longPrimitive;

		@Positive
		private double doublePrimitive;

		@Positive
		private float floatPrimitive;

		@Positive
		private Byte byteObject;

		@Positive
		private Short shortObject;

		@Positive
		private Integer intObject;

		@Positive
		private Long longObject;

		@Positive
		private Double doubleObject;

		@Positive
		private Float floatObject;
	}

	private static class PositiveOrZeroEntity {

		@PositiveOrZero
		private BigDecimal bigDecimal;

		@PositiveOrZero
		private BigInteger bigInteger;

		@PositiveOrZero
		private byte bytePrimitive;

		@PositiveOrZero
		private short shortPrimitive;

		@PositiveOrZero
		private int intPrimitive;

		@PositiveOrZero
		private long longPrimitive;

		@PositiveOrZero
		private double doublePrimitive;

		@PositiveOrZero
		private float floatPrimitive;

		@PositiveOrZero
		private Byte byteObject;

		@PositiveOrZero
		private Short shortObject;

		@PositiveOrZero
		private Integer intObject;

		@PositiveOrZero
		private Long longObject;

		@PositiveOrZero
		private Double doubleObject;

		@PositiveOrZero
		private Float floatObject;
	}
}
