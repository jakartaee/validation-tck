/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.builtinconstraints;

import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectNumberOfViolations;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectPropertyPaths;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.Negative;
import javax.validation.constraints.Positive;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests for {@link Negative} and {@link Positive} built-in constraints.
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
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "k")
	public void testNegativeConstraint() {
		Validator validator = TestUtil.getValidatorUnderTest();
		NegativeEntity dummy = new NegativeEntity();

		Set<ConstraintViolation<NegativeEntity>> constraintViolations = validator.validate( dummy );
		assertCorrectNumberOfViolations( constraintViolations, 0 );

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
		assertCorrectPropertyPaths(
				constraintViolations,
				"bytePrimitive", "intPrimitive", "longPrimitive", "shortPrimitive", "doublePrimitive", "floatPrimitive",
				"byteObject", "intObject", "longObject", "shortObject", "doubleObject", "floatObject",
				"bigDecimal", "bigInteger"
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
		assertCorrectNumberOfViolations( constraintViolations, 0 );

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
		assertCorrectNumberOfViolations( constraintViolations, 0 );
	}

	@Test
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "k")
	public void testStrictNegativeConstraint() {
		Validator validator = TestUtil.getValidatorUnderTest();
		StrictNegativeEntity dummy = new StrictNegativeEntity();

		Set<ConstraintViolation<StrictNegativeEntity>> constraintViolations = validator.validate( dummy );
		assertCorrectPropertyPaths(
				constraintViolations,
				"bytePrimitive", "intPrimitive", "longPrimitive", "shortPrimitive", "doublePrimitive", "floatPrimitive"
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
		assertCorrectPropertyPaths(
				constraintViolations,
				"bytePrimitive", "intPrimitive", "longPrimitive", "shortPrimitive", "doublePrimitive", "floatPrimitive",
				"byteObject", "intObject", "longObject", "shortObject", "doubleObject", "floatObject",
				"bigDecimal", "bigInteger"
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

		assertCorrectPropertyPaths(
				constraintViolations,
				"bytePrimitive", "intPrimitive", "longPrimitive", "shortPrimitive", "doublePrimitive", "floatPrimitive",
				"byteObject", "intObject", "longObject", "shortObject", "doubleObject", "floatObject",
				"bigDecimal", "bigInteger"
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
		assertCorrectNumberOfViolations( constraintViolations, 0 );
	}

	@Test
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "k")
	public void testNegativeConstraintInfinityAndNaN() {
		Validator validator = TestUtil.getValidatorUnderTest();
		NegativeEntity dummy = new NegativeEntity();

		dummy.floatObject = Float.NEGATIVE_INFINITY;
		dummy.doubleObject = Double.NEGATIVE_INFINITY;

		Set<ConstraintViolation<NegativeEntity>> constraintViolations = validator.validate( dummy );
		assertCorrectNumberOfViolations( constraintViolations, 0 );

		dummy.floatObject = Float.POSITIVE_INFINITY;
		dummy.doubleObject = Double.POSITIVE_INFINITY;

		constraintViolations = validator.validate( dummy );
		assertCorrectPropertyPaths( constraintViolations, "doubleObject", "floatObject" );

		dummy.floatObject = Float.NaN;
		dummy.doubleObject = Double.NaN;

		constraintViolations = validator.validate( dummy );
		assertCorrectPropertyPaths( constraintViolations, "doubleObject", "floatObject" );
	}

	@Test
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "l")
	public void testPositiveConstraint() {
		Validator validator = TestUtil.getValidatorUnderTest();
		PositiveEntity dummy = new PositiveEntity();

		Set<ConstraintViolation<PositiveEntity>> constraintViolations = validator.validate( dummy );
		assertCorrectNumberOfViolations( constraintViolations, 0 );

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
		assertCorrectNumberOfViolations( constraintViolations, 0 );

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
		assertCorrectNumberOfViolations( constraintViolations, 0 );

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
		assertCorrectPropertyPaths(
				constraintViolations,
				"bytePrimitive", "intPrimitive", "longPrimitive", "shortPrimitive", "doublePrimitive", "floatPrimitive",
				"byteObject", "intObject", "longObject", "shortObject", "doubleObject", "floatObject",
				"bigDecimal", "bigInteger"
		);
	}

	@Test
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "l")
	public void testStrictPositiveConstraint() {
		Validator validator = TestUtil.getValidatorUnderTest();
		StrictPositiveEntity dummy = new StrictPositiveEntity();

		Set<ConstraintViolation<StrictPositiveEntity>> constraintViolations = validator.validate( dummy );
		assertCorrectPropertyPaths(
				constraintViolations,
				"bytePrimitive", "intPrimitive", "longPrimitive", "shortPrimitive", "doublePrimitive", "floatPrimitive"
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
		assertCorrectNumberOfViolations( constraintViolations, 0 );

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

		assertCorrectPropertyPaths(
				constraintViolations,
				"bytePrimitive", "intPrimitive", "longPrimitive", "shortPrimitive", "doublePrimitive", "floatPrimitive",
				"byteObject", "intObject", "longObject", "shortObject", "doubleObject", "floatObject",
				"bigDecimal", "bigInteger"
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
		assertCorrectPropertyPaths(
				constraintViolations,
				"bytePrimitive", "intPrimitive", "longPrimitive", "shortPrimitive", "doublePrimitive", "floatPrimitive",
				"byteObject", "intObject", "longObject", "shortObject", "doubleObject", "floatObject",
				"bigDecimal", "bigInteger"
		);
	}

	@Test
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "l")
	public void testPositiveConstraintInfinityAndNaN() {
		Validator validator = TestUtil.getValidatorUnderTest();
		PositiveEntity dummy = new PositiveEntity();

		dummy.floatObject = Float.POSITIVE_INFINITY;
		dummy.doubleObject = Double.POSITIVE_INFINITY;

		Set<ConstraintViolation<PositiveEntity>> constraintViolations = validator.validate( dummy );
		assertCorrectNumberOfViolations( constraintViolations, 0 );

		dummy.floatObject = Float.NEGATIVE_INFINITY;
		dummy.doubleObject = Double.NEGATIVE_INFINITY;

		constraintViolations = validator.validate( dummy );
		assertCorrectPropertyPaths( constraintViolations, "doubleObject", "floatObject" );

		dummy.floatObject = Float.NaN;
		dummy.doubleObject = Double.NaN;

		constraintViolations = validator.validate( dummy );
		assertCorrectPropertyPaths( constraintViolations, "doubleObject", "floatObject" );
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

	private static class StrictNegativeEntity {

		@Negative(strict = true)
		private BigDecimal bigDecimal;

		@Negative(strict = true)
		private BigInteger bigInteger;

		@Negative(strict = true)
		private byte bytePrimitive;

		@Negative(strict = true)
		private short shortPrimitive;

		@Negative(strict = true)
		private int intPrimitive;

		@Negative(strict = true)
		private long longPrimitive;

		@Negative(strict = true)
		private double doublePrimitive;

		@Negative(strict = true)
		private float floatPrimitive;

		@Negative(strict = true)
		private Byte byteObject;

		@Negative(strict = true)
		private Short shortObject;

		@Negative(strict = true)
		private Integer intObject;

		@Negative(strict = true)
		private Long longObject;

		@Negative(strict = true)
		private Double doubleObject;

		@Negative(strict = true)
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

	private static class StrictPositiveEntity {

		@Positive(strict = true)
		private BigDecimal bigDecimal;

		@Positive(strict = true)
		private BigInteger bigInteger;

		@Positive(strict = true)
		private byte bytePrimitive;

		@Positive(strict = true)
		private short shortPrimitive;

		@Positive(strict = true)
		private int intPrimitive;

		@Positive(strict = true)
		private long longPrimitive;

		@Positive(strict = true)
		private double doublePrimitive;

		@Positive(strict = true)
		private float floatPrimitive;

		@Positive(strict = true)
		private Byte byteObject;

		@Positive(strict = true)
		private Short shortObject;

		@Positive(strict = true)
		private Integer intObject;

		@Positive(strict = true)
		private Long longObject;

		@Positive(strict = true)
		private Double doubleObject;

		@Positive(strict = true)
		private Float floatObject;
	}
}
