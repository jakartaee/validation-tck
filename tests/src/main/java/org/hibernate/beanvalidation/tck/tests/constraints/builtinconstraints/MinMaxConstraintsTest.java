/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.builtinconstraints;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertNumberOfViolations;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertCorrectPropertyPaths;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

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
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class MinMaxConstraintsTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( MinMaxConstraintsTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "g")
	public void testMinConstraint() {
		Validator validator = TestUtil.getValidatorUnderTest();
		MinDummyEntity dummy = new MinDummyEntity();

		Set<ConstraintViolation<MinDummyEntity>> constraintViolations = validator.validate( dummy );
		// only the min constraints on the primitive values should fail. Object values re still null and should pass per spec
		assertNumberOfViolations( constraintViolations, 4 );
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
		assertNumberOfViolations( constraintViolations, 6 );
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
		assertNumberOfViolations( constraintViolations, 0 );
	}

	@Test
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "h")
	public void testMaxConstraint() {
		Validator validator = TestUtil.getValidatorUnderTest();
		MaxDummyEntity dummy = new MaxDummyEntity();

		dummy.intPrimitive = 102;
		dummy.longPrimitive = 1234;
		dummy.bytePrimitive = 102;
		dummy.shortPrimitive = 102;

		Set<ConstraintViolation<MaxDummyEntity>> constraintViolations = validator.validate( dummy );
		// only the max constraints on the primitive values should fail. Object values re still null and should pass per spec
		assertNumberOfViolations( constraintViolations, 4 );
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
		assertNumberOfViolations( constraintViolations, 6 );
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
		assertNumberOfViolations( constraintViolations, 0 );
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
