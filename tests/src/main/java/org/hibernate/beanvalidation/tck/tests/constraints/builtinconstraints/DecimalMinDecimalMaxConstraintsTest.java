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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests for {@link DecimalMin} and {@link DecimalMax} built-in constraints.
 *
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class DecimalMinDecimalMaxConstraintsTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( DecimalMinDecimalMaxConstraintsTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS_DECIMALMIN, id = "a")
	public void testDecimalMinConstraint() {
		Validator validator = TestUtil.getValidatorUnderTest();
		DecimalMinDummyEntity dummy = new DecimalMinDummyEntity();

		Set<ConstraintViolation<DecimalMinDummyEntity>> constraintViolations = validator.validate( dummy );
		// only the min constraints on the primitive values should fail. Object values re still null and should pass per spec
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( DecimalMin.class ).withProperty( "bytePrimitive" ),
				violationOf( DecimalMin.class ).withProperty( "intPrimitive" ),
				violationOf( DecimalMin.class ).withProperty( "longPrimitive" ),
				violationOf( DecimalMin.class ).withProperty( "shortPrimitive" )
		);

		dummy.intPrimitive = 101;
		dummy.longPrimitive = 1001;
		dummy.bytePrimitive = 111;
		dummy.shortPrimitive = 142;

		dummy.intObject = Integer.valueOf( "100" );
		dummy.longObject = Long.valueOf( "0" );
		dummy.byteObject = Byte.parseByte( "-1" );
		dummy.shortObject = Short.parseShort( "3" );
		dummy.bigDecimal = BigDecimal.valueOf( 100.9 );
		dummy.bigInteger = BigInteger.valueOf( 100 );

		constraintViolations = validator.validate( dummy );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( DecimalMin.class ).withProperty( "byteObject" ),
				violationOf( DecimalMin.class ).withProperty( "intObject" ),
				violationOf( DecimalMin.class ).withProperty( "longObject" ),
				violationOf( DecimalMin.class ).withProperty( "shortObject" ),
				violationOf( DecimalMin.class ).withProperty( "bigDecimal" ),
				violationOf( DecimalMin.class ).withProperty( "bigInteger" )
		);

		dummy.intObject = Integer.valueOf( "101" );
		dummy.longObject = Long.valueOf( "12345" );
		dummy.byteObject = Byte.parseByte( "102" );
		dummy.shortObject = Short.parseShort( "111" );
		dummy.bigDecimal = BigDecimal.valueOf( 101.1 );
		dummy.bigInteger = BigInteger.valueOf( 101 );

		constraintViolations = validator.validate( dummy );
		assertNoViolations( constraintViolations );
	}

	@Test
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS_DECIMALMAX, id = "a")
	public void testDecimalMaxConstraint() {
		Validator validator = TestUtil.getValidatorUnderTest();
		DecimalMaxDummyEntity dummy = new DecimalMaxDummyEntity();

		dummy.intPrimitive = 102;
		dummy.longPrimitive = 1234;
		dummy.bytePrimitive = 102;
		dummy.shortPrimitive = 102;

		Set<ConstraintViolation<DecimalMaxDummyEntity>> constraintViolations = validator.validate( dummy );
		// only the max constraints on the primitive values should fail. Object values re still null and should pass per spec
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( DecimalMax.class ).withProperty( "bytePrimitive" ),
				violationOf( DecimalMax.class ).withProperty( "intPrimitive" ),
				violationOf( DecimalMax.class ).withProperty( "longPrimitive" ),
				violationOf( DecimalMax.class ).withProperty( "shortPrimitive" )
		);


		dummy.intPrimitive = 101;
		dummy.longPrimitive = 100;
		dummy.bytePrimitive = 99;
		dummy.shortPrimitive = 42;

		dummy.intObject = Integer.valueOf( "102" );
		dummy.longObject = Long.valueOf( "12345" );
		dummy.byteObject = Byte.parseByte( "111" );
		dummy.shortObject = Short.parseShort( "1234" );
		dummy.bigDecimal = BigDecimal.valueOf( 102 );
		dummy.bigInteger = BigInteger.valueOf( 102 );

		constraintViolations = validator.validate( dummy );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( DecimalMax.class ).withProperty( "byteObject" ),
				violationOf( DecimalMax.class ).withProperty( "intObject" ),
				violationOf( DecimalMax.class ).withProperty( "longObject" ),
				violationOf( DecimalMax.class ).withProperty( "shortObject" ),
				violationOf( DecimalMax.class ).withProperty( "bigDecimal" ),
				violationOf( DecimalMax.class ).withProperty( "bigInteger" )
		);

		dummy.intObject = Integer.valueOf( "101" );
		dummy.longObject = Long.valueOf( "100" );
		dummy.byteObject = Byte.parseByte( "100" );
		dummy.shortObject = Short.parseShort( "101" );
		dummy.bigDecimal = BigDecimal.valueOf( 100.9 );
		dummy.bigInteger = BigInteger.valueOf( 100 );

		constraintViolations = validator.validate( dummy );
		assertNoViolations( constraintViolations );
	}

	private static class DecimalMaxDummyEntity {
		@DecimalMax("101.000000000")
		private BigDecimal bigDecimal;

		@DecimalMax("1.01E+2")
		private BigInteger bigInteger;

		@DecimalMax("101")
		private byte bytePrimitive;

		@DecimalMax("101")
		private short shortPrimitive;

		@DecimalMax("101")
		private int intPrimitive;

		@DecimalMax("101")
		private long longPrimitive;

		@DecimalMax("101")
		private Byte byteObject;

		@DecimalMax("101")
		private Short shortObject;

		@DecimalMax("101")
		private Integer intObject;

		@DecimalMax("101")
		private Long longObject;
	}

	private static class DecimalMinDummyEntity {
		@DecimalMin("101.000000000")
		private BigDecimal bigDecimal;

		@DecimalMin("1.01E+2")
		private BigInteger bigInteger;

		@DecimalMin("101")
		private byte bytePrimitive;

		@DecimalMin("101")
		private short shortPrimitive;

		@DecimalMin("101")
		private int intPrimitive;

		@DecimalMin("101")
		private long longPrimitive;

		@DecimalMin("101")
		private Byte byteObject;

		@DecimalMin("101")
		private Short shortObject;

		@DecimalMin("101")
		private Integer intObject;

		@DecimalMin("101")
		private Long longObject;
	}

}
