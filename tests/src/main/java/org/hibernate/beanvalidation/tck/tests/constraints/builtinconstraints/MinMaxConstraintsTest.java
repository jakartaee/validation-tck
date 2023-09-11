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
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests for {@link Min} and {@link Max} built-in constraints.
 *
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "3.0.0")
public class MinMaxConstraintsTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( MinMaxConstraintsTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS_MIN, id = "a")
	public void testMinConstraint() {
		Validator validator = TestUtil.getValidatorUnderTest();
		MinDummyEntity dummy = new MinDummyEntity();

		Set<ConstraintViolation<MinDummyEntity>> constraintViolations = validator.validate( dummy );
		// only the min constraints on the primitive values should fail. Object values re still null and should pass per spec
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Min.class ).withProperty( "bytePrimitive" ),
				violationOf( Min.class ).withProperty( "intPrimitive" ),
				violationOf( Min.class ).withProperty( "longPrimitive" ),
				violationOf( Min.class ).withProperty( "shortPrimitive" )
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
				violationOf( Min.class ).withProperty( "byteObject" ),
				violationOf( Min.class ).withProperty( "intObject" ),
				violationOf( Min.class ).withProperty( "longObject" ),
				violationOf( Min.class ).withProperty( "shortObject" ),
				violationOf( Min.class ).withProperty( "bigDecimal" ),
				violationOf( Min.class ).withProperty( "bigInteger" )
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
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS_MAX, id = "a")
	public void testMaxConstraint() {
		Validator validator = TestUtil.getValidatorUnderTest();
		MaxDummyEntity dummy = new MaxDummyEntity();

		dummy.intPrimitive = 102;
		dummy.longPrimitive = 1234;
		dummy.bytePrimitive = 102;
		dummy.shortPrimitive = 102;

		Set<ConstraintViolation<MaxDummyEntity>> constraintViolations = validator.validate( dummy );
		// only the max constraints on the primitive values should fail. Object values re still null and should pass per spec
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Max.class ).withProperty( "bytePrimitive" ),
				violationOf( Max.class ).withProperty( "intPrimitive" ),
				violationOf( Max.class ).withProperty( "longPrimitive" ),
				violationOf( Max.class ).withProperty( "shortPrimitive" )
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
				violationOf( Max.class ).withProperty( "byteObject" ),
				violationOf( Max.class ).withProperty( "intObject" ),
				violationOf( Max.class ).withProperty( "longObject" ),
				violationOf( Max.class ).withProperty( "shortObject" ),
				violationOf( Max.class ).withProperty( "bigDecimal" ),
				violationOf( Max.class ).withProperty( "bigInteger" )
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

	private static class MinDummyEntity {
		@Min(101)
		private BigDecimal bigDecimal;

		@Min(101)
		private BigInteger bigInteger;

		@Min(101)
		private byte bytePrimitive;

		@Min(101)
		private short shortPrimitive;

		@Min(101)
		private int intPrimitive;

		@Min(101)
		private long longPrimitive;

		@Min(101)
		private Byte byteObject;

		@Min(101)
		private Short shortObject;

		@Min(101)
		private Integer intObject;

		@Min(101)
		private Long longObject;
	}

	private static class MaxDummyEntity {
		@Max(101)
		private BigDecimal bigDecimal;

		@Max(101)
		private BigInteger bigInteger;

		@Max(101)
		private byte bytePrimitive;

		@Max(101)
		private short shortPrimitive;

		@Max(101)
		private int intPrimitive;

		@Max(101)
		private long longPrimitive;

		@Max(101)
		private Byte byteObject;

		@Max(101)
		private Short shortObject;

		@Max(101)
		private Integer intObject;

		@Max(101)
		private Long longObject;
	}

}
