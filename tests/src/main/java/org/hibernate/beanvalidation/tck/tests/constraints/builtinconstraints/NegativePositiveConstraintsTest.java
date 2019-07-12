/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.builtinconstraints;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertNumberOfViolations;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.pathWith;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.Negative;
import javax.validation.constraints.NegativeOrZero;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

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
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
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
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "bytePrimitive" ),
				pathWith()
						.property( "intPrimitive" ),
				pathWith()
						.property( "longPrimitive" ),
				pathWith()
						.property( "shortPrimitive" ),
				pathWith()
						.property( "doublePrimitive" ),
				pathWith()
						.property( "floatPrimitive" )
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
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "bytePrimitive" ),
				pathWith()
						.property( "intPrimitive" ),
				pathWith()
						.property( "longPrimitive" ),
				pathWith()
						.property( "shortPrimitive" ),
				pathWith()
						.property( "doublePrimitive" ),
				pathWith()
						.property( "floatPrimitive" ),
				pathWith()
						.property( "byteObject" ),
				pathWith()
						.property( "intObject" ),
				pathWith()
						.property( "longObject" ),
				pathWith()
						.property( "shortObject" ),
				pathWith()
						.property( "doubleObject" ),
				pathWith()
						.property( "floatObject" ),
				pathWith()
						.property( "bigDecimal" ),
				pathWith()
						.property( "bigInteger" )
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

		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "bytePrimitive" ),
				pathWith()
						.property( "intPrimitive" ),
				pathWith()
						.property( "longPrimitive" ),
				pathWith()
						.property( "shortPrimitive" ),
				pathWith()
						.property( "doublePrimitive" ),
				pathWith()
						.property( "floatPrimitive" ),
				pathWith()
						.property( "byteObject" ),
				pathWith()
						.property( "intObject" ),
				pathWith()
						.property( "longObject" ),
				pathWith()
						.property( "shortObject" ),
				pathWith()
						.property( "doubleObject" ),
				pathWith()
						.property( "floatObject" ),
				pathWith()
						.property( "bigDecimal" ),
				pathWith()
						.property( "bigInteger" )
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
		assertNumberOfViolations( constraintViolations, 0 );
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
		assertNumberOfViolations( constraintViolations, 0 );

		dummy.floatObject = Float.POSITIVE_INFINITY;
		dummy.doubleObject = Double.POSITIVE_INFINITY;

		constraintViolations = validator.validate( dummy );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "doubleObject" ),
				pathWith()
						.property( "floatObject" )
		);

		dummy.floatObject = Float.NaN;
		dummy.doubleObject = Double.NaN;

		constraintViolations = validator.validate( dummy );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "doubleObject" ),
				pathWith()
						.property( "floatObject" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS_NEGATIVEORZERO, id = "a")
	public void testNegativeOrZeroConstraint() {
		Validator validator = TestUtil.getValidatorUnderTest();
		NegativeOrZeroEntity dummy = new NegativeOrZeroEntity();

		Set<ConstraintViolation<NegativeOrZeroEntity>> constraintViolations = validator.validate( dummy );
		assertNumberOfViolations( constraintViolations, 0 );

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
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "bytePrimitive" ),
				pathWith()
						.property( "intPrimitive" ),
				pathWith()
						.property( "longPrimitive" ),
				pathWith()
						.property( "shortPrimitive" ),
				pathWith()
						.property( "doublePrimitive" ),
				pathWith()
						.property( "floatPrimitive" ),
				pathWith()
						.property( "byteObject" ),
				pathWith()
						.property( "intObject" ),
				pathWith()
						.property( "longObject" ),
				pathWith()
						.property( "shortObject" ),
				pathWith()
						.property( "doubleObject" ),
				pathWith()
						.property( "floatObject" ),
				pathWith()
						.property( "bigDecimal" ),
				pathWith()
						.property( "bigInteger" )
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
		assertNumberOfViolations( constraintViolations, 0 );

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
		assertNumberOfViolations( constraintViolations, 0 );
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
		assertNumberOfViolations( constraintViolations, 0 );

		dummy.floatObject = Float.POSITIVE_INFINITY;
		dummy.doubleObject = Double.POSITIVE_INFINITY;

		constraintViolations = validator.validate( dummy );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "doubleObject" ),
				pathWith()
						.property( "floatObject" )
		);

		dummy.floatObject = Float.NaN;
		dummy.doubleObject = Double.NaN;

		constraintViolations = validator.validate( dummy );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "doubleObject" ),
				pathWith()
						.property( "floatObject" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS_POSITIVE, id = "a")
	public void testPositiveConstraint() {
		Validator validator = TestUtil.getValidatorUnderTest();
		PositiveEntity dummy = new PositiveEntity();

		Set<ConstraintViolation<PositiveEntity>> constraintViolations = validator.validate( dummy );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "bytePrimitive" ),
				pathWith()
						.property( "intPrimitive" ),
				pathWith()
						.property( "longPrimitive" ),
				pathWith()
						.property( "shortPrimitive" ),
				pathWith()
						.property( "doublePrimitive" ),
				pathWith()
						.property( "floatPrimitive" )
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
		assertNumberOfViolations( constraintViolations, 0 );

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

		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "bytePrimitive" ),
				pathWith()
						.property( "intPrimitive" ),
				pathWith()
						.property( "longPrimitive" ),
				pathWith()
						.property( "shortPrimitive" ),
				pathWith()
						.property( "doublePrimitive" ),
				pathWith()
						.property( "floatPrimitive" ),
				pathWith()
						.property( "byteObject" ),
				pathWith()
						.property( "intObject" ),
				pathWith()
						.property( "longObject" ),
				pathWith()
						.property( "shortObject" ),
				pathWith()
						.property( "doubleObject" ),
				pathWith()
						.property( "floatObject" ),
				pathWith()
						.property( "bigDecimal" ),
				pathWith()
						.property( "bigInteger" )
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
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "bytePrimitive" ),
				pathWith()
						.property( "intPrimitive" ),
				pathWith()
						.property( "longPrimitive" ),
				pathWith()
						.property( "shortPrimitive" ),
				pathWith()
						.property( "doublePrimitive" ),
				pathWith()
						.property( "floatPrimitive" ),
				pathWith()
						.property( "byteObject" ),
				pathWith()
						.property( "intObject" ),
				pathWith()
						.property( "longObject" ),
				pathWith()
						.property( "shortObject" ),
				pathWith()
						.property( "doubleObject" ),
				pathWith()
						.property( "floatObject" ),
				pathWith()
						.property( "bigDecimal" ),
				pathWith()
						.property( "bigInteger" )
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
		assertNumberOfViolations( constraintViolations, 0 );

		dummy.floatObject = Float.NEGATIVE_INFINITY;
		dummy.doubleObject = Double.NEGATIVE_INFINITY;

		constraintViolations = validator.validate( dummy );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "doubleObject" ),
				pathWith()
						.property( "floatObject" )
		);

		dummy.floatObject = Float.NaN;
		dummy.doubleObject = Double.NaN;

		constraintViolations = validator.validate( dummy );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "doubleObject" ),
				pathWith()
						.property( "floatObject" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS_POSITIVEORZERO, id = "a")
	public void testPositiveOrZeroConstraint() {
		Validator validator = TestUtil.getValidatorUnderTest();
		PositiveOrZeroEntity dummy = new PositiveOrZeroEntity();

		Set<ConstraintViolation<PositiveOrZeroEntity>> constraintViolations = validator.validate( dummy );
		assertNumberOfViolations( constraintViolations, 0 );

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
		assertNumberOfViolations( constraintViolations, 0 );

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
		assertNumberOfViolations( constraintViolations, 0 );

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
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "bytePrimitive" ),
				pathWith()
						.property( "intPrimitive" ),
				pathWith()
						.property( "longPrimitive" ),
				pathWith()
						.property( "shortPrimitive" ),
				pathWith()
						.property( "doublePrimitive" ),
				pathWith()
						.property( "floatPrimitive" ),
				pathWith()
						.property( "byteObject" ),
				pathWith()
						.property( "intObject" ),
				pathWith()
						.property( "longObject" ),
				pathWith()
						.property( "shortObject" ),
				pathWith()
						.property( "doubleObject" ),
				pathWith()
						.property( "floatObject" ),
				pathWith()
						.property( "bigDecimal" ),
				pathWith()
						.property( "bigInteger" )
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
		assertNumberOfViolations( constraintViolations, 0 );

		dummy.floatObject = Float.NEGATIVE_INFINITY;
		dummy.doubleObject = Double.NEGATIVE_INFINITY;

		constraintViolations = validator.validate( dummy );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "doubleObject" ),
				pathWith()
						.property( "floatObject" )
		);

		dummy.floatObject = Float.NaN;
		dummy.doubleObject = Double.NaN;

		constraintViolations = validator.validate( dummy );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "doubleObject" ),
				pathWith()
						.property( "floatObject" )
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
