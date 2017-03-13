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
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;
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
public class DecimalMinDecimalMaxConstraintsTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClass( DecimalMinDecimalMaxConstraintsTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = "7", id = "a")
	@SpecAssertion(section = "7", id = "i")
	public void testDecimalMinConstraint() {
		Validator validator = TestUtil.getValidatorUnderTest();
		DecimalMinDummyEntity dummy = new DecimalMinDummyEntity();

		Set<ConstraintViolation<DecimalMinDummyEntity>> constraintViolations = validator.validate( dummy );
		// only the min constraints on the primitive values should fail. Object values re still null and should pass per spec
		assertCorrectNumberOfViolations( constraintViolations, 4 );
		assertCorrectPropertyPaths(
				constraintViolations, "bytePrimitive", "intPrimitive", "longPrimitive", "shortPrimitive"
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
		assertCorrectNumberOfViolations( constraintViolations, 6 );
		assertCorrectPropertyPaths(
				constraintViolations, "byteObject", "intObject", "longObject", "shortObject", "bigDecimal", "bigInteger"
		);

		dummy.intObject = Integer.valueOf( "101" );
		dummy.longObject = Long.valueOf( "12345" );
		dummy.byteObject = Byte.parseByte( "102" );
		dummy.shortObject = Short.parseShort( "111" );
		dummy.bigDecimal = BigDecimal.valueOf( 101.1 );
		dummy.bigInteger = BigInteger.valueOf( 101 );

		constraintViolations = validator.validate( dummy );
		assertCorrectNumberOfViolations( constraintViolations, 0 );
	}

	@Test
	@SpecAssertion(section = "7", id = "a")
	@SpecAssertion(section = "7", id = "j")
	public void testDecimalMaxConstraint() {
		Validator validator = TestUtil.getValidatorUnderTest();
		DecimalMaxDummyEntity dummy = new DecimalMaxDummyEntity();

		dummy.intPrimitive = 102;
		dummy.longPrimitive = 1234;
		dummy.bytePrimitive = 102;
		dummy.shortPrimitive = 102;

		Set<ConstraintViolation<DecimalMaxDummyEntity>> constraintViolations = validator.validate( dummy );
		// only the max constraints on the primitive values should fail. Object values re still null and should pass per spec
		assertCorrectNumberOfViolations( constraintViolations, 4 );
		assertCorrectPropertyPaths(
				constraintViolations, "bytePrimitive", "intPrimitive", "longPrimitive", "shortPrimitive"
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
		assertCorrectNumberOfViolations( constraintViolations, 6 );
		assertCorrectPropertyPaths(
				constraintViolations, "byteObject", "intObject", "longObject", "shortObject", "bigDecimal", "bigInteger"
		);

		dummy.intObject = Integer.valueOf( "101" );
		dummy.longObject = Long.valueOf( "100" );
		dummy.byteObject = Byte.parseByte( "100" );
		dummy.shortObject = Short.parseShort( "101" );
		dummy.bigDecimal = BigDecimal.valueOf( 100.9 );
		dummy.bigInteger = BigInteger.valueOf( 100 );

		constraintViolations = validator.validate( dummy );
		assertCorrectNumberOfViolations( constraintViolations, 0 );
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
